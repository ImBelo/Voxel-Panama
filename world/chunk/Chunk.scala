// Chunk.scala
class Chunk(val cx: Int, val cy: Int, val cz: Int):
  val CHUNK_SIZE = 16
  
  // 3D array of blocks
  private val blocks = Array.fill[Block](CHUNK_SIZE, CHUNK_SIZE, CHUNK_SIZE)(Block.Air)
  val needsRemesh = false
  
  // Mesh data (regenerated when blocks change)
  private var mesh: Option[ChunkMesh] = None
  private var meshDirty = true
  
  def getBlock(x: Int, y: Int, z: Int): Block =
    if x >= 0 && x < CHUNK_SIZE && y >= 0 && y < CHUNK_SIZE && z >= 0 && z < CHUNK_SIZE then
      blocks(x)(y)(z)
    else Block.Air
  
  def setBlock(x: Int, y: Int, z: Int, block: Block): Unit =
    blocks(x)(y)(z) = block
    meshDirty = true
  
  def isBlockSolid(x: Int, y: Int, z: Int): Boolean =
    getBlock(x, y, z).isSolid
  
  // Generate mesh only visible faces
  def generateMesh(getChunkNeighbor: (Int, Int, Int) => Chunk): ChunkMesh = {
    val verts= scala.collection.mutable.ArrayBuffer[Float]()
    val inds = scala.collection.mutable.ArrayBuffer[Int]()
    var vertexCount = 0

    for{
      x <- 0 until CHUNK_SIZE
      y <- 0 until CHUNK_SIZE
      z <- 0 until CHUNK_SIZE
      }{
        if blocks(x)(y)(z) != Block.Air then {
          // --- FRONT FACE (+Z) ---
          if (isFaceVisible(x, y, z + 1, getChunkNeighbor)) {
            addFace(verts, inds, x, y, z, Face.Front, vertexCount)
            vertexCount += 4
          }

          // --- BACK FACE (-Z) ---
          if (isFaceVisible(x, y, z - 1, getChunkNeighbor)) {
            addFace(verts, inds, x, y, z, Face.Back, vertexCount)
            vertexCount += 4
          }

          // --- TOP FACE (+Y) ---
          if (isFaceVisible(x, y + 1, z, getChunkNeighbor)) {
            addFace(verts, inds, x, y, z, Face.Top, vertexCount)
            vertexCount += 4
          }

          // --- BOTTOM FACE (-Y) ---
          if (isFaceVisible(x, y - 1, z, getChunkNeighbor)) {
            addFace(verts, inds, x, y, z, Face.Bottom, vertexCount)
            vertexCount += 4
          }

          // --- RIGHT FACE (+X) ---
          if (isFaceVisible(x + 1, y, z, getChunkNeighbor)) {
            addFace(verts, inds, x, y, z, Face.Right, vertexCount)
            vertexCount += 4
          }

          // --- LEFT FACE (-X) ---
          if (isFaceVisible(x - 1, y, z, getChunkNeighbor)) {
            addFace(verts, inds, x, y, z, Face.Left, vertexCount)
            vertexCount += 4
          }
        }
      }


    ChunkMesh(verts.toArray, inds.toArray)
  }

    private def isFaceVisible(nx: Int, ny: Int, nz: Int, getChunkNeighbor: (Int, Int, Int) => Chunk): Boolean = {
      // If Y goes out of world vertical bounds, just draw the face
      if (ny < 0 || ny >= 256) return true 

      // Check if neighbor is within THIS current chunk boundaries
      if (nx >= 0 && nx < 16 && ny >= 0 && ny < 16 && nz >= 0 && nz < 16) {
        return getBlock(nx, ny, nz) == Block.Air // Visible if neighbor is Air
      }

      // Border Control: Calculate which neighboring chunk we need to ask
      val targetCx = this.cx + Math.floorDiv(nx, 16)
      val targetCy = this.cy + Math.floorDiv(ny, 16)
      val targetCz = this.cz + Math.floorDiv(nz, 16)

      // Normalize coordinates to local 0-15 space for the neighbor chunk
      val localX = Math.floorMod(nx, 16)
      val localY = Math.floorMod(ny, 16)
      val localZ = Math.floorMod(nz, 16)

      val neighbor = getChunkNeighbor(targetCx, targetCy, targetCz)
      neighbor.getBlock(localX, localY, localZ) == Block.Air
    }
  
  private def isNeighborSolid(
    x: Int, y: Int, z: Int, face: Face,
    neighborGetter: (Int, Int, Int) => Chunk
  ): Boolean =
    val worldX = cx * CHUNK_SIZE + x + face.dx
    val worldY = cy * CHUNK_SIZE + y + face.dy
    val worldZ = cz * CHUNK_SIZE + z + face.dz
    
    val chunkX = worldX >> 4  // Divide by 16
    val chunkY = worldY >> 4
    val chunkZ = worldZ >> 4
    
    val localX = worldX & 15  // Mod 16
    val localY = worldY & 15
    val localZ = worldZ & 15
    
    val chunk = neighborGetter(chunkX, chunkY, chunkZ)
    if chunk == null then false
    else chunk.isBlockSolid(localX, localY, localZ)
