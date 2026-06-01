import java.lang.foreign.*
object ShaderBuilder:
  inline def build(gl: GL, sources: ShaderSources)(using arena: Arena): ShaderProgram =
    val compiler = new ShaderStage(gl)
    val vs = compiler.compile(sources.vertex, gl.GL_VERTEX_SHADER)
    val fs = compiler.compile(sources.fragment, gl.GL_FRAGMENT_SHADER)

    val progId = gl.createProgram()
    gl.attachShader(progId, vs.toInt)
    gl.attachShader(progId, fs.toInt)
    gl.linkProgram(progId)

    gl.deleteShader(vs.toInt)
    gl.deleteShader(fs.toInt)

    ShaderProgram(gl, ProgramId(progId))
