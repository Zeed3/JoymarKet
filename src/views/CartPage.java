package views;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import model.CartItem;
import model.Customer;
import model.Product;

/**
 * View class for displaying and managing the shopping cart.
 * Allows updating quantities, removing items, and proceeding to checkout.
 */

public class CartPage {
    
    private Stage stage;
    private Scene scene;
    private BorderPane mainContainer;
    private VBox contentContainer;
    private Customer customer;
    private TableView<CartItem> cartTable;
    private Label totalLabel;
    private Label messageLabel;
    private Button checkoutButton;
    private Button updateButton;
    private Button removeButton;
    private Button refreshButton;
    private Button backButton;
    private TextField quantityField;
    
    public CartPage(Stage stage, Customer customer) {
        this.stage = stage;
        this.customer = customer;
        initialize();
        setLayout();
        setEventHandlers();
        loadCart();
    }
    
    private void initialize() {
        mainContainer = new BorderPane();
        contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(20));
        
        Label titleLabel = new Label("My Shopping Cart");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        cartTable = new TableView<>();
        cartTable.setPrefHeight(400);
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<CartItem, String> idCol = new TableColumn<>("Product ID");
        idCol.setCellValueFactory(data -> {
            Product product = data.getValue().getProduct();
            return new javafx.beans.property.SimpleStringProperty(
                product != null ? product.getIdProduct() : ""
            );
        });
        idCol.setPrefWidth(100);
        
        TableColumn<CartItem, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(data -> {
            Product product = data.getValue().getProduct();
            return new javafx.beans.property.SimpleStringProperty(
                product != null ? product.getName() : ""
            );
        });
        nameCol.setPrefWidth(200);
        
        TableColumn<CartItem, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> {
            Product product = data.getValue().getProduct();
            double price = product != null ? product.getPrice() : 0.0;
            return new javafx.beans.property.SimpleStringProperty(
                "Rp " + String.format("%,.0f", price)
            );
        });
        priceCol.setPrefWidth(120);
        
        TableColumn<CartItem, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data -> {
            int quantity = data.getValue().getCount();
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(quantity));
        });
        qtyCol.setPrefWidth(100);
        qtyCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<CartItem, String> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(data -> {
            double subtotal = data.getValue().getSubtotal();
            return new javafx.beans.property.SimpleStringProperty(
                "Rp " + String.format("%,.0f", subtotal)
            );
        });
        subtotalCol.setPrefWidth(150);
        
        cartTable.getColumns().add(idCol);
        cartTable.getColumns().add(nameCol);
        cartTable.getColumns().add(priceCol);
        cartTable.getColumns().add(qtyCol);
        cartTable.getColumns().add(subtotalCol);
        
        totalLabel = new Label("Total: Rp 0");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Label qtyLabel = new Label("New Quantity:");
        quantityField = new TextField();
        quantityField.setPromptText("Enter new quantity");
        quantityField.setPrefWidth(100);
        
        updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        updateButton.setPrefWidth(100);
        
        removeButton = new Button("Remove");
        removeButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 14px;");
        removeButton.setPrefWidth(100);
        
        refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setPrefWidth(100);
        
        checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        checkoutButton.setPrefWidth(100);
        
        backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setPrefWidth(100);
        
        actionBox.getChildren().addAll(qtyLabel, quantityField, updateButton, removeButton, refreshButton, checkoutButton, backButton);
        
        contentContainer.getChildren().addAll(titleLabel, cartTable, totalLabel, messageLabel, actionBox);
    }
    
    private void setLayout() {
        mainContainer.setCenter(contentContainer);
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        scene = new Scene(mainContainer, 900, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - My Cart");
    }
    
    private void setEventHandlers() {
        updateButton.setOnAction(e -> handleUpdate());
        removeButton.setOnAction(e -> handleRemove());
        checkoutButton.setOnAction(e -> handleCheckout());
        refreshButton.setOnAction(e -> loadCart());
        backButton.setOnAction(e -> {
            Customer refreshedCustomer = Customer.getCustomer(customer.getIdCustomer());
            CustomerDashboard dashboard = new CustomerDashboard(stage, refreshedCustomer);
            dashboard.show();
        });
    }
    
    private void loadCart() {
        ArrayList<CartItem> cartItems = CartItem.getCartItems(customer.getIdCustomer());
        cartTable.setItems(FXCollections.observableArrayList(cartItems));
        cartTable.refresh();
        
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        totalLabel.setText("Total: Rp " + String.format("%,.0f", total));
        
        if (cartItems.isEmpty()) {
            showMessage("Your cart is empty", true);
        } else {
            showMessage("Cart loaded: " + cartItems.size() + " item(s)", false);
        }
    }
    
    private void handleUpdate() {
        CartItem selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select an item to update", true);
            return;
        }
        
        String qtyText = quantityField.getText().trim();
        if (qtyText.isEmpty()) {
            showMessage("Please enter new quantity", true);
            return;
        }
        
        try {
            int newQty = Integer.parseInt(qtyText);
            if (newQty <= 0) {
                showMessage("Quantity must be greater than 0", true);
                return;
            }
            
            Product product = selected.getProduct();
            int oldQty = selected.getCount();
            int qtyDifference = newQty - oldQty;
            
            int availableStock = product.getStock() + oldQty;
            
            if (newQty > availableStock) {
                showMessage("Insufficient stock. Available: " + availableStock, true);
                return;
            }
            
            boolean success = CartItem.editCartItem(
                selected.getIdCustomer(), 
                selected.getIdProduct(), 
                newQty
            );
            
            if (success) {
                if (qtyDifference > 0) {
                    product.reduceStock(qtyDifference);
                } else if (qtyDifference < 0) {
                    product.increaseStock(Math.abs(qtyDifference));
                }
                
                loadCart();
                quantityField.clear();
                showMessage("✓ Cart updated successfully!", false);
            } else {
                showMessage("Failed to update cart", true);
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid quantity format", true);
        }
    }
    
    private void handleRemove() {
        CartItem selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select an item to remove", true);
            return;
        }
        
        // ONLY ALERT FOR DELETION
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Remove");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to remove this item from cart?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int quantity = selected.getCount();
                Product product = selected.getProduct();
                
                boolean success = CartItem.deleteCartItem(
                    selected.getIdCustomer(), 
                    selected.getIdProduct()
                );
                
                if (success) {
                    product.increaseStock(quantity);
                    loadCart();
                    showMessage("✓ Item removed and stock returned: +" + quantity, false);
                } else {
                    showMessage("Failed to remove item", true);
                }
            }
        });
    }
    
    private void handleCheckout() {
        ArrayList<CartItem> cartItems = CartItem.getCartItems(customer.getIdCustomer());
        
        if (cartItems.isEmpty()) {
            showMessage("Cannot checkout - cart is empty", true);
            return;
        }
        
        CheckoutPage checkoutPage = new CheckoutPage(stage, customer, cartItems);
        checkoutPage.show();
    }
    
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px; -fx-font-weight: bold;");
        }
    }
    
    public void show() {
        stage.show();
    }
}