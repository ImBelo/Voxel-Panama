error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/Uniform.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/Uniform.scala
text:
```scala
import org.joml.Matrix4f
import java.lang.forr
trait Uniform[U]:
  extension (value: U)
    inline def set(program: ShaderProgram, location: Int): Unit

// Given instances for common types – all inlined
given Uniform[Float] with
  extension (v: Float)
    inline def set(p: ShaderProgram, loc: Int): Unit = p.gl.uniform1f(loc, v)

given Uniform[(Float, Float, Float)] with
  extension (v: (Float, Float, Float))
    inline def set(p: ShaderProgram, loc: Int): Unit =
      p.gl.uniform3f(loc, v._1, v._2, v._3)

given Uniform[Matrix4f] with
  extension (m: Matrix4f)
    inline def set(p: ShaderProgram, loc: Int): Unit =
      m.get(p.matrixArray)
      MemorySegment.copy(p.matrixArray, 0, p.matrixBuffer, ValueLayout.JAVA_FLOAT, 0, 16)
      p.gl.uniformMatrix4fv(loc, 1, false, p.matrixBuffer)

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