package main;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.StageManager;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		StageManager.setStage(primaryStage);
		StageManager.switchScene(null);
	}

}
