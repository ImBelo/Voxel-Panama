import java.lang.foreign.*

class InputHandler(glfw: Glfw, window: GlfwWindow, camera: Camera, mouseSensitivity: Float = 0.05f)(using arena: Arena) {
  val mouseHandler = new MouseHandler(camera, mouseSensitivity)
  mouseHandler.register(window.handle, glfw, arena) 
  val keyboardHandler = new KeyboardHandler(glfw,window,camera)

  def update(deltaTime: Float): Unit = {
    mouseHandler.update(arena)
    keyboardHandler.processInput(deltaTime)
  }
}
