#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
//uniform vec3 c_position;

in vec3 normal;
in vec3 vertColor;

layout(location = 0) out vec3 diffuse;
layout(location = 1) out vec3 normals;

void main()
{
   diffuse = vertColor;
   normals = normal;
}
