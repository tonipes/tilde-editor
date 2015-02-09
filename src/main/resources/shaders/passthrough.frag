#version 330 core

uniform sampler2D diffuse;
uniform sampler2D normal;

in vec2 UV;

layout(location = 0) out vec3 outColor;

void main(){
    vec3 diffuseColor = texture(diffuse, UV).xyz;
    vec3 normalColor = texture(normal, UV).xyz;
    outColor = 0.5*(diffuseColor + normalColor);
}