package views;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CartItem;
import model.Customer;
import model.Product;

/**
 * View class for browsing and adding products to cart.
 * Displays available products and handles cart additions.
 */

public class ProductPage {
    
    private Stage stage;
    private Scene scene;
    private BorderPane mainContainer;
    private VBox contentContainer;
    private Customer customer;
    
    private Label titleLabel;
    private TableView<Product> productTable;
    private TextField quantityField;
    private Button addToCartButton;
    private Button refreshButton;
    private Button backButton;
    private Label messageLabel;
    
    public ProductPage(Stage stage, Customer customer) {
        this.stage = stage;
        this.customer = customer;
        initialize();
        setLayout();
        setEventHandlers();
        loadProducts();
    }
    
    private void initialize() {
        mainContainer = new BorderPane();
        contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(20));
        
        titleLabel = new Label("Available Products");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        productTable = new TableView<>();
        productTable.setPrefHeight(400);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productTable.setPlaceholder(new Label("No products available. Click Refresh to reload."));
        
       
        TableColumn<Product, String> idCol = new TableColumn<>("Product ID");
        idCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getIdProduct())
        );
        idCol.setPrefWidth(100);
        
       
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getName())
        );
        nameCol.setPrefWidth(250);
        
       
        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> {
            double price = data.getValue().getPrice();
            return new javafx.beans.property.SimpleStringProperty(
                "Rp " + String.format("%,.0f", price)
            );
        });
        priceCol.setPrefWidth(120);
        
        
        TableColumn<Product, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getStock())
            )
        );
        stockCol.setPrefWidth(100);
        stockCol.setStyle("-fx-alignment: CENTER;");
        
        
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory())
        );
        categoryCol.setPrefWidth(150);
        
        productTable.getColumns().add(idCol);
        productTable.getColumns().add(nameCol);
        productTable.getColumns().add(priceCol);
        productTable.getColumns().add(stockCol);
        productTable.getColumns().add(categoryCol);
        
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Label qtyLabel = new Label("Quantity:");
        quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");
        quantityField.setPrefWidth(100);
        
        addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        addToCartButton.setPrefWidth(120);
        
        refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setPrefWidth(100);
        
        backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setPrefWidth(100);
        
        actionBox.getChildren().addAll(qtyLabel, quantityField, addToCartButton, refreshButton, backButton);
        
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        
        contentContainer.getChildren().addAll(titleLabel, productTable, actionBox, messageLabel);
    }
    
    private void setLayout() {
        mainContainer.setCenter(contentContainer);
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        scene = new Scene(mainContainer, 900, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Products");
    }
    
    private void setEventHandlers() {
        addToCartButton.setOnAction(e -> handleAddToCart());
        
        refreshButton.setOnAction(e -> {
            loadProducts();
        });
        
        backButton.setOnAction(e -> {
            Customer refreshedCustomer = Customer.getCustomer(customer.getIdCustomer());
            CustomerDashboard dashboard = new CustomerDashboard(stage, refreshedCustomer);
            dashboard.show();
        });
    }
    
    private void loadProducts() {
        ArrayList<Product> products = Product.getAvailableProducts();
        ObservableList<Product> productList = FXCollections.observableArrayList(products);
        productTable.setItems(productList);
        productTable.refresh();
        
        if (products.isEmpty()) {
            showMessage("⚠️ No products available", true);
        } else {
            showMessage("✅ Loaded " + products.size() + " products successfully", false);
        }
    }
    
    private void handleAddToCart() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        
        if (selectedProduct == null) {
            showMessage("Please select a product", true);
            return;
        }
        
        String qtyText = quantityField.getText().trim();
        if (qtyText.isEmpty()) {
            showMessage("Please enter quantity", true);
            return;
        }
        
        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            showMessage("Quantity must be a number", true);
            return;
        }
        
        if (quantity <= 0) {
            showMessage("Quantity must be greater than 0", true);
            return;
        }
        
        if (quantity > selectedProduct.getStock()) {
            showMessage("Insufficient stock. Available: " + selectedProduct.getStock(), true);
            return;
        }
        
        
        boolean success = CartItem.createCartItem(
            customer.getIdCustomer(), 
            selectedProduct.getIdProduct(), 
            quantity
        );
        
        if (success) {
            
            boolean stockReduced = selectedProduct.reduceStock(quantity);
            
            if (stockReduced) {
                
                loadProducts();
                quantityField.clear();
                showMessage("✅ Added to cart and stock updated!", false);
            } else {
                showMessage("Added to cart but failed to update stock", true);
            }
        } else {
            showMessage("Failed to add to cart", true);
        }
    }
    
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        }
    }
    
    public void show() {
        stage.show();
    }
}