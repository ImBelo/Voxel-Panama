#version 330 core
out vec4 FragColor;

in vec3 FragPos;
in vec3 Normal;
in vec2 v_TexCoords; // 👈 Incoming UV coordinates from the vertex shader

uniform sampler2D u_TextureAtlas; // 👈 Your stitched texture grid asset
uniform vec3 u_LightPos;   
uniform vec3 u_LightColor; 

void main() {
  // 1. Sample the block color from your Texture Atlas instead of a global uniform
  vec4 texColor = texture(u_TextureAtlas, v_TexCoords);

  if (texColor.a < 0.1) {
      discard;
  }

  // 2. Ambient Light (static background glow)
  float ambientStrength = 0.25;
  vec3 ambient = ambientStrength * u_LightColor;

  // 3. Diffuse Light (directional shading)
  vec3 norm = normalize(Normal);
  vec3 lightDir = normalize(u_LightPos - FragPos);

  // Dot product determines shading angle based on block face direction
  float diff = max(dot(norm, lightDir), 0.0);
  vec3 diffuse = diff * u_LightColor;

  // 4. Combine ambient and diffuse light, then multiply by the sampled texture color
  vec3 lightingResult = (ambient + diffuse) * texColor.rgb;
  
  FragColor = vec4(lightingResult, texColor.a);
}
