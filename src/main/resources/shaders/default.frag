#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
uniform vec3 c_position;

in vec3 vertColor;
out vec4 outColor;

void main()
{
   outColor = vec4(vertColor, 1);
}
