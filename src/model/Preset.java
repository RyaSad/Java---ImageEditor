package model;

import java.io.Serializable;

import javafx.scene.control.Toggle;

public class Preset implements Serializable{
	
	public String name;

	public double red;
	public double green;
	public double blue;
	public int fx_toggle;
	
	public double hue;
	public double saturation;
	public double contrast;
	public double brightness;
	
	public double scramble;
	
	public Preset(String name) {
		this.name = name;

	}
	
	public String toString() {
		return this.name;
	}
	

}
