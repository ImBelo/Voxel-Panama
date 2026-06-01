error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/window.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/window.scala
text:
```scala
// window.scala
import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import java.nio.charset.StandardCharsets
import scala.util.Using
import org.joml.Matrix4f
import org.joml.Vector3f
import MemoryUtils.withArena
import ArenaType.*

object Window {

  // OpenGL Constants needed for drawing
  private val GL_COLOR_BUFFER_BIT = 0x00004000
  private val GL_DEPTH_BUFFER_BIT = 0x00000100
  private val GL_DEPTH_TEST       = 0x00000B71
  private val GL_TRIANGLES        = 0x00000004
  private val GL_STATIC_DRAW      = 0x000088E4
  private val GL_ARRAY_BUFFER     = 0x00008892
  private val GL_ELEMENT_ARRAY_BUFFER = 0x00008893
  private val GL_FLOAT            = 0x00001406
  private val GL_UNSIGNED_INT     = 0x00001405
  private val GLFW_RAW_MOUSE_MOTION = 0x00033001  // GLFW 3.3+ constant
  private val GLFW_TRUE = 1

  private val GLFW_KEY_W = 87
  private val GLFW_KEY_S = 83
  private val GLFW_KEY_A = 65
  private val GLFW_KEY_D = 68
  private val GLFW_KEY_SPACE = 32
  private val GLFW_KEY_LEFT_SHIFT = 340
  var firstMouse: Boolean = true
  var lastX: Double = 0.0
  var lastY: Double = 0.0


  def start(width: Int, height: Int, title: String): Unit = {
    val linker = Linker.nativeLinker()

    // 1. Load GLFW (Windowing)
    System.load("/usr/lib/x86_64-linux-gnu/libglfw.so.3") 

    // 2. Load libGL (OpenGL Graphics Functions)
    // On standard Linux systems, this file or its symbolic links live here:
    try {
      System.load("/usr/lib/x86_64-linux-gnu/libGL.so.1")
    } catch {
      case _: UnsatisfiedLinkError => 
        // Fallback for some Linux distributions where the link points straight to libGL.so
        System.load("/usr/lib/libGL.so")
    }


    val lookup = SymbolLookup.loaderLookup()

    val glfw = new Glfw(lookup, linker)
    val gl   = new GL(lookup, linker)

    // 1. Clean, highly readable engine setup sequence
    if (glfw.init() == 0) throw new RuntimeException("GLFW Init Failed")   

    withArena(Confined) { arena =>
      val camera = new Camera()
      // Lock and hide mouse cursor for first-person control
      val mouseLook = new MouseLook(camera,0.05f)


      // 2. ALLOCATE WINDOW STRING NATIVELY FIRST
      val titleBytes = title.getBytes(StandardCharsets.UTF_8)
      val windowTitle = arena.allocate(titleBytes.length + 1)
      MemorySegment.copy(titleBytes, 0, windowTitle, ValueLayout.JAVA_BYTE, 0, titleBytes.length)
      windowTitle.set(ValueLayout.JAVA_BYTE, titleBytes.length, 0.toByte)

      // 3. CREATE WINDOW AND ACTIVATE CONTEXT IMMEDIATELY
      val windowPtr = glfw.createPrimaryWindow(windowTitle)
      if (windowPtr == MemorySegment.NULL) { 
        glfw.terminate()
        throw new RuntimeException("Window Generation Failed") 
      }

      mouseLook.register(windowPtr, glfw, arena)


      glfw.makeContextCurrent(windowPtr)
      if (glfw.rawMouseMotionSupported()) {
        // Enable raw mouse motion
        glfw.setInputMode(windowPtr, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
      }
      // OpenGL configuration options are now safe to set via the abstraction layer!
      gl.enable(GL_DEPTH_TEST) 
      gl.clearColor(0.2f, 0.4f, 0.6f, 1.0f) 

      // 4. NOW INSTANTIATE SHADER AND MESH (Passing 'gl' directly!)
      // Note: Make sure your Shader constructor is updated to just accept your 'gl: GL' wrapper!
      val shader = new Shader(gl, arena)

     // val projectionMatrix = new Matrix4f().perspective(Math.toRadians(45.0).toFloat, 800.0f / 600.0f, 0.1f, 100.0f)

      // --- CUBE DATA ARRAYS ---
      val vertices = Array[Float](
        -0.5f,  0.5f,  0.5f,  
        0.5f,  0.5f,  0.5f,  
        -0.5f, -0.5f,  0.5f,  
        0.5f, -0.5f,  0.5f,  
        -0.5f,  0.5f, -0.5f,  
        0.5f,  0.5f, -0.5f,  
        -0.5f, -0.5f, -0.5f,  
        0.5f, -0.5f, -0.5f   
      )

      val indices = Array[Int](
        0, 1, 2,  1, 3, 2, 
        4, 5, 6,  5, 7, 6, 
        4, 0, 6,  0, 2, 6, 
        1, 5, 3,  5, 7, 3, 
        4, 5, 0,  5, 1, 0, 
        2, 3, 6,  3, 7, 6  
      )

      // Mesh now only needs our clean unified 'gl' command manager
      val cubeMesh = new Mesh(vertices, indices, arena, gl)
      if (glfw.rawMouseMotionSupported()) {
        glfw.setInputMode(windowPtr, glfw.GLFW_RAW_MOUSE_MOTION, glfw.GLFW_TRUE)
      }

      // 2. Lock and disable the cursor completely
      glfw.setInputMode(windowPtr, glfw.GLFW_CURSOR, glfw.GLFW_CURSOR_DISABLED)
      val lightPosLoc   = shader.getUniformLocation("u_LightPos")
      val lightColLoc   = shader.getUniformLocation("u_LightColor")
      val modelLoc      = shader.getUniformLocation("u_Model")
      val viewLoc       = shader.getUniformLocation("u_View")
      val projectionLoc = shader.getUniformLocation("u_Projection")
      val colorLoc      = shader.getUniformLocation("u_ObjectColor")

      // For performance, pre-allocate your matrix math targets so you don't allocate objects inside the loop
      val modelMatrix      = new Matrix4f()
      val viewMatrix       = new Matrix4f()
      val projectionMatrix = new Matrix4f()
      val pWidth  = arena.allocate(ValueLayout.JAVA_INT)
      val pHeight = arena.allocate(ValueLayout.JAVA_INT)


      // --- RENDER LOOP ---
      var shouldClose = false
      while (!shouldClose) {
        // Use our abstract glfw handler to process inputs
        processInput(windowPtr, camera, glfw)
        mouseLook.update(arena)  // Pass arena for each frame

        glfw.getFramebufferSize(windowPtr, pWidth, pHeight)
        val width  = pWidth.get(ValueLayout.JAVA_INT, 0).toFloat
        val height = pHeight.get(ValueLayout.JAVA_INT, 0).toFloat
        val aspectRatio = if (height > 0) width / height else 1.0f

        camera.getProjectionMatrix(projectionMatrix, aspectRatio)
        shader.setMatrix4f(projectionLoc, projectionMatrix)

        // Clear screen via clean API call
        gl.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
"
        shader.use()
        gl.uniform3f(colorLoc, 0.2f, 0.8f, 0.2f)      // Give your voxel mesh a clean base green color
        gl.uniform3f(lightPosLoc, 5.0f, 10.0f, 5.0f) // Sun position up high
        gl.uniform3f(lightColLoc, 1.0f, 1.0f, 0.95f) // Warm slightly yellow sunlight

        // 3. Update Camera Transformations
        // Fetch current view and projection calculations from your camera systems
        camera.getViewMatrix(viewMatrix)
        camera.getProjectionMatrix(projectionMatrix,aspectRatio) // Assuming you have an aspect ratio calculator here

        // Reset model matrix to identity (0, 0, 0 center)
        modelMatrix.identity() 

        // 4. Upload all transformations to the GPU
        shader.setMatrix4f(modelLoc, modelMatrix)
        shader.setMatrix4f(viewLoc, viewMatrix)
        shader.setMatrix4f(projectionLoc, projectionMatrix)       
        cubeMesh.draw()

        // Swap buffers and poll OS window states cleanly
        glfw.swapBuffers(windowPtr)
        glfw.pollEvents()

        // Type-safe status check via wrapper
        if (glfw.windowShouldClose(windowPtr) != 0) {
          shouldClose = true
        }
      }
    }

    glfw.terminate()
  }

  def processInput(windowPtr: MemorySegment, camera: Camera, glfw: Glfw): Unit = {
    val speed = 0.05f

    val moveVector = new Vector3f()
    val rightVector = new Vector3f()
    val horizontalForward = Vector3f(camera.forward.x, 0f, camera.forward.z).normalize()

    val verticalSpeed = 0.05f

    if (glfw.getKey(windowPtr, 256) == 1) { 
      // Force the window to flag itself for closure
      // Assuming you have glfwSetWindowShouldClose bound:
      glfw.setWindowShouldClose(windowPtr, 1) 
    }
    // 1. Press Space -> Move straight UP (Increase Y)
    val spacePressed: Int = glfw.getKey(windowPtr, GLFW_KEY_SPACE)
    if (spacePressed == 1) {
      camera.position.y += verticalSpeed
    }

    // 2. Press Left Shift -> Move straight DOWN (Decrease Y)
    val shiftPressed: Int = glfw.getKey(windowPtr, GLFW_KEY_LEFT_SHIFT)
    if (shiftPressed == 1) {
      camera.position.y -= verticalSpeed
    }
    val wPressed: Int = glfw.getKey(windowPtr, GLFW_KEY_W)
    if (wPressed == 1) {
      horizontalForward.mul(speed, moveVector)
      camera.position.add(moveVector)
    }

    // Press S -> Move Backward (horizontal only)
    val sPressed: Int = glfw.getKey(windowPtr, GLFW_KEY_S)
    if (sPressed == 1) {
      horizontalForward.mul(speed, moveVector)
      camera.position.sub(moveVector)
    }

    // Press A -> Strafe Left (already horizontal, but use horizontalForward)
    val aPressed: Int = glfw.getKey(windowPtr, GLFW_KEY_A)
    if (aPressed == 1) {
      horizontalForward.cross(camera.up, rightVector).normalize()  // Already works
      rightVector.mul(speed, moveVector)
      camera.position.sub(moveVector)
    }

    // Press D -> Strafe Right (already horizontal, but use horizontalForward)
    val dPressed: Int = glfw.getKey(windowPtr, GLFW_KEY_D)
    if (dPressed == 1) {
      horizontalForward.cross(camera.up, rightVector).normalize()
      rightVector.mul(speed, moveVector)
      camera.position.add(moveVector)
    }
  }
}

```


presentation compiler configuration:
Scala version: 3.8.2-bin-nonbootstrapped
Classpath:
<WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.2/scala3-library_3-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/joml/joml/1.10.8/joml-1.10.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.2/scala-library-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/sourcegraph/semanticdb-javac/0.10.0/semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main/META-INF/best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -release 21 -Ywith-best-effort-tasty




#### Error stacktrace:

```
dotty.tools.pc.DiagnosticProvider.diagnostics(DiagnosticProvider.scala:19)
	dotty.tools.pc.ScalaPresentationCompiler.didChange$$anonfun$1(ScalaPresentationCompiler.scala:509)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.withNonInterruptableCompiler$$anonfun$1(CompilerAccess.scala:133)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1516)
```
#### Short summary: 

java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'