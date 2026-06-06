// ChunkMesh.scala
case class ChunkMesh(vertices: Array[Float], indices: Array[Int])

// In Chunk class, add face vertices:
private def addFace(
  verts: scala.collection.mutable.ArrayBuffer[Float],
  inds: scala.collection.mutable.ArrayBuffer[Int],
  x: Int, y: Int, z: Int,
  face: Face,
  startIndex: Int,
  blockType: Int 
): Unit = {
  
  val atlasIndex = blockType - 1
  val col = atlasIndex % 4
  val row = atlasIndex / 4

  // 1. Calculate base percentage boundaries (0.0 to 1.0)
  val uMin = col * 0.25f
  val uMax = uMin + 0.25f
  
  // OpenGL reads from bottom-to-top, so row 0 is at the top (1.0f)
  val vMax = 1.0f - (row * 0.25f) 
  val vMin = vMax - 0.25f      

  // Your unrolled face match statement stays EXACTLY the same below...
  // 1. Unrolled direct appends. Zero heap allocations!
  face match {
    case Face.Front => // +Z
      verts ++= Array(
        // Pos(x,y,z)         // Normal(nx,ny,nz)  // UVs(u,v)
        x+0f, y+0f, z+1f,   0f, 0f, 1f,   uMin, vMin,
        x+1f, y+0f, z+1f,   0f, 0f, 1f,   uMax, vMin,
        x+1f, y+1f, z+1f,   0f, 0f, 1f,   uMax, vMax,
        x+0f, y+1f, z+1f,   0f, 0f, 1f,   uMin, vMax 
      )
    case Face.Back => // -Z
      verts ++= Array(
        x+1f, y+0f, z+0f,   0f, 0f, -1f,  uMin, vMin,
        x+0f, y+0f, z+0f,   0f, 0f, -1f,  uMax, vMin,
        x+0f, y+1f, z+0f,   0f, 0f, -1f,  uMax, vMax,
        x+1f, y+1f, z+0f,   0f, 0f, -1f,  uMin, vMax 
      )
    case Face.Top => // +Y
      verts ++= Array(
        x+0f, y+1f, z+1f,   0f, 1f, 0f,   uMin, vMin,
        x+1f, y+1f, z+1f,   0f, 1f, 0f,   uMax, vMin,
        x+1f, y+1f, z+0f,   0f, 1f, 0f,   uMax, vMax,
        x+0f, y+1f, z+0f,   0f, 1f, 0f,   uMin, vMax 
      )
    case Face.Bottom => // -Y
      verts ++= Array(
        x+0f, y+0f, z+0f,   0f, -1f, 0f,  uMin, vMin,
        x+1f, y+0f, z+0f,   0f, -1f, 0f,  uMax, vMin,
        x+1f, y+0f, z+1f,   0f, -1f, 0f,  uMax, vMax,
        x+0f, y+0f, z+1f,   0f, -1f, 0f,  uMin, vMax 
      )
    case Face.Right => // +X
      verts ++= Array(
        x+1f, y+0f, z+1f,   1f, 0f, 0f,   uMin, vMin,
        x+1f, y+0f, z+0f,   1f, 0f, 0f,   uMax, vMin,
        x+1f, y+1f, z+0f,   1f, 0f, 0f,   uMax, vMax,
        x+1f, y+1f, z+1f,   1f, 0f, 0f,   uMin, vMax 
      )
    case Face.Left => // -X
      verts ++= Array(
        x+0f, y+0f, z+0f,   -1f, 0f, 0f,  uMin, vMin,
        x+0f, y+0f, z+1f,   -1f, 0f, 0f,  uMax, vMin,
        x+0f, y+1f, z+1f,   -1f, 0f, 0f,  uMax, vMax,
        x+0f, y+1f, z+0f,   -1f, 0f, 0f,  uMin, vMax 
      )
  }

  // 2. Add indices for 2 triangles (4 vertices)
  inds ++= Array(
    startIndex, startIndex+1, startIndex+2, 
    startIndex+2, startIndex+3, startIndex  
  )
}
