import Timing.DeltaTime

class TimeTracker:
  private var lastTime: Long = System.nanoTime()

  inline def tick(): DeltaTime =
    val now = System.nanoTime()
    val elapsedNanos = now - lastTime
    lastTime = now
    
    // Instantly casts the primitive float math straight into our Opaque Type
    DeltaTime((elapsedNanos / 1_000_000_000.0).toFloat)
