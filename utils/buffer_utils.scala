// BufferUtils.scala
import java.lang.foreign.*


object BufferUtils {
  // Automatically calculates size and copies Scala Float arrays to off-heap memory
  def toNative(array: Array[Float], arena: Arena): MemorySegment = {
    //val seg = arena.allocate(array.length * ValueLayout.JAVA_FLOAT.byteSize())
    val seg = arena.allocate(ValueLayout.JAVA_FLOAT, array.length)
    MemorySegment.copy(array, 0, seg, ValueLayout.JAVA_FLOAT, 0, array.length)
    seg
  }

  // Automatically calculates size and copies Scala Int arrays to off-heap memory

  def toNative(array: Array[Int], arena: Arena): MemorySegment = {
    //val seg = arena.allocate(array.length * ValueLayout.JAVA_INT.byteSize())
    val seg = arena.allocate(ValueLayout.JAVA_FLOAT, array.length)

    MemorySegment.copy(array, 0, seg, ValueLayout.JAVA_INT, 0, array.length)
    seg
  }
}
