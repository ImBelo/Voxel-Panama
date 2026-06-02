import java.lang.foreign.{MemorySegment, Arena, ValueLayout}
import scala.compiletime.uninitialized


class MouseHandler(camera: Camera, sensitivity: Float = 0.5f) {
  private var glfw: Glfw = uninitialized
  private var window: MemorySegment = uninitialized
  private var cursorXPtr: MemorySegment = uninitialized
  private var cursorYPtr: MemorySegment = uninitialized
  private var windowWidth: Int = uninitialized
  private var windowHeight: Int = uninitialized
  private var deadZone: Int = 10
  private var initialized = false
  // Constants - add these if not in your Glfw object
  
  def register(window: MemorySegment, glfw: Glfw, arena: Arena): Unit = {
    initialized = true
    this.glfw = glfw
    this.window = window
    
    // Allocate
    cursorXPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)
    cursorYPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)
    val widthPtr = arena.allocate(ValueLayout.JAVA_INT)
    val heightPtr = arena.allocate(ValueLayout.JAVA_INT)

    glfw.getFramebufferSize(window, widthPtr, heightPtr)
    windowWidth = widthPtr.get(ValueLayout.JAVA_INT, 0)
    windowHeight = heightPtr.get(ValueLayout.JAVA_INT, 0)
    
    glfw.setCursorPos(window, windowWidth / 2.0, windowHeight / 2.0)
  }
  
  // Call this every frame in your game loop
  def update(arena: Arena): Unit = {
    if (!initialized) return
    glfw.getCursorPos(window, cursorXPtr, cursorYPtr)
    
    val mouseX = cursorXPtr.get(ValueLayout.JAVA_DOUBLE, 0)
    val mouseY = cursorYPtr.get(ValueLayout.JAVA_DOUBLE, 0)
    
    val centerX = windowWidth / 2.0
    val centerY = windowHeight / 2.0
    
    // Check X axis (left/right)
    if (mouseX < centerX - deadZone) {
      val distance = (centerX - mouseX).toFloat
      val turnAmount = distance * sensitivity
      camera.yaw -= turnAmount
      camera.updateDirection()
    }
    else if (mouseX > centerX + deadZone) {

      val distance = (mouseX - centerX).toFloat
      val turnAmount = distance * sensitivity
      camera.yaw += turnAmount
      camera.updateDirection()

    }
    // Check Y axis (up/down)
    if (mouseY < centerY - deadZone) {
      val distance = (centerY - mouseY).toFloat
      val pitchAmount = distance * sensitivity
      camera.pitch += pitchAmount
      if (camera.pitch > 89f) camera.pitch = 89f
      if (camera.pitch < -89f) camera.pitch = -89f
      camera.updateDirection()
    }
    else if (mouseY > centerY + deadZone) {
      val distance = (mouseY - centerY).toFloat
      val pitchAmount = distance * sensitivity
      camera.pitch -= pitchAmount
      if (camera.pitch > 89f) camera.pitch = 89f
      if (camera.pitch < -89f) camera.pitch = -89f
      camera.updateDirection()
    }
    
    // Always warp cursor back to center
    glfw.setCursorPos(window, centerX, centerY)
  }


}
