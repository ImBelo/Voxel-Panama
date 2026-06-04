// GL.scala
import java.lang.foreign.*
import java.lang.foreign.Linker.Option.*

import java.lang.invoke.MethodHandle
import javax.xml.validation.SchemaFactoryLoader


class GL(lookup: SymbolLookup, linker: Linker) {
  private final val glCreateShader_H: MethodHandle = linker.downcallHandle(
    lookup.find("glCreateShader").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT),
    Linker.Option.critical(false) // 🔥 Modern replacement for isTrivial()
  )

  // 2. Trivial state/setter functions should always use Linker.Option.isTrivial()
  private final val glShaderSource_H: MethodHandle = linker.downcallHandle(
    lookup.find("glShaderSource").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS),
    Linker.Option.critical(false) // 🔥 Modern replacement for isTrivial()

  )

  private final val glCompileShader_H: MethodHandle = linker.downcallHandle(
    lookup.find("glCompileShader").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT),
    Linker.Option.critical(false) // 🔥 Modern replacement for isTrivial()
  )
  private final val glCreateProgram_H = linker.downcallHandle(
    lookup.find("glCreateProgram").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT))
  private final val glAttachShader_H = linker.downcallHandle(
    lookup.find("glAttachShader").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private final val glLinkProgram_H = linker.downcallHandle(
    lookup.find("glLinkProgram").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private final val glUseProgram_H = linker.downcallHandle(
    lookup.find("glUseProgram").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private final val glGetUniformLocation_H = linker.downcallHandle(
    lookup.find("glGetUniformLocation").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private final val glUniformMatrix4fv_H = linker.downcallHandle(
    lookup.find("glUniformMatrix4fv").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.ADDRESS))
  private final val glClearColor_H = linker.downcallHandle(
    lookup.find("glClearColor").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT))
  private final val glClear_H = linker.downcallHandle(
    lookup.find("glClear").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private final val glEnable_H = linker.downcallHandle(
    lookup.find("glEnable").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private final val glGenBuffers_H = linker.downcallHandle(
    lookup.find("glGenBuffers").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private final val glBindBuffer_H = linker.downcallHandle(
    lookup.find("glBindBuffer").get(), 
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private final val glBufferData_H = linker.downcallHandle(
    lookup.find("glBufferData").get(), 
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))
  private final val glEnableVertexAttribArray_H = linker.downcallHandle(
    lookup.find("glEnableVertexAttribArray").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private final val glVertexAttribPointer_H = linker.downcallHandle(
    lookup.find("glVertexAttribPointer").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT,
    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG))
  private final val glDrawElements_H = linker.downcallHandle(
    lookup.find("glDrawElements").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG))
  private final val glUniform3f_H: MethodHandle = linker.downcallHandle(
    lookup.find("glUniform3f").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT)
  )
  private final val glDeleteShader_H: MethodHandle = linker.downcallHandle(
    lookup.find("glDeleteShader").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
  )
  private final val glBindVertexArrayHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glBindVertexArray").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
  )
  private final val glGenVertexArraysHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glGenVertexArrays").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
  )
  private final val glViewportHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glViewport").get(),
    FunctionDescriptor.ofVoid(
      ValueLayout.JAVA_INT, // x
      ValueLayout.JAVA_INT, // y
      ValueLayout.JAVA_INT, // width
      ValueLayout.JAVA_INT  // height
    )
  )
  private final val glUniform1fHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glUniform1f").get(),
    FunctionDescriptor.ofVoid(
      ValueLayout.JAVA_INT,  // location: The uniform variable's memory slot index
      ValueLayout.JAVA_FLOAT // v0: The float value to pass to the shader
    )
  )
  private final val glDeleteBuffersHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glDeleteBuffers").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
  )

  private final val glDeleteVertexArraysHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glDeleteVertexArrays").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
  )
  def deleteBuffers(n: Int, buffers: MemorySegment): Unit =
    glDeleteBuffersHandle.invokeExact(n, buffers)

  def deleteVertexArrays(n: Int, arrays: MemorySegment): Unit =
    glDeleteVertexArraysHandle.invokeExact(n, arrays)
  def uniform1f(location: Int, value: Float): Unit = {
    glUniform1fHandle.invokeExact(location, value)
  }

  def viewport(x: Int, y: Int, width: Int, height: Int): Unit = {
    glViewportHandle.invokeExact(x, y, width, height)
  }
  def genVertexArrays(n: Int, arraysOutPointer: MemorySegment): Unit = {
    glGenVertexArraysHandle.invokeExact(n, arraysOutPointer)
  }
  def bindVertexArray(arrayId: Int): Unit = {
    glBindVertexArrayHandle.invokeExact(arrayId)
  }

  def deleteShader(shader: Int): Unit = 
    glDeleteShader_H.invokeExact(shader)

  def uniform3f(loc: Int,r: Float, g: Float, b: Float): Unit = 
    glUniform3f_H.invokeExact(loc,r,g,b)

  def createShader(shaderType: Int): Int = glCreateShader_H.invokeExact(shaderType)
  
  def shaderSource(shader: Int, count: Int, stringArray: MemorySegment, lengthArray: MemorySegment): Unit = 
    glShaderSource_H.invokeExact(shader, count, stringArray, lengthArray)
    
  def compileShader(shader: Int): Unit = glCompileShader_H.invokeExact(shader)
  
  def createProgram(): Int = glCreateProgram_H.invokeExact()
  
  def attachShader(program: Int, shader: Int): Unit = glAttachShader_H.invokeExact(program, shader)
  
  def linkProgram(program: Int): Unit = glLinkProgram_H.invokeExact(program)
  
  def useProgram(program: Int): Unit = glUseProgram_H.invokeExact(program)
  
  def getUniformLocation(program: Int, name: MemorySegment): Int = glGetUniformLocation_H.invokeExact(program, name)
  
  def uniformMatrix4fv(location: Int, count: Int, transpose: Byte, value: MemorySegment): Unit = 
    glUniformMatrix4fv_H.invokeExact(location, count, transpose, value)
    
  def clearColor(r: Float, g: Float, b: Float, a: Float): Unit = glClearColor_H.invokeExact(r, g, b, a)

  def clear(mask: Int): Unit = glClear_H.invokeExact(mask)
  
  def enable(cap: Int): Unit = glEnable_H.invokeExact(cap)
  
  def genBuffers(n: Int, buffers: MemorySegment): Unit = glGenBuffers_H.invokeExact(n, buffers)
  
  def bindBuffer(target: Int, buffer: Int): Unit = glBindBuffer_H.invokeExact(target, buffer)
  
  def bufferData(target: Int, size: Long, data: MemorySegment, usage: Int): Unit = 
    glBufferData_H.invokeExact(target, size, data, usage)
    
  def enableVertexAttribArray(index: Int): Unit = glEnableVertexAttribArray_H.invokeExact(index)
  
  def vertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Byte, stride: Int, pointer: Long): Unit = 
    glVertexAttribPointer_H.invokeExact(index, size, `type`, normalized, stride, pointer)

  def drawElements(mode: Int, count: Int, `type`: Int, indices: Long): Unit = 
    glDrawElements_H.invokeExact(mode, count, `type`, indices)
}
