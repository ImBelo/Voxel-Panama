import java.lang.foreign.*
import java.lang.invoke.MethodHandle
import org.joml.Matrix4f
import org.joml.Vector3f
import MemoryUtils.withArena
import ArenaType.*

import GlfwCostants.*
import GLCostants.*


import org.joml.Vector3f

class KeyboardHandler(glfw: Glfw, window: GlfwWindow, camera: Camera) {

  // 1. PRE-ALLOCATE MEMORY
  // By moving these out of the method, they are only created ONCE when the game starts.
  // We will reuse these exact same memory blocks every frame.
  private val moveVector = new Vector3f()
  private val rightVector = new Vector3f()
  private val horizontalForward = new Vector3f()

  // Base speed (eventually you'll want to multiply this by deltaTime)
  private val unitsPerSecond = 4.0f

  // Notice: Removed redundant arguments. The class already has access to glfw, window, and camera!
  def processInput(deltaTime: Float): Unit = {
    // Cache the handle locally for faster access
    val windowHandle = window.handle
    val frameSpeed = unitsPerSecond * deltaTime

    // 2. USE CONSTANTS INSTEAD OF MAGIC NUMBERS (256 = GLFW_KEY_ESCAPE)
    if (glfw.getKey(windowHandle, GLFW_KEY_ESCAPE) == GLFW_PRESS) { 
      EngineProfiler.printMetrics()
      glfw.setWindowShouldClose(windowHandle, 1) 
    }

    // --- VERTICAL MOVEMENT ---
    if (glfw.getKey(windowHandle, GLFW_KEY_SPACE) == GLFW_PRESS) {
      camera.position.y += frameSpeed
    }
    if (glfw.getKey(windowHandle, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
      camera.position.y -= frameSpeed
    }

    // --- HORIZONTAL MOVEMENT ---
    // 3. CACHE KEY STATES FIRST
    val wPressed = glfw.getKey(windowHandle, GLFW_KEY_W) == GLFW_PRESS
    val sPressed = glfw.getKey(windowHandle, GLFW_KEY_S) == GLFW_PRESS
    val aPressed = glfw.getKey(windowHandle, GLFW_KEY_A) == GLFW_PRESS
    val dPressed = glfw.getKey(windowHandle, GLFW_KEY_D) == GLFW_PRESS

    // 4. CONDITIONAL MATH
    // Only calculate vectors if the player is actually trying to move!
    if (wPressed || sPressed || aPressed || dPressed) {
      
      // Update our pre-allocated vector in-place (Zero allocations!)
      horizontalForward.set(camera.forward.x, 0f, camera.forward.z).normalize()

      if (wPressed) {
        horizontalForward.mul(frameSpeed, moveVector)
        camera.position.add(moveVector)
      }
      if (sPressed) {
        horizontalForward.mul(frameSpeed, moveVector)
        camera.position.sub(moveVector)
      }

      // 5. CACHE CROSS PRODUCT
      // Only calculate the right vector once, and only if strafing
      if (aPressed || dPressed) {
        horizontalForward.cross(camera.up, rightVector).normalize()
        
        if (aPressed) {
          rightVector.mul(frameSpeed, moveVector)
          camera.position.sub(moveVector)
        }
        if (dPressed) {
          rightVector.mul(frameSpeed, moveVector)
          camera.position.add(moveVector)
        }
      }
    }
  }
}
