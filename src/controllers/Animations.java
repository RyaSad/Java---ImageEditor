package controllers;


import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.GlowBackground;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Animations {
	
	
	static ArrayList<AnimationFX> ALL_FX = new ArrayList<AnimationFX>();
	
	public Color RED = new Color(1,0,0,1);
	public Color GREEN = new Color(0,1,0,1);
	public Color BLUE = new Color(0,0,1,1);
	
	public Animations() {
		if(ALL_FX.size() == 0) {
			
		String dir = "C:\\Users\\ryans\\OneDrive\\eclipse-workspace\\AnimateFX-master\\AnimateFX-master\\animatefx\\src\\main\\java\\animatefx\\animation";
		
		 File file = null;
	     File[] paths;
	      
	     try {  
	         file = new File(dir);
	         paths = file.listFiles();
	         for(File path:paths) {	 
		         String temp = path.getName();
		         temp = temp.substring(0, temp.length()-5);
		         
		         
		         try {
						Class<?> cl = Class.forName("animatefx.animation." + temp);
						
						try {
							AnimationFX fx = (AnimationFX) cl.newInstance();
							ALL_FX.add(fx);
						} catch (InstantiationException | IllegalAccessException e) {
							continue;
						}
						
					} catch (ClassNotFoundException e) {
						continue;
					}
		         
	         }
	         
	      } catch(Exception e) {
	         e.printStackTrace();
	         return;
	      }
		
		}else {
			System.out.println(ALL_FX.size());
		}
	}
	
	public void Animate(AnimationFX fx, Node node, int cycles, Double duration, double speed) {
		fx.setNode(node);
		fx.setCycleCount(cycles);
		fx.setDelay(new Duration(duration));
		fx.setSpeed(speed);
		fx.play();
	}
	
	public void Animate(AnimationFX fx, Node node, int cycles, Double duration, double speed, AnimationFX nextFX) {
		fx.setNode(node);
		fx.setCycleCount(cycles);
		fx.setDelay(new Duration(duration));
		fx.setSpeed(speed);
		fx.playOnFinished(nextFX);
		fx.play();
	}
	
	public AnimationFX BuildAnimation(AnimationFX fx, Node node, int cycles, Double duration, double speed) {
		fx.setNode(node);
		fx.setCycleCount(cycles);
		fx.setDelay(new Duration(duration));
		fx.setSpeed(speed);
		return fx;
	}
	
	public void Glow(Region node, Color start, Color end, int color_steps, int cycles, double speed) {
		GlowBackground gb = new GlowBackground(node, start, end, color_steps);
		gb.setCycleCount(cycles).setDelay(new Duration(0.0)).setSpeed(speed);
		
		gb.play();
	}
	
	public void CycleImages(ImageView display, Image newImage, double speed) {
		FadeOut out = new FadeOut(display);
		out.setCycleCount(1).setDelay(new Duration(0.0)).setSpeed(speed);
		
		
		out.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				display.setImage(newImage);
				centerImage(display);
			}
			
		});
		
		FadeIn in = new FadeIn(display);
		in.setCycleCount(1).setDelay(new Duration(0.0)).setSpeed(speed);
		
		out.playOnFinished(in);
		out.play();
	}
	
	public void FadeOutAndIn(Node nodeOut, Node nodeIn, double speed) {
		FadeOut out = new FadeOut(nodeOut);
		out.setCycleCount(1).setDelay(new Duration(0.0)).setSpeed(speed);
		
		FadeIn in = new FadeIn(nodeIn);
		in.setCycleCount(1).setDelay(new Duration(0.0)).setSpeed(speed);
		
		out.playOnFinished(in);
		out.play();
	}
	
	
	public void centerImage(ImageView display) {
        Image img = display.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = display.getFitWidth() / img.getWidth();
            double ratioY = display.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            display.setX((display.getFitWidth() - w) / 2);
            display.setY((display.getFitHeight() - h) / 2);

        }
    }
	
	public void RandomAnimation(Node node, int cycles, Double duration, double speed) {
		Random rand = new Random();
		boolean anim = false;
		
		while(!anim) {
		
		AnimationFX fx = ALL_FX.get(rand.nextInt(ALL_FX.size()));
		try {
			fx.setNode(node);
			fx.setCycleCount(cycles).setDelay(new Duration(duration)).setSpeed(speed);
			fx.setResetOnFinished(true);
			fx.play();
			anim = true;
		}catch(Exception e) {
			fx = ALL_FX.get(rand.nextInt(ALL_FX.size()));
		}
		
		}
	}
	
	private void addAllFx(AnimationFX...animationFXs) {
		for(AnimationFX fx: animationFXs) {
			ALL_FX.add(fx);
		}
	}
}
