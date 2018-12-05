package controllers;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import animatefx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AppData;
import model.DefaultPreset;
import model.FilterProperties;
import model.MyImage;
import model.Preset;
import model.Quality;
import tasks.ExportTask;

public class EditorController extends AppData{
	/*	Display	*/
	@FXML
	ImageView image_display;
	
	@FXML
	ImageView image_loading;
	
	
	/*	Buttons	*/
	@FXML
	Button button_export;
	
	@FXML
	Button button_import;
	
	@FXML
	Button button_openWorkspace;
	
	@FXML
	Button button_newPreset;
	
	@FXML
	Button button_deletePreset;
	
	@FXML
	Button button_randomize;
	
	@FXML
	Button button_settings;
		
	
	/* Text	*/	
	@FXML
	Text progress_export_text;
	
	@FXML
	Text text_redValue;
	
	@FXML
	Text text_greenValue;
	
	@FXML
	Text text_blueValue;
	
	@FXML
	Text text_contrastValue;
	
	@FXML
	Text text_brightnessValue;
	
	@FXML
	Text text_saturationValue;
	
	@FXML
	Text text_hueValue;

	@FXML
	Text text_scrambleValue;
	
	
	/*	Progress Bar	*/	
	@FXML
	ProgressBar progress_export;
	
	
	/*	Sliders	*/
	@FXML
	Slider slider_red;
	
	@FXML
	Slider slider_green;
	
	@FXML
	Slider slider_blue;
	
	@FXML
	Slider slider_contrast;
	
	@FXML
	Slider slider_brightness;
	
	@FXML
	Slider slider_saturation;
	
	@FXML
	Slider slider_hue;
	
	@FXML
	Slider slider_scramble;
	
	
	/*	CheckBox	*/
	@FXML
	CheckBox checkBox_greyscale;
	
	@FXML
	CheckBox checkBox_sepia;
	
	@FXML
	CheckBox checkBox_invert;
	
	/*	Toggles	*/
	@FXML
	ToggleGroup fx_group;
	
	@FXML
	RadioButton toggle_none;
	
	@FXML
	RadioButton toggle_greyscale;
	
	@FXML
	RadioButton toggle_sepia;
	
	@FXML
	RadioButton toggle_invert;
	
	/*	Dropdown list	*/
	
	@FXML
	ComboBox<Preset> dropdown_presets;
	
	
	Stage thisStage;
	
	MyImage thisImage;
	
	Image fxImage;
	
	ObservableList<Preset> presets = FXCollections.observableArrayList();
	
	static Animations ANIMATIONS = new Animations();
	
	

	
	@FXML
	void initialize() {
		
		importSettings();
		pending_update.setSelected(false);
		
		presets.add(new DefaultPreset());
		load_presets_from_disk();
		dropdown_presets.setItems(presets);
		dropdown_presets.setValue(presets.get(0));		
		
		 dropdown_presets.valueProperty().addListener(new ChangeListener<Preset>() {
	            public void changed(ObservableValue<? extends Preset> ov,
	            		Preset old_val, Preset new_val) {
	                    load_preset(new_val);
	            }
	        });
		 
		 pending_update.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
            		Boolean old_val, Boolean new_val) {
            	
            		pending_update.setSelected(false);
                    if(new_val == true && fxImage != null) {
                    	refreshQuality();
                    	
                    }
            }
		 });
		 
		
		 slider_red.valueProperty().addListener(sliderListener(text_redValue, "%.0f"));
		 slider_red.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_red, 0.0));

		 slider_green.valueProperty().addListener(sliderListener(text_greenValue, "%.0f"));
		 slider_green.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_green, 0.0));

		 slider_blue.valueProperty().addListener(sliderListener(text_blueValue, "%.0f"));
		 slider_blue.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_blue, 0.0));

		 slider_contrast.valueProperty().addListener(sliderListener(text_contrastValue, "%.2f"));
		 slider_contrast.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_contrast, 0.0));
		 
		 slider_brightness.valueProperty().addListener(sliderListener(text_brightnessValue, "%.2f"));
		 slider_brightness.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_brightness, 0.0));
		 
		 slider_saturation.valueProperty().addListener(sliderListener(text_saturationValue, "%.2f"));
		 slider_saturation.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_saturation, 0.0));

		 slider_hue.valueProperty().addListener(sliderListener(text_hueValue, "%.2f"));
		 slider_hue.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_hue, 0.0));
		 
		 slider_scramble.valueProperty().addListener(sliderListener(text_scrambleValue, "%.2f"));
		 slider_scramble.setOnMouseClicked(mouseEvent(MouseButton.MIDDLE, 1, slider_scramble, 0.0));
		 
		 fx_group.selectedToggleProperty().addListener(toggleListener());
		
	}
	
	private ChangeListener<Number> sliderListener(Text value_text, String format){
		return new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    value_text.setText(String.format(format, new_val));
	                    set_filters();
	            }
	        };
	}
	
	private ChangeListener<Toggle> toggleListener(){
		
		return new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
            		Toggle old_val, Toggle new_val) {
                    set_filters();
            }
        };
        
	}
	
	private EventHandler<MouseEvent> mouseEvent(MouseButton button, int click_count, Node target_node, double newValue){
		return new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(button) && mouseEvent.getClickCount() == click_count){
		        	if(target_node instanceof Slider) {
		        		Slider s = (Slider) target_node;
		        		s.setValue(newValue);
		        	}
		        }
		    }
		};
	}
	
	private void set_filters() {
		
		if(!fx_group.getSelectedToggle().equals(toggle_none)) {
			disableRGB(true);
		}else {
			disableRGB(false);
		}
		
		if(thisImage == null) {
			return;
		}
		
		Image i = thisImage.applyGlobalFilter(buildFilterProperties());
		image_display.setImage(i);
	}
	
	private void disableRGB(boolean b) {
		slider_red.setDisable(b);
		slider_green.setDisable(b);
		slider_blue.setDisable(b);
	}
	
	private boolean isToggle(RadioButton rb) {
		return fx_group.getSelectedToggle().equals(rb);
	}
	
	public void ButtonAction(ActionEvent event) throws IOException, InterruptedException {
		Button b = (Button) event.getSource();
		thisStage = (Stage) b.getScene().getWindow();
		
		if(b == button_import) {
			
			File import_file = fileSelect("Select a file to import");
			if(import_file == null) {
				return;
			}
			resetSliders();
			thisImage = new MyImage(import_file.getCanonicalPath().toString());
			
			try {
				thisImage.applyGlobalFilter(buildFilterProperties());
			}catch(Exception e) {
				showError("Unable to edit this image.\n" + e.getLocalizedMessage());
				return;
			}
			
			Image i = new Image(import_file.toURI().toString());
			fxImage = i;
			
			BufferedImage reScaled = scaleDown(fxImage);
			Image updatedImage = SwingFXUtils.toFXImage(reScaled, null);
			
			thisImage.setNewImage(reScaled);

			//image_display.setImage(updatedImage);
			//centerImage();
			//ANIMATIONS.Animate(new FadeIn(), image_display, 1, 0.0, 1.5);
			
			if(image_display.getImage() == null) {
				ANIMATIONS.Animate(new FadeIn(), image_display, 1, 0.0, 1.5);
				image_display.setImage(updatedImage);
				ANIMATIONS.centerImage(image_display);
			}else {
				ANIMATIONS.CycleImages(image_display, updatedImage, 1.5);
			}
			
			
		}
		else if(b == button_export) {
			if(thisImage == null) {
				showError("No image to export.");
				return;
			}
	        
			String filepath = fileSave();
			if(filepath == null) {
				return;
			}
			
			progress_export.setProgress(0.0);
			FadeInUp animBar = new FadeInUp(progress_export);
			animBar.setCycleCount(1).setDelay(new Duration(0.0)).setSpeed(2.0);
			animBar.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					BufferedImage temp = thisImage.image;
					BufferedImage toExport = SwingFXUtils.fromFXImage(fxImage, null);
					thisImage.image = toExport;
			        Task<Void> exportTask = new ExportTask(toExport, buildFilterProperties(), progress_export, progress_export_text, image_loading,  filepath);
			        Thread exportThread = new Thread(exportTask);
			        exportThread.setDaemon(true);
			        exportThread.start();
			        thisImage.image = temp;
				}
				
			});
			
			ANIMATIONS.Animate(new FadeInUp(), progress_export_text, 1, 0.0, 2.0);
				progress_export_text.setText("Writing Image...");
				progress_export_text.setVisible(true);
				
			ANIMATIONS.Animate(new FadeInUp(), image_loading, 1, 0.0, 2.0);
				image_loading.setVisible(true);
				
			animBar.play();
				progress_export.setVisible(true);
				
		}
		else if(b == button_openWorkspace) {
			Runtime.getRuntime().exec("explorer.exe " + System.getProperty("user.dir"));
			return;
		}
		else if(b == button_newPreset) {
			String preset_name = inputBox("Create new preset", "Enter a name for the new preset:");
			if(preset_name != null && !preset_name.equals("")) {
				createPreset(preset_name);
			}
		}
		else if(b == button_deletePreset) {
			
			Preset selected_preset = dropdown_presets.getValue();
			 
			if(selected_preset.toString().equals("Default")) {
				 showError("Cannot delete default preset.");
				 return;
			 }
			 
			Alert alert = new Alert(AlertType.CONFIRMATION,"Are you sure you want to delete?", ButtonType.YES, ButtonType.CANCEL);
		     Optional<ButtonType> response = alert.showAndWait();
			 if (response.get() == ButtonType.CANCEL) {
				 return;
			 }
			 
			 File file = new File(".\\presets\\" + selected_preset.toString());
			 if(!file.delete()) {
				 showError("Unable to delete preset.");
				 return;
			 }
			 presets.remove(selected_preset);
			 
			 dropdown_presets.setItems(presets);
			 dropdown_presets.setValue(presets.get(0));
		}
		else if(b == button_randomize) {
			//ANIMATIONS.Animate(new JackInTheBox(), button_randomize, 1, 0.0, 2.0);
			ANIMATIONS.RandomAnimation(button_randomize, 1, 0.0, 1.0);
			randomizeValues();
		}
		else if(b == button_settings) {
			settings.displaySettingsWindow();
		}
	}
	
	
	public void refreshQuality() {
		BufferedImage reScaled = scaleDown(fxImage);
		Image updatedImage = SwingFXUtils.toFXImage(reScaled, null);
		
		thisImage.setNewImage(reScaled);
		image_display.setImage(updatedImage);
		ANIMATIONS.centerImage(image_display);
		set_filters();
	}
	
	public void randomizeValues() {
		Random rand = new Random();
		slider_red.setValue((2*rand.nextDouble() - 1)* 255);
		slider_green.setValue((2*rand.nextDouble()-1) * 255);
		slider_blue.setValue((2*rand.nextDouble()-1) * 255);
		slider_hue.setValue(2*rand.nextDouble()-1);
		slider_saturation.setValue(2*rand.nextDouble()-1);
		slider_contrast.setValue(2*rand.nextDouble()-1);
		slider_brightness.setValue(2*rand.nextDouble()-1);
	}
	
	public void createPreset(String name) {
		Preset preset = new Preset(name);
		
		preset.red = slider_red.getValue();
		preset.green = slider_green.getValue();
		preset.blue = slider_blue.getValue();
		
		Toggle l = fx_group.getSelectedToggle();
		
		if(l == toggle_none) {
			preset.fx_toggle = 0;
		}else if(l == toggle_greyscale) {
			preset.fx_toggle = 1;
		}else if(l == toggle_sepia) {
			System.out.println("here");
			preset.fx_toggle = 2;
		}else if(l == toggle_invert) {
			preset.fx_toggle = 3;
		}
		
		preset.hue = slider_hue.getValue();
		preset.saturation = slider_saturation.getValue();
		preset.contrast = slider_contrast.getValue();
		preset.brightness = slider_brightness.getValue();
		
		preset.scramble = slider_scramble.getValue();
		
		presets.add(preset);
		dropdown_presets.setItems(presets);
		dropdown_presets.setValue(preset);
		
		savePreset(preset);
	}
	
	public void load_preset(Preset p) {
		slider_red.setValue(p.red);
		slider_green.setValue(p.green);
		slider_blue.setValue(p.blue);
		
		switch(p.fx_toggle) {
			case 1: fx_group.selectToggle(toggle_greyscale); break;
			case 2: fx_group.selectToggle(toggle_sepia); break;
			case 3: fx_group.selectToggle(toggle_invert); break;
			default: fx_group.selectToggle(toggle_none); break;
		}
		
		slider_hue.setValue(p.hue);
		slider_saturation.setValue(p.saturation);
		slider_contrast.setValue(p.contrast);
		slider_brightness.setValue(p.brightness);
		
		slider_scramble.setValue(p.scramble);
	}
	
	public void load_presets_from_disk() {
		 File file = null;
	     File[] paths;
	      
	     try {  
	         file = new File(".\\presets\\");
	         paths = file.listFiles();
	         for(File preset_file: paths) {	 
		         String name = preset_file.getName();
		         Preset p = restorePreset(name);
		         presets.add(p);
	         }
	         
	      } catch(Exception e) {
	    	 showError("Could not load presets.");
	         return;
	      }
	}
	
	public void CheckBoxAction(ActionEvent event) {
		CheckBox cb = (CheckBox) event.getSource();
		
		if(cb == checkBox_greyscale || cb == checkBox_sepia || cb == checkBox_invert) {
			set_filters();
			return;
		}
	}
	
	private void resetSliders() {
		slider_red.setValue(0.0);
		slider_green.setValue(0.0);
		slider_blue.setValue(0.0);
		
		slider_contrast.setValue(0.0);
		slider_brightness.setValue(0.0);
		slider_saturation.setValue(0.0);
		slider_hue.setValue(0.0);
		slider_scramble.setValue(0.0);
		
		fx_group.selectToggle(toggle_none);
		
		progress_export.setVisible(false);
		progress_export_text.setVisible(false);
		
		dropdown_presets.setValue(presets.get(0));
	}
	
	
	public File fileSelect(String title) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.setInitialDirectory(new File(settings.getImportPath()));
		String imageExtensions[]= {"*.png","*.jpg"};
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", imageExtensions));
		
		File f = fileChooser.showOpenDialog(thisStage);
    	return f;
	}
	
	public String fileSave() {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export image");
        fileChooser.setInitialDirectory(new File(settings.getExportPath()));
        String imageExtensions[]= {"*.png"};
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", imageExtensions));
		
		File file = fileChooser.showSaveDialog(thisStage);
		
        String path = "";
        try {
        	path = file.getCanonicalPath();
        }catch(Exception e) {
        	return null;
        }
        return path;
	}
	
	public String inputBox(String title, String prompt) {
		 TextInputDialog inputBox = new TextInputDialog();
		 inputBox.setTitle(title);
		 inputBox.setHeaderText(prompt);
		 inputBox.setWidth(150);
		 inputBox.showAndWait();
		 return inputBox.getResult();
	 }
	
	public void showError(String errorMessage) {
	    Alert alert = new Alert(AlertType.ERROR,errorMessage);
		alert.showAndWait();
	 }
	
	public FilterProperties buildFilterProperties() {
		return new FilterProperties((int)slider_red.getValue(), (int)slider_green.getValue(), (int)slider_blue.getValue(),
				(float) (slider_contrast.getValue() + 1), (float) slider_brightness.getValue(), (float) slider_saturation.getValue(), (float) slider_hue.getValue(),
				isToggle(toggle_greyscale), isToggle(toggle_sepia), isToggle(toggle_invert), (float) slider_scramble.getValue());
	}
	
	public BufferedImage scaleDown(Image image) {
		double maxP = settings.getQuality().getQuality();
		
		int scaleFactor = (int) Math.round(Math.max(image.getWidth(), image.getHeight()) / maxP);
		
		if(scaleFactor < 1) {
			scaleFactor = 1;
		}
		
		BufferedImage img = new BufferedImage((int)image.getWidth()/scaleFactor, (int)image.getHeight()/scaleFactor, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < (int)img.getHeight(); i++) {
			for(int j = 0; j < (int)img.getWidth(); j++) {

				javafx.scene.paint.Color c = image.getPixelReader().getColor(j*scaleFactor, i*scaleFactor);
				
				Color cc = new Color((int)(c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
				
				img.setRGB(j, i, cc.getRGB());
			}
		}
		return img;
	}
}
