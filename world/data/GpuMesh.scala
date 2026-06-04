import java.lang.foreign.{Arena, ValueLayout,MemorySegment}

class GpuMesh(gl: GL)(using arena: Arena) {

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
  private inline val VERTEX_STRIDE = 8 * FLOAT_BYTES.toInt
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
  private inline def FLOAT_SIZE = 4
  private inline def STRIDE = 8 * FLOAT_SIZE

  // 3. MESH BUILDER
  def build(vertices: Array[Float], indices: Array[Int]): Unit = {
    indexCount = indices.length
    
    // Bulk memcpy to native memory (Zero-overhead transfer)
    val nativeVertices = MemoryUtils.toNative(vertices, arena)
    val nativeIndices  = MemoryUtils.toNative(indices, arena)

    gl.bindVertexArray(vaoId)

    upload(GL_ARRAY_BUFFER, vboId, nativeVertices, vertices.length * FLOAT_SIZE)
    upload(GL_ELEMENT_ARRAY_BUFFER, eboId, nativeIndices, indices.length * FLOAT_SIZE)

    setupAttrib(0, 3, 0)
    setupAttrib(1, 3, 3 * FLOAT_SIZE)
    setupAttrib(2, 2, 6 * FLOAT_SIZE)

    gl.bindVertexArray(0)
  }
  private inline def upload(target: Int, id: Int, data: MemorySegment, size: Long): Unit =
      gl.bindBuffer(target, id)
      gl.bufferData(target, size, data, GL_STATIC_DRAW)

  private inline def setupAttrib(index: Int, size: Int, offset: Int): Unit =
    gl.vertexAttribPointer(index, size, GL_FLOAT, 0.toByte, STRIDE, offset.toLong)
    gl.enableVertexAttribArray(index)

  // 4. THE HOT LOOP RENDER CALL
  // Using `inline` forces the Scala compiler to paste these two lines 
  // directly into your render loop, bypassing the method call stack entirely!
  def cleanup(): Unit = {
    // 1. Unbind the VAO for safety before deleting
    gl.bindVertexArray(0)

    // 2. Delete the VBO
    if (vboId != 0) {
      // Allocate 4 bytes of native memory for 1 integer
      val vboSegment = arena.allocate(java.lang.Integer.BYTES)
      // Write the vboId into the native segment
      vboSegment.set(java.lang.foreign.ValueLayout.JAVA_INT, 0, vboId)
      // Pass count (1) and the pointer segment
      gl.deleteBuffers(1, vboSegment)
    }

    // 3. Delete the EBO
    if (eboId != 0) {
      val eboSegment = arena.allocate(java.lang.Integer.BYTES)
      eboSegment.set(java.lang.foreign.ValueLayout.JAVA_INT, 0, eboId)
      gl.deleteBuffers(1, eboSegment)
    }

    // 4. Delete the VAO
    if (vaoId != 0) {
      val vaoSegment = arena.allocate(java.lang.Integer.BYTES)
      vaoSegment.set(java.lang.foreign.ValueLayout.JAVA_INT, 0, vaoId)
      gl.deleteVertexArrays(1, vaoSegment)
    }
  }
  inline def draw(): Unit = {
    gl.bindVertexArray(vaoId)
    gl.drawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L)
  }
}
