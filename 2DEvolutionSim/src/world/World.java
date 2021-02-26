package world;

public class World {
	private Tile[][] world;
	private int size;
	private double worldAge;
	
	public World(int size) {
		this.size = size;
		this.worldAge = 0.0;
		world = new Tile[size][size];
	}
	
	public Tile[][] getWorld() {
		return this.world;
	}
	
	public void generate() {
		for (int y = 0; y < world.length; y++) {
			for (int x = 0; x < world[y].length; x++) {
				int rand = (int) (Math.random() * 10) + 1;
				if (rand == 1 || rand == 2) {
					world[y][x] = new Tile(TileType.Ocean);
				} else if (rand == 3) {
					world[y][x] = new Tile(TileType.Barren);
				} else {
					world[y][x] = new Tile(TileType.Fertile);
				}
			}
		}
	}
	
	public void update(double speed) {
		for (int y = 0; y < world.length; y++) {
			for (int x = 0; x < world[y].length; x++) {
				world[y][x].update(speed);
			}
		}
	}
	
	public double getWorldAge() {
		return this.worldAge;
	}
	
	public void setWorldAge(double newAge) {
		this.worldAge = newAge;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public Tile getTile(int x, int y) {
		return world[y][x];
	}
}
