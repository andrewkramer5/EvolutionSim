package world;

import javafx.scene.paint.Color;

public class Tile {
	
	private TileType type;
	private Color color;
	private float food;
	private float foodCap;
	private float greenValue;
	private float growthRate;
	private float creatureEnergyDeductionFactor;
	
	public Tile(TileType t) {
		this.type = t;
		this.food = 0.0f;
		this.greenValue = 0.39f;
		
		if (this.type == TileType.Ocean) {
			this.color = new Color(0.39, 0.58, 0.93, 1.0);
			this.growthRate = 0;
			this.foodCap = 0;
			this.creatureEnergyDeductionFactor = 2.0f;
		} else if (this.type == TileType.Barren) {
			this.color = new Color(0.59, 0.59, 0.59, 1.0);
			this.growthRate = 0;
			this.foodCap = 0;
			this.creatureEnergyDeductionFactor = 1.0f;
		} else {
			this.color = new Color(0.51, this.greenValue, 0, 1.0);
			this.growthRate = (float) (Math.random() * 0.5) + 0.05f;
			this.foodCap = (float) (Math.random() * 150) + 150.0f;
			this.creatureEnergyDeductionFactor = 1.0f;
		}
	}
	
	public void update(double speed) {
		if (this.type == TileType.Fertile) {
			if (food < foodCap) {
				food += growthRate * speed;
				greenValue = ((food * 100.0f / 300.0f) + 100.0f) / 255.0f;
				color = new Color(0.51, greenValue, 0, 1.0);
			} else {
				//grow nothing
			}
		}
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public float getCreatureEnergyDeductionFactor() {
		return this.creatureEnergyDeductionFactor;
	}
	
	public float getFood() {
		return this.food;
	}
}
