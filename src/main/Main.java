package main;

import javafx.application.Application;
import javafx.stage.Stage;
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