#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
uniform vec3 c_position;
uniform vec4 l_color;
uniform vec3 l_position;

//layout(location = 0) in vec3 pos;
//layout(location = 1) in vec2 tex;
//layout(location = 2) in vec3 norm;

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 norm;
layout(location = 2) in vec2 tex;
layout(location = 3) in vec3 color;

out vec3 toLight;
out vec3 toCamera;
out vec3 normal;
out vec2 texcoords;
out vec3 vertColor;

void main()
{
    // Color from vertex
    vertColor = color;//vec3(0.5,0.5,0.5);

    // Position in world space
    vec3 position = (m_model * vec4(pos.xyz, 1.0)).xyz;

    // Normal in world space
    normal = (m_model * vec4(norm, 0)).xyz;

    // Vector to light in world space
    toLight = vec4(l_position, 1.0).xyz - position;

    // Vector to camera in world space
    toCamera = vec4(c_position, 1.0).xyz - position;

    // Texture coordinates
    texcoords = tex;

    // Position in projection (final)
    gl_Position = m_proj * m_view * m_model * vec4(pos, 1.0);


}