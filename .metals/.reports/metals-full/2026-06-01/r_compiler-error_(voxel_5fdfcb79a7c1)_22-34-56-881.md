error id: 83AAE395BFF1783E007BFFA54786EF70
file://<WORKSPACE>/window.scala
### java.lang.AssertionError: assertion failed: position error, parent span does not contain child span
parent      = new InputHandler(_root_.scala.Predef.???) # -1,
parent span = <1910..8239>,
child       = _root_.scala.Predef.??? # -1,
child span  = <1927..8244>

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

      val inputHandler = new InputHandler(


      // 2. ALLOCATE WINDOW STRING NATIVELY FIRST
      val window = new GlfwWindow(glfw, "Meow", arena)



      glfw.makeContextCurrent(window.handle)
      if (glfw.rawMouseMotionSupported()) {
        // Enable raw mouse motion
        glfw.setInputMode(window.handle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
      }
      // OpenGL configuration options are now safe to set via the abstraction layer!
      gl.enable(GL_DEPTH_TEST) 
      gl.clearColor(0.2f, 0.4f, 0.6f, 1.0f) 

      // 4. NOW INSTANTIATE SHADER AND MESH (Passing 'gl' directly!)
      // Note: Make sure your Shader constructor is updated to just accept your 'gl: GL' wrapper!
      val shader = new Shader(gl, arena)

      // val projectionMatrix = new Matrix4f().perspective(Math.toRadians(45.0).toFloat, 800.0f / 600.0f, 0.1f, 100.0f)

      // --- CUBE DATA ARRAYS ---
      // --- UPGRADED CUBE DATA ARRAYS (24 Vertices) ---
      // Structure: X, Y, Z,   NX, NY, NZ
      val vertices = Array[Float](
        // --- FRONT FACE (Normal: 0, 0, 1) ---
      -0.5f, -0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 0: Bottom-Left
      0.5f, -0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 1: Bottom-Right
      0.5f,  0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 2: Top-Right
      -0.5f,  0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 3: Top-Left

      // --- BACK FACE (Normal: 0, 0, -1) ---
      -0.5f, -0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 4: Bottom-Left
      0.5f, -0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 5: Bottom-Right
      0.5f,  0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 6: Top-Right
      -0.5f,  0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 7: Top-Left

      // --- TOP FACE (Normal: 0, 1, 0) ---
      -0.5f,  0.5f,  0.5f,   0.0f,  1.0f,  0.0f, // 8: Front-Left
      0.5f,  0.5f,  0.5f,   0.0f,  1.0f,  0.0f, // 9: Front-Right
      0.5f,  0.5f, -0.5f,   0.0f,  1.0f,  0.0f, // 10: Back-Right
      -0.5f,  0.5f, -0.5f,   0.0f,  1.0f,  0.0f, // 11: Back-Left

      // --- BOTTOM FACE (Normal: 0, -1, 0) ---
      -0.5f, -0.5f,  0.5f,   0.0f, -1.0f,  0.0f, // 12: Front-Left
      0.5f, -0.5f,  0.5f,   0.0f, -1.0f,  0.0f, // 13: Front-Right
      0.5f, -0.5f, -0.5f,   0.0f, -1.0f,  0.0f, // 14: Back-Right
      -0.5f, -0.5f, -0.5f,   0.0f, -1.0f,  0.0f, // 15: Back-Left

      // --- RIGHT FACE (Normal: 1, 0, 0) ---
      0.5f, -0.5f,  0.5f,   1.0f,  0.0f,  0.0f, // 16: Bottom-Front
      0.5f, -0.5f, -0.5f,   1.0f,  0.0f,  0.0f, // 17: Bottom-Back
      0.5f,  0.5f, -0.5f,   1.0f,  0.0f,  0.0f, // 18: Top-Back
      0.5f,  0.5f,  0.5f,   1.0f,  0.0f,  0.0f, // 19: Top-Front

      // --- LEFT FACE (Normal: -1, 0, 0) ---
      -0.5f, -0.5f,  0.5f,  -1.0f,  0.0f,  0.0f, // 20: Bottom-Front
      -0.5f, -0.5f, -0.5f,  -1.0f,  0.0f,  0.0f, // 21: Bottom-Back
      -0.5f,  0.5f, -0.5f,  -1.0f,  0.0f,  0.0f, // 22: Top-Back
      -0.5f,  0.5f,  0.5f,  -1.0f,  0.0f,  0.0f  // 23: Top-Front
    )

      // --- UPGRADED INDEX ARRAY (Mapping the 24 Vertices into Triangles) ---
      val indices = Array[Int](
        0, 1, 2,   2, 3, 0,   // Front
        4, 5, 6,   6, 7, 4,   // Back
        8, 9, 10,  10, 11, 8,  // Top
        12, 13, 14, 14, 15, 12, // Bottom
        16, 17, 18, 18, 19, 16, // Right
        20, 21, 22, 22, 23, 20  // Left
      )
     

      // Mesh now only needs our clean unified 'gl' command manager
      val cubeMesh = new Mesh(vertices, indices, arena, gl)
      if (glfw.rawMouseMotionSupported()) {
        glfw.setInputMode(window.handle, glfw.GLFW_RAW_MOUSE_MOTION, glfw.GLFW_TRUE)
      }

      // 2. Lock and disable the cursor completely
      glfw.setInputMode(window.handle, glfw.GLFW_CURSOR, glfw.GLFW_CURSOR_DISABLED)
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
        processInput(window.handle, camera, glfw)
        mouseLook.update(arena)  // Pass arena for each frame

        glfw.getFramebufferSize(window.handle, pWidth, pHeight)
        val width  = pWidth.get(ValueLayout.JAVA_INT, 0).toFloat
        val height = pHeight.get(ValueLayout.JAVA_INT, 0).toFloat
        val aspectRatio = if (height > 0) width / height else 1.0f

        camera.getProjectionMatrix(projectionMatrix, aspectRatio)
        shader.setMatrix4f(projectionLoc, projectionMatrix)

        // Clear screen via clean API call
        gl.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
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
        glfw.swapBuffers(window.handle)
        glfw.pollEvents()

        // Type-safe status check via wrapper
        if (glfw.windowShouldClose(window.handle) != 0) {
          shouldClose = true
        }
      }
    }

    glfw.terminate()
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
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:10)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:186)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:216)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:327)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:216)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:216)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:327)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:216)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:216)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:327)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:216)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:216)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:327)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:216)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:216)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:327)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:216)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:211)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:216)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:327)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:216)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:237)
	dotty.tools.dotc.parsing.Parser.parse$$anonfun$1(ParserPhase.scala:39)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:533)
	dotty.tools.dotc.parsing.Parser.parse(ParserPhase.scala:40)
	dotty.tools.dotc.parsing.Parser.$anonfun$2(ParserPhase.scala:52)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:495)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:599)
	scala.collection.immutable.List.prependedAll(List.scala:156)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:911)
	dotty.tools.dotc.parsing.Parser.runOn(ParserPhase.scala:51)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:380)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1324)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:373)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1$$anonfun$2(Run.scala:420)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1$$anonfun$adapted$1(Run.scala:420)
	scala.Function0.apply$mcV$sp(Function0.scala:42)
	dotty.tools.dotc.Run.showProgress(Run.scala:482)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:420)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:432)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:432)
	dotty.tools.dotc.Run.compileSources(Run.scala:319)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:165)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:44)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:357)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:158)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1516)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: position error, parent span does not contain child span
parent      = new InputHandler(_root_.scala.Predef.???) # -1,
parent span = <1910..8239>,
child       = _root_.scala.Predef.??? # -1,
child span  = <1927..8244>