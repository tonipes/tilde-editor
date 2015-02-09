#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;

in vec3 vertColor;

layout(location = 0) out vec3 diffuse;
layout(location = 1) out vec3 normals;

void main()
{
   diffuse = vertColor;
   normals = vec3(1,1,1);
}
