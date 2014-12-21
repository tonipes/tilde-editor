#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;

out vec2 texCoords;

void main()
{
    texCoords = tex;
    gl_Position = m_proj * m_view * m_model * vec4(pos, 1.0);
}