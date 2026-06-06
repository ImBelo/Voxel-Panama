import org.joml.Vector3f
import Timing.DeltaTime

class Player {
  val transform: Transform = new Transform()
  val position = transform.position
  private val unitsPerSecond = 20.0f



  // Pre-allocated orientation vectors
  val forward = new Vector3f(0.0f, 0.0f, -1.0f)
  val up      = new Vector3f(0.0f, 1.0f, 0.0f)
  val right   = new Vector3f(1.0f, 0.0f, 0.0f) // Fixed: Now visible to KeyboardHandler
  private val horizontalForward = new Vector3f()
  private val directionAccumulator = new Vector3f()
  private val moveVector = new Vector3f()
  def moveUp(deltaTime: DeltaTime) = {
    val frameSpeed = unitsPerSecond * deltaTime.seconds
    position.y += frameSpeed
  }
  def moveDown(deltaTime:DeltaTime) = {
    val frameSpeed = unitsPerSecond * deltaTime.seconds
    position.y -= frameSpeed
  }
  


  def updateDirection(): Unit = {
    // Clamp pitch to prevent flipping upside down
    if (transform.pitch > 89.0f) transform.pitch = 89.0f
    if (transform.pitch < -89.0f) transform.pitch = -89.0f

    // Calculate new forward vector based on player transform yaw/pitch
    forward.x = (Math.cos(Math.toRadians(transform.yaw)) * Math.cos(Math.toRadians(transform.pitch))).toFloat
    forward.y = Math.sin(Math.toRadians(transform.pitch)).toFloat
    forward.z = (Math.sin(Math.toRadians(transform.yaw)) * Math.cos(Math.toRadians(transform.pitch))).toFloat
    forward.normalize()
    
    // Dynamically recalculate the right vector based on new forward
    forward.cross(up, right).normalize()
  }
  def moveRelativeHorizontal(forwardInput: Float, strafeInput: Float, speed: Float): Unit = {
    if (forwardInput == 0f && strafeInput == 0f) return

    directionAccumulator.set(0f, 0f, 0f)
    
    // Project the forward vector onto the XZ plane so the player doesn't fly/sink when looking up/down
    horizontalForward.set(forward.x, 0f, forward.z).normalize()

    // Accumulate the movement intents
    if (forwardInput != 0f) {
      directionAccumulator.add(horizontalForward.x * forwardInput, 0f, horizontalForward.z * forwardInput)
    }
    if (strafeInput != 0f) {
      directionAccumulator.add(right.x * strafeInput, 0f, right.z * strafeInput)
    }

    // Apply the movement safely
    if (directionAccumulator.lengthSquared() > 0f) {
      directionAccumulator.normalize().mul(speed, moveVector)
      position.add(moveVector)
    }
  }
}
