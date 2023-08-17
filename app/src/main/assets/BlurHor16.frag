precision mediump float;

uniform sampler2D tex;
uniform float uPixelSize;
uniform float uCoeffs[17];
uniform float uCoeffsSumInv;

varying vec2 varyTexCoord;

void main()
{
	vec3 sum = vec3(0.0);
	float blurSize = uPixelSize;
	
	sum += texture2D(tex, vec2(varyTexCoord.x - 16.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[0];
	sum += texture2D(tex, vec2(varyTexCoord.x - 15.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[1];
	sum += texture2D(tex, vec2(varyTexCoord.x - 14.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[2];
	sum += texture2D(tex, vec2(varyTexCoord.x - 13.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[3];
	sum += texture2D(tex, vec2(varyTexCoord.x - 12.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[4];
	sum += texture2D(tex, vec2(varyTexCoord.x - 11.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[5];
	sum += texture2D(tex, vec2(varyTexCoord.x - 10.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[6];
	sum += texture2D(tex, vec2(varyTexCoord.x - 9.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[7];
	sum += texture2D(tex, vec2(varyTexCoord.x - 8.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[8];
	sum += texture2D(tex, vec2(varyTexCoord.x - 7.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[9];
	sum += texture2D(tex, vec2(varyTexCoord.x - 6.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[10];
	sum += texture2D(tex, vec2(varyTexCoord.x - 5.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[11];
	sum += texture2D(tex, vec2(varyTexCoord.x - 4.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[12];
	sum += texture2D(tex, vec2(varyTexCoord.x - 3.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[13];
	sum += texture2D(tex, vec2(varyTexCoord.x - 2.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[14];
	sum += texture2D(tex, vec2(varyTexCoord.x - 1.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[15];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y)).rgb * uCoeffs[16];
	sum += texture2D(tex, vec2(varyTexCoord.x + 1.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[15];
	sum += texture2D(tex, vec2(varyTexCoord.x + 2.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[14];
	sum += texture2D(tex, vec2(varyTexCoord.x + 3.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[13];
	sum += texture2D(tex, vec2(varyTexCoord.x + 4.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[12];
	sum += texture2D(tex, vec2(varyTexCoord.x + 5.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[11];
	sum += texture2D(tex, vec2(varyTexCoord.x + 6.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[10];
	sum += texture2D(tex, vec2(varyTexCoord.x + 7.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[9];
	sum += texture2D(tex, vec2(varyTexCoord.x + 8.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[8];
	sum += texture2D(tex, vec2(varyTexCoord.x + 9.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[7];
	sum += texture2D(tex, vec2(varyTexCoord.x + 10.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[6];
	sum += texture2D(tex, vec2(varyTexCoord.x + 11.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[5];
	sum += texture2D(tex, vec2(varyTexCoord.x + 12.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[4];
	sum += texture2D(tex, vec2(varyTexCoord.x + 13.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[3];
	sum += texture2D(tex, vec2(varyTexCoord.x + 14.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[2];
	sum += texture2D(tex, vec2(varyTexCoord.x + 15.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[1];
	sum += texture2D(tex, vec2(varyTexCoord.x + 16.0*blurSize, varyTexCoord.y)).rgb * uCoeffs[0];
	
	sum *= uCoeffsSumInv;
	
	/*
	sum += texture2D(tex, vec2(varyTexCoord.x - 5.5*blurSize, varyTexCoord.y)).rgb * 0.09;
	sum += texture2D(tex, vec2(varyTexCoord.x - 3.5*blurSize, varyTexCoord.y)).rgb * 0.14;
	sum += texture2D(tex, vec2(varyTexCoord.x - 1.5*blurSize, varyTexCoord.y)).rgb * 0.2;
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y)).rgb * 0.14;
	sum += texture2D(tex, vec2(varyTexCoord.x + 1.5*blurSize, varyTexCoord.y)).rgb * 0.2;
	sum += texture2D(tex, vec2(varyTexCoord.x + 3.5*blurSize, varyTexCoord.y)).rgb * 0.14;
	sum += texture2D(tex, vec2(varyTexCoord.x + 5.5*blurSize, varyTexCoord.y)).rgb * 0.09;
	*/

	gl_FragColor = vec4((sum), 1.0);
}
