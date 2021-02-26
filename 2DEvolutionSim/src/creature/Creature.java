package creature;

import java.util.Random;

import javafx.scene.paint.Color;

public class Creature {
	private String name;
	private Random r;
	private float rotation;
	private float x, y;
	private float frX, frY;
	private float flX, flY;
	private float energy;
	private float[] inputs;
	private float[][] weights1;
	private float[] hidden1;
	private float[][] weights2;
	private float[] hidden2;
	private float[][] weights3;
	private float[] outputs;
	
	public Creature(int mapHeight) {
		r = new Random();
		name = genName();
		x = (float) (Math.random() * mapHeight);
		y = (float) (Math.random() * mapHeight);
		inputs = new float[10];
		hidden1 = new float[10];
		hidden2 = new float[10];
		outputs = new float[8];
		weights1 = new float[inputs.length][hidden1.length];
		weights2 = new float[hidden1.length][hidden2.length];
		weights3 = new float[hidden2.length][outputs.length];
		generateWeights();
		flX = x;
		flY = y;
		frX = x;
		frY = y;
		energy = 120.0f;
		rotation = 0;
	}
	
	public Creature(Creature parent) {
		name = genName(parent.getName());
	}
	
	public void update(int mapH, double speed, boolean paused, float pixelsPerTile, Color centered, Color fl, Color fr) {
		if (!paused) {
			
			calculateAi(centered, fl, fr);
			
			/*
			for (int i = 0; i < 10; i++) {
				System.out.println(i + "\t" + String.format("%.3f \t %.3f \t %.3f", inputs[i], hidden1[i], hidden2[i]));
			}
			
			System.out.println("Outputs: " + String.format("%.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f", outputs[0], outputs[1], outputs[2], outputs[3], outputs[4], outputs[5], outputs[6], outputs[7]));
			*/
			rotation += outputs[3] * speed;
			if (rotation > 360.0f) {
				rotation -= 360.0f;
			} else if (rotation < 360.0f) {
				rotation += 360.0f;
			}
			
			x += Math.sin(rotation * Math.PI / 180.0f) * outputs[2] * speed * 0.5;
			y += Math.cos(rotation * Math.PI / 180.0f) * outputs[2] * speed * 0.5;
			
			if (x > mapH) {
				x = mapH - 1;
			}
			
			if (y > mapH) {
				y = mapH - 1;
			}
			
			if (x <= 0) {
				x = 1;
			}
			
			if (y <= 0) {
				y = 1;
			}
			
			flX = x + (float) (Math.sin((rotation * Math.PI / 180.0f) + (1.0f / 6.0f * Math.PI)) * 4 * getRadius(pixelsPerTile));
			flY = y + (float) (Math.cos((rotation * Math.PI / 180.0f) + (1.0f / 6.0f * Math.PI)) * 4 * getRadius(pixelsPerTile));
			
			frX = x + (float) (Math.sin((rotation * Math.PI / 180.0f) + (-1.0f / 6.0f * Math.PI)) * 4 * getRadius(pixelsPerTile));
			frY = y + (float) (Math.cos((rotation * Math.PI / 180.0f) + (-1.0f / 6.0f * Math.PI)) * 4 * getRadius(pixelsPerTile));
		}
	}
	
	private void calculateAi(Color c, Color fl, Color fr) {
		inputs = new float[] {(float) c.getRed(), (float) c.getGreen(), (float) c.getBlue(),
				(float) fr.getRed(), (float) fr.getGreen(), (float) fr.getBlue(),
				(float) fl.getRed(), (float) fl.getGreen(), (float) fl.getBlue(), energy};
		
		for (int i = 0; i < hidden1.length; i++) {
			float weightedSum = 0;
			for (int j = 0; j < inputs.length; j++) {
				weightedSum += inputs[j] * weights1[j][i];
			}
			
			hidden1[i] = AiMath.sigmoid(weightedSum);
		}
		
		for (int i = 0; i < hidden2.length; i++) {
			float weightedSum = 0;
			for (int j = 0; j < hidden1.length; j++) {
				weightedSum += hidden1[j] * weights2[j][i];
			}
			
			hidden2[i] = AiMath.sigmoid(weightedSum);
		}
		
		for (int i = 0; i < outputs.length; i++) {
			float weightedSum = 0;
			for (int j = 0; j < hidden2.length; j++) {
				weightedSum += hidden2[j] * weights3[j][i];
			}
			
			if (i < 5) {
				outputs[i] = AiMath.hyperTan(weightedSum);
			} else {
				outputs[i] = AiMath.sigmoid(weightedSum);
			}
		}
	}
	
	private void generateWeights() {
		for (int i = 0; i <  weights1.length; i++) {
			for (int j = 0; j < weights1[i].length; j++) {
				weights1[i][j] = (r.nextFloat() * 2.0f) - 1.0f;
			}
		}
		
		for (int i = 0; i <  weights2.length; i++) {
			for (int j = 0; j < weights2[i].length; j++) {
				weights2[i][j] = (r.nextFloat() * 2.0f) - 1.0f;
			}
		}
		
		for (int i = 0; i <  weights3.length; i++) {
			for (int j = 0; j < weights3[i].length; j++) {
				weights3[i][j] = (r.nextFloat() * 2.0f) - 1.0f;
			}
		}
	}
	
	private void generateWeights(Creature parent) {
		
	}
	
	private String genName() {
		String tempName = "";
		int rand = (int) (Math.random() * 6) + 4;
		for (int i = 0; i < rand; i++) {
			tempName += (char) ((int) (Math.random() * 26) + (int) 'a');
		}
		
		for (int i = 1; i < tempName.length() - 2; i++) {
			if (("aeiouy".indexOf(tempName.charAt(i - 1)) < 0) && ("aeiouy".indexOf(tempName.charAt(i)) < 0) && ("aeiouy".indexOf(tempName.charAt(i + 1)) < 0)) {
				char letter = "aeiouy".charAt((int) (Math.random() * 6));
				char[] letters = tempName.toCharArray();
				letters[i + ((int) (Math.random() * 3) - 1)] = letter;
				tempName = String.valueOf(letters);
			} else if (("aeiouy".indexOf(tempName.charAt(i - 1)) >= 0) && ("aeiouy".indexOf(tempName.charAt(i)) >= 0) && ("aeiouy".indexOf(tempName.charAt(i + 1)) >= 0)) {
				char letter = "bcdfghjklmnpqrstvwxz".charAt((int) (Math.random() * 20));
				char[] letters = tempName.toCharArray();
				letters[i + ((int) (Math.random() * 3) - 1)] = letter;
				tempName = String.valueOf(letters);
			}
		}
		
		return tempName;
	}
	
	private String genName(String pName) {
		String tempName1 = pName.substring(0, pName.length() / 2);
		String tempName2 = genName();
		tempName2 = tempName2.substring(tempName2.length() / 2);
		String tempName = tempName1 + tempName2;
		
		return tempName;
	}
	
	public float getRadius(float pixelsPerTile) {
		float radius = (float) Math.sqrt(energy);
		float pixelRadius = (pixelsPerTile * radius) / 80.0f;
		
		return pixelRadius;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Color getColor() {
		return new Color(outputs[5], outputs[6], outputs[7], 1.0f);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getFLX() {
		return flX;
	}
	
	public float getFLY() {
		return flY;
	}
	
	public float getFRX() {
		return frX;
	}
	
	public float getFRY() {
		return frY;
	}
}
