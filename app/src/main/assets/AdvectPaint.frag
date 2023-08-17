precision highp float;

uniform sampler2D texPaint;
uniform sampler2D texInput;
uniform sampler2D texVel;
uniform vec2 uVelScale;
uniform float uFadeCoeff;
uniform float uBorderMirror;
uniform float uBorderRepeat;

varying highp vec2 varyTexCoord;

vec2 repeatAndMirrorTex(vec2 t)
{
    t = t.x > 1.0 ? vec2(mix(t.x, t.x - 1.0, uBorderRepeat), mix(t.y, 1.0 - t.y, uBorderMirror)) : t;
    t = t.x < 0.0 ? vec2(mix(t.x, t.x + 1.0, uBorderRepeat), mix(t.y, 1.0 - t.y, uBorderMirror)) : t;
    return t;
}


void main()
{
	// vec2 offset = (texture2D(texVel, varyTexCoord).rg * 2.0 - vec2(1.0)) * 0.5 * uVelScale;
	highp vec2 offset = texture2D(texVel, varyTexCoord).rg * uVelScale;
	highp vec2 samplePos = varyTexCoord - offset;
	
	samplePos = repeatAndMirrorTex(samplePos);
	samplePos = repeatAndMirrorTex(samplePos.yx).yx;
	//samplePos.yx = repeatAndMirrorTex(samplePos.yx);
	
	vec4 paintInput = texture2D(texInput, samplePos);
	paintInput = 25.0 * (paintInput * paintInput); // decoding of encoding defined in Procedures.cpp -> copyVelAndPaintInputLoop
	vec4 sample = texture2D(texPaint, samplePos) + paintInput;

	gl_FragColor = sample * uFadeCoeff;
}
