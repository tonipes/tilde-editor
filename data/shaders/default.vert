#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;
layout(location = 2) in vec3 norm;

out float fragBrightness;
out vec2 fragTexCoord;

void main()
{

    gl_Position = m_proj * m_view * m_model * vec4(pos, 1.0);

    /*vec3 cameraPosition = -m_view[3].xyz * mat3(m_view);
    vec3 lightPosition = vec3(m_model * vec4(cameraPosition, 1));
    float distance = length(lightPosition-gl_Position.xyz);
    mat3 normalMatrix = transpose(inverse(mat3(m_model)));
    vec3 normal = norm;

    vec3 surfaceToLight = cameraPosition - gl_Position.xyz;
    float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
    brightness = clamp(brightness, 0, 1);*/

    fragBrightness = 1.0;
    fragTexCoord = tex;
}