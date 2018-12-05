package tasks;

import java.util.concurrent.TimeUnit;

import javafx.stage.Stage;

public class StageFadeOutTask implements Runnable{
	
	Stage stage;
	
	public StageFadeOutTask(Stage stage) {
		this.stage = stage;
	}


	@Override
	public void run() {
		while(stage.getOpacity() > 0) {
			stage.setOpacity(stage.getOpacity() - 0.1);
			
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
