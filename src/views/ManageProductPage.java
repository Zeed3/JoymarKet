package views;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import model.Admin;
import model.Product;

/**
 * View class for admin product management interface.
 * Allows viewing, updating stock, and adding new products.
 */

public class ManageProductPage {
    
    private Stage stage;
    private Admin admin;
    private TableView<Product> table;
    private TextField stockField;
    
    public ManageProductPage(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
        initialize();
    }
    
    private void initialize() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label title = new Label("Manage Products");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        table = new TableView<>();
        table.setPrefHeight(450);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No products available"));
        
        
        TableColumn<Product, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getIdProduct())
        );
        idCol.setPrefWidth(80);
        
       
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getName())
        );
        nameCol.setPrefWidth(200);
        
        
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
        stockCol.setPrefWidth(80);
        stockCol.setStyle("-fx-alignment: CENTER;");
        
        
        TableColumn<Product, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory())
        );
        catCol.setPrefWidth(150);
        
        table.getColumns().add(idCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(stockCol);
        table.getColumns().add(catCol);
        
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);
        
        Label stockLabel = new Label("New Stock:");
        stockField = new TextField();
        stockField.setPromptText("Enter new stock");
        stockField.setPrefWidth(100);
        
        Button updateButton = new Button("Update Stock");
        updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        updateButton.setPrefWidth(120);
        updateButton.setOnAction(e -> updateStock());
        
        Button addProductButton = new Button("Add New Product");
        addProductButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        addProductButton.setPrefWidth(150);
        addProductButton.setOnAction(e -> showAddProductDialog());
        
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> {
            AdminView dashboard = new AdminView(stage, admin);
            dashboard.show();
        });
        
        actionBox.getChildren().addAll(stockLabel, stockField, updateButton, addProductButton, backButton);
        
        container.getChildren().addAll(title, table, actionBox);
        
        loadProducts();
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, 900, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Manage Products");
    }
    
    private void loadProducts() {
        ArrayList<Product> products = Product.getAllProducts();
        table.setItems(FXCollections.observableArrayList(products));
        table.refresh();
    }
    
    private void updateStock() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a product", Alert.AlertType.WARNING);
            return;
        }
        
        String stockText = stockField.getText().trim();
        if (stockText.isEmpty()) {
            showAlert("Please enter stock", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            int newStock = Integer.parseInt(stockText);
            if (newStock < 0) {
                showAlert("Stock cannot be negative", Alert.AlertType.WARNING);
                return;
            }
            
            boolean success = Product.editProductStock(selected.getIdProduct(), newStock);
            if (success) {
                loadProducts();
                stockField.clear();
                showAlert("Stock updated successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Failed to update stock", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid stock number", Alert.AlertType.WARNING);
        }
    }
    
    private void showAddProductDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter product details");
        
        ButtonType addButton = new ButtonType("Add");
        ButtonType cancelButton = new ButtonType("Cancel");
        dialog.getDialogPane().getButtonTypes().addAll(addButton, cancelButton);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");
        
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        
        TextField stockField = new TextField();
        stockField.setPromptText("Stock");
        
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Stock:"), 0, 2);
        grid.add(stockField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == addButton) {
                try {
                    String name = nameField.getText().trim();
                    String priceText = priceField.getText().trim();
                    String stockText = stockField.getText().trim();
                    String category = categoryField.getText().trim();
                    
                    if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || category.isEmpty()) {
                        showAlert("Please fill all fields", Alert.AlertType.WARNING);
                        return;
                    }
                    
                    double price = Double.parseDouble(priceText);
                    int stock = Integer.parseInt(stockText);
                    
                    if (price <= 0) {
                        showAlert("Price must be greater than 0", Alert.AlertType.WARNING);
                        return;
                    }
                    
                    if (stock < 0) {
                        showAlert("Stock cannot be negative", Alert.AlertType.WARNING);
                        return;
                    }
                    
                    String productId = generateProductId();
                    Product newProduct = new Product(productId, name, price, stock, category);
                    
                    if (Product.addProduct(newProduct)) {
                        showAlert("Product added successfully!", Alert.AlertType.INFORMATION);
                        loadProducts();
                    } else {
                        showAlert("Failed to add product", Alert.AlertType.ERROR);
                    }
                    
                } catch (NumberFormatException e) {
                    showAlert("Invalid price or stock format", Alert.AlertType.WARNING);
                }
            }
        });
    }
    
    private String generateProductId() {
        ArrayList<Product> products = Product.getAllProducts();
        int maxNum = 0;
        
        for (Product p : products) {
            String id = p.getIdProduct();
            if (id.startsWith("PR")) {
                try {
                    int num = Integer.parseInt(id.substring(2));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException e) {
                    
                }
            }
        }
        
        return String.format("PR%03d", maxNum + 1);
    }
    
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.INFORMATION ? "Success" : "Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show() {
        stage.show();
    }
}