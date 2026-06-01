// Shader.scala
import java.lang.foreign.*
import java.nio.charset.StandardCharsets
import org.joml.Matrix4f

class Shader(

  gl: GL, // Accepts the unified wrapper!
  arena: Arena
) {

  private val GL_VERTEX_SHADER   = 0x8B31
  private val GL_FRAGMENT_SHADER = 0x8B30

  // 1. Simple Vertex Shader source code
  private val vertexSource =
    """#version 330 core
      |layout (location = 0) in vec3 aPos;
      |uniform mat4 u_View;
      |uniform mat4 u_Projection;
      |void main() {
      |   gl_Position = u_Projection * u_View * vec4(aPos, 1.0);
      |}
    """.stripMargin


  // 2. Simple Fragment Shader source code (solid white)
  private val fragmentSource =
    """#version 330 core
      |out vec4 FragColor;
      |void main() {
      |   FragColor = vec4(1.0, 1.0, 1.0, 1.0);
      |}
    """.stripMargin

  val programId: Int = compileProgram()


  def use(): Unit = gl.useProgram(programId)

  // Uploads a JOML Matrix4f down to our GPU uniform variable slot
  def setMatrix4f(name: String, matrix: Matrix4f): Unit = {

    val nameBytes = name.getBytes(StandardCharsets.UTF_8)
    val nameSeg = arena.allocate(nameBytes.length + 1)
    MemorySegment.copy(nameBytes, 0, nameSeg, ValueLayout.JAVA_BYTE, 0, nameBytes.length)
    nameSeg.set(ValueLayout.JAVA_BYTE, nameBytes.length, 0.toByte)

    val location: Int = gl.getUniformLocation(programId, nameSeg)
    if (location != -1) {
      val matBuffer = arena.allocate(16 * 4) // 16 floats, 4 bytes each
      val matArray = new Array[Float](16)
      matrix.get(matArray)
      MemorySegment.copy(matArray, 0, matBuffer, ValueLayout.JAVA_FLOAT, 0, 16)
      
      gl.uniformMatrix4fv(location, 1, 0.toByte, matBuffer)
    }
  }


  private def compileProgram(): Int = {
    val vs = compileShader(GL_VERTEX_SHADER, vertexSource)
    val fs = compileShader(GL_FRAGMENT_SHADER, fragmentSource)
    val program: Int = gl.createProgram()
    gl.attachShader(program, vs)
    gl.attachShader(program, fs)
    gl.linkProgram(program)
    program
  }

  private def compileShader(shaderType: Int, source: String): Int = {
    val shader: Int = gl.createShader(shaderType)
    val sourceBytes = source.getBytes(StandardCharsets.UTF_8)
    val sourceSeg = arena.allocate(sourceBytes.length + 1)
    MemorySegment.copy(sourceBytes, 0, sourceSeg, ValueLayout.JAVA_BYTE, 0, sourceBytes.length)
    sourceSeg.set(ValueLayout.JAVA_BYTE, sourceBytes.length, 0.toByte)

    val pointerArray = arena.allocate(ValueLayout.ADDRESS)

    pointerArray.set(ValueLayout.ADDRESS, 0, sourceSeg)

    gl.shaderSource(shader, 1, pointerArray, MemorySegment.NULL)
    gl.compileShader(shader)
    shader
  }
}
