#version 330 core

uniform sampler2D tex;
uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
uniform vec3 p_camera;

in vec2 fragTexCoord;
in float fragBrightness;

out vec4 outColor;

void main()
{
    vec4 color = texture(tex, fragTexCoord);
    outColor = vec4(fragBrightness * color.rgb, color.a);
}