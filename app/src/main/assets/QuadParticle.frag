precision mediump float;

uniform sampler2D texture;

varying vec3 varColor;
varying vec2 varTexCoord;

void main()
{
	gl_FragColor = texture2D(texture, varTexCoord) * vec4(varColor, 1.0);
	//gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
