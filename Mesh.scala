import java.lang.foreign.{Arena, ValueLayout}

class Mesh(
  vertices: Array[Float],
  indices: Array[Int],
  gl: GL // Only pass the unified wrapper object!
)(using arena: Arena) {

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
