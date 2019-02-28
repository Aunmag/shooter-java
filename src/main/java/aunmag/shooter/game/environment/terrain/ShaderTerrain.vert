#version 120

attribute vec3 vertices;
attribute vec2 textures;
varying vec2 texturesCoordinates;
uniform mat4 projection;
uniform int quantity;

void main() {
    texturesCoordinates = textures * quantity;
    gl_Position = projection * vec4(vertices, 1);
}
