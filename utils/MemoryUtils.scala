
import java.lang.foreign.{Arena, MemorySegment, ValueLayout, SegmentAllocator}
import java.nio.charset.StandardCharsets
import scala.quoted.*

enum ArenaType:
  case Confined,Shared,Auto 

object MemoryUtils {
  inline def withArena[T](inline arenaType: ArenaType)(inline f: Arena ?=> T): T = {
    inline arenaType match {
      case ArenaType.Confined =>
        val arena = Arena.ofConfined()
        try f(using arena) finally arena.close()
      case ArenaType.Shared =>
        val arena = Arena.ofShared()
        try f(using arena) finally arena.close()
      case ArenaType.Auto =>
        val arena = Arena.ofAuto()
        f(using arena)
    }
  }
  inline def malloc(bytes: Long)(using arena: Arena): MemorySegment = 
    arena.allocate(bytes)
  inline def malloc(layout: java.lang.foreign.MemoryLayout)(using arena: Arena): MemorySegment = 
    arena.allocate(layout.byteSize())
  inline def calloc(bytes: Long, alignment: Long)(using arena: Arena): MemorySegment =
    arena.allocate(bytes, alignment)  
  inline def allocString(str: String)(using arena: Arena): MemorySegment =
    arena.allocateFrom(str, StandardCharsets.UTF_8)
  inline def allocFloats(inline values: Float*)(using arena: Arena): MemorySegment = {
    val seg = arena.allocate(ValueLayout.JAVA_FLOAT, values.length)
    for (i <- values.indices) {
      seg.setAtIndex(ValueLayout.JAVA_FLOAT, i, values(i))
    }
    seg
  }

  inline def allocInts(inline values: Int*)(using arena: Arena): MemorySegment = {
    val seg = arena.allocate(ValueLayout.JAVA_INT, values.length)
    for (i <- values.indices) {
      seg.setAtIndex(ValueLayout.JAVA_INT, i, values(i))
    }
    seg
  }
  
    
    inline def toNative(array: Array[Float])(using arena: Arena): MemorySegment = {
      val seg = arena.allocate(ValueLayout.JAVA_FLOAT, array.length)
      MemorySegment.copy(array, 0, seg, ValueLayout.JAVA_FLOAT, 0, array.length)
      seg

    }

    /** Copies a Scala Int array into a freshly allocated segment */
    inline def toNative(array: Array[Int])(using arena: Arena): MemorySegment = {
      val seg = arena.allocate(ValueLayout.JAVA_INT, array.length)  // fixed: was JAVA_FLOAT
      MemorySegment.copy(array, 0, seg, ValueLayout.JAVA_INT, 0, array.length)
      seg
    }


  opaque type NativeOp[A] = Arena => A

  object NativeOp:
    // pure simply returns the value
    inline def pure[A](inline value: A): NativeOp[A] = 
      _ => value

  // 2. The monadic extension methods are marked inline
  extension [A](inline fa: NativeOp[A])
    inline def flatMap[B](inline f: A => NativeOp[B]): NativeOp[B] =
      (arena: Arena) => 
        // The compiler evaluates and copy-pastes 'fa' and 'f' right here!
        val resultA = fa(arena)
        f(resultA)(arena)

    inline def map[B](inline f: A => B): NativeOp[B] =
      (arena: Arena) => f(fa(arena))

  // 3. The Executor that runs the monadic chain
  inline def runNative[A](inline op: NativeOp[A]): A =
    val arena = Arena.ofConfined()
    try op(arena) finally arena.close()

}

