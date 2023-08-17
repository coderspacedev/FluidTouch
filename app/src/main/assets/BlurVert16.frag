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
	
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 16.0*blurSize)).rgb * uCoeffs[0];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 15.0*blurSize)).rgb * uCoeffs[1];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 14.0*blurSize)).rgb * uCoeffs[2];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 13.0*blurSize)).rgb * uCoeffs[3];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 12.0*blurSize)).rgb * uCoeffs[4];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 11.0*blurSize)).rgb * uCoeffs[5];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 10.0*blurSize)).rgb * uCoeffs[6];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 9.0*blurSize)).rgb * uCoeffs[7];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 8.0*blurSize)).rgb * uCoeffs[8];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 7.0*blurSize)).rgb * uCoeffs[9];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 6.0*blurSize)).rgb * uCoeffs[10];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 5.0*blurSize)).rgb * uCoeffs[11];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 4.0*blurSize)).rgb * uCoeffs[12];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 3.0*blurSize)).rgb * uCoeffs[13];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 2.0*blurSize)).rgb * uCoeffs[14];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 1.0*blurSize)).rgb * uCoeffs[15];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y)).rgb * uCoeffs[16];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 1.0*blurSize)).rgb * uCoeffs[15];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 2.0*blurSize)).rgb * uCoeffs[14];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 3.0*blurSize)).rgb * uCoeffs[13];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 4.0*blurSize)).rgb * uCoeffs[12];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 5.0*blurSize)).rgb * uCoeffs[11];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 6.0*blurSize)).rgb * uCoeffs[10];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 7.0*blurSize)).rgb * uCoeffs[9];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 8.0*blurSize)).rgb * uCoeffs[8];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 9.0*blurSize)).rgb * uCoeffs[7];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 10.0*blurSize)).rgb * uCoeffs[6];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 11.0*blurSize)).rgb * uCoeffs[5];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 12.0*blurSize)).rgb * uCoeffs[4];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 13.0*blurSize)).rgb * uCoeffs[3];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 14.0*blurSize)).rgb * uCoeffs[2];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 15.0*blurSize)).rgb * uCoeffs[1];
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 16.0*blurSize)).rgb * uCoeffs[0];
	
	sum *= uCoeffsSumInv;
	
	/*
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 5.5*blurSize)).rgb * 0.09;
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 3.5*blurSize)).rgb * 0.14;
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y - 1.5*blurSize)).rgb * 0.2;
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y)).rgb * 0.14;
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 1.5*blurSize)).rgb * 0.2;
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 3.5*blurSize)).rgb * 0.14;
	sum += texture2D(tex, vec2(varyTexCoord.x, varyTexCoord.y + 5.5*blurSize)).rgb * 0.09;
	*/
	
	gl_FragColor = vec4((sum), 1.0);
}
