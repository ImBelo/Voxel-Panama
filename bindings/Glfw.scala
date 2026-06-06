// Glfw.scala
import java.lang.foreign.*

import java.lang.invoke.MethodHandle

class Glfw(lookup: SymbolLookup, linker: Linker) {

  private final val glfwInit_H = linker.downcallHandle(
    lookup.find("glfwInit").get(), 
    FunctionDescriptor.of(ValueLayout.JAVA_INT))
  private final val glfwCreateWindow_H = linker.downcallHandle(
    lookup.find("glfwCreateWindow").get(),
    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS))
  private final val glfwMakeContextCurrent_H = linker.downcallHandle(
    lookup.find("glfwMakeContextCurrent").get(), 
    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS))
  private final val glfwSwapBuffers_H = linker.downcallHandle(
    lookup.find("glfwSwapBuffers").get(), 
    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS))
  private final val glfwWindowShouldClose_H = linker.downcallHandle(
    lookup.find("glfwWindowShouldClose").get(), 
    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private final val glfwPollEvents_H = linker.downcallHandle(
    lookup.find("glfwPollEvents").get(), 
    FunctionDescriptor.ofVoid(),
    Linker.Option.critical(false))
  private final val glfwTerminate_H = linker.downcallHandle(
    lookup.find("glfwTerminate").get(), FunctionDescriptor.ofVoid())
  private final val glfwGetKey_H = linker.downcallHandle(
    lookup.find("glfwGetKey").get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))
  private final val glfwGetPrimaryMonitor_H = linker.downcallHandle(
    lookup.find("glfwGetPrimaryMonitor").get(),
    FunctionDescriptor.of(ValueLayout.ADDRESS)
  )

  private final val glfwSetInputMode_H = linker.downcallHandle(
    lookup.find("glfwSetInputMode").get()
  , FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private final val glfwGetCursorPos_H = linker.downcallHandle(
    lookup.find("glfwGetCursorPos").get(),
    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS))
  private final val glfwSetCursorPos_H = linker.downcallHandle(
    lookup.find("glfwSetCursorPos").get(),
    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE))
  private final val glfwGetFramebufferSize_H = linker.downcallHandle(
    lookup.find("glfwGetFramebufferSize").get(),
    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
  )
  private final val glfwSetWindowShouldClose_H = linker.downcallHandle(
    lookup.find("glfwSetWindowShouldClose").get(),
    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT)
  )
  private final val glfwCreateStandardCursor_H = linker.downcallHandle(
    lookup.find("glfwCreateStandardCursor").get(),
    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT)
  )

  private final val glfwRawMouseMotionSupported_H = linker.downcallHandle(
    lookup.find("glfwRawMouseMotionSupported").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT)
  )

  private final val glfwSetCursorPosCallback_H = linker.downcallHandle(
    lookup.find("glfwSetCursorPosCallback").get(),
    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
  )
  private final val glfwSwapIntervalHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glfwSwapInterval").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
  )

  def getPrimaryMonitor(): MemorySegment =
    glfwGetPrimaryMonitor_H.invokeExact().asInstanceOf[MemorySegment]

  def swapInterval(interval: Int): Unit = 
    glfwSwapIntervalHandle.invokeExact(interval)

  def getWindowSize(window: MemorySegment,arena: Arena): (Int, Int) = {
    // Allocate native memory for output values
    val widthPtr = arena.allocate(ValueLayout.JAVA_INT)
    val heightPtr = arena.allocate(ValueLayout.JAVA_INT)
    glfwGetWindowSize_H.invoke(window, widthPtr, heightPtr)
    // Read values from native memory
    val width = widthPtr.get(ValueLayout.JAVA_INT, 0)
    val height = heightPtr.get(ValueLayout.JAVA_INT, 0)
    (width, height)
  }
  private final val glfwGetWindowSize_H = linker.downcallHandle(
    lookup.find("glfwGetWindowSize").get(),
    FunctionDescriptor.ofVoid(
      ValueLayout.ADDRESS,  // window
      ValueLayout.ADDRESS,  // width (output pointer)
      ValueLayout.ADDRESS   // height (output pointer)
    )
  )
  def getFramebufferSize(window: MemorySegment, arena: Arena): (Int, Int) = {
    val widthPtr = arena.allocate(ValueLayout.JAVA_INT)
    val heightPtr = arena.allocate(ValueLayout.JAVA_INT)
    glfwGetFramebufferSize_H.invoke(window, widthPtr, heightPtr)
    (widthPtr.get(ValueLayout.JAVA_INT, 0), heightPtr.get(ValueLayout.JAVA_INT, 0))
  }

  def getCursorPos(window: MemorySegment, arena: Arena): (Double, Double) = {
    val xposPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)
    val yposPtr = arena.allocate(ValueLayout.JAVA_DOUBLE)

    glfwGetCursorPos_H.invoke(window, xposPtr, yposPtr)
    (xposPtr.get(ValueLayout.JAVA_DOUBLE, 0), yposPtr.get(ValueLayout.JAVA_DOUBLE, 0))
  }


  def setCursorPos(window: MemorySegment, xpos: Double, ypos: Double): Unit = {
    glfwSetCursorPos_H.invoke(window, xpos, ypos)
  }


  // 2. Add this to your public wrapped API section
  def setCursorPosCallback(window: MemorySegment, callback: MemorySegment): MemorySegment = {
    glfwSetCursorPosCallback_H.invokeExact(window, callback).asInstanceOf[MemorySegment]
  }
  def rawMouseMotionSupported(): Boolean = {
    val result = glfwRawMouseMotionSupported_H.invoke().asInstanceOf[Int]
    result == 1
  }

// 2. Add these to your public wrapped API section
  def createStandardCursor(shape: Int): MemorySegment =
    glfwCreateStandardCursor_H.invokeExact(shape).asInstanceOf[MemorySegment]

  def setWindowShouldClose(window: MemorySegment, value: Int): Unit =
    glfwSetWindowShouldClose_H.invoke(window, value)

  // Add to your public wrapped API section
  def getFramebufferSize(window: MemorySegment, widthPtr: MemorySegment, heightPtr: MemorySegment): Unit =
    glfwGetFramebufferSize_H.invoke(window, widthPtr, heightPtr)

  // Add these to your public wrapped API section
  def setInputMode(window: MemorySegment, mode: Int, value: Int): Unit = 
    glfwSetInputMode_H.invoke(window, mode, value)

  def getCursorPos(window: MemorySegment, xpos: MemorySegment, ypos: MemorySegment): Unit = 
    glfwGetCursorPos_H.invoke(window, xpos, ypos)

  def init(): Int = glfwInit_H.invokeExact()
  
  def createWindow(w: Int, h: Int, title: MemorySegment, monitor: MemorySegment, share: MemorySegment): MemorySegment = 
    glfwCreateWindow_H.invokeExact(w, h, title, monitor, share).asInstanceOf[MemorySegment]
  def createPrimaryWindow(width: Int,height: Int,title: MemorySegment): MemorySegment = {
    val monitorPtr = getPrimaryMonitor()

    val windowPtr = createWindow(width,height,title, monitorPtr, MemorySegment.NULL)

    if (windowPtr == MemorySegment.NULL) {
      throw new RuntimeException("Failed to create fullscreen GLFW window")
    }
    windowPtr

  }
    
  def makeContextCurrent(window: MemorySegment): Unit = glfwMakeContextCurrent_H.invoke(window)

  
  def swapBuffers(window: MemorySegment): Unit = glfwSwapBuffers_H.invoke(window)
  
  def windowShouldClose(window: MemorySegment): Int = glfwWindowShouldClose_H.invokeExact(window)
  
  def pollEvents(): Unit = glfwPollEvents_H.invoke()
  
  def terminate(): Unit = glfwTerminate_H.invoke()
  
  def getKey(window: MemorySegment, key: Int): Int = glfwGetKey_H.invokeExact(window, key)
}
