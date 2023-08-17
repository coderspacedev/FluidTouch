precision mediump float;

uniform sampler2D texture;

varying vec3 varColor;
varying vec2 varTexCoord;

uniform sampler2D texGlow;
uniform vec2 uScreenRes;
uniform float uGlowIntensity;

uniform sampler2D texShadow;

uniform float shadowActive;
uniform float lightActive;
uniform float inverseShadow;

uniform vec4 uLightPosRadiusIntensity;
uniform vec2 uViewportSizeNormalized;
uniform vec3 uLightColor;

vec3 glowSource()
{
	float dist = length((uLightPosRadiusIntensity.xy - gl_FragCoord.xy / uScreenRes) * uViewportSizeNormalized);
	vec3 v = uLightColor * uLightPosRadiusIntensity.w * clamp(1.0 - sqrt(0.5 * dist / uLightPosRadiusIntensity.z), 0.0, 1.0);
	return v*v;
}

void main()
{
	vec3 glow = texture2D(texGlow, gl_FragCoord.xy / uScreenRes).rgb;
	glow += lightActive * glowSource();
	
	// this readout of glow should be different when light or shadow is enabled (see glow_active_light_shadow), but particle glowing is quite rough anyway
	glow = clamp(2.0 * glow, 0.0, 1.0); 
	
	float shadow = texture2D(texShadow, gl_FragCoord.xy / uScreenRes).r;
	shadow = clamp(shadow, 0.2, 1.0);
	shadow = 1.0 - shadowActive*(1.0 - shadow);
	
	vec3 base = vec3(0.5) * (1.0 - uGlowIntensity);
	float shadowingFactor = mix(shadow, 1.0 - shadow, inverseShadow);
	vec3 brightness = base + shadowingFactor * glow.rgb * uGlowIntensity;
	
	gl_FragColor = vec4(brightness, 1.0) * texture2D(texture, varTexCoord) * vec4(varColor, 1.0);
	//gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
