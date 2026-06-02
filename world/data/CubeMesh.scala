import java.lang.foreign.{Arena, ValueLayout}

class CubeMesh(gl: GL)(using arena: Arena) {

  // 1. ZERO-COST CONSTANTS
  // Using `inline val` tells the compiler to physically paste the literal numbers 
  // directly into the bytecode. They occupy zero memory on the JVM heap and 
  // require no CPU memory lookups!
  private inline val GL_ARRAY_BUFFER         = 0x00008892
  private inline val GL_ELEMENT_ARRAY_BUFFER = 0x00008893
  private inline val GL_STATIC_DRAW          = 0x000088E4
  private inline val GL_FLOAT                = 0x00001406
  private inline val GL_UNSIGNED_INT         = 0x00001405
  private inline val GL_TRIANGLES            = 0x00000004

  // Pre-calculated byte offsets (Disappears completely at compile time)
  private inline val FLOAT_BYTES   = 4L
  private inline val VERTEX_STRIDE = 6 * FLOAT_BYTES.toInt
  private inline val NORMAL_OFFSET = 3L * FLOAT_BYTES

  private var indexCount: Int = 0

  // 2. BATCHED HARDWARE ID GENERATION
  val vaoId: Int = {
    val ptr = arena.allocate(ValueLayout.JAVA_INT)
    gl.genVertexArrays(1, ptr)
    ptr.get(ValueLayout.JAVA_INT, 0)
  }

  // Optimize: Allocate ONE memory segment for TWO integers, making ONE native downcall
  private val _bufferPtr = arena.allocate(ValueLayout.JAVA_INT, 2)
  gl.genBuffers(2, _bufferPtr)
  
  val vboId: Int = _bufferPtr.getAtIndex(ValueLayout.JAVA_INT, 0)
  val eboId: Int = _bufferPtr.getAtIndex(ValueLayout.JAVA_INT, 1)

  // 3. MESH BUILDER
  def build(vertices: Array[Float], indices: Array[Int]): Unit = {
    indexCount = indices.length
    
    // Bulk memcpy to native memory (Zero-overhead transfer)
    val nativeVertices = BufferUtils.toNative(vertices, arena)
    val nativeIndices  = BufferUtils.toNative(indices, arena)

    gl.bindVertexArray(vaoId)

    // Upload Vertices
    gl.bindBuffer(GL_ARRAY_BUFFER, vboId)
    gl.bufferData(GL_ARRAY_BUFFER, vertices.length * FLOAT_BYTES, nativeVertices, GL_STATIC_DRAW)

    // Upload Indices
    gl.bindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
    gl.bufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * FLOAT_BYTES, nativeIndices, GL_STATIC_DRAW)

    // Setup Attributes (Position)
    gl.vertexAttribPointer(0, 3, GL_FLOAT, 0.toByte, VERTEX_STRIDE, 0L)
    gl.enableVertexAttribArray(0)

    // Setup Attributes (Normal)
    gl.vertexAttribPointer(1, 3, GL_FLOAT, 0.toByte, VERTEX_STRIDE, NORMAL_OFFSET)
    gl.enableVertexAttribArray(1)

    // Unbind VAO safely at initialization time
    gl.bindVertexArray(0)
  }

  // 4. THE HOT LOOP RENDER CALL
  // Using `inline` forces the Scala compiler to paste these two lines 
  // directly into your render loop, bypassing the method call stack entirely!
  inline def draw(): Unit = {
    gl.bindVertexArray(vaoId)
    gl.drawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L)
  }
}
