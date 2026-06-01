error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/ShaderProgram.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/ShaderProgram.scala
text:
```scala
import java.lang.foreign.*
i
opaque type ProgramId = Int
object ProgramId:
  def apply(id: Int): ProgramId = id
  extension (id: ProgramId) def toInt: Int = id

class ShaderProgram(gl: GL, val id: ProgramId)(using arena: Arena):
  // Cache uniform locations (looked up only once)
  private val uniformLocations = scala.collection.mutable.Map.empty[String, Int]

  // Reusable matrix upload buffer (same as before)
  private val matrixBuffer: MemorySegment = arena.allocate(16 * 4)
  private val matrixArray: Array[Float]  = new Array[Float](16)

  def use(): Unit = gl.useProgram(id.toInt)

  /** Get location (cached) with a temporary arena for the string */
  def getUniformLocation(name: String): Int =
    uniformLocations.getOrElseUpdate(name, {
      withArena(ArenaType.Confined) { local =>
        val seg = local.allocateFrom(name)
        gl.getUniformLocation(id.toInt, seg)
      }
    })

  // Type‑class based uniform setting (inline, zero overhead)
  inline def set[U](name: String, value: U)(using u: Uniform[U]): Unit =
    u.set(this, getUniformLocation(name), value)

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