package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import animatefx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AppData;
import model.Quality;
import tasks.FilterTask;
import tasks.StageFadeOutTask;

public class SettingsController extends AppData{
	
	@FXML
	AnchorPane pane;
	
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
	
	static Animations ANIMATIONS = new Animations();
	
	ArrayList<AnimationFX> fx = new ArrayList<AnimationFX>();
	
	
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
		
		
		ANIMATIONS.Animate(new SlideInUp(), pane, 1, 0.0, 3.0);
		
	}
	
	
	
	@SuppressWarnings("unlikely-arg-type")
	public void ButtonAction(ActionEvent event) throws InterruptedException {
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
			
			//ANIMATIONS.Animate(new FadeOutDownBig(), b, 1, 0.0, 2.0);
			
			
		
			/*
			RotateOut anim = new RotateOut(pane);
			anim.setCycleCount(1).setDelay(new Duration(0.0)).setSpeed(2.0);
			anim.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					exportSettings();
					pending_update.setSelected(true);
					thisStage.close();
					
				}
				
			});
			
			anim.play();
			*/
			
			//TimeUnit.SECONDS.sleep(1);
			
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
