import org.joml.Vector3f

class Transform(
  val position: Vector3f = new Vector3f(0.0,0.0,0.0),
  var pitch: Float = 0.0f,
  var yaw: Float = 0.0f
)
