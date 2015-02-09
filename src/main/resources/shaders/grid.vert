#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;


layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 color;

out vec3 vertColor;

void main()
{
    // Color from vertex
    vertColor = color;

    // Position in projection (final)
    vec4 world = m_model * vec4(pos, 1.0);
    vec4 view = m_view * world;

    gl_Position = m_proj * view;


}
