#version 330 core

uniform sampler2D diffuse;
uniform sampler2D normal;
uniform sampler2D material;
uniform sampler2D id;

in vec2 UV;

layout(location = 0) out vec3 outColor;

void main(){
    vec3 diffuseColor = texture(diffuse, UV).xyz;
    vec3 normalColor = texture(normal, UV).xyz;
    vec3 materialColor = texture(material , UV).xyz;
    vec3 idColor = texture(normal, UV).xyz;


    outColor = 0.25*(diffuseColor + normalColor);
    //outColor = normalColor;
}