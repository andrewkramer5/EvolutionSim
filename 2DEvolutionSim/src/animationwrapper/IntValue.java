package animationwrapper;

public class IntValue {
	private int num;
	
	public IntValue() {
		num = 0;
	}
	
	public IntValue(int num) {
		this.num = num;
	}
	
	public void set(int num) {
		this.num = num;
	}
	
	public int get() {
		return this.num;
	}
	
	public void increment() {
		this.num++;
	}
	
	public void increment(int num) {
		this.num += num;
	}
}
