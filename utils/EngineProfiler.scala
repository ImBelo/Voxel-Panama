object EngineProfiler {
  // Toggle this at compile-time. When false, the compiler completely rips out 
  // the tracking code from your render loop!
  final val Enabled = true 

  private val metrics = java.util.concurrent.ConcurrentHashMap[String, Long]()

  // Inline means zero allocation and zero lambda-overhead per frame
  inline def zone[A](name: String)(inline block: => A): A = {
    if (Enabled) {
      val start = System.nanoTime()
      val result = block
      val duration = System.nanoTime() - start
      
      // Store or accumulate the time (simple overwrite for tracking current frame time)
      metrics.put(name, duration)
      result
    } else {
      block
    }
  }

  def printMetrics(): Unit = {
    if (Enabled) {
      import scala.jdk.CollectionConverters._
      println("\n--- ENGINE PERFORMANCE METRICS ---")
      metrics.asScala.foreach { case (name, nano) =>
        println(f"$name%-25s : ${nano / 1_000_000.0}%6.3f ms")
      }
    }
  }
}
