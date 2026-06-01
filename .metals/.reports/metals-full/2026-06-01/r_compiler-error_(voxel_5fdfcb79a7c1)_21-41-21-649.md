error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/InputHandler.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/InputHandler.scala
text:
```scala
import java.lang.foreign.*

class InputHandler(glfw: Glfw, window: GlfwWindow, camera: Camera, mouseSensitivity: Float = 0.05f)(using arena: Arena) {
  val mouseHa = new MouseLook(camera, mouseSensitivity)
  mouseLook.register(window.handle, glfw, arena) 

  def update(): Unit = mouseLook.update(arena)
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