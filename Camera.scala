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
  private val targetTmp = new org.joml.Vector3f()

  def getViewMatrix(dest: org.joml.Matrix4f): Unit = {
    // Clear and update our pre-allocated target vector
    position.add(forward, targetTmp) 

    // Calculate the lookAt matrix directly into the destination matrix
    dest.identity().lookAt(position, targetTmp, up)
  }
  def getProjectionMatrix(dest: org.joml.Matrix4f, aspectRatio: Float): Unit = {
    val fov = Math.toRadians(60.0).toFloat // 60-degree Field of View
    val nearPlane = 0.1f                  // Don't render things closer than this
    val farPlane = 1000.0f                // Voxel render distance horizon

    // Calculate perspective directly into the destination matrix
    dest.identity().perspective(fov, aspectRatio, nearPlane, farPlane)
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
