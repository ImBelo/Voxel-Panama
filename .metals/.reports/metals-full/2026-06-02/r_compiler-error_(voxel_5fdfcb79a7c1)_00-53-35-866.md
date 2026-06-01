error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/ShaderProgram.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/ShaderProgram.scala
text:
```scala
import java.lang.foreign.*
import MemoryUtils.withArena
import org.joml.Matrix4f
package your.package.here

import MemoryUtils.withArena
import org.lwjgl.glfw.GLFW.*
import java.lang.foreign.{Arena, MemorySegment, ValueLayout}
import org.joml.Matrix4f
import scala.collection.mutable

opaque type ProgramId = Int
object ProgramId:
  def apply(id: Int): ProgramId = id
  extension (id: ProgramId) def toInt: Int = id

trait Uniform[U]:
  extension (value: U)
    inline def set(program: ShaderProgram, location: Int): Unit

class ShaderProgram(private val gl: GL, val id: ProgramId)(using arena: Arena):
  // Cache uniform locations
  private val uniformLocations = mutable.Map.empty[String, Int]

  // Pre-allocated matrix upload buffers
  private val matrixBuffer: MemorySegment = arena.allocate(16 * 4)
  private val matrixArray: Array[Float]  = new Array[Float](16)

  def use(): Unit = gl.useProgram(id.toInt)

  def getUniformLocation(name: String): Int =
    uniformLocations.getOrElseUpdate(name, {
      withArena(ArenaType.Confined) { local =>
        val seg = local.allocateFrom(name)
        gl.getUniformLocation(id.toInt, seg)
      }
    })

  /** Zero-cost uniform setter – inlined to direct GL calls */
  inline def set[U](name: String, value: U)(using u: Uniform[U]): Unit =
    value.set(this, getUniformLocation(name))

object ShaderProgram{
  // ------ Uniform iven instances (access to private members allowed here) ------
  
  given Uniform[Float] with
    extension (v: Float)
      inline def set(p: ShaderProgram, loc: Int): Unit =
        p.gl.uniform1f(loc, v)

  given Uniform[(Float, Float, Float)] with
    extension (v: (Float, Float, Float))
      inline def set(p: ShaderProgram, loc: Int): Unit =
        p.gl.uniform3f(loc, v._1, v._2, v._3)

  given Uniform[Matrix4f] with
    extension (m: Matrix4f)
      inline def set(p: ShaderProgram, loc: Int): Unit =
        // 1. Extract matrix into reusable Java array
        m.get(p.matrixArray)
        // 2. Copy to pre-allocated off-heap buffer
        MemorySegment.copy(p.matrixArray, 0, p.matrixBuffer, ValueLayout.JAVA_FLOAT, 0, 16)
        // 3. Upload to GPU – FIX: 0.toByte instead of false
        p.gl.uniformMatrix4fv(loc, 1, 0.toByte, p.matrixBuffer)

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