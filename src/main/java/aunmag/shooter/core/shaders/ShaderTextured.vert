#version 120

attribute vec3 vertices;
attribute vec2 textures;
varying vec2 texturesCoordinates;
uniform mat4 projection;

void main() {
    texturesCoordinates = textures;
    gl_Position = projection * vec4(vertices, 1);
}
