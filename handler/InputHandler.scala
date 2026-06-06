import java.lang.foreign.*
import java.lang.foreign.MemorySegment
import Timing.DeltaTime

class InputHandler(window: GlfwWindow, player: Player, mouseSensitivity: Float = 0.05f) {
  val mouseHandler = new MouseHandler(window,player, mouseSensitivity)
  val keyboardHandler = new KeyboardHandler(window, player)

  def update(deltaTime: DeltaTime): Unit = {
    mouseHandler.update()
    keyboardHandler.processInput(deltaTime)
  }
}
