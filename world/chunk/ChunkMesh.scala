// ChunkMesh.scala
case class ChunkMesh(vertices: Array[Float], indices: Array[Int])

// In Chunk class, add face vertices:
private def addFace(
  verts: scala.collection.mutable.ArrayBuffer[Float],
  inds: scala.collection.mutable.ArrayBuffer[Int],
  x: Int, y: Int, z: Int,
  face: Face,
  startIndex: Int
): Unit = {
  
  // 1. Unrolled direct appends. Zero heap allocations!
  face match {
    case Face.Front => // +Z
      verts ++= Array(
        x+0f, y+0f, z+1f,   0f, 0f, 1f,   0f, 0f,
        x+1f, y+0f, z+1f,   0f, 0f, 1f,   1f, 0f,
        x+1f, y+1f, z+1f,   0f, 0f, 1f,   1f, 1f,
        x+0f, y+1f, z+1f,   0f, 0f, 1f,   0f, 1f 
      )
    case Face.Back => // -Z
      verts ++= Array(
        x+1f, y+0f, z+0f,   0f, 0f, -1f,  0f, 0f,
        x+0f, y+0f, z+0f,   0f, 0f, -1f,  1f, 0f,
        x+0f, y+1f, z+0f,   0f, 0f, -1f,  1f, 1f,
        x+1f, y+1f, z+0f,   0f, 0f, -1f,  0f, 1f 
      )
    case Face.Top => // +Y
      verts ++= Array(
        x+0f, y+1f, z+1f,   0f, 1f, 0f,   0f, 0f,
        x+1f, y+1f, z+1f,   0f, 1f, 0f,   1f, 0f,
        x+1f, y+1f, z+0f,   0f, 1f, 0f,   1f, 1f,
        x+0f, y+1f, z+0f,   0f, 1f, 0f,   0f, 1f 
      )
    case Face.Bottom => // -Y
      verts ++= Array(
        x+0f, y+0f, z+0f,   0f, -1f, 0f,  0f, 0f,
        x+1f, y+0f, z+0f,   0f, -1f, 0f,  1f, 0f,
        x+1f, y+0f, z+1f,   0f, -1f, 0f,  1f, 1f,
        x+0f, y+0f, z+1f,   0f, -1f, 0f,  0f, 1f 
      )
    case Face.Right => // +X
      verts ++= Array(
        x+1f, y+0f, z+1f,   1f, 0f, 0f,   0f, 0f,
        x+1f, y+0f, z+0f,   1f, 0f, 0f,   1f, 0f,
        x+1f, y+1f, z+0f,   1f, 0f, 0f,   1f, 1f,
        x+1f, y+1f, z+1f,   1f, 0f, 0f,   0f, 1f 
      )
    case Face.Left => // -X
      verts ++= Array(
        x+0f, y+0f, z+0f,   -1f, 0f, 0f,  0f, 0f,
        x+0f, y+0f, z+1f,   -1f, 0f, 0f,  1f, 0f,
        x+0f, y+1f, z+1f,   -1f, 0f, 0f,  1f, 1f,
        x+0f, y+1f, z+0f,   -1f, 0f, 0f,  0f, 1f 
      )
  }

  // 2. Add indices for 2 triangles (4 vertices)
  inds ++= Array(
    startIndex, startIndex+1, startIndex+2, 
    startIndex+2, startIndex+3, startIndex  
  )
}
