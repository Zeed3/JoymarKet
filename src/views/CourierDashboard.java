package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Courier;

/**
 * View class for the courier dashboard interface.
 * Provides access to delivery management and logout.
 */

public class CourierDashboard {
    
    private Stage stage;
    private Courier courier;
    
    public CourierDashboard(Stage stage, Courier courier) {
        this.stage = stage;
        this.courier = courier;
        initialize();
    }
    
    private void initialize() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        Label welcome = new Label("Courier Dashboard");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label nameLabel = new Label("Welcome, " + courier.getFullName());
        nameLabel.setStyle("-fx-font-size: 18px;");
        
        Button viewDeliveries = createButton("My Deliveries", "#2196F3");
        viewDeliveries.setOnAction(e -> {
        	ViewDeliveriesPage page = new ViewDeliveriesPage(stage, courier);
            page.show();
        });
        
        Button logout = createButton("Logout", "#F44336");
        logout.setOnAction(e -> {
            LoginView loginPage = new LoginView(stage);
            loginPage.show();
        });
        
        container.getChildren().addAll(welcome, nameLabel, viewDeliveries, logout);
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Courier");
    }
    
    private Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(300);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px;");
        return btn;
    }
    
    public void show() {
        stage.show();
    }
}
