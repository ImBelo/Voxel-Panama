error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/KeyboardHandler.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/KeyboardHandler.scala
text:
```scala
import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import org.joml.Matrix4f
import org.joml.Vector3f
import MemoryUtils.withArena
import ArenaType.*



class KeyboardHandler(glfw: Glfw, window: GlfwWindow, camera: Camera) {
    def processInput(window: Glf, camera: Camera, glfw: Glfw): Unit = {
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
    val spacePressed: Int = glfw.getKey(windowPtr, glfw.GLFW_KEY_SPACE)
    if (spacePressed == 1) {
      camera.position.y += verticalSpeed
    }

    // 2. Press Left Shift -> Move straight DOWN (Decrease Y)
    val shiftPressed: Int = glfw.getKey(windowPtr, glfw.GLFW_KEY_LEFT_SHIFT)
    if (shiftPressed == 1) {
      camera.position.y -= verticalSpeed
    }
    val wPressed: Int = glfw.getKey(windowPtr, glfw.GLFW_KEY_W)
    if (wPressed == 1) {
      horizontalForward.mul(speed, moveVector)
      camera.position.add(moveVector)
    }

    // Press S -> Move Backward (horizontal only)
    val sPressed: Int = glfw.getKey(windowPtr, glfw.GLFW_KEY_S)
    if (sPressed == 1) {
      horizontalForward.mul(speed, moveVector)
      camera.position.sub(moveVector)
    }

    // Press A -> Strafe Left (already horizontal, but use horizontalForward)
    val aPressed: Int = glfw.getKey(windowPtr, glfw.GLFW_KEY_A)
    if (aPressed == 1) {
      horizontalForward.cross(camera.up, rightVector).normalize()  // Already works
      rightVector.mul(speed, moveVector)
      camera.position.sub(moveVector)
    }

    // Press D -> Strafe Right (already horizontal, but use horizontalForward)
    val dPressed: Int = glfw.getKey(windowPtr, glfw.GLFW_KEY_D)
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