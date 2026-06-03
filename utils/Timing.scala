
object Timing:
  // Outside this object, DeltaTime is an completely opaque type with no public constructor
  opaque type DeltaTime = Float

  object DeltaTime:
    // Safe factory method to wrap our raw primitive float
    inline def apply(seconds: Float): DeltaTime = seconds

  // Attach zero-cost helper methods directly onto our opaque type
  extension (dt: DeltaTime)
    inline def seconds: Float = dt
    inline def milliseconds: Float = dt * 1000.0f
    inline def toFPS: Float = if (dt > 0.0f) 1.0f / dt else 0.0f
