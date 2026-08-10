#version 330

layout(location = 0) out vec4 fragColor;

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;

void main() {
    vec4 textureColor = texture(Sampler0, texCoord0);

    if (textureColor.a < 0.1) {
        discard;
    }

    fragColor = vec4(1.0, 0.15, 0.15, 1.0);
}