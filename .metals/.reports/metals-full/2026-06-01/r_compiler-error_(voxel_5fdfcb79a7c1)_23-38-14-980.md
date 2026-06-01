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



import org.joml.Vector3f

class KeyboardHandler(glfw: Glfw, window: GlfwWindow, camera: Camera) {

  // 1. PRE-ALLOCATE MEMORY
  // By moving these out of the method, they are only created ONCE when the game starts.
  // We will reuse these exact same memory blocks every frame.
  private val moveVector = new Vector3f()
  private val rightVector = new Vector3f()
  private val horizontalForward = new Vector3f()

  // Base speed (eventually you'll want to multiply this by deltaTime)
  private val baseSpeed = 0.05f

  // Notice: Removed redundant arguments. The class already has access to glfw, window, and camera!
  def processInput(deltaTime): Unit = {
    // Cache the handle locally for faster access
    val windowHandle = window.handle

    // 2. USE CONSTANTS INSTEAD OF MAGIC NUMBERS (256 = GLFW_KEY_ESCAPE)
    if (glfw.getKey(windowHandle, glfw.GLFW_KEY_ESCAPE) == glfw.GLFW_PRESS) { 
      EngineProfiler.printMetrics()
      glfw.setWindowShouldClose(windowHandle, 1) 
    }

    // --- VERTICAL MOVEMENT ---
    if (glfw.getKey(windowHandle, glfw.GLFW_KEY_SPACE) == glfw.GLFW_PRESS) {
      camera.position.y += baseSpeed
    }
    if (glfw.getKey(windowHandle, glfw.GLFW_KEY_LEFT_SHIFT) == glfw.GLFW_PRESS) {
      camera.position.y -= baseSpeed
    }

    // --- HORIZONTAL MOVEMENT ---
    // 3. CACHE KEY STATES FIRST
    val wPressed = glfw.getKey(windowHandle, glfw.GLFW_KEY_W) == glfw.GLFW_PRESS
    val sPressed = glfw.getKey(windowHandle, glfw.GLFW_KEY_S) == glfw.GLFW_PRESS
    val aPressed = glfw.getKey(windowHandle, glfw.GLFW_KEY_A) == glfw.GLFW_PRESS
    val dPressed = glfw.getKey(windowHandle, glfw.GLFW_KEY_D) == glfw.GLFW_PRESS

    // 4. CONDITIONAL MATH
    // Only calculate vectors if the player is actually trying to move!
    if (wPressed || sPressed || aPressed || dPressed) {
      
      // Update our pre-allocated vector in-place (Zero allocations!)
      horizontalForward.set(camera.forward.x, 0f, camera.forward.z).normalize()

      if (wPressed) {
        horizontalForward.mul(baseSpeed, moveVector)
        camera.position.add(moveVector)
      }
      if (sPressed) {
        horizontalForward.mul(baseSpeed, moveVector)
        camera.position.sub(moveVector)
      }

      // 5. CACHE CROSS PRODUCT
      // Only calculate the right vector once, and only if strafing
      if (aPressed || dPressed) {
        horizontalForward.cross(camera.up, rightVector).normalize()
        
        if (aPressed) {
          rightVector.mul(baseSpeed, moveVector)
          camera.position.sub(moveVector)
        }
        if (dPressed) {
          rightVector.mul(baseSpeed, moveVector)
          camera.position.add(moveVector)
        }
      }
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