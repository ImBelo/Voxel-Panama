import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import org.joml.Matrix4f
import org.joml.Vector3f
import MemoryUtils.withArena
import ArenaType.*

import GlfwCostants.*
import GLCostants.*


import org.joml.Vector3f
import Timing.DeltaTime

class KeyboardHandler(glfw: Glfw, window: GlfwWindow, camera: Camera) {

  private val moveVector = new Vector3f()
  private val rightVector = new Vector3f()
  private val horizontalForward = new Vector3f()

  private val directionAccumulator = new Vector3f()

  private val unitsPerSecond = 20.0f

  def processInput(deltaTime: DeltaTime): Unit = {
    val windowHandle = window.handle
    val frameSpeed = unitsPerSecond * deltaTime.seconds

    if (glfw.getKey(windowHandle, GLFW_KEY_ESCAPE) == GLFW_PRESS) { 
      EngineProfiler.printMetrics()
      glfw.setWindowShouldClose(windowHandle, 1) 
    }

    if (glfw.getKey(windowHandle, GLFW_KEY_SPACE) == GLFW_PRESS) {
      camera.position.y += frameSpeed
    }
    if (glfw.getKey(windowHandle, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
      camera.position.y -= frameSpeed
    }

    val wPressed = glfw.getKey(windowHandle, GLFW_KEY_W) == GLFW_PRESS
    val sPressed = glfw.getKey(windowHandle, GLFW_KEY_S) == GLFW_PRESS
    val aPressed = glfw.getKey(windowHandle, GLFW_KEY_A) == GLFW_PRESS
    val dPressed = glfw.getKey(windowHandle, GLFW_KEY_D) == GLFW_PRESS

    if (wPressed || sPressed || aPressed || dPressed) {
      directionAccumulator.set(0f, 0f, 0f)
      horizontalForward.set(camera.forward.x, 0f, camera.forward.z).normalize()
      if (wPressed) directionAccumulator.add(horizontalForward)
      if (sPressed) directionAccumulator.sub(horizontalForward)
      if (aPressed || dPressed) {
        horizontalForward.cross(camera.up, rightVector).normalize()
        if (dPressed) directionAccumulator.add(rightVector)
        if (aPressed) directionAccumulator.sub(rightVector)
      }
      if (directionAccumulator.lengthSquared() > 0f) {
        directionAccumulator.normalize()
        directionAccumulator.mul(frameSpeed, moveVector)

        camera.position.add(moveVector)
      }
    }
  }
}

