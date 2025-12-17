package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;

/**
 * View class for the customer dashboard interface.
 * Displays balance, navigation menu, and quick action cards for shopping.
 */

public class CustomerDashboard{
    
	private Stage stage;
    private Customer customer;
    private Label balanceLabel;
    private Scene scene;
    private BorderPane mainContainer;
    
    public CustomerDashboard(Stage stage, Customer customer) {
        this.stage = stage;
        this.customer = customer;
        initialize();
    }
    
    private void initialize() {
    	mainContainer = new BorderPane();
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");

        
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(20, 20, 10, 20));
        topSection.setAlignment(Pos.CENTER);
        topSection.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 0 0 2 0;");

        Label titleLabel = new Label("JoymarKet");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label welcomeLabel = new Label("Welcome, " + customer.getFullName());
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #555;");

        balanceLabel = new Label("Balance: Rp " + String.format("%,.0f", customer.getBalance()));
        balanceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");

        topSection.getChildren().addAll(titleLabel, welcomeLabel, balanceLabel);

        // HORIZONTAL NAVBAR
        HBox navbar = new HBox(0);
        navbar.setAlignment(Pos.CENTER);
        navbar.setStyle("-fx-background-color: #2196F3;");

        Button viewProducts = createNavButton("View Products", "#2196F3");
        viewProducts.setOnAction(e -> {
            ProductPage page = new ProductPage(stage, customer);
            page.show();
        });

        Button viewCart = createNavButton("My Cart", "#2196F3");
        viewCart.setOnAction(e -> {
            CartPage page = new CartPage(stage, customer);
            page.show();
        });

        Button orderHistory = createNavButton("Order History", "#2196F3");
        orderHistory.setOnAction(e -> {
            OrderHistory page = new OrderHistory(stage, customer);
            page.show();
        });

        Button topUp = createNavButton("Top Up Balance", "#2196F3");
        topUp.setOnAction(e -> {
            TopupPage page = new TopupPage(stage, customer);
            page.show();
        });

        Button editProfile = createNavButton("Edit Profile", "#2196F3");
        editProfile.setOnAction(e -> {
            EditProfile page = new EditProfile(stage, customer);
            page.show();
        });

        Button logout = createNavButton("Logout", "#F44336");
        logout.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText("Confirm Logout");
            confirm.setContentText("Are you sure you want to logout?");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LoginView page = new LoginView(stage);
                    page.show();
                }
            });
        });

        navbar.getChildren().addAll(
            viewProducts,
            viewCart,
            orderHistory,
            topUp,
            editProfile,
            logout
        );

        
        VBox centerContent = new VBox(30);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(50));

        Label welcomeTitle = new Label("Welcome to JoymarKet!");
        welcomeTitle.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label welcomeSubtitle = new Label("Your one-stop shop for quality products");
        welcomeSubtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");

        
        HBox quickActions = new HBox(20);
        quickActions.setAlignment(Pos.CENTER);

        VBox shopCard = createQuickActionCard(
            "🛍️",
            "Shop Now",
            "Browse our products",
            e -> {
                ProductPage page = new ProductPage(stage, customer);
                page.show();
            }
        );

        VBox cartCard = createQuickActionCard(
            "🛒",
            "View Cart",
            "Check your items",
            e -> {
                CartPage page = new CartPage(stage, customer);
                page.show();
            }
        );

        VBox ordersCard = createQuickActionCard(
            "📦",
            "My Orders",
            "Track your orders",
            e -> {
                OrderHistory page = new OrderHistory(stage, customer);
                page.show();
            }
        );

        quickActions.getChildren().addAll(shopCard, cartCard, ordersCard);

        centerContent.getChildren().addAll(welcomeTitle, welcomeSubtitle, quickActions);

        // FOOTER
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15));
        footer.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 2 0 0 0;");

        Label footerLabel = new Label("© 2025 JoymarKet. All rights reserved.");
        footerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");

        footer.getChildren().add(footerLabel);

        
        VBox topWithNavbar = new VBox(0);
        topWithNavbar.getChildren().addAll(topSection, navbar);

        mainContainer.setTop(topWithNavbar);
        mainContainer.setCenter(centerContent);
        mainContainer.setBottom(footer);

        scene = new Scene(mainContainer, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Customer Dashboard");
    }
    private Button createNavButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(50);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: rgba(255,255,255,0.3);" +
            "-fx-border-width: 0 1 0 1;" +
            "-fx-background-radius: 0;" +
            "-fx-cursor: hand;"
        );

        
        button.setOnMouseEntered(e -> {
            if (color.equals("#F44336")) {
                button.setStyle(
                    "-fx-background-color: #D32F2F;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-color: rgba(255,255,255,0.3);" +
                    "-fx-border-width: 0 1 0 1;" +
                    "-fx-background-radius: 0;" +
                    "-fx-cursor: hand;"
                );
            } else {
                button.setStyle(
                    "-fx-background-color: #1976D2;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-color: rgba(255,255,255,0.3);" +
                    "-fx-border-width: 0 1 0 1;" +
                    "-fx-background-radius: 0;" +
                    "-fx-cursor: hand;"
                );
            }
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: rgba(255,255,255,0.3);" +
                "-fx-border-width: 0 1 0 1;" +
                "-fx-background-radius: 0;" +
                "-fx-cursor: hand;"
            );
        });

        return button;
    }
    private VBox createQuickActionCard(String icon, String title, String subtitle, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefSize(250, 200);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 48px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        card.getChildren().addAll(iconLabel, titleLabel, subtitleLabel);

        
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: #E3F2FD;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });

        card.setOnMouseClicked(e -> action.handle(new javafx.event.ActionEvent()));

        return card;
    }
        
    
    private Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(300);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px;");
        return btn;
    }
    
    public void refresh() {
        customer = Customer.getCustomer(customer.getIdCustomer());
        balanceLabel.setText("Balance: Rp " + String.format("%,.0f", customer.getBalance()));
    }
    
    public void show() {
        stage.show();
    }
}