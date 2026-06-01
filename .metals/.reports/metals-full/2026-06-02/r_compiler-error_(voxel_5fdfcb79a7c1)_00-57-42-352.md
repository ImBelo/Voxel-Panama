error id: 54E02E28BFDC429DC2A86C7B1C82764F
file://<WORKSPACE>/Shader.scala
### java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/Shader.scala
text:
```scala
import java.lang.foreign.*
import java.nio.charset.StandardCharsets
import org.joml.Matrix4f
import MemoryUtils.withArena
$
class Shader(
  private val gl: GL,    // Accepts your unified wrapper
)(using arena: Arena) {


  // --- 1. PRE-ALLOCATED HOT PATCH BUFFERS ---
  // These are allocated ONCE per shader instance at startup.
  // The render loop will overwrite these instead of creating new allocations!
  private val matrixBuffer: MemorySegment = arena.allocate(16 * 4) // 16 floats * 4 bytes
  private val matArray: Array[Float]     = new Array[Float](16)

  // --- 2. SHADER SOURCE CODE ---
  private val vertexSource =
    """#version 330 core
  layout (location = 0) in vec3 aPos;
  layout (location = 1) in vec3 aNormal; // New: Normal direction for each vertex

  out vec3 FragPos;  // Pass world position to Fragment Shader
  out vec3 Normal;   // Pass transformed normal to Fragment Shader

  uniform mat4 u_Model;
  uniform mat4 u_View;
  uniform mat4 u_Projection;

  void main() {
    // Calculate the vertex position in world space
    FragPos = vec3(u_Model * vec4(aPos, 1.0));

    // Transform the normal vector to match world space rotations
    // (Inverse-transpose handles non-uniform scaling safely)
    Normal = mat3(transpose(inverse(u_Model))) * aNormal;  

    gl_Position = u_Projection * u_View * vec4(FragPos, 1.0);
  }
  """.stripMargin

  private val fragmentSource =
    """#version 330 core
  out vec4 FragColor;

  in vec3 FragPos;
  in vec3 Normal;

  uniform vec3 u_ObjectColor;
  uniform vec3 u_LightPos;   // Position of your light source (e.g., vec3(2.0, 4.0, 3.0))
  uniform vec3 u_LightColor; // Color of the light (e.g., vec3(1.0, 1.0, 1.0) for white)

  void main() {
    // 1. Ambient Light (static background glow)
    float ambientStrength = 0.25;
    vec3 ambient = ambientStrength * u_LightColor;

    // 2. Diffuse Light (directional shading)
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(u_LightPos - FragPos);

    // Dot product determines the angle. max() prevents negative values (light from behind)
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * u_LightColor;

    // 3. Combine them to color the pixel
    vec3 result = (ambient + diffuse) * u_ObjectColor;
    FragColor = vec4(result, 1.0);
  }    """.stripMargin

  // Compile and link immediately when the object is instantiated
  val programId: Int = compileProgram()

  def use(): Unit = gl.useProgram(programId)

  // --- 3. HIGH PERFORMANCE UNIFORM UPLOADS ---

  /**
   * Helper to look up uniform locations safely at startup.
   * Uses a temporary confined arena so it doesn't leak memory into your main arena.
   */
  def getUniformLocation(name: String): Int = {
    withArena(ArenaType.Confined){ localArena ?=>
      val nameSeg = localArena.allocateFrom(name)
      gl.getUniformLocation(programId, nameSeg)
    }
  }

  /**
   * Uploads a JOML Matrix4f down to our GPU uniform variable slot without allocating any frame memory.
   */
  def setMatrix4f(location: Int, matrix: Matrix4f): Unit = {
    // 1. Dump matrix data into the reusable Java heap array
    matrix.get(matArray)

    // 2. Copy the Java array into the pre-allocated off-heap buffer
    MemorySegment.copy(matArray, 0, matrixBuffer, ValueLayout.JAVA_FLOAT, 0, 16)

    // 3. Send straight to the GPU
    gl.uniformMatrix4fv(location, 1, 0.toByte, matrixBuffer)
  }

  // --- 4. PRIVATE COMPILATION LOGIC (INTERNAL) ---

  private def compileProgram(): Int = {
    val vs = compileShader(GL_VERTEX_SHADER, vertexSource)
    val fs = compileShader(GL_FRAGMENT_SHADER, fragmentSource)
    
    val program: Int = gl.createProgram()
    gl.attachShader(program, vs)
    gl.attachShader(program, fs)
    gl.linkProgram(program)
    
    // Clean up intermediate shader objects after linking
    gl.deleteShader(vs)
    gl.deleteShader(fs)
    
    program
  }

  private def compileShader(shaderType: Int, source: String): Int = {
    val shader: Int = gl.createShader(shaderType)
    
    // Simplification: allocateFrom automatically adds the null-terminator byte!
    val sourceSeg = arena.allocateFrom(source)

    // Set up the pointer-to-pointer array required by glShaderSource
    val pointerArray = arena.allocate(ValueLayout.ADDRESS)
    pointerArray.set(ValueLayout.ADDRESS, 0, sourceSeg)

    gl.shaderSource(shader, 1, pointerArray, MemorySegment.NULL)
    gl.compileShader(shader)
    shader
  }
}

```


presentation compiler configuration:
Scala version: 3.8.2-bin-nonbootstrapped
Classpath:
<WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.2/scala3-library_3-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/joml/joml/1.10.8/joml-1.10.8.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.2/scala-library-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/sourcegraph/semanticdb-javac/0.10.0/semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>/.scala-build/voxel_dfcb79a7c1/classes/main/META-INF/best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -release 21 -Ywith-best-effort-tasty




#### Error stacktrace:

```
dotty.tools.pc.DiagnosticProvider.diagnostics(DiagnosticProvider.scala:19)
	dotty.tools.pc.ScalaPresentationCompiler.didChange$$anonfun$1(ScalaPresentationCompiler.scala:509)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.withNonInterruptableCompiler$$anonfun$1(CompilerAccess.scala:133)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1516)
```
#### Short summary: 

java.lang.NoSuchMethodError: 'boolean scala.meta.pc.VirtualFileParams.shouldReturnDiagnostics()'