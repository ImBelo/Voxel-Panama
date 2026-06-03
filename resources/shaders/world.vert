#version 330 core
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
