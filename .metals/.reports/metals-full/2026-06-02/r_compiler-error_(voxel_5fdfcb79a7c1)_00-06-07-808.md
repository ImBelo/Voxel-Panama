error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/Camera.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/Camera.scala
text:
```scala
// Camera.scala
import org.joml.Matrix4f

import org.joml.Vector3f

class Camera {
  val position = new Vector3f(0.0f, 0.0f, 3.0f) // Start 3 units back from center
  val forward  = new Vector3f(0.0f, 0.0f, -1.0f)
  val up       = new Vector3f(0.0f, 1.0f, 0.0f)


  var yaw: Float   = -90.0f // Left/Right angle
  var pitch: Float = 0.0f   // Up/Down angle

  // Generates the final matrix representing the camera's transformation of the world
  private val targetTmp = new org.joml.Vector3f()

  def getViewMatrix(dest: org.joml.Matrix4f): Unit = {
    // Clear and update our pre-allocated target vector
    position.add(forward, targetTmp) 

    // Calculate the lookAt matrix directly into the destination matrix
    dest.identity().lookAt(position, targetTmp, up)
  }
  def getProjectionMatrix(dest: org.joml.Matrix4f, aspectRatio: Float): Unit = {
    val fov = Math.toRadians(60.0).toFloat // 60-degree Field of View
    val nearPlane = 0.1f                  // Don't render things closer than this
    val farPlane = 1000.0f                // Voxel render distance horizon

    // Calculate perspective directly into the destination matrix
    dest.identity().perspective(fov, aspectRatio, nearPlane, farPlane)
  }

  // Updates the forward vector based on mouse angles (Yaw and Pitch)
  def updateDirection(): Unit = {
    // Clamp pitch to prevent the camera flipping upside down
    if (pitch > 89.0f) pitch = 89.0f
    if (pitch < -89.0f) pitch = -89.0f

    val direction = new Vector3f()
    direction.x = Math.cos(Math.toRadians(yaw)).toFloat * Math.cos(Math.toRadians(pitch)).toFloat
    direction.y = Math.sin(Math.toRadians(pitch)).toFloat
    direction.z = Math.sin(Math.toRadians(yaw)).toFloat * Math.cos(Math.toRadians(pitch)).toFloat
    
    direction.normalize(forward)

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