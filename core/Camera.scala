import org.joml.{Matrix4f, Vector3f, FrustumIntersection}

class Camera(targetTransform: Transform, targetForward: Vector3f, targetUp: Vector3f) {
  private val viewProjMatrix = new Matrix4f()
  private val frustum = new FrustumIntersection()
  private val targetTmp = new Vector3f()

  def updateFrustum(viewMatrix: Matrix4f, projectionMatrix: Matrix4f): Unit = {
    projectionMatrix.mul(viewMatrix, viewProjMatrix)
    frustum.set(viewProjMatrix)
  }

  def isChunkVisible(cx: Int, cy: Int, cz: Int): Boolean = {
    val minX = (cx << 4).toFloat
    val minY = (cy << 4).toFloat
    val minZ = (cz << 4).toFloat
    frustum.testAab(minX, minY, minZ, minX + 16f, minY + 16f, minZ + 16f)
  }

  def getViewMatrix(dest: Matrix4f): Unit = {
    // We read directly from the target we are following
    targetTransform.position.add(targetForward, targetTmp) 
    dest.identity().lookAt(targetTransform.position, targetTmp, targetUp)
  }

  def getProjectionMatrix(dest: Matrix4f, aspectRatio: Float): Unit = {
    val fov = Math.toRadians(60.0).toFloat
    dest.identity().perspective(fov, aspectRatio, 0.1f, 1000.0f)
  }
}
