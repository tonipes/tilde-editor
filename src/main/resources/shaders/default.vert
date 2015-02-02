#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
uniform vec3 c_position;


layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 norm;
layout(location = 2) in vec2 tex;
layout(location = 3) in vec3 color;

out vec3 vertColor;

void main()
{
    // Color from vertex
    vertColor = color;//vec3(0.5,0.5,0.5);

    // Position in projection (final)
    gl_Position = m_proj * m_view * m_model* vec4(pos, 1.0);


}
