#version 120

uniform sampler2D sampler;
uniform vec4 colour;
varying vec2 texturesCoordinates;

void main() {
    gl_FragColor = texture2D(sampler, texturesCoordinates) * colour;
}
