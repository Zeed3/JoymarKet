package utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class for managing JavaFX stages and switching scenes.
 * Provides static methods to set and switch scenes in the primary stage.
 */

public class StageManager {
	private static Stage primaryStage;

	public static Stage getStage() {
		return primaryStage;
	}
	
	public static void setStage(Stage stage) {
        primaryStage = stage;
    }
	
	public static void switchScene(Scene scene) {
		primaryStage.setScene(scene);
		primaryStage.show();
    }
}
