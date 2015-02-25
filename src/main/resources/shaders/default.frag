#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
//uniform vec3 c_position;

in vec3 fragNormal;
in vec3 fragVertColor;

layout(location = 0) out vec3 diffuse;
layout(location = 1) out vec3 normal;
layout(location = 2) out vec3 material;
layout(location = 3) out vec3 id;

void main()
{
   diffuse = fragVertColor;
   normal = fragNormal;
   material = vec3(0,1,0);
   id = vec3(1,0,0);


}
