error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/MouseLook.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/MouseLook.scala
text:
```scala
import java.lang.foreign.{MemorySegment, Arena, ValueLayout}
import scala.compiletime.uninitialized


class MouseL(camera: Camera, sensitivity: Float = 0.5f) {
  private var glfw: Glfw = uninitialized
  private var window: MemorySegment = uninitialized
  private val persistentArena = Arena.ofConfined() // or however you create it
  private var cursorXPtr: MemorySegment = uninitialized
  private var cursorYPtr: MemorySegment = uninitialized
  private var windowWidth: Int = uninitialized
  private var windowHeight: Int = uninitialized
  private var deadZone: Int = 10
  private var initialized = false

  
  // Constants - add these if not in your Glfw object
  private final val GLFW_CURSOR = 0x00033001
  private final val GLFW_CURSOR_HIDDEN = 0x00034002
  
  def register(window: MemorySegment, glfw: Glfw, arena: Arena): Unit = {
    initialized = true
    this.glfw = glfw
    this.window = window
    
    // Allocate
    cursorXPtr = persistentArena.allocate(ValueLayout.JAVA_DOUBLE)
    cursorYPtr = persistentArena.allocate(ValueLayout.JAVA_DOUBLE)
    val widthPtr = arena.allocate(ValueLayout.JAVA_INT)
    val heightPtr = arena.allocate(ValueLayout.JAVA_INT)


    
    glfw.getFramebufferSize(window, widthPtr, heightPtr)
    windowWidth = widthPtr.get(ValueLayout.JAVA_INT, 0)
    windowHeight = heightPtr.get(ValueLayout.JAVA_INT, 0)
    
    glfw.setCursorPos(window, windowWidth / 2.0, windowHeight / 2.0)
  }
  
  // Call this every frame in your game loop
  def update(arena: Arena): Unit = {
    if (!initialized) return
    glfw.getCursorPos(window, cursorXPtr, cursorYPtr)
    
    val mouseX = cursorXPtr.get(ValueLayout.JAVA_DOUBLE, 0)
    val mouseY = cursorYPtr.get(ValueLayout.JAVA_DOUBLE, 0)
    
    val centerX = windowWidth / 2.0
    val centerY = windowHeight / 2.0
    
    // Check X axis (left/right)
    if (mouseX < centerX - deadZone) {
      val distance = (centerX - mouseX).toFloat
      val turnAmount = distance * sensitivity
      camera.yaw -= turnAmount
      camera.updateDirection()
    }
    else if (mouseX > centerX + deadZone) {

      val distance = (mouseX - centerX).toFloat
      val turnAmount = distance * sensitivity
      camera.yaw += turnAmount
      camera.updateDirection()

    }
    // Check Y axis (up/down)
    if (mouseY < centerY - deadZone) {
      val distance = (centerY - mouseY).toFloat
      val pitchAmount = distance * sensitivity
      camera.pitch += pitchAmount
      if (camera.pitch > 89f) camera.pitch = 89f
      if (camera.pitch < -89f) camera.pitch = -89f
      camera.updateDirection()
    }
    else if (mouseY > centerY + deadZone) {
      val distance = (mouseY - centerY).toFloat
      val pitchAmount = distance * sensitivity
      camera.pitch -= pitchAmount
      if (camera.pitch > 89f) camera.pitch = 89f
      if (camera.pitch < -89f) camera.pitch = -89f
      camera.updateDirection()
    }
    
    // Always warp cursor back to center
    glfw.setCursorPos(window, centerX, centerY)
  }

  def cleanup(): Unit = {
    persistentArena.close()  // Don't forget to close!
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