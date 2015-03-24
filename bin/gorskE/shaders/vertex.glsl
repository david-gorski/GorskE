#version 150 core

in vec3 in_Position;
in vec3 in_Color;
in vec3 in_Normal;
in vec2 in_TextureCoord;

out vec3 pass_Color;
out vec2 pass_TextureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(void) {
	mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(in_Position, 1.0);
	
	pass_Color = in_Color;
	pass_TextureCoord = in_TextureCoord;
}
