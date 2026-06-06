import scala.util.Random

object Noise {
  // Gradient vectors for 2D noise
  private final val grad2: Array[Array[Double]] = Array(
    Array( 1.0,  1.0), Array(-1.0,  1.0), Array( 1.0, -1.0), Array(-1.0, -1.0),
    Array( 1.0,  0.0), Array(-1.0,  0.0), Array( 0.0,  1.0), Array( 0.0, -1.0)
  )

  // Permutation table
  private final val perm: Array[Int] = new Array[Int](512)

  // Seed initialisation (default seed = 0)
  setSeed(0)

  /** Recalculate permutation table for a given seed. */
  def setSeed(seed: Long): Unit = {
    val rnd = new Random(seed)
    val p = (0 until 256).map(_ => rnd.nextInt(256)).toArray
    for (i <- 0 until 256) {
      perm(i) = p(i)
      perm(i + 256) = p(i)
    }
  }

  /** Smoothstep interpolation */
  private inline def fade(t: Double): Double =
    t * t * t * (t * (t * 6.0 - 15.0) + 10.0)

  /** Linear interpolation */
  private inline def lerp(a: Double, b: Double, t: Double): Double =
    a + t * (b - a)

  /** 2D dot product for gradient vectors */
  private inline def dot2(g: Array[Double], x: Double, y: Double): Double =
    g(0) * x + g(1) * y

  /** Core 2D Perlin noise function.
    * @param x  x coordinate (scaled arbitrarily)
    * @param y  y coordinate (scaled arbitrarily)
    * @return   value typically in [-1, 1]
    */
  def perlin(x: Double, y: Double): Double = {
    // Determine grid cell
    val xi = x.floor.toInt & 255
    val yi = y.floor.toInt & 255

    // Relative coordinates inside the cell
    val xf = x - x.floor
    val yf = y - y.floor

    // Fade curves
    val u = fade(xf)
    val v = fade(yf)

    // Hash corners
    val aa = perm(perm(xi) + yi)
    val ab = perm(perm(xi) + yi + 1)
    val ba = perm(perm(xi + 1) + yi)
    val bb = perm(perm(xi + 1) + yi + 1)

    // Gradient mixes
    val x1 = lerp(
      dot2(grad2(aa % 8), xf, yf),
      dot2(grad2(ba % 8), xf - 1, yf),
      u
    )
    val x2 = lerp(
      dot2(grad2(ab % 8), xf, yf - 1),
      dot2(grad2(bb % 8), xf - 1, yf - 1),
      u
    )

    // Final interpolation, scaled to approximately [-1,1]
    lerp(x1, x2, v)
  }
}
