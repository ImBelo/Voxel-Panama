error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/GlfwWindow.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/GlfwWindow.scala
text:
```scala
import java.lang.foreign.*

class GlfwWindow(width: Int, Height: Int, glfw: Glfw, title: String, arena: Arena) {
  private val windowTitle = arena.allocateFrom(title) // helper to copy string + null terminator
  val handle: MemorySegment = glfw.createPrimaryWindow(widthwindowTitle)
  if (handle == MemorySegment.NULL) {
    glfw.terminate()
    throw new RuntimeException("Failed to create window")
  }

  def makeContextCurrent(): Unit = glfw.makeContextCurrent(handle)

  def enableRawMouseMotion(): Unit =
    if (glfw.rawMouseMotionSupported())
      glfw.setInputMode(handle, glfw.GLFW_RAW_MOUSE_MOTION, glfw.GLFW_TRUE)

  def disableCursor(): Unit =
    glfw.setInputMode(handle, glfw.GLFW_CURSOR, glfw.GLFW_CURSOR_DISABLED)

  def framebufferSize(outWidth: MemorySegment, outHeight: MemorySegment): (Float, Float) = {
    glfw.getFramebufferSize(handle, outWidth, outHeight)
    val w = outWidth.get(ValueLayout.JAVA_INT, 0).toFloat
    val h = outHeight.get(ValueLayout.JAVA_INT, 0).toFloat
    (w, h)
  }

  def swapBuffers(): Unit = glfw.swapBuffers(handle)
  def pollEvents(): Unit = glfw.pollEvents()
  def shouldClose(): Boolean = glfw.windowShouldClose(handle) != 0
  //def destroy(): Unit = glfw.destroyWindow(handle)
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