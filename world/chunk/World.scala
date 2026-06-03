// World.scala
class World:
  private val chunks = scala.collection.mutable.Map[(Int, Int, Int), Chunk]()
  val CHUNK_SIZE = 16
  
  val emptyChunk = new Chunk(0, 0, 0):
    override def getBlock(x: Int, y: Int, z: Int): Block = Block.Air
  
  // 2. SAFE LOOKUP: Use this for face culling. It never generates terrain!
  def getChunk(cx: Int, cy: Int, cz: Int): Chunk =
    chunks.getOrElse((cx, cy, cz), emptyChunk)
  
  // 3. EXPLICIT GENERATION: Use this in your main loop when the player moves
  def loadOrGenerateChunk(cx: Int, cy: Int, cz: Int): Chunk =
    chunks.getOrElseUpdate((cx, cy, cz), {
      val chunk = new Chunk(cx, cy, cz)
      generateChunkTerrain(chunk)  
      chunk
    })
  
  def getBlock(worldX: Int, worldY: Int, worldZ: Int): Block =
    val chunk = getChunk(worldX >> 4, worldY >> 4, worldZ >> 4)
    chunk.getBlock(worldX & 15, worldY & 15, worldZ & 15)
  
  def setBlock(worldX: Int, worldY: Int, worldZ: Int, block: Block): Unit =
    val cx = worldX >> 4
    val cy = worldY >> 4
    val cz = worldZ >> 4
    val chunk = getChunk(cx, cy, cz)
    chunk.setBlock(worldX & 15, worldY & 15, worldZ & 15, block)
    
   /* // If block is on chunk boundary, mark neighbor dirty too
    if worldX & 15 == 0 then getChunk(cx-1, cy, cz).markDirty()
    if worldX & 15 == 15 then getChunk(cx+1, cy, cz).markDirty()*/
    // ... same for Y and Z
  
  private def generateChunkTerrain(chunk: Chunk): Unit =
    // Simple heightmap terrain
    for
      x <- 0 until CHUNK_SIZE
      y <- 0 until CHUNK_SIZE
      z <- 0 until CHUNK_SIZE
    do
      val worldX = chunk.cx * CHUNK_SIZE + x
      val worldZ = chunk.cz * CHUNK_SIZE + z
     // val height = (Noise.perlin(worldX * 0.01, worldZ * 0.01) * 10 + 20).toInt
      chunk.setBlock(x,y,z, Block.Dirt)

    /*  for y <- 0 until height.min(CHUNK_SIZE) do
        val block = y match
          case h if h == height - 1 => Block.Grass
          case h if h > height - 5 => Block.Dirt
          case _ => Block.Stone
        chunk.setBlock(x, y, z, block)*/
