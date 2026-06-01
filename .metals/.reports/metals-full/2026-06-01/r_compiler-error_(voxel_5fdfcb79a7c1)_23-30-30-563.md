error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/Glfw.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/Glfw.scala
text:
```scala
// Glfw.scala
import java.lang.foreign.*

import java.lang.invoke.MethodHandle

class Glfw(lookup: SymbolLookup, linker: Linker) {

  private val glfwInit_H = linker.downcallHandle(lookup.find("glfwInit").get(), FunctionDescriptor.of(ValueLayout.JAVA_INT))
  private val glfwCreateWindow_H = linker.downcallHandle(lookup.find("glfwCreateWindow").get(), FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS))
  private val glfwMakeContextCurrent_H = linker.downcallHandle(lookup.find("glfwMakeContextCurrent").get(), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS))
  private val glfwSwapBuffers_H = linker.downcallHandle(lookup.find("glfwSwapBuffers").get(), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS))
  private val glfwWindowShouldClose_H = linker.downcallHandle(lookup.find("glfwWindowShouldClose").get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private val glfwPollEvents_H = linker.downcallHandle(lookup.find("glfwPollEvents").get(), FunctionDescriptor.ofVoid())
  private val glfwTerminate_H = linker.downcallHandle(lookup.find("glfwTerminate").get(), FunctionDescriptor.ofVoid())
  private val glfwGetKey_H = linker.downcallHandle(lookup.find("glfwGetKey").get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))
  val GLFW_CURSOR = 0x00033001
  val GLFW_CURSOR_NORMAL = 0x00034001
  val GLFW_CURSOR_DISABLED = 0x00034003
  val GLFW_CURSOR_HIDDEN = 0x00034002 // Add this line
  val GLFW_TRUE = 1
  val GLFW_RAW_MOUSE_MOTION = 0x00033005
  val GLFW_CURSOR_ARROW = 0x00036001 // Standard arrow shape constant if needed

  val GLFW_KEY_W: Int             = 87
  val GLFW_KEY_A: Int             = 65
  val GLFW_KEY_S: Int             = 83
  val GLFW_KEY_D: Int             = 68

  // --- Action Keys ---
  val GLFW_KEY_SPACE: Int         = 32
  val GLFW_KEY_LEFT_SHIFT: Int    = 340
  val GLFW_KEY_LEFT_CONTROL: Int  = 341
  val GLFW_KEY_ESCAPE: Int        = 256
  val GLFW_KEY_ENTER: Int         = 257

  val GLFW_RELEASE: Int  = 0
  val GLFW_PRESS: Int    = 1
  val GLFW_REPEAT: Int   = 2

  private val glfwGetPrimaryMonitor_H = linker.downcallHandle(
  lookup.find("glfwGetPrimaryMonitor").get(),
  FunctionDescriptor.of(ValueLayout.ADDRESS)
)

  def getPrimaryMonitor(): MemorySegment =
    glfwGetPrimaryMonitor_H.invokeExact().asInstanceOf[MemorySegment]

  // Add these to your private downcall handles list
  private val glfwSetInputMode_H = linker.downcallHandle(lookup.find("glfwSetInputMode").get(), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private val glfwGetCursorPos_H = linker.downcallHandle(lookup.find("glfwGetCursorPos").get(), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS))

  private val glfwSetCursorPos_H = linker.downcallHandle(lookup.find("glfwSetCursorPos").get(), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE))
  private val glfwGetFramebufferSize_H = linker.downcallHandle(
  lookup.find("glfwGetFramebufferSize").get(),
  FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
)
  private val glfwSetWindowShouldClose_H = linker.downcallHandle(
  lookup.find("glfwSetWindowShouldClose").get(),
  FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT)
)
  private val glfwCreateStandardCursor_H = linker.downcallHandle(
  lookup.find("glfwCreateStandardCursor").get(),
  FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT)
)

  private val glfwRawMouseMotionSupported_H = linker.downcallHandle(
  lookup.find("glfwRawMouseMotionSupported").get(),
  FunctionDescriptor.of(ValueLayout.JAVA_INT)
)

  private val glfwSetCursorPosCallback_H = linker.downcallHandle(
    lookup.find("glfwSetCursorPosCallback").get(),
    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
  )
  private val glfwSwapIntervalHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glfwSwapInterval").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
  )
  
  /**
   * Sets the swap interval for the current OpenGL context (VSync toggle).
   * * @param interval 0 to disable VSync (unlocked framerate), 
   * 1 to sync with monitor refresh rate.
   */
  def swapInterval(interval: Int): Unit = {
      glfwSwapIntervalHandle.invokeExact(interval)
      case e: Throwable => throw new RuntimeException("Failed to execute glfwSwapInterval", e)
    }
  }

 
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

  private val glfwGetWindowSize_H = linker.downcallHandle(

    lookup.find("glfwGetWindowSize").get(),
    FunctionDescriptor.ofVoid(
      ValueLayout.ADDRESS,  // window
      ValueLayout.ADDRESS,  // width (output pointer)

      ValueLayout.ADDRESS   // height (output pointer)
    )
  )

  // Add these to your Glfw class
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

```


presentation compiler configuration:
Scala version: 3.8.2-bin-nonbootstrapped
Classpath:
<WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.2/scala3-library_3-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/joml/joml/1.10.8/joml-1.10.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.2/scala-library-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/sourcegraph/semanticdb-javac/0.10.0/semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main/META-INF/best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -release 21 -Ywith-best-effort-tasty




#### Error stacktrace:

```
dotty.tools.pc.DiagnosticProvider.diagnostics(DiagnosticProvider.scala:19)
	dotty.tools.pc.ScalaPresentationCompiler.didChange$$anonfun$1(ScalaPresentationCompiler.scala:509)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.withNonInterruptableCompiler$$anonfun$1(CompilerAccess.scala:133)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1516)
```
#### Short summary: 

java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'