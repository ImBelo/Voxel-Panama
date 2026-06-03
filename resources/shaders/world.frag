#version 330 core
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
}    
