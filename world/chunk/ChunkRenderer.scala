// ChunkRenderer.scala
import java.lang.foreign.*
import org.joml.Matrix4f
class ChunkRenderer(using arena: Arena, gl: GL):
  private val chunkMeshes = scala.collection.mutable.Map[(Int, Int, Int), GpuMesh]()
  
  private val chunkModelMatrix = new Matrix4f()

  def updateChunk(chunk: Chunk, world: World): Unit =
    val mesh = chunk.generateMesh((cx, cy, cz) => world.getChunk(cx, cy, cz))
    
    // 🛑 PREVENT MEMORY LEAK: Destroy old OpenGL buffers before overwriting!
    chunkMeshes.get((chunk.cx, chunk.cy, chunk.cz)).foreach { oldGpuMesh =>
      // Call whatever method you have in GpuMesh to run glDeleteBuffers / glDeleteVertexArrays
      oldGpuMesh.cleanup() 
    }
    
    val gpuMesh = new GpuMesh(gl)
    gpuMesh.build(mesh.vertices, mesh.indices)
    chunkMeshes((chunk.cx, chunk.cy, chunk.cz)) = gpuMesh
  
  def render(shader: ShaderProgram, camera: Camera): Unit =
    import ShaderProgram.given
    for (pos, gpuMesh) <- chunkMeshes do
      val (cx, cy, cz) = pos
      
      if camera.isChunkVisible(cx, cy, cz) then
        // ✅ Reuse the matrix! Reset to identity (0,0,0), then translate. Zero GC allocations!
        chunkModelMatrix.identity()
        chunkModelMatrix.translate(cx * 16f, cy * 16f, cz * 16f)
        
        shader.set("u_Model", chunkModelMatrix)
        gpuMesh.draw()
