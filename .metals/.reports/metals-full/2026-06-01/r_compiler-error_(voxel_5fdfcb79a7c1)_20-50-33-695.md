error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/Mesh.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/Mesh.scala
text:
```scala
import java.lang.foreign.*


class Mesh(
  vertices: Array[Float],
  indices: Array[Int],
  arena: Arena,
  gl: GL // Only pass the unified wrapper object!
) {

  // OpenGL Constants needed internally
  private val GL_ARRAY_BUFFER = 0x00008892
  private val GL_ELEMENT_ARRAY_BUFFER = 0x00008893
  private val GL_STATIC_DRAW = 0x000088E4
  private val GL_FLOAT = 0x00001406
  private val GL_UNSIGNED_INT = 0x00001405
  private val GL_TRIANGLES = 0x00000004

  // Unique IDs assigned by the GPU graphics card
  val vboId: Int = allocateBufferId()
  val iboId: Int = allocateBufferId()
  val indexCount: Int = indices.length
  

  // Automatically upload data on instantiation
  upload()

  private def allocateBufferId(): Int = {
    val idPtr = arena.allocate(ValueLayout.JAVA_INT)
    gl.genBuffers(1, idPtr) // Clean wrapper call
    idPtr.get(ValueLayout.JAVA_INT, 0)
  }

  private def upload(): Unit = {
    val nativeVertices = BufferUtils.toNative(vertices, arena)
    val nativeIndices = BufferUtils.toNative(indices, arena)

    // Pipe Vertices to VBO

    gl.bindBuffer(GL_ARRAY_BUFFER, vboId)
    gl.bufferData(GL_ARRAY_BUFFER, vertices.length * 4L, nativeVertices, GL_STATIC_DRAW)


    // Pipe Indices to IBO
    gl.bindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId)
    gl.bufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4L, nativeIndices, GL_STATIC_DRAW)
  }

  // Binds buffers, sets up attributes, and draws the mesh instantly
  def draw(): Unit = {
    gl.bindBuffer(GL_ARRAY_BUFFER, vboId)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0, 3, GL_FLOAT, 0.toByte, 3 * 4, 0L)

    gl.bindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId)

    gl.drawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L)
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