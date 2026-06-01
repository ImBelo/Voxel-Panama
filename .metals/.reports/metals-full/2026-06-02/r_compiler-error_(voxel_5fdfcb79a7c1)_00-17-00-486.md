error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/Mesh.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/Mesh.scala
text:
```scala
import java.lang.foreign.{Arena, ValueLayout}

class Mesh(
  vertices: Array[Float],
  indices: Array[Int],
  gl: GL // Only pass the unified wrapper object!
)(using arena: Are) {

  // OpenGL Constants needed internally
  private val GL_ARRAY_BUFFER         = 0x00008892
  private val GL_ELEMENT_ARRAY_BUFFER = 0x00008893
  private val GL_STATIC_DRAW          = 0x000088E4
  private val GL_FLOAT                = 0x00001406
  private val GL_UNSIGNED_INT         = 0x00001405
  private val GL_TRIANGLES            = 0x00000004

  val indexCount: Int = indices.length

  // 1. GENERATE HARDWARE IDs
  val vaoId: Int = allocateVertexArrayId()
  val vboId: Int = allocateBufferId()
  val eboId: Int = allocateBufferId() // Element Buffer Object (also called IBO)

  // 2. INITIALIZE THE MESH IMMEDIATELY
  init()

  private def init(): Unit = {
    // Convert JVM heap arrays to native Panama memory
    val nativeVertices = BufferUtils.toNative(vertices, arena)
    val nativeIndices  = BufferUtils.toNative(indices, arena)

    // A. Bind VAO first! This tells OpenGL to start recording our state.
    gl.bindVertexArray(vaoId)

    // B. Bind VBO and upload vertex data (Positions + Normals)
    gl.bindBuffer(GL_ARRAY_BUFFER, vboId)
    gl.bufferData(GL_ARRAY_BUFFER, vertices.length * 4L, nativeVertices, GL_STATIC_DRAW)

    // C. Bind EBO and upload index data (Triangles)
    gl.bindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
    gl.bufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4L, nativeIndices, GL_STATIC_DRAW)

    // D. Configure the Pointers (These settings are permanently saved into the VAO!)
    val stride = 6 * java.lang.Float.BYTES

    // Layout Location 0: Position (X, Y, Z)
    gl.vertexAttribPointer(0, 3, GL_FLOAT, 0.toByte, stride, 0L)
    gl.enableVertexAttribArray(0)

    // Layout Location 1: Normal (NX, NY, NZ)
    val normalOffset = 3L * java.lang.Float.BYTES
    gl.vertexAttribPointer(1, 3, GL_FLOAT, 0.toByte, stride, normalOffset)
    gl.enableVertexAttribArray(1)

    // E. Unbind the VAO. This is a safety measure so other code doesn't accidentally mess up our mesh state.
    gl.bindVertexArray(0)
  }

  // --- PANAMA ALLOCATION HELPERS ---
  
  private def allocateBufferId(): Int = {
    val idPtr = arena.allocate(ValueLayout.JAVA_INT)
    gl.genBuffers(1, idPtr) 
    idPtr.get(ValueLayout.JAVA_INT, 0)
  }

  private def allocateVertexArrayId(): Int = {
    val idPtr = arena.allocate(ValueLayout.JAVA_INT)
    gl.genVertexArrays(1, idPtr) // Make sure your GL wrapper has this mapped!
    idPtr.get(ValueLayout.JAVA_INT, 0)
  }

  // --- THE RENDER CALL ---

  def draw(): Unit = {
    // Because the VAO remembered absolutely everything (the VBO, the EBO, and the layout pointers),
    // drawing is now just TWO lines of code!
    gl.bindVertexArray(vaoId)
    gl.drawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L)
    
    // (Optional but good practice) unbind after drawing
    gl.bindVertexArray(0)
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