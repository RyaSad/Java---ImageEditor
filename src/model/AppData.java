package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AppData {
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
}
