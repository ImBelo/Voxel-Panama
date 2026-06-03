// Face.scala
enum Face(val dx: Int, val dy: Int, val dz: Int):
  case Front  extends Face( 0,  0,  1)
  case Back   extends Face( 0,  0, -1)
  case Top    extends Face( 0,  1,  0)
  case Bottom extends Face( 0, -1,  0)
  case Right  extends Face( 1,  0,  0)
  case Left   extends Face(-1,  0,  0)
