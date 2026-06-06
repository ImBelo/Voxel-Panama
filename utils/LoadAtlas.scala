import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.lang.foreign.{Arena, ValueLayout}
import GLCostants.*
import MemoryUtils.*

def loadTextureAtlas(gl: GL, filePath: String)(using arena: Arena): Int = {
  // 1. Read the image
  val image = ImageIO.read(new File(filePath))
  val width = image.getWidth
  val height = image.getHeight

  // 2. Extract standard ARGB integer pixels safely
  val pixels = new Array[Int](width * height)
  image.getRGB(0, 0, width, height, pixels, 0, width)

  // 3. Allocate native memory for exactly width * height * 4 bytes
  val nativeBuffer = malloc(width * height * 4L)

  // 4. Unpack Java's ARGB ints into perfect RGBA native bytes!
  for (i <- 0 until pixels.length) {
    val argb = pixels(i)
    val a = (argb >> 24) & 0xFF
    val r = (argb >> 16) & 0xFF
    val g = (argb >> 8) & 0xFF
    val b = argb & 0xFF

    val offset = i * 4L
    nativeBuffer.set(ValueLayout.JAVA_BYTE, offset,     r.toByte) // Red
    nativeBuffer.set(ValueLayout.JAVA_BYTE, offset + 1, g.toByte) // Green
    nativeBuffer.set(ValueLayout.JAVA_BYTE, offset + 2, b.toByte) // Blue
    nativeBuffer.set(ValueLayout.JAVA_BYTE, offset + 3, a.toByte) // Alpha
  }

  // 5. Native OpenGL Object Generation
  val textureIdBuffer = arena.allocate(ValueLayout.JAVA_INT)
  gl.genTextures(1, textureIdBuffer)
  val textureId = textureIdBuffer.get(ValueLayout.JAVA_INT, 0)

  gl.bindTexture(GL_TEXTURE_2D, textureId)

  // 6. Upload with standard GL_RGBA now that our buffer is perfectly aligned
  gl.texImage2D(
    GL_TEXTURE_2D,           // GL_TEXTURE_2D
    0,                // Mipmap level 0
    GL_RGBA,           // Internal Format: GL_RGBA (0x1908)
    width, 
    height, 
    0,                // Border
    GL_RGBA,           // 🟢 FIX: Format is now standard GL_RGBA (0x1908)
    GL_UNSIGNED_BYTE,           // Data type: GL_UNSIGNED_BYTE (0x1401)
    nativeBuffer
  )

  // 🚀 CRITICAL FOR VOXELS: Turn off texture filtering/blurring!
  gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
  gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
  gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
  gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

  textureId
}
