package model;

public class FilterProperties {
	
	public int r;
	public int g;
	public int b;
	public float cLevel;
	public float bLevel;
	public float sat;
	public float hue;
	public boolean greyscale;
	public boolean sepia;
	public boolean invert;
	public float scramble_factor;
	
	public FilterProperties(int r, int g, int b, float cLevel, float bLevel, float sat, float hue, boolean greyscale,
			boolean sepia, boolean invert, float scramble_factor) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.cLevel = cLevel;
		this.bLevel = bLevel;
		this.sat = sat;
		this.hue = hue;
		this.greyscale = greyscale;
		this.sepia = sepia;
		this.invert = invert;
		this.scramble_factor = scramble_factor;
	}
	
	

}
