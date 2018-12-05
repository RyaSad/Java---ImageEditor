package tasks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import animatefx.animation.*;
import controllers.Animations;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.FilterProperties;
import model.MyImage;

public class ExportTask extends Task<Void>{

	private BufferedImage img;
	private FilterProperties fp;
	private ProgressBar bar;
	private Text progText;
	private String filepath;
	private MyImage mi;
	
	private ImageView loading_image;
	
	static Animations ANIMATIONS = new Animations();
	
	public ExportTask(BufferedImage img, FilterProperties fp, ProgressBar bar, Text progText, ImageView loading, String filepath) {
		this.bar = bar;
		this.filepath = filepath;
		this.progText = progText;
		this.fp = fp;
		
		this.mi = new MyImage(img);
		this.img = SwingFXUtils.fromFXImage(mi.applyGlobalFilter(this.fp), null);
		this.loading_image = loading;
	}
	
	@Override
	protected Void call() throws Exception {
		TimeUnit.SECONDS.sleep(1);
		double factor = 1 / ((double) (img.getWidth() * img.getHeight()));
		
		BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		int i,j;
		for(i = 0; i < img.getWidth(); i++) {
			for(j = 0; j < img.getHeight(); j++) {
				newImage.setRGB(i, j, img.getRGB(i, j));
				bar.setProgress(bar.getProgress() + factor);
			}
		}
		
		progText.setText("Saving to Disk...");
		TimeUnit.SECONDS.sleep(1);
		File output_file = new File(filepath);
		
		try {
			ImageIO.write(newImage, "png", output_file);
		} catch (IOException e) {
			System.out.println("Unable to write image file.");
			System.exit(1);
		}
		
		bar.setProgress(1.0);
		progText.setText("Exported to: " + filepath);
		loading_image.setVisible(false);
		
		ANIMATIONS.Animate(new Bounce(), progText, 1, 0.0, 1.5);
		ANIMATIONS.Animate(new Bounce(), bar, 1, 0.0, 1.5);
		
		return null;
	}
	
}
