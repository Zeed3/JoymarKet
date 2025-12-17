package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;

public class AdminView {
    
    private Stage stage;
    private Admin admin;
    
    public AdminView(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
        initialize();
    }
    
    private void initialize() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        Label welcome = new Label("Admin Dashboard");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label nameLabel = new Label("Welcome, " + admin.getFullName());
        nameLabel.setStyle("-fx-font-size: 18px;");
        
        Button manageProducts = createButton("Manage Products", "#2196F3");
        manageProducts.setOnAction(e -> {
            ManageProductPage page = new ManageProductPage(stage, admin);
            page.show();
        });
        
        Button viewCouriers = createButton("View All Couriers", "#FF9800");
        viewCouriers.setOnAction(e -> {
            ViewCouriersPage page = new ViewCouriersPage(stage, admin);
            page.show();
        });
        
        Button viewOrders = createButton("View All Orders", "#9C27B0");
        viewOrders.setOnAction(e -> {
            ViewOrdersPage page = new ViewOrdersPage(stage, admin);
            page.show();
        });
        Button viewDeliveriesButton = createButton("View All Deliveries", "#9C27B0");
        viewDeliveriesButton.setOnAction(e -> {
            ViewDeliveriesPage page = new ViewDeliveriesPage(stage, admin); // Pass Admin
            page.show();
        });
        
        Button logout = createButton("Logout", "#F44336");
        logout.setOnAction(e -> {
            LoginView loginPage = new LoginView(stage);
            loginPage.show();
        });
        
        container.getChildren().addAll(welcome, nameLabel, manageProducts, 
            viewCouriers, viewOrders, logout);
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Admin");
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
