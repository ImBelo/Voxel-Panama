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
