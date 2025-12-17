package main;

import javafx.application.Application;
import javafx.stage.Stage;
<<<<<<< HEAD
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
=======
import views.LoginView;


public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Start with Login Page
        LoginView loginPage = new LoginView(primaryStage);
        loginPage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
>>>>>>> update_version
