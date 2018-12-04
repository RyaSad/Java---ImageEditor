package model;

public enum Quality {
	VERY_LOW(352.0),
	LOW(480.0),
	NORMAL(858.0),
	HIGH(1280.0),
	VERY_HIGH(1920.0),
	ULTRA(3840.0);
	
	
	double resolution;
	
	Quality(double res) {
		this.resolution = res;
	}
	
	
	public double getQuality() {
		return this.resolution;
	}
	
	public void setQuality(double res) {
		this.resolution = res;
	}
}
