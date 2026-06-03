// GL.scala
import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import javax.xml.validation.SchemaFactoryLoader


class GL(lookup: SymbolLookup, linker: Linker) {




  private val glCreateShader_H = linker.downcallHandle(
    lookup.find("glCreateShader").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private val glShaderSource_H = linker.downcallHandle(
    lookup.find("glShaderSource").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS))
  private val glCompileShader_H = linker.downcallHandle(
    lookup.find("glCompileShader").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glCreateProgram_H = linker.downcallHandle(
    lookup.find("glCreateProgram").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT))
  private val glAttachShader_H = linker.downcallHandle(
    lookup.find("glAttachShader").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private val glLinkProgram_H = linker.downcallHandle(
    lookup.find("glLinkProgram").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glUseProgram_H = linker.downcallHandle(
    lookup.find("glUseProgram").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glGetUniformLocation_H = linker.downcallHandle(
    lookup.find("glGetUniformLocation").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private val glUniformMatrix4fv_H = linker.downcallHandle(
    lookup.find("glUniformMatrix4fv").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.ADDRESS))
  private val glClearColor_H = linker.downcallHandle(
    lookup.find("glClearColor").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT))
  private val glClear_H = linker.downcallHandle(
    lookup.find("glClear").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glEnable_H = linker.downcallHandle(
    lookup.find("glEnable").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glGenBuffers_H = linker.downcallHandle(
    lookup.find("glGenBuffers").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private val glBindBuffer_H = linker.downcallHandle(
    lookup.find("glBindBuffer").get(), 
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private val glBufferData_H = linker.downcallHandle(
    lookup.find("glBufferData").get(), 
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))
  private val glEnableVertexAttribArray_H = linker.downcallHandle(
    lookup.find("glEnableVertexAttribArray").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glVertexAttribPointer_H = linker.downcallHandle(
    lookup.find("glVertexAttribPointer").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT,
    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG))
  private val glDrawElements_H = linker.downcallHandle(
    lookup.find("glDrawElements").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG))
  private val glUniform3f_H: MethodHandle = linker.downcallHandle(
    lookup.find("glUniform3f").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT)
  )
  private val glDeleteShader_H: MethodHandle = linker.downcallHandle(
    lookup.find("glDeleteShader").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
  )
  private val glBindVertexArrayHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glBindVertexArray").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
  )
  private val glGenVertexArraysHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glGenVertexArrays").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
  )
  private val glViewportHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glViewport").get(),
    FunctionDescriptor.ofVoid(
      ValueLayout.JAVA_INT, // x
      ValueLayout.JAVA_INT, // y
      ValueLayout.JAVA_INT, // width
      ValueLayout.JAVA_INT  // height
    )
  )
  private val glUniform1fHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glUniform1f").get(),
    FunctionDescriptor.ofVoid(
      ValueLayout.JAVA_INT,  // location: The uniform variable's memory slot index
      ValueLayout.JAVA_FLOAT // v0: The float value to pass to the shader
    )
  )
  private val glDeleteBuffersHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glDeleteBuffers").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
  )

  private val glDeleteVertexArraysHandle: MethodHandle = linker.downcallHandle(
    lookup.find("glDeleteVertexArrays").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
  )
  def deleteBuffers(n: Int, buffers: MemorySegment): Unit =
    glDeleteBuffersHandle.invoke(n, buffers)

  def deleteVertexArrays(n: Int, arrays: MemorySegment): Unit =
    glDeleteVertexArraysHandle.invoke(n, arrays)
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

  def uniform3f(loc: Int,r: Float, g: Float, b: Float) = 
    glUniform3f_H.invoke(loc,r,g,b)

  def createShader(shaderType: Int): Int = glCreateShader_H.invokeExact(shaderType)
  
  def shaderSource(shader: Int, count: Int, stringArray: MemorySegment, lengthArray: MemorySegment): Unit = 
    glShaderSource_H.invoke(shader, count, stringArray, lengthArray)
    
  def compileShader(shader: Int): Unit = glCompileShader_H.invoke(shader)
  
  def createProgram(): Int = glCreateProgram_H.invokeExact()
  
  def attachShader(program: Int, shader: Int): Unit = glAttachShader_H.invoke(program, shader)
  
  def linkProgram(program: Int): Unit = glLinkProgram_H.invoke(program)
  
  def useProgram(program: Int): Unit = glUseProgram_H.invoke(program)
  
  def getUniformLocation(program: Int, name: MemorySegment): Int = glGetUniformLocation_H.invokeExact(program, name)
  
  def uniformMatrix4fv(location: Int, count: Int, transpose: Byte, value: MemorySegment): Unit = 
    glUniformMatrix4fv_H.invoke(location, count, transpose, value)
    
  def clearColor(r: Float, g: Float, b: Float, a: Float): Unit = glClearColor_H.invoke(r, g, b, a)

  def clear(mask: Int): Unit = glClear_H.invoke(mask)
  
  def enable(cap: Int): Unit = glEnable_H.invoke(cap)
  
  def genBuffers(n: Int, buffers: MemorySegment): Unit = glGenBuffers_H.invoke(n, buffers)
  
  def bindBuffer(target: Int, buffer: Int): Unit = glBindBuffer_H.invoke(target, buffer)
  
  def bufferData(target: Int, size: Long, data: MemorySegment, usage: Int): Unit = 
    glBufferData_H.invoke(target, size, data, usage)
    
  def enableVertexAttribArray(index: Int): Unit = glEnableVertexAttribArray_H.invoke(index)
  
  def vertexAttribPointer(index: Int, size: Int, `type`: Int, normalized: Byte, stride: Int, pointer: Long): Unit = 
    glVertexAttribPointer_H.invoke(index, size, `type`, normalized, stride, pointer)

  def drawElements(mode: Int, count: Int, `type`: Int, indices: Long): Unit = 
    glDrawElements_H.invoke(mode, count, `type`, indices)
}
