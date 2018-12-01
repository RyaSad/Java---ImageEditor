package app;
	
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/Editor.fxml"));
			Scene scene = new Scene(root, 980, 700);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("ImageEditor v1.2");
			//primaryStage.setOpacity(.9);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent winEvent){
		        Platform.exit();
		        System.exit(0);
		    }
		});
	}
	
	public static void main(String[] args) {

		launch(args);
	}
	


}


