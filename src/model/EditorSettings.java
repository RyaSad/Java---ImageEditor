package model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditorSettings implements Serializable{
	
	private Quality quality;
	
	private String importPath;
	private String exportPath;
	
	public EditorSettings() {
		loadSettingsFromDisk();
	}
	
	public void loadSettingsFromDisk() {
		File config = new File(System.getProperty("user.dir") + "\\config");
		
		if(!config.exists()) {
			return;
		}
		
		
	}
	
	public void exportSettingsToDisk() {
		
	}
	
	private void setQuality(Quality q) {
		this.quality = q;
	}
	
	public void displaySettingsWindow() {
		try {
			Stage stage = new Stage();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/Settings.fxml"));
			Scene scene = new Scene(root, 300, 400);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("ImageEditor v1.5");
			stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
