
import java.io.{BufferedReader, InputStreamReader}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors

object ShaderLoader {

  /**
   * Loads a shader string from the project resources.
   * Supports both classpath loading and direct local filesystem fallback.
   *
   * @param path Relative path from inside the resources folder (e.g., "shaders/world.vert")
   */
  def loadFromResources(path: String): String = {
    // Attempt 1: Standard ClassLoader lookup
    val classLoader = Thread.currentThread().getContextClassLoader
    val inputStream = classLoader.getResourceAsStream(path)

    if (inputStream != null) {
      try {
        val reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        val content = reader.lines().collect(Collectors.joining("\n"))
        reader.close()
        content
      } catch {
        case e: Exception => throw new RuntimeException(s"❌ Error reading classpath resource: $path", e)
      }
    } else {
      // Attempt 2: Filesystem Fallback (looks from the project root directory)
      val fallbackPath = Paths.get("resources", path)
      if (Files.exists(fallbackPath)) {
        try {
          val bytes = Files.readAllBytes(fallbackPath)
          new String(bytes, StandardCharsets.UTF_8)
        } catch {
          case e: Exception => throw new RuntimeException(s"❌ Error reading fallback filesystem path: $fallbackPath", e)
        }
      } else {
        // Ultimate Fail: Print an explicit error showing your exact pathing layout
        val absoluteRoot = Paths.get("").toAbsolutePath
        throw new IllegalArgumentException(
          s"❌ [Shader Load Failure]\n" +
          s"   Could not find asset: '$path' via ClassLoader,\n" +
          s"   and could not find fallback file at: '$fallbackPath'.\n" +
          s"   Current project working directory: $absoluteRoot"
        )
      }
    }
  }
}
