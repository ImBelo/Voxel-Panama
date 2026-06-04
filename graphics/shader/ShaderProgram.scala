import MemoryUtils.withArena
import MemoryUtils.*
import java.lang.foreign.{Arena, MemorySegment, ValueLayout}
import org.joml.Matrix4f
import scala.collection.mutable

opaque type ProgramId = Int
object ProgramId:
  def apply(id: Int): ProgramId = id
  extension (id: ProgramId) def toInt: Int = id

trait Uniform[U]:
  extension (value: U)
    inline def set(program: ShaderProgram, location: Int): Unit

class ShaderProgram(private val gl: GL, val id: ProgramId)(using arena: Arena){
  // Cache uniform locations
  private val uniformLocations = mutable.Map.empty[String, Int]

  // Pre-allocated matrix upload buffers
  private val matrixBuffer: MemorySegment = arena.allocate(16 * 4)
  private val matrixArray: Array[Float]  = new Array[Float](16)

  def use(): Unit = gl.useProgram(id.toInt)

  def getUniformLocation(name: String): Int =
    uniformLocations.getOrElseUpdate(name, {
      withArena(ArenaType.Confined) { arena ?=>
        val seg = arena.allocString(name)
        gl.getUniformLocation(id.toInt, seg)
      }
    })

  /** Zero-cost uniform setter – inlined to direct GL calls DONT USE THIS IN LOOP BECAUSE MAP OVERHEAD */
  inline def set[U](name: String, value: U)(using u: Uniform[U]): Unit =
    value.set(this, getUniformLocation(name))

  /** Zero-cost: Use this in loops */
  inline def set[U](location: Int, value: U)(using u: Uniform[U]): Unit =
    value.set(this, location)
}

object ShaderProgram{
  // ------ Uniform iven instances (access to private members allowed here) ------
  
  given Uniform[Float] with
    extension (v: Float)
      inline def set(p: ShaderProgram, loc: Int): Unit =
        p.gl.uniform1f(loc, v)

  given Uniform[(Float, Float, Float)] with
    extension (v: (Float, Float, Float))
      inline def set(p: ShaderProgram, loc: Int): Unit =
        p.gl.uniform3f(loc, v._1, v._2, v._3)

  given Uniform[Matrix4f] with
    extension (m: Matrix4f)
      inline def set(p: ShaderProgram, loc: Int): Unit =
        // 1. Extract matrix into reusable Java array
        m.get(p.matrixArray)
        // 2. Copy to pre-allocated off-heap buffer
        MemorySegment.copy(p.matrixArray, 0, p.matrixBuffer, ValueLayout.JAVA_FLOAT, 0, 16)
        // 3. Upload to GPU – FIX: 0.toByte instead of false
        p.gl.uniformMatrix4fv(loc, 1, 0.toByte, p.matrixBuffer)
}
