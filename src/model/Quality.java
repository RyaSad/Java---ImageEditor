package model;

public enum Quality {
	LOW(480.0),
	NORMAL(720.0),
	HIGH(1920.0),
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
