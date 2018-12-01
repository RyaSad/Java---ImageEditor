package controllers;


import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AppData;
import model.DefaultPreset;
import model.DefaultPreset.BadPreset;
import model.MyImage;
import model.Preset;
import tasks.ExportTask;

public class EditorController {
	/*	Display	*/
	@FXML
	ImageView image_display;
	
	
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
	
	AppData data = new AppData();

	
	@FXML
	void initialize() {
		
		dropdown_presets.setEditable(false);
		progress_export_text.setVisible(false);
		progress_export.setVisible(false);
		
		text_redValue.setText("0");
		text_greenValue.setText("0");
		text_blueValue.setText("0");
		
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
		
		
		 slider_red.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_redValue.setText(String.format("%.0f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_red.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_red.setValue(0.0);
			        }
			    }
			});
		 
		 slider_green.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_greenValue.setText(String.format("%.0f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_green.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_green.setValue(0.0);
			        }
			    }
			});
		 
		 slider_blue.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_blueValue.setText(String.format("%.0f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_blue.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_blue.setValue(0.0);
			        }
			    }
			});
		 
		 slider_contrast.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_contrastValue.setText(String.format("%.2f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_contrast.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_contrast.setValue(0.0);
			        }
			    }
			});
		 
		 slider_brightness.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_brightnessValue.setText(String.format("%.2f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_brightness.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_brightness.setValue(0.0);
			        }
			    }
			});
		 
		 slider_saturation.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_saturationValue.setText(String.format("%.2f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_saturation.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_saturation.setValue(0.0);
			        }
			    }
			});
		 
		 slider_hue.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_hueValue.setText(String.format("%.2f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_hue.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_hue.setValue(0.0);
			        }
			    }
			});
		 
		 slider_scramble.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    text_scrambleValue.setText(String.format("%.2f", new_val));
	                    set_filters();
	            }
	        });
		 
		 slider_scramble.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        if(mouseEvent.getButton().equals(MouseButton.MIDDLE)){
			        	slider_scramble.setValue(0.0);
			        }
			    }
			});
		 
		 
		 fx_group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	            public void changed(ObservableValue<? extends Toggle> ov,
	            		Toggle old_val, Toggle new_val) {
	                    set_filters();
	            }
	        });
		
		
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
		
		Image i = thisImage.applyGlobalFilter((int)slider_red.getValue(), (int)slider_green.getValue(), (int)slider_blue.getValue(),
				(float) slider_contrast.getValue() + 1, (float) slider_brightness.getValue(), (float) slider_saturation.getValue(), (float) slider_hue.getValue(),
				isToggle(toggle_greyscale), isToggle(toggle_sepia), isToggle(toggle_invert), (float) slider_scramble.getValue());
		fxImage = i;
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
			thisImage = new MyImage(import_file.getCanonicalPath().toString());
			
			try {
				set_filters();
			}catch(Exception e) {
				showError("Unable to edit this image.\n" + e.getLocalizedMessage());
				return;
			}
			
			Image i = new Image(import_file.toURI().toString());
			fxImage = i;
			image_display.setImage(i);
			resetSliders();
			centerImage();
			
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
			
	        Task<Void> exportTask = new ExportTask(SwingFXUtils.fromFXImage(fxImage, null), progress_export, progress_export_text, filepath);
	        Thread exportThread = new Thread(exportTask);
	        exportThread.setDaemon(true);
	        exportThread.start();

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
			randomizeValues();
		}
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
		
		//slider_scramble.setValue(rand.nextDouble());
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
		
		data.savePreset(preset);
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
		         Preset p = data.restorePreset(name);
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
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		String imageExtensions[]= {"*.png","*.jpg"};
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", imageExtensions));
		
		File f = fileChooser.showOpenDialog(thisStage);
    	return f;
	}
	
	public String fileSave() {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export image");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        String imageExtensions[]= {"*.png","*.jpg"};
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
	
	public void centerImage() {
        Image img = image_display.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = image_display.getFitWidth() / img.getWidth();
            double ratioY = image_display.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            image_display.setX((image_display.getFitWidth() - w) / 2);
            image_display.setY((image_display.getFitHeight() - h) / 2);

        }
    }
}
