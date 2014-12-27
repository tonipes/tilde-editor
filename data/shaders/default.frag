#version 330 core

uniform sampler2D tex;
uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;

in vec2 fragTexCoord;
in float fragBrightness;

out vec4 outColor;

void main()
{

    vec4 color = texture(tex, fragTexCoord);
    outColor = vec4((fragBrightness+0.2) * 0.5 * color.rgb, color.a);
}