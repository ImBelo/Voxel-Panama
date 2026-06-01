error id: 07F2D7E8BA0ECD32ED250EA196DBFC29
file://<WORKSPACE>/GL.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.



action parameters:
offset: 3283
uri: file://<WORKSPACE>/GL.scala
text:
```scala
// GL.scala
import java.lang.foreign.*
import java.lang.invoke.MethodHandle


class GL(lookup: SymbolLookup, linker: Linker) {


  private val glCreateShader_H = linker.downcallHandle(lookup.find("glCreateShader").get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private val glShaderSource_H = linker.downcallHandle(lookup.find("glShaderSource").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS))
  private val glCompileShader_H = linker.downcallHandle(lookup.find("glCompileShader").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glCreateProgram_H = linker.downcallHandle(lookup.find("glCreateProgram").get(), FunctionDescriptor.of(ValueLayout.JAVA_INT))
  private val glAttachShader_H = linker.downcallHandle(lookup.find("glAttachShader").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private val glLinkProgram_H = linker.downcallHandle(lookup.find("glLinkProgram").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glUseProgram_H = linker.downcallHandle(lookup.find("glUseProgram").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))

  private val glGetUniformLocation_H = linker.downcallHandle(lookup.find("glGetUniformLocation").get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private val glUniformMatrix4fv_H = linker.downcallHandle(lookup.find("glUniformMatrix4fv").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.ADDRESS))
  private val glClearColor_H = linker.downcallHandle(lookup.find("glClearColor").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT))
  private val glClear_H = linker.downcallHandle(lookup.find("glClear").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glEnable_H = linker.downcallHandle(lookup.find("glEnable").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glGenBuffers_H = linker.downcallHandle(lookup.find("glGenBuffers").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.ADDRESS))
  private val glBindBuffer_H = linker.downcallHandle(lookup.find("glBindBuffer").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT))
  private val glBufferData_H = linker.downcallHandle(lookup.find("glBufferData").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))
  private val glEnableVertexAttribArray_H = linker.downcallHandle(lookup.find("glEnableVertexAttribArray").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
  private val glVertexAttribPointer_H = linker.downcallHandle(lookup.find("glVertexAttribPointer").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG))
  private val glDrawElements_H = linker.downcallHandle(lookup.find("glDrawElements").get(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG))

  private val glGetUniformLocation_@@: MethodHandle = linker.downcallHandle(
    lookup.find("glGetUniformLocation").get(),
    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
  )

  // glUniform3f(GLint location, GLfloat v0, GLfloat v1, GLfloat v2)
  private val glUniform3f: MethodHandle = linker.downcallHandle(
    lookup.find("glUniform3f").get(),
    FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT)
  )

  def getUniformLocation(program: Int,uniform: Int): MemorySegment =
    glGetUniformLocation(program,uniform)
  
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

```


presentation compiler configuration:
Scala version: 3.7.4-bin-nonbootstrapped
Classpath:
<WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.7.4/scala3-library_3-3.7.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/joml/joml/1.10.8/joml-1.10.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.16/scala-library-2.13.16.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/sourcegraph/semanticdb-javac/0.10.0/semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main/META-INF/best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -Ywith-best-effort-tasty




#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:131)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:128)
	scala.collection.immutable.List.apply(List.scala:79)
	dotty.tools.pc.InferCompletionType$.inferType(InferExpectedType.scala:94)
	dotty.tools.pc.InferCompletionType$.inferType(InferExpectedType.scala:62)
	dotty.tools.pc.completions.Completions.advancedCompletions(Completions.scala:542)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:131)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:139)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:197)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1583)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 0