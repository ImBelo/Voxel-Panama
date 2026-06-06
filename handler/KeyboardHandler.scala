import org.joml.Vector3f
import Timing.DeltaTime
import GlfwCostants.*

class KeyboardHandler(window: GlfwWindow, player: Player) {
  private val moveVector = new Vector3f()
  private val horizontalForward = new Vector3f()
  private val directionAccumulator = new Vector3f()
  private val unitsPerSecond = 20.0f

  def processInput(deltaTime: DeltaTime): Unit = {
    val windowHandle = window.handle
    val frameSpeed = unitsPerSecond * deltaTime.seconds

    if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
      EngineProfiler.printMetrics()
      window.close()
    }
    if (window.isKeyPressed(GLFW_KEY_SPACE)) {
      player.transform.position.y += frameSpeed
    }
    if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
      player.transform.position.y -= frameSpeed
    }
    var forwardInput = 0f
    var strafeInput  = 0f

    if (window.isKeyPressed(GLFW_KEY_W)) forwardInput += 1f
    if (window.isKeyPressed(GLFW_KEY_S)) forwardInput -= 1f
    if (window.isKeyPressed(GLFW_KEY_D)) strafeInput  += 1f
    if (window.isKeyPressed(GLFW_KEY_A)) strafeInput  -= 1f

    player.moveRelativeHorizontal(forwardInput, strafeInput, frameSpeed)
  }
}
