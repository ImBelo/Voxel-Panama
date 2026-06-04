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
import Timing.DeltaTime
import scala.util.boundary, boundary.break

final class EngineState(
  val glfw: Glfw,
  val gl: GL,
  val window: GlfwWindow,
  val camera: Camera,
  val inputHandler: InputHandler,
  val chunkRenderer: ChunkRenderer,
  val shader: ShaderProgram,
  val world: World,
  // Cached Uniform Locations
  val locColor: Int,
  val locLightPos: Int,
  val locLightColor: Int,
  val locModel: Int,
  val locView: Int,
  val locProjection: Int
)

object Game {
  inline def withEngine[T](
    inline setup: Arena ?=> T,
    inline runLoop: (Arena, T) => Unit,
    inline cleanup: T => Unit = (resources: T) => ()
  ): Unit = {
    withArena(ArenaType.Confined) { arena ?=>
      val resources = setup
      try runLoop(arena, resources)
      finally cleanup(resources)
    }
  }
  inline def engineLoop(inline frame: boundary.Label[Unit] ?=> Unit): Unit = {
    boundary:
      while true do 
        frame 
  }
  def start(width: Int, height: Int, title: String): Unit = {
    import ShaderProgram.given
    val linker = Linker.nativeLinker()
    System.load("/usr/lib/x86_64-linux-gnu/libglfw.so.3") 
    try System.load("/usr/lib/x86_64-linux-gnu/libGL.so.1")
    catch case _: UnsatisfiedLinkError => System.load("/usr/lib/libGL.so")

    val lookup = SymbolLookup.loaderLookup()
    val glfw = new Glfw(lookup, linker)
    val gl   = new GL(lookup, linker)
    if (glfw.init() == 0) throw new RuntimeException("GLFW Init Failed")   

    withEngine(
      setup = { (arena: Arena) ?=>
        val world = new World()
        val camera = new Camera()
        val window = new GlfwWindow(width, height, glfw, title)
        val inputHandler = new InputHandler(glfw, window, camera, 0.05)
        val chunkRenderer = new ChunkRenderer(gl)

        window.makeContextCurrent()
        window.disableCursor()
        window.turnOffVSync()
        window.enableRawMouseMotion()

        gl.enable(GL_DEPTH_TEST) 
        gl.clearColor(0.2f, 0.4f, 0.6f, 1.0f) 

        import ShaderProgram.given
        val shader = ShaderBuilder.build(gl, ShaderSources(
          ShaderLoader.loadFromResources("shaders/world.vert"), 
          ShaderLoader.loadFromResources("shaders/world.frag")
        ))

        val bottomChunk = world.loadOrGenerateChunk(0, 0, 0)
        val middleChunk = world.loadOrGenerateChunk(1, 0, 0) 
        val topChunk    = world.loadOrGenerateChunk(2, 0, 0)  
        chunkRenderer.updateChunk(bottomChunk, world)
        chunkRenderer.updateChunk(middleChunk, world)
        chunkRenderer.updateChunk(topChunk, world)

        EngineState(
          glfw, gl, window, camera, inputHandler, chunkRenderer, shader, world,
          shader.getUniformLocation("u_ObjectColor"),
          shader.getUniformLocation("u_LightPos"),
          shader.getUniformLocation("u_LightColor"),
          shader.getUniformLocation("u_Model"),
          shader.getUniformLocation("u_View"),
          shader.getUniformLocation("u_Projection")
        )
      },
      runLoop = (arena, state) => {
        val tracker = new TimeTracker()
        val modelMatrix      = new Matrix4f()
        val viewMatrix       = new Matrix4f()
        val projectionMatrix = new Matrix4f()
        val winHandle = state.window.handle 
        engineLoop {
          state.glfw.pollEvents()
          if state.window.shouldClose() then break()

          val deltaTime: DeltaTime = tracker.tick()

          EngineProfiler.zone("Total Frame"):
            EngineProfiler.zone("Input Processing"):
              state.inputHandler.update(deltaTime)
            EngineProfiler.zone("GPU clear"):
              state.gl.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

            EngineProfiler.zone("Math & Uniform Updates"):
              state.shader.use()
              state.camera.getViewMatrix(viewMatrix)
              state.camera.getProjectionMatrix(projectionMatrix, state.window.currentAspectRatio)
              modelMatrix.identity() 

              state.shader.set(state.locColor,      (0.2f, 0.8f, 0.2f))  
              state.shader.set(state.locLightPos,   (5.0f, 100.0f, 5.0f))  
              state.shader.set(state.locLightColor, (1.0f, 1.0f, 0.95f))  
              state.shader.set(state.locModel,      modelMatrix)            
              state.shader.set(state.locView,       viewMatrix)               
              state.shader.set(state.locProjection, projectionMatrix)

            EngineProfiler.zone("GPU Draw Dispatch"):
              state.chunkRenderer.render(state.shader, state.camera)

            EngineProfiler.zone("Frame Present & Poll"):
              state.glfw.swapBuffers(winHandle)
        }
      },
      cleanup = (state: EngineState) => {
        state.glfw.terminate()
      }
      )
  }
}


