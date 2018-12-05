package model;

import java.io.File;
import java.io.Serializable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditorSettings implements Serializable{
	
	private AppData data;
	
	private Quality quality = Quality.NORMAL;
	
	private String importPath = System.getProperty("user.dir");
	private String exportPath = System.getProperty("user.dir");
	
	public EditorSettings() {
		
	}
	
	public void exportSettingsToDisk() {
		
	}
	
	public void setQuality(Quality q) {
		this.quality = q;
	}
	
	public Quality getQuality() {
		return quality;
	}
	
	public String getImportPath() {
		return importPath;
	}
	
	public String getExportPath() {
		return exportPath;
	}
	
	public void setImportPath(String path) {
		this.importPath = path;
	}
	
	public void setExportPath(String path) {
		this.exportPath = path;
	}
	
	public void displaySettingsWindow() {
		try {
			Stage stage = new Stage();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/Settings.fxml"));
			Scene scene = new Scene(root, 368, 400);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Editor Settings");
			stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
