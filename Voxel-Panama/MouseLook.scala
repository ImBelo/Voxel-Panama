import java.lang.foreign.{MemorySegment, Arena, ValueLayout}
import scala.compiletime.uninitialized


class MouseLook(camera: Camera, sensitivity: Float = 0.5f) {
  private var glfw: Glfw = uninitialized
  private var window: MemorySegment = uninitialized
  private var windowWidth: Int = 1200
  private var windowHeight: Int = 800
  private var deadZone: Int = 10
  
  // Constants - add these if not in your Glfw object
  private final val GLFW_CURSOR = 0x00033001
  private final val GLFW_CURSOR_HIDDEN = 0x00034002
  
  def register(window: MemorySegment, glfw: Glfw, arena: Arena): Unit = {
    this.glfw = glfw
    this.window = window
    
    // Fix: getFramebufferSize requires MemorySegment pointers
    val widthPtr = arena.allocate(ValueLayout.JAVA_INT)
    val heightPtr = arena.allocate(ValueLayout.JAVA_INT)
    glfw.getFramebufferSize(window, widthPtr, heightPtr)
    windowWidth = widthPtr.get(ValueLayout.JAVA_INT, 0)
    windowHeight = heightPtr.get(ValueLayout.JAVA_INT, 0)
    
    
    // Center cursor initially
    glfw.setCursorPos(window, windowWidth / 2.0, windowHeight / 2.0)
  }
  
  // Call this every frame in your game loop
  def update(arena: Arena): Unit = {
    // Fix: getCursorPos requires MemorySegment pointers
    val xposPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)
    val yposPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)
    glfw.getCursorPos(window, xposPtr, yposPtr)
    
    val mouseX = xposPtr.get(ValueLayout.JAVA_DOUBLE, 0)

    val mouseY = yposPtr.get(ValueLayout.JAVA_DOUBLE, 0)
    println(mouseX)
    println(mouseY)
    
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
