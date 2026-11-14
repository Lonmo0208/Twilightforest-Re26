#version 330
#extension GL_ARB_separate_shader_objects : require

layout(location = 0) out vec4 fragColor;

uniform sampler2D Sampler0;

layout(location = 0) in vec2 texCoord0;
layout(location = 1) in vec4 vertexColor;

void main() {
    vec4 textureColor = texture(Sampler0, texCoord0);

    if (textureColor.a < 0.1) {
        discard;
    }

    fragColor = vec4(1.0, 0.15, 0.15, 1.0);
}
