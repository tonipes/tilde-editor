#version 330 core

struct lightSource{
    vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec4 position;
	float constantAttenuation;
    float linearAttenuation;
    float quadraticAttenuation;
};

struct material{
	vec4 emission;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float shininess;
};

uniform sampler2D t_diffuse;
uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;
uniform vec3 c_position;

uniform vec4 l_color;
uniform vec3 l_position;

in vec3 toLight;
in vec3 toCamera;
in vec3 normal;
in vec2 texcoords;

out vec4 outColor;


vec3 specularComponent(in vec3 N, in vec3 L, in vec3 V)
{
   float specularTerm = 0;

   // calculate specular reflection only if
   // the surface is oriented to the light source
   if(dot(N, L) > 0)
   {
      // half vector
      vec3 H = normalize(L + V);
      specularTerm = pow(dot(N, H), 64);
   }
   return (l_color.rgb * specularTerm) * l_color.a;
}

vec3 diffuseComponent(in vec3 N, in vec3 L)
{
   float diffuseTerm = clamp(dot(N, L), 0, 1);
   return (l_color.rgb * diffuseTerm) * l_color.a;
}

void main()
{
    // Old calculations
    //float dot_product = max(dot(toLight, normal), 0.0);
    //outColor = texture(tex, texcoords) * dot_product;

   // normalizevectors after interpolation
   vec3 L = normalize(toLight);
   vec3 V = normalize(toCamera);
   vec3 N = normalize(normal);

   vec3 amb = vec3(0.3,0.3,0.3);
   vec3 dif = diffuseComponent(N, L);
   vec3 spe = specularComponent(N, L, V);

   //vec3 diffuseColor = (texture(t_diffuse, texcoords).rgb + 0.7*length(dif)*l_color.rbg + length(amb) * amb) / 2;
   vec3 diffuseColor = texture(t_diffuse, texcoords).rgb;
   outColor = vec4(diffuseColor * (dif + spe + amb), 1);
   //vec3 diffuseColor = texture(t_diffuse, texcoords).rgb;
   //outColor = vec4(max(diffuseColor * (dif + spe),amb * diffuseColor),1);
}
