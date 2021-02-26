package creature;

public class AiMath {
	
	//encloses num between 0 and 1
	public static float sigmoid(float num) {
		return (float) (1.0f / (1.0f + Math.pow(Math.E, -num)));
	}
	
	//encloses num between -1 and 1
	public static float hyperTan(float num) {
		return (float) ((1.0f - Math.pow(Math.E, -2.0f * num)) / (1.0f + Math.pow(Math.E, 2.0f * num)));
	}
}
