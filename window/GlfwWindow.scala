import java.lang.foreign.*
import GlfwCostants.*
import MemoryUtils.*

class GlfwWindow(width: Int, height: Int, glfw: Glfw, title: String)(using arena: Arena) {
  private val windowTitle = arena.allocateFrom(title) // helper to copy string + null terminator
  val handle: MemorySegment = glfw.createPrimaryWindow(width,height,windowTitle)
  val currentAspectRatio = width.floatValue/height.floatValue()
  if (handle == MemorySegment.NULL) {
    glfw.terminate()
    throw new RuntimeException("Failed to create window")
  }
  def makeContextCurrent(): Unit = glfw.makeContextCurrent(handle)

  def enableRawMouseMotion(): Unit =
    if (glfw.rawMouseMotionSupported())
      glfw.setInputMode(handle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)

  def turnOffVSync(): Unit =
    glfw.swapInterval(0)

  def disableCursor(): Unit =
    glfw.setInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)

  def framebufferSize(outWidth: MemorySegment, outHeight: MemorySegment): (Float, Float) = {
    glfw.getFramebufferSize(handle, outWidth, outHeight)
    val w = outWidth.get(ValueLayout.JAVA_INT, 0).toFloat
    val h = outHeight.get(ValueLayout.JAVA_INT, 0).toFloat
    (w, h)
  }

  def getWidth: Int = {
    val wPtr = java.lang.foreign.Arena.ofAuto().allocate(ValueLayout.JAVA_INT)
    val hPtr = java.lang.foreign.Arena.ofAuto().allocate(ValueLayout.JAVA_INT)
    glfw.getFramebufferSize(handle, wPtr, hPtr)
    wPtr.get(ValueLayout.JAVA_INT, 0)
  }

  def getHeight: Int = {
    val wPtr = java.lang.foreign.Arena.ofAuto().allocate(ValueLayout.JAVA_INT)
    val hPtr = java.lang.foreign.Arena.ofAuto().allocate(ValueLayout.JAVA_INT)
    glfw.getFramebufferSize(handle, wPtr, hPtr)
    hPtr.get(ValueLayout.JAVA_INT, 0)
  }

  // Encapsulate the raw pointer mutation inside the window class safely
  def getCursorPosition(xOutPtr: MemorySegment, yOutPtr: MemorySegment): Unit = {
    glfw.getCursorPos(handle, xOutPtr, yOutPtr)
  }

  def setCursorPosition(x: Double, y: Double): Unit = {
    glfw.setCursorPos(handle, x, y)
  }
  def swapBuffers(): Unit = glfw.swapBuffers(handle)
  def pollEvents(): Unit = glfw.pollEvents()
  def shouldClose(): Boolean = glfw.windowShouldClose(handle) != 0
  def close(): Unit = glfw.setWindowShouldClose(handle,1)
  def isKeyPressed(keyCode: Int): Boolean = {
    glfw.getKey(handle, keyCode) == GlfwCostants.GLFW_PRESS
  }
  //def destroy(): Unit = glfw.destroyWindow(handle)
}
