#version 150 core

uniform mat4 MVP;

in vec3 in_Position;
in vec3 in_Color;
in vec3 in_Normal;
in vec2 in_TextureCoord;

out vec3 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {
    gl_Position = MVP * vec4(in_Position, 1.0);
	
	pass_Color = in_Color;
	pass_TextureCoord = in_TextureCoord;
}
