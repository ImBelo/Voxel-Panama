// window.scala
import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import java.nio.charset.StandardCharsets
import scala.util.Using
import org.joml.Matrix4f
import org.joml.Vector3f
import MemoryUtils.withArena
import ArenaType.*
import CubeGeometry.*
import GLCostants.*
import GlfwCostants.*
import MemoryUtils.* 


object Game {
  // OpenGL Constants needed for drawing

  var firstMouse: Boolean = true
  var lastX: Double = 0.0
  var lastY: Double = 0.0
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



  def start(width: Int, height: Int, title: String): Unit = {
    val linker = Linker.nativeLinker()
    // 1. Load GLFW (Windowing)
    System.load("/usr/lib/x86_64-linux-gnu/libglfw.so.3") 

    // 2. Load libGL (OpenGL Graphics Functions)
    // On standard Linux systems, this file or its symbolic links live here:
    try {
      System.load("/usr/lib/x86_64-linux-gnu/libGL.so.1")
    } catch {
      case _: UnsatisfiedLinkError => 
        // Fallback for some Linux distributions where the link points straight to libGL.so
        System.load("/usr/lib/libGL.so")
    }
    val lookup = SymbolLookup.loaderLookup()

    val glfw = new Glfw(lookup, linker)
    val gl   = new GL(lookup, linker)

    if (glfw.init() == 0) throw new RuntimeException("GLFW Init Failed")   

    withArena(Confined) { arena ?=> 
      val camera = new Camera()
      val window = new GlfwWindow(width,height,glfw,title)
      val inputHandler = new InputHandler(glfw,window,camera,0.05)
      // Any drawing commands should go in window.handle pointer
      glfw.makeContextCurrent(window.handle)
      glfw.setInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)

      // THIS TURNS OFF VSYNC NEED TO USE DELTA TIME FOR INPUTS
      glfw.swapInterval(0) 
      window.enableRawMouseMotion()
      // Enables 3D Occlusion
      gl.enable(GL_DEPTH_TEST) 
      // COlors the sky blue
      gl.clearColor(0.2f, 0.4f, 0.6f, 1.0f) 

      import ShaderProgram.given
      val shader = ShaderBuilder.build(gl, ShaderSources(vertexSource, fragmentSource))
      // --- UPGRADED CUBE DATA ARRAYS (24 Vertices) ---
      // Structure: X, Y, Z,   NX, NY, NZ
      val vertices = CubeGeometry.vertices
      // --- UPGRADED INDEX ARRAY (Mapping the 24 Vertices into Triangles) ---
      val indices = CubeGeometry.indices     
      // Mesh now only needs our clean unified 'gl' command manager
      val cubeMesh = new CubeMesh(gl)
      cubeMesh.build(vertices,indices)
      // For performance, pre-allocate your matrix math targets so you don't allocate objects inside the loop
      val modelMatrix      = new Matrix4f()
      val viewMatrix       = new Matrix4f()
      val projectionMatrix = new Matrix4f()
      // --- RENDER LOOP ---
      var lastTime = System.nanoTime()
      @annotation.tailrec
      def renderLoop(): Unit = {
        glfw.pollEvents()
        if !window.shouldClose() then {
          // 1. The top-level zone tracking the cumulative execution time of the entire frame
          val currentTime = System.nanoTime()
          val deltaTime = ((currentTime - lastTime) / 1_000_000_000.0).toFloat
          lastTime = currentTime // Update for the next frame
          EngineProfiler.zone("Total Frame") {
            EngineProfiler.zone("Input Processing") {
              inputHandler.update(deltaTime)
            }
            EngineProfiler.zone("GPU clear") {
              gl.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
            }
            EngineProfiler.zone("Math & Uniform Updates") {
              shader.use()
              camera.getViewMatrix(viewMatrix)
              camera.getProjectionMatrix(projectionMatrix, window.currentAspectRatio)
              modelMatrix.identity() // Reset model matrix to identity (0, 0, 0 center)
              shader.set("u_ObjectColor", (0.2f, 0.8f, 0.2f))  // Green base color
              shader.set("u_LightPos",    (5.0f, 10.0f, 5.0f))  // Sun position
              shader.set("u_LightColor",  (1.0f, 1.0f, 0.95f))  // Warm sunlight
              shader.set("u_Model",      modelMatrix)            // Model matrix
              shader.set("u_View",       viewMatrix)             // View matrix
              shader.set("u_Projection", projectionMatrix)       // Projection matrix

            }
            EngineProfiler.zone("GPU Draw Dispatch") {
              gl.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
              cubeMesh.draw()
            }
            EngineProfiler.zone("Frame Present & Poll") {
              glfw.swapBuffers(window.handle)
            }
          }
          renderLoop()
        }
      }
      renderLoop()
      glfw.terminate()
    }

  }
}
