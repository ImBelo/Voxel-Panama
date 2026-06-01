// Camera.scala
import org.joml.Matrix4f

import org.joml.Vector3f

class Camera {
  val position = new Vector3f(0.0f, 0.0f, 3.0f) // Start 3 units back from center
  val forward  = new Vector3f(0.0f, 0.0f, -1.0f)

  val up       = new Vector3f(0.0f, 1.0f, 0.0f)


  var yaw: Float   = -90.0f // Left/Right angle
  var pitch: Float = 0.0f   // Up/Down angle

  // Generates the final matrix representing the camera's transformation of the world
  def getViewMatrix: Matrix4f = {
    val target = new Vector3f()
    position.add(forward, target) // Target point is (position + forward vector)
    
    new Matrix4f().lookAt(position, target, up)
  }

  // Updates the forward vector based on mouse angles (Yaw and Pitch)
  def updateDirection(): Unit = {
    // Clamp pitch to prevent the camera flipping upside down
    if (pitch > 89.0f) pitch = 89.0f
    if (pitch < -89.0f) pitch = -89.0f

    val direction = new Vector3f()
    direction.x = Math.cos(Math.toRadians(yaw)).toFloat * Math.cos(Math.toRadians(pitch)).toFloat
    direction.y = Math.sin(Math.toRadians(pitch)).toFloat
    direction.z = Math.sin(Math.toRadians(yaw)).toFloat * Math.cos(Math.toRadians(pitch)).toFloat
    
    direction.normalize(forward)

  }
}
