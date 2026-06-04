import MemoryUtils.withArena
import MemoryUtils.*
import java.lang.foreign.*

opaque type ShaderStageId = Int
object ShaderStageId:
  def apply(id: Int): ShaderStageId = id
  extension (id: ShaderStageId) def toInt: Int = id

class ShaderStage(gl: GL)(using arena: Arena):
  def compile(source: String, stageType: Int): ShaderStageId =
    val id = gl.createShader(stageType)
    val srcSeg = arena.allocString(source)
    val ptrArr = arena.allocate(ValueLayout.ADDRESS)
    ptrArr.set(ValueLayout.ADDRESS, 0, srcSeg)
    gl.shaderSource(id, 1, ptrArr, MemorySegment.NULL)
    gl.compileShader(id)
    ShaderStageId(id)
