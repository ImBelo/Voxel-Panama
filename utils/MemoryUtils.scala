
import java.lang.foreign.{Arena, MemorySegment, ValueLayout, SegmentAllocator}

enum ArenaType:
  case Confined
  case Shared 
  case Auto 

object MemoryUtils {
  
  // Zero-cost arena resource management
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
  
  // Allocate helpers - Arena IS a SegmentAllocator
  extension (arena: Arena) {
    inline def allocFloats(inline values: Float*): MemorySegment = {
      val seg = arena.allocate(ValueLayout.JAVA_FLOAT, values.length)
      for (i <- values.indices) {
        seg.setAtIndex(ValueLayout.JAVA_FLOAT, i, values(i))
      }
      seg
    }
    
    inline def allocInts(inline values: Int*): MemorySegment = {
      val seg = arena.allocate(ValueLayout.JAVA_INT, values.length)
      for (i <- values.indices) {
        seg.setAtIndex(ValueLayout.JAVA_INT, i, values(i))
      }
      seg
    }
    
    inline def allocString(inline str: String): MemorySegment = {
      arena.allocateFrom(str)  // Arena inherits this from SegmentAllocator
    }
  }
}
