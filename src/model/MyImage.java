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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import tasks.FilterTask;

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
	
	private FilterProperties fp;
	
	
	public MyImage(BufferedImage bi) {
		this.image = bi;
		this.image_width = bi.getWidth();
		this.image_height = bi.getHeight();
		this.total_pixels = this.image_height * this.image_width;
	}

	
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
	
	public void setNewImage(BufferedImage bi) {
		this.image = bi;
		this.image_width = bi.getWidth();
		this.image_height = bi.getHeight();
		this.total_pixels = bi.getWidth() * bi.getHeight();
	}


	public Image applyGlobalFilter(FilterProperties fp) {
		this.fp = fp;
		
		this.tempImage = deepCopy(this.image);
		
		if(this.fp.cLevel != 1.0) {
			RescaleOp rescaleOp = new RescaleOp(this.fp.cLevel, 0, null);
			rescaleOp.filter(tempImage, tempImage);
		}
		
		/*	Change Pixels	*/
		runThreads(Runtime.getRuntime().availableProcessors());
		
		Image updatedImage = SwingFXUtils.toFXImage(tempImage, null);
		return updatedImage;
	}
	
	public void runThreads(int nThreads) {
		Future<?>[] futures = new Future<?>[nThreads];
		
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		
		int height = this.tempImage.getHeight();
		int interval = height / nThreads;
		
		int s = 0; 
		int e = interval;
		for(int i = 0; i < nThreads; i++) {
			futures[i] = exec.submit(new FilterTask(this.tempImage, this.fp, s, e));
			s = e;
			e += interval;
			
			if(i == nThreads - 2) {
				e = Math.max(e,height);
			}
		}
		
		for(Future<?> f: futures) {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException ec) {
				ec.printStackTrace();
				System.exit(1);
			}
			
			
			
		}
	}
	
	
	@SuppressWarnings("static-access")
	public void ChangePixels(BufferedImage image, int startX, int endX, int startY, int endY) {
		
		image = scrambleImage(image, fp.scramble_factor); // will need to have function fixed for multi-threading
		
		for(int i = startX; i < endX; i++) {
			for(int j = startY; j < endY; j++) {
				
				Color c = new Color(image.getRGB(i, j));
				
				int rx = 0, gx = 0, bx = 0;
				
				if(fp.sepia) {
					rx = (int) ((0.393 * (double) c.getRed()) + 0.769 * ((double) c.getGreen()) + 0.189 * ((double) c.getBlue()));
					gx = (int) ((0.349 * (double) c.getRed()) + 0.686 * ((double) c.getGreen()) + 0.168 * ((double) c.getBlue()));
					bx = (int) ((0.272 * (double) c.getRed()) + 0.534 * ((double) c.getGreen()) + 0.131 * ((double) c.getBlue()));
					
					rx = Math.min(rx, 255);
					gx = Math.min(gx, 255);
					bx = Math.min(bx, 255);
				}else if(fp.greyscale) {
					rx = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
					gx = rx; bx = rx;
				}else if(fp.invert) {
					rx = 255 - c.getRed();
					gx = 255 - c.getGreen();
					bx = 255 - c.getBlue();
				}else {
					rx = Math.min(Math.max(0, c.getRed() + fp.r), 255);
					gx = Math.min(Math.max(0, c.getGreen() + fp.g), 255);
					bx = Math.min(Math.max(0, c.getBlue() + fp.b), 255);
				}
			
				float[] hsb = new float[3];
				c.RGBtoHSB(rx, gx, bx, hsb);
				
				float newSat = Math.min(fp.sat + hsb[1], 1.0f);
				float newBright = Math.min(fp.bLevel + hsb[2], 1.0f);
				
				newSat = Math.max(newSat, 0.0f);
				newBright = Math.max(newBright, 0.0f);
				
				image.setRGB(i, j, c.HSBtoRGB(fp.hue + hsb[0], newSat, newBright));
				
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

