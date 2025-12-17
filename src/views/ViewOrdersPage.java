package views;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import javafx.collections.FXCollections;
import model.Courier;
import model.Delivery;
import model.OrderHeader;

public class ViewOrdersPage {
    
    private Stage stage;
    private Admin admin;
    private TableView<OrderHeader> table;
    private ComboBox<String> courierCombo;
    
    public ViewOrdersPage(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
        initialize();
    }
    
    private void initialize() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label title = new Label("All Orders");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        table = new TableView<>();
        table.setPrefHeight(450);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No orders yet"));
        
        
        TableColumn<OrderHeader, String> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getIdOrder())
        );
        idCol.setPrefWidth(150);
        
        
        TableColumn<OrderHeader, String> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getIdCustomer())
        );
        customerCol.setPrefWidth(150);
        
        
        TableColumn<OrderHeader, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> {
            Timestamp orderDate = data.getValue().getOrderAt();
            if (orderDate != null) {
                LocalDateTime ldt = orderDate.toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return new javafx.beans.property.SimpleStringProperty(ldt.format(formatter));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        dateCol.setPrefWidth(150);
        
        
        TableColumn<OrderHeader, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus())
        );
        statusCol.setPrefWidth(150);
        statusCol.setStyle("-fx-alignment: CENTER;");
        
        table.getColumns().add(idCol);
        table.getColumns().add(customerCol);
        table.getColumns().add(dateCol);
        table.getColumns().add(statusCol);
        
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Label courierLabel = new Label("Assign Courier:");
        courierCombo = new ComboBox<>();
        courierCombo.setPromptText("Select courier");
        courierCombo.setPrefWidth(200);
        
        Button assignButton = new Button("Assign");
        assignButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        assignButton.setPrefWidth(100);
        assignButton.setOnAction(e -> assignCourier());
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setPrefWidth(100);
        refreshButton.setOnAction(e -> {
            loadOrders();
            loadCouriers();
        });
        
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> {
            AdminView dashboard = new AdminView(stage, admin);
            dashboard.show();
        });
        
        actionBox.getChildren().addAll(courierLabel, courierCombo, assignButton, refreshButton, backButton);
        
        container.getChildren().addAll(title, table, actionBox);
        
        loadOrders();
        loadCouriers();
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, 900, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - All Orders");
    }
    
    private void loadOrders() {
        ArrayList<OrderHeader> orders = OrderHeader.getAllOrderHeaders();
        table.setItems(FXCollections.observableArrayList(orders));
        table.refresh();
    }
    
    private void loadCouriers() {
        courierCombo.getItems().clear();
        ArrayList<Courier> couriers = Courier.getAllCouriers();
        for (Courier c : couriers) {
            courierCombo.getItems().add(c.getIdCourier() + " - " + c.getFullName());
        }
    }
    
    private void assignCourier() {
        OrderHeader selected = table.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select an order to assign");
            alert.showAndWait();
            return;
        }
        
        String courierSelection = courierCombo.getValue();
        if (courierSelection == null || courierSelection.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a courier");
            alert.showAndWait();
            return;
        }
        
        
        String courierId = courierSelection.split(" - ")[0].trim();
        
        System.out.println("=== ASSIGNING COURIER ===");
        System.out.println("Order ID: " + selected.getIdOrder());
        System.out.println("Courier ID: " + courierId);
        System.out.println("Current Order Status: " + selected.getStatus());
        
        
        Delivery existingDelivery = Delivery.getDeliveryByOrder(selected.getIdOrder());
        if (existingDelivery != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Already Assigned");
            alert.setHeaderText(null);
            alert.setContentText("This order already has a courier assigned:\n" +
                               "Courier ID: " + existingDelivery.getIdCourier() + "\n\n" +
                               "Cannot reassign courier.");
            alert.showAndWait();
            return;
        }
        
        
        boolean deliveryCreated = Delivery.createDelivery(selected.getIdOrder(), courierId);
        
        System.out.println("Delivery created: " + (deliveryCreated ? "SUCCESS ✓" : "FAILED ✗"));
        
        if (deliveryCreated) {
            
            boolean statusUpdated = OrderHeader.updateOrderStatus(selected.getIdOrder(), "Processed");
            
            System.out.println("Order status updated: " + (statusUpdated ? "SUCCESS ✓" : "FAILED ✗"));
            
            
            Delivery verifyDelivery = Delivery.getDeliveryByOrder(selected.getIdOrder());
            if (verifyDelivery != null) {
                System.out.println("✓ VERIFICATION: Delivery exists in database");
                System.out.println("  - Order: " + verifyDelivery.getIdOrder());
                System.out.println("  - Courier: " + verifyDelivery.getIdCourier());
                System.out.println("  - Status: " + verifyDelivery.getStatus());
            } else {
                System.err.println("✗ WARNING: Delivery not found after creation!");
            }
            
            
            loadOrders();
            courierCombo.setValue(null);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Courier Assigned Successfully");
            alert.setContentText("Order ID: " + selected.getIdOrder() + "\n" +
                               "Courier: " + courierSelection + "\n" +
                               "Status: Processed\n\n");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to assign courier.\n" +
                               "Please check console for details.");
            alert.showAndWait();
        }
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