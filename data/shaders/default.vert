#version 330 core

uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
uniform vec3 p_camera;

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;
layout(location = 2) in vec3 norm;

out float fragBrightness;
out vec2 fragTexCoord;

void main()
{

    gl_Position = m_proj * m_view * m_model * vec4(pos, 1.0);

    fragBrightness = 0.5;
    fragTexCoord = tex;
}