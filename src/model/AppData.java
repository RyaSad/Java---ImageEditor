package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.control.ToggleButton;

public class AppData {
	
	protected static EditorSettings settings;
	
	protected static ToggleButton pending_update = new ToggleButton();
	
	
	public void savePreset(Preset p) {
		File file = new File(".\\presets\\" + p.toString());
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(p);
			oos.close();
		}catch (Exception e){
			return;
		}
	}
    

    public Preset restorePreset(String name) {
		File file = new File(".\\presets\\" + name);
		if(!file.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Preset p = (Preset) ois.readObject();
			ois.close();
			return p;
		}catch (Exception e){
			System.out.println(e.getLocalizedMessage());
			return null;
		}
	}
    
    
    public void exportSettings() {
		File file = new File(".\\config");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Unable to create new config file.");
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(settings);
			oos.close();
		}catch (Exception e){
			return;
		}
	}
    

    public void importSettings() {
		File file = new File(".\\config");
	
		if(!file.exists()) {
			settings = new EditorSettings();
			exportSettings();
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			settings = (EditorSettings) ois.readObject();
			ois.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
