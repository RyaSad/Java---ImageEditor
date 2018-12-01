package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class MyImage{

	public ArrayList<Color> COLORS = new ArrayList<Color>();
	
	public BufferedImage image;
	
	private BufferedImage tempImage;
	
	public int image_width;
	public int image_height;
	public int total_pixels;
	
	public int totalRGB = 0;
	public int totalR = 0;
	public int totalG = 0;
	public int totalB = 0;
	
	private int r;
	private int g;
	private int b;
	private float cLevel;
	private float bLevel;
	private float sat;
	private float hue;
	private boolean greyscale;
	private boolean sepia;
	private boolean invert;
	private float scramble_factor;
	
	public MyImage(String filepath) {
		
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(filepath));
			this.image = bi;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		this.image_width = bi.getWidth();
		this.image_height = bi.getHeight();
		this.total_pixels = this.image_height * this.image_width;

	}


	public Image applyGlobalFilter(int r, int g, int b, float cLevel, float bLevel, float sat, float hue, boolean greyscale, boolean sepia, boolean invert, float scramble_factor) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.cLevel = cLevel;
		this.bLevel = bLevel;
		this.sat = sat;
		this.hue = hue;
		this.greyscale = greyscale;
		this.sepia = sepia;
		this.invert = invert;
		this.scramble_factor = scramble_factor;
		
		this.tempImage = deepCopy(this.image);
		
		ChangePixels(this.tempImage, 0, this.image_width, 0, this.image_height); // Full scale
		
		//ChangePixels(this.tempImage, 0, this.image_width, 0, this.image_height/10); // Thread 1
		
		//ChangePixels(this.tempImage, this.image_width/2, this.image_width, 0, this.image_height/2); // Thread 2
		
		//ChangePixels(this.tempImage, this.image_width/2, this.image_width, this.image_height/2, this.image_height); // Thread 3
		
		//ChangePixels(this.tempImage, 0, this.image_width/2, this.image_height/2, this.image_height); // Thread 4
		
		
		if(this.cLevel != 1.0) {
			RescaleOp rescaleOp = new RescaleOp(this.cLevel, 20, null);
			rescaleOp.filter(tempImage, tempImage);
		}
		Image updatedImage = SwingFXUtils.toFXImage(tempImage, null);
		return updatedImage;
	}
	
	
	@SuppressWarnings("static-access")
	public void ChangePixels(BufferedImage image, int startX, int endX, int startY, int endY) {
		
		image = scrambleImage(image, scramble_factor); // will need to have function fixed for multi-threading
		
		for(int i = startX; i < endX; i++) {
			for(int j = startY; j < endY; j++) {
				
				Color c = new Color(image.getRGB(i, j));
				
				int rx = 0, gx = 0, bx = 0;
				
				if(sepia) {
					rx = (int) ((0.393 * (double) c.getRed()) + 0.769 * ((double) c.getGreen()) + 0.189 * ((double) c.getBlue()));
					gx = (int) ((0.349 * (double) c.getRed()) + 0.686 * ((double) c.getGreen()) + 0.168 * ((double) c.getBlue()));
					bx = (int) ((0.272 * (double) c.getRed()) + 0.534 * ((double) c.getGreen()) + 0.131 * ((double) c.getBlue()));
					
					rx = Math.min(rx, 255);
					gx = Math.min(gx, 255);
					bx = Math.min(bx, 255);
				}else if(greyscale) {
					rx = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
					gx = rx; bx = rx;
				}else if(invert) {
					rx = 255 - c.getRed();
					gx = 255 - c.getGreen();
					bx = 255 - c.getBlue();
				}else {
					rx = Math.min(Math.max(0, c.getRed() + r), 255);
					gx = Math.min(Math.max(0, c.getGreen() + g), 255);
					bx = Math.min(Math.max(0, c.getBlue() + b), 255);
				}
			
				float[] hsb = new float[3];
				c.RGBtoHSB(rx, gx, bx, hsb);
				
				float newSat = Math.min(sat + hsb[1], 1.0f);
				float newBright = Math.min(bLevel + hsb[2], 1.0f);
				
				newSat = Math.max(newSat, 0.0f);
				newBright = Math.max(newBright, 0.0f);
				
				image.setRGB(i, j, c.HSBtoRGB(hue + hsb[0], newSat, newBright));
				
			}
		}
		return;
	}

	private BufferedImage scrambleImage(BufferedImage bi, float factor) {
		
			Random r = new Random();
			
			int ScrambleFactor = (int) (bi.getHeight() * bi.getWidth() * factor);
			
			for(int i = 0; i < ScrambleFactor; i++) {
				int rx1 = r.nextInt(bi.getWidth());
				int ry1 = r.nextInt(bi.getHeight());
				
				int rx2 = r.nextInt(bi.getWidth());
				int ry2 = r.nextInt(bi.getHeight());
				
				Color c1 = new Color(bi.getRGB(rx1, ry1));
				Color c2 = new Color(bi.getRGB(rx2, ry2));
				
				Color temp = new Color(c1.getRGB());
				
				c1 = new Color(c2.getRGB());
				c2 = new Color(temp.getRGB());
				
				bi.setRGB(rx1, ry1, c1.getRGB());
				bi.setRGB(rx2, ry2, c2.getRGB());
			}
			
			return bi;
		}

	public BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
}

