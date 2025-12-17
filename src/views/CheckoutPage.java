package views;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CartItem;
import model.Customer;
import model.OrderHeader;
import model.Product;
import model.Promo;

public class CheckoutPage {
    
    private Stage stage;
    private Customer customer;
    private ArrayList<CartItem> cartItems;
    private VBox container;
    private TextField promoField;
    private Label totalLabel;
    private Label discountLabel;
    private Label finalLabel;
    private double totalAmount;
    private double discount;
    
    public CheckoutPage(Stage stage, Customer customer, ArrayList<CartItem> cartItems) {
        this.stage = stage;
        this.customer = customer;
        this.cartItems = cartItems;
        calculateTotal();
        initialize();
    }
    
    private void calculateTotal() {
        totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }
    }
    
    private void initialize() {
        container = new VBox(15);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);
        
        Label title = new Label("Checkout");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        ListView<String> itemsList = new ListView<>();
        for (CartItem item : cartItems) {
            Product p = item.getProduct();
            itemsList.getItems().add(p.getName() + " x" + item.getCount() + 
                " = Rp " + String.format("%,.0f", item.getSubtotal()));
        }
        itemsList.setPrefHeight(200);
        
        HBox promoBox = new HBox(10);
        promoBox.setAlignment(Pos.CENTER);
        Label promoLabel = new Label("Promo Code:");
        promoField = new TextField();
        promoField.setPromptText("Optional");
        Button applyPromo = new Button("Apply");
        applyPromo.setOnAction(e -> applyPromoCode());
        promoBox.getChildren().addAll(promoLabel, promoField, applyPromo);
        
        totalLabel = new Label("Subtotal: Rp " + String.format("%,.0f", totalAmount));
        discountLabel = new Label("Discount: Rp 0");
        finalLabel = new Label("Total: Rp " + String.format("%,.0f", totalAmount));
        finalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label balanceLabel = new Label("Your Balance: Rp " + String.format("%,.0f", customer.getBalance()));
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button confirmButton = new Button("Confirm Order");
        confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> confirmOrder());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> {
            CartPage cartPage = new CartPage(stage, customer);
            cartPage.show();
        });
        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        
        container.getChildren().addAll(title, itemsList, promoBox, totalLabel, 
            discountLabel, finalLabel, balanceLabel, buttonBox);
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, 800, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Checkout");
    }
    
    private void applyPromoCode() {
        String code = promoField.getText().trim();
        if (code.isEmpty()) return;
        
        Promo promo = Promo.getPromoByCode(code);
        if (promo == null) {
            showAlert("Invalid promo code");
            return;
        }
        
        discount = promo.calculateDiscount(totalAmount);
        double finalAmount = totalAmount - discount;
        
        discountLabel.setText("Discount: -Rp " + String.format("%,.0f", discount));
        finalLabel.setText("Total: Rp " + String.format("%,.0f", finalAmount));
    }
    
    private void confirmOrder() {
        double finalAmount = totalAmount - discount;
        
        if (!customer.hasSufficientBalance(finalAmount)) {
            showAlert("Insufficient balance");
            return;
        }
        
        String promoCode = promoField.getText().trim();
        String idPromo = null;
        if (!promoCode.isEmpty()) {
            Promo promo = Promo.getPromoByCode(promoCode);
            if (promo != null) {
                idPromo = promo.getIdPromo();
            }
        }
        
        OrderHeader order = OrderHeader.createOrderHeader(
            customer.getIdCustomer(), idPromo, cartItems);
        
        if (order != null) {
            customer.deductBalance(finalAmount);
            CartItem.clearCart(customer.getIdCustomer());
            
            for (CartItem item : cartItems) {
                Product p = Product.getProduct(item.getIdProduct());
                p.reduceStock(item.getCount());
            }
 
            CustomerDashboard dashboard = new CustomerDashboard(stage, customer);
            dashboard.show();
        } else {
            showAlert("Order failed");
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show() {
        stage.show();
    }
}