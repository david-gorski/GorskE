#version 150 core

in vec3 pass_Color;
in vec2 pass_TextureCoord;

out vec4 fragColor;

uniform sampler2D tex;

void main(void) {
	vec4 textureColor = texture(tex, pass_TextureCoord);
    fragColor = vec4(pass_Color, 1.0) * textureColor;
}