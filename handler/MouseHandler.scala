import java.lang.foreign.ValueLayout

class MouseHandler(window: GlfwWindow, player: Player, sensitivity: Float = 0.05f) {
  private val arena = java.lang.foreign.Arena.ofAuto()
  private val cursorXPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)
  private val cursorYPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)

  private val deadZone: Int = 10

  // 1. Initialize immediately! No more fragile 'register' boilerplate or 'uninitialized' vars
  private val centerX = window.getWidth * 0.5
  private val centerY = window.getHeight * 0.5
  window.setCursorPosition(centerX, centerY)

  def update(): Unit = {
    // 2. Safely populate our local pointers via the clean window abstraction
    window.getCursorPosition(cursorXPtr, cursorYPtr)
    
    val mouseX = cursorXPtr.get(ValueLayout.JAVA_DOUBLE, 0)
    val mouseY = cursorYPtr.get(ValueLayout.JAVA_DOUBLE, 0)
    
    var changed = false

    // Check X axis (Left / Right turning)
    if (mouseX < centerX - deadZone) {
      player.transform.yaw -= (centerX - mouseX).toFloat * sensitivity
      changed = true
    } else if (mouseX > centerX + deadZone) {
      player.transform.yaw += (mouseX - centerX).toFloat * sensitivity
      changed = true
    }
    
    // Check Y axis (Up / Down looking)
    if (mouseY < centerY - deadZone) {
      player.transform.pitch += (centerY - mouseY).toFloat * sensitivity
      changed = true
    } else if (mouseY > centerY + deadZone) {
      player.transform.pitch -= (mouseY - centerY).toFloat * sensitivity
      changed = true
    }
    
    if (changed) {
      player.updateDirection()
    }
    
    // Always warp cursor back to center to avoid hitting OS screen boundaries
    window.setCursorPosition(centerX, centerY)
  }
}
