
enum Block(val id: Byte, val isSolid: Boolean, val isTransparent: Boolean):
  case Air    extends Block(0, false, true)
  case Stone  extends Block(1, true,  false)
  case Dirt   extends Block(2, true,  false)
  case Grass  extends Block(3, true,  false)
  case Sand   extends Block(4, true,  false)
  case Water  extends Block(5, true,  true)
  case Wood   extends Block(6, true,  false)
  case Leaves extends Block(7, true,  true)
