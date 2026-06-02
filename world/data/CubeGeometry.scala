object CubeGeometry{
  val stride = 6
  val vertices = Array[Float](
    // --- FRONT FACE (Normal: 0, 0, 1) ---
  -0.5f, -0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 0: Bottom-Left
  0.5f, -0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 1: Bottom-Right
  0.5f,  0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 2: Top-Right
  -0.5f,  0.5f,  0.5f,   0.0f,  0.0f,  1.0f, // 3: Top-Left

  // --- BACK FACE (Normal: 0, 0, -1) ---
  -0.5f, -0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 4: Bottom-Left
  0.5f, -0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 5: Bottom-Right
  0.5f,  0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 6: Top-Right
  -0.5f,  0.5f, -0.5f,   0.0f,  0.0f, -1.0f, // 7: Top-Left

  // --- TOP FACE (Normal: 0, 1, 0) ---
  -0.5f,  0.5f,  0.5f,   0.0f,  1.0f,  0.0f, // 8: Front-Left
  0.5f,  0.5f,  0.5f,   0.0f,  1.0f,  0.0f, // 9: Front-Right
  0.5f,  0.5f, -0.5f,   0.0f,  1.0f,  0.0f, // 10: Back-Right
  -0.5f,  0.5f, -0.5f,   0.0f,  1.0f,  0.0f, // 11: Back-Left

  // --- BOTTOM FACE (Normal: 0, -1, 0) ---
  -0.5f, -0.5f,  0.5f,   0.0f, -1.0f,  0.0f, // 12: Front-Left
  0.5f, -0.5f,  0.5f,   0.0f, -1.0f,  0.0f, // 13: Front-Right
  0.5f, -0.5f, -0.5f,   0.0f, -1.0f,  0.0f, // 14: Back-Right
  -0.5f, -0.5f, -0.5f,   0.0f, -1.0f,  0.0f, // 15: Back-Left

  // --- RIGHT FACE (Normal: 1, 0, 0) ---
  0.5f, -0.5f,  0.5f,   1.0f,  0.0f,  0.0f, // 16: Bottom-Front
  0.5f, -0.5f, -0.5f,   1.0f,  0.0f,  0.0f, // 17: Bottom-Back
  0.5f,  0.5f, -0.5f,   1.0f,  0.0f,  0.0f, // 18: Top-Back
  0.5f,  0.5f,  0.5f,   1.0f,  0.0f,  0.0f, // 19: Top-Front

  // --- LEFT FACE (Normal: -1, 0, 0) ---
  -0.5f, -0.5f,  0.5f,  -1.0f,  0.0f,  0.0f, // 20: Bottom-Front
  -0.5f, -0.5f, -0.5f,  -1.0f,  0.0f,  0.0f, // 21: Bottom-Back
  -0.5f,  0.5f, -0.5f,  -1.0f,  0.0f,  0.0f, // 22: Top-Back
  -0.5f,  0.5f,  0.5f,  -1.0f,  0.0f,  0.0f  // 23: Top-Front
)

  // --- UPGRADED INDEX ARRAY (Mapping the 24 Vertices into Triangles) ---
  val indices = Array[Int](
    0, 1, 2,   2, 3, 0,   // Front
    4, 5, 6,   6, 7, 4,   // Back
    8, 9, 10,  10, 11, 8,  // Top
    12, 13, 14, 14, 15, 12, // Bottom
    16, 17, 18, 18, 19, 16, // Right
    20, 21, 22, 22, 23, 20  // Left
  )
}
