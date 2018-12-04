package tasks;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import model.FilterProperties;

public class FilterTask implements Runnable{
	
	private BufferedImage img;
	private FilterProperties fp;
	
	private int startY;
	private int endY;
		
	public FilterTask(BufferedImage img, FilterProperties fp, int startY, int endY) {
		this.img = img;
		this.fp = fp;
		this.startY = startY;
		this.endY = endY;
	}

	@Override
	public void run() {
		ChangePixels(img, 0, img.getWidth(), startY, endY);
	}
	
	public void ChangePixels(BufferedImage image, int startX, int endX, int startY, int endY) {
		
		//image = scrambleImage(image, fp.scramble_factor); // will need to have function fixed for multi-threading
		
		Graphics2D g = this.img.createGraphics();
		
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
				
				
				//image.setRGB(i, j, c.HSBtoRGB(fp.hue + hsb[0], newSat, newBright));
				
				//rgbArray[index] = c.HSBtoRGB(fp.hue + hsb[0], newSat, newBright);

				
				
				
				g.setColor(new Color(c.HSBtoRGB(fp.hue + hsb[0], newSat, newBright)));
				g.fillRect(i, j, 1, 1);
				
				
			}
		}
		g.dispose();
		//System.out.println(rgbArray.length);
		//image.setRGB(startX, startY, this.img.getWidth(), endY - startY, rgbArray, 0, this.img.getWidth());
		/*
		for(int rgb: rgbArray) {
			Color c = new Color(rgb);
			System.out.println(c.toString());
		}
		*/
		//image.setRGB(0, 0, this.img.getWidth(), this.img.getHeight(), rgbArray, 0, this.img.getWidth());
		
		return;
	}



}