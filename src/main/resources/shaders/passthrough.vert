#version 330 core

uniform sampler2D diffuse;
uniform sampler2D normal;

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 uv;

out vec2 UV;

void main()
{
    UV = uv;
    gl_Position = vec4(pos, 1.0);
}
