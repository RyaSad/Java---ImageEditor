package tasks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import model.MyImage;

public class ExportTask extends Task<Void>{

	private BufferedImage img;
	private ProgressBar bar;
	private Text progText;
	private String filepath;
	
	public ExportTask(BufferedImage img, ProgressBar bar, Text progText, String filepath) {
		this.img = img;
		this.bar = bar;
		this.filepath = filepath;
		this.progText = progText;
	}
	
	@Override
	protected Void call() throws Exception {
		progText.setText("Exporting Image");
		progText.setVisible(true);
		bar.setVisible(true);
		bar.setProgress(0.0);
		double factor = 1 / ((double) (img.getWidth() * img.getHeight()));
		
		BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		int i,j;
		for(i = 0; i < img.getWidth(); i++) {
			for(j = 0; j < img.getHeight(); j++) {
				newImage.setRGB(i, j, img.getRGB(i, j));
				bar.setProgress(bar.getProgress() + factor);
			}
		}
		
		File output_file = new File(filepath);
		try {
			ImageIO.write(newImage, "png", output_file);
		} catch (IOException e) {
			System.out.println("Unable to write image file.");
			System.exit(1);
		}
		bar.setProgress(1.0);
		progText.setText("Image successfully exported to: " + filepath);
		return null;
	}

}