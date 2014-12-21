#version 330 core

uniform mat4 model_m;

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;

out vec2 texCoords;

void main()
{
    texCoords = tex;
    gl_Position = model_m * vec4(pos, 1.0);
}