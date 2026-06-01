import java.lang.foreign.*

class GlfwWindow(width: Int, height: Int, glfw: Glfw, title: String, arena: Arena) {
  private val windowTitle = arena.allocateFrom(title) // helper to copy string + null terminator
  val handle: MemorySegment = glfw.createPrimaryWindow(width,height,windowTitle)
  val currentAspectRatio = width/height
  if (handle == MemorySegment.NULL) {
    glfw.terminate()
    throw new RuntimeException("Failed to create window")
  }

  def makeContextCurrent(): Unit = glfw.makeContextCurrent(handle)

  def enableRawMouseMotion(): Unit =
    if (glfw.rawMouseMotionSupported())
      glfw.setInputMode(handle, glfw.GLFW_RAW_MOUSE_MOTION, glfw.GLFW_TRUE)

  def disableCursor(): Unit =
    glfw.setInputMode(handle, glfw.GLFW_CURSOR, glfw.GLFW_CURSOR_DISABLED)

  def framebufferSize(outWidth: MemorySegment, outHeight: MemorySegment): (Float, Float) = {
    glfw.getFramebufferSize(handle, outWidth, outHeight)
    val w = outWidth.get(ValueLayout.JAVA_INT, 0).toFloat
    val h = outHeight.get(ValueLayout.JAVA_INT, 0).toFloat
    (w, h)
  }

  def swapBuffers(): Unit = glfw.swapBuffers(handle)
  def pollEvents(): Unit = glfw.pollEvents()
  def shouldClose(): Boolean = glfw.windowShouldClose(handle) != 0
  //def destroy(): Unit = glfw.destroyWindow(handle)
}
