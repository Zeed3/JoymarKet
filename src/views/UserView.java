package views;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

/**
 * Component class for user navigation bar.
 * Provides buttons for navigation between different user functions.
 */

public class UserView extends HBox {

    public UserView(Consumer<String> onNav) {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color:#2c3e50;");

        Button productsBtn = create("Products");
        Button cartBtn = create("Cart");
        Button topupBtn = create("Top Up");
        Button ordersBtn = create("Orders");
        Button profileBtn = create("Profile");
        Button logoutBtn = createRed("Logout");

       
        productsBtn.setOnAction(e -> onNav.accept("PRODUCTS"));
        cartBtn.setOnAction(e -> onNav.accept("CART"));
        topupBtn.setOnAction(e -> onNav.accept("TOPUP"));
        ordersBtn.setOnAction(e -> onNav.accept("ORDERS"));
        profileBtn.setOnAction(e -> onNav.accept("PROFILE"));
        logoutBtn.setOnAction(e -> onNav.accept("LOGOUT"));

        getChildren().addAll(productsBtn, cartBtn, topupBtn, ordersBtn, profileBtn, logoutBtn);
    }

    private Button create(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#34495e; -fx-text-fill:white;");
        return b;
    }

    private Button createRed(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white;");
        return b;
    }
}
