package controllers;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.AppData;
import model.Quality;

public class SettingsController extends AppData{
	
	@FXML
	HBox quality_container;
	
	@FXML
	Button button_saveAndExit;
	
	@FXML
	Button button_selectImportFolder;
	
	@FXML
	Button button_selectExportFolder;
	
	@FXML
	Button button_defaultImport;
	
	@FXML
	Button button_defaultExport;
	
	@FXML
	ToggleGroup display_quality;
	
	static Stage thisStage;
	
	
	@FXML
	void initialize() {
		
		switch(settings.getQuality()) {
			case VERY_LOW: display_quality.selectToggle((Toggle) quality_container.getChildren().get(0)); break;
			case LOW: display_quality.selectToggle((Toggle) quality_container.getChildren().get(1)); break;
			case NORMAL: display_quality.selectToggle((Toggle) quality_container.getChildren().get(2)); break;
			case HIGH: display_quality.selectToggle((Toggle) quality_container.getChildren().get(3)); break;
			case VERY_HIGH: display_quality.selectToggle((Toggle) quality_container.getChildren().get(4)); break;
			case ULTRA: display_quality.selectToggle((Toggle) quality_container.getChildren().get(5)); break;
		}
	}
	
	
	
	@SuppressWarnings("unlikely-arg-type")
	public void ButtonAction(ActionEvent event) {
		Button b = (Button) event.getSource();
		thisStage = (Stage) b.getScene().getWindow();
		
		if(b == button_saveAndExit) {
			
			switch(quality_container.getChildren().indexOf(display_quality.getSelectedToggle())) {
				case 0: settings.setQuality(Quality.VERY_LOW); break;
				case 1: settings.setQuality(Quality.LOW); break;
				case 2: settings.setQuality(Quality.NORMAL); break;
				case 3: settings.setQuality(Quality.HIGH); break;
				case 4: settings.setQuality(Quality.VERY_HIGH); break;
				case 5: settings.setQuality(Quality.ULTRA); break;
			}
			
			exportSettings();
			pending_update.setSelected(true);
			thisStage.close();
			
		}
		else if(b == button_selectImportFolder) {
			File dir = chooseDirectory();
			if(dir == null) { return; }
			
			try {
				settings.setImportPath(dir.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(b == button_selectExportFolder) {
			File dir = chooseDirectory();
			if(dir == null) { return; }
			
			try {
				settings.setExportPath(dir.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(b == button_defaultImport) {
			settings.setImportPath(System.getProperty("user.dir"));
		}
		else if(b == button_defaultExport) {
			settings.setExportPath(System.getProperty("user.dir"));
		}
	}
	
	
	private File chooseDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select a folder.");
		return directoryChooser.showDialog(thisStage);
		
	}
}
