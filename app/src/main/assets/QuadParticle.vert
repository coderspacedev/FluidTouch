attribute vec2 attribPosition;
attribute vec3 attribColor;
attribute vec2 attribTexCoord;

varying vec3 varColor;
varying vec2 varTexCoord;

void main()
{
  gl_Position = vec4(attribPosition, 0.0, 1.0);
  varColor = attribColor;
  varTexCoord = attribTexCoord;
}