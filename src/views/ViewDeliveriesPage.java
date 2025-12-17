package views;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import model.Admin;
import model.Courier;
import model.Delivery;
import model.OrderHeader;
import model.User;

/**
 * View class for viewing delivery information.
 * Supports both admin (all deliveries) and courier (own deliveries) views.
 */

public class ViewDeliveriesPage {
    
    private Stage stage;
    private User user;
    private TableView<DeliveryDisplayData> table;
    private boolean isAdmin;
    
    
    public ViewDeliveriesPage(Stage stage, Courier courier) {
        this.stage = stage;
        this.user = courier;
        this.isAdmin = false;
        initialize();
    }
    
    
    public ViewDeliveriesPage(Stage stage, Admin admin) {
        this.stage = stage;
        this.user = admin;
        this.isAdmin = true;
        initialize();
    }
    
    private void initialize() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label title = new Label(isAdmin ? "All Deliveries" : "My Deliveries");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        table = new TableView<>();
        table.setPrefHeight(450);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label(isAdmin ? "No deliveries yet" : "No deliveries assigned yet"));
        
        setupTableColumns();
        
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        
        if (!isAdmin) {
            Button deliveredButton = new Button("Mark as Delivered");
            deliveredButton.setPrefWidth(150);
            deliveredButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
            deliveredButton.setOnAction(e -> markAsDelivered());
            actionBox.getChildren().add(deliveredButton);
        }
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setPrefWidth(100);
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setOnAction(e -> loadDeliveries());
        
        Button backButton = new Button("Back");
        backButton.setPrefWidth(100);
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setOnAction(e -> goBack());
        
        actionBox.getChildren().addAll(refreshButton, backButton);
        
        container.getChildren().addAll(title, table, actionBox);
        
        loadDeliveries();
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, isAdmin ? 1100 : 900, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - " + (isAdmin ? "All Deliveries" : "My Deliveries"));
    }
    
    private void setupTableColumns() {
        TableColumn<DeliveryDisplayData, String> orderCol = new TableColumn<>("Order ID");
        orderCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().idOrder)
        );
        orderCol.setPrefWidth(isAdmin ? 100 : 150);
        table.getColumns().add(orderCol);
        
        if (isAdmin) {
            TableColumn<DeliveryDisplayData, String> courierIdCol = new TableColumn<>("Courier ID");
            courierIdCol.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().idCourier)
            );
            courierIdCol.setPrefWidth(100);
            table.getColumns().add(courierIdCol);
            
            TableColumn<DeliveryDisplayData, String> courierNameCol = new TableColumn<>("Courier Name");
            courierNameCol.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().courierName)
            );
            courierNameCol.setPrefWidth(150);
            table.getColumns().add(courierNameCol);
        }
        
        TableColumn<DeliveryDisplayData, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().status)
        );
        statusCol.setPrefWidth(120);
        statusCol.setStyle("-fx-alignment: CENTER;");
        table.getColumns().add(statusCol);
        
        TableColumn<DeliveryDisplayData, String> dateCol = new TableColumn<>("Order Date");
        dateCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().orderDate)
        );
        dateCol.setPrefWidth(120);
        table.getColumns().add(dateCol);
        
        TableColumn<DeliveryDisplayData, String> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().idCustomer)
        );
        customerCol.setPrefWidth(120);
        table.getColumns().add(customerCol);
        
        if (isAdmin) {
            TableColumn<DeliveryDisplayData, String> totalCol = new TableColumn<>("Total Amount");
            totalCol.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().totalAmount)
            );
            totalCol.setPrefWidth(120);
            table.getColumns().add(totalCol);
        }
    }
    
    private void loadDeliveries() {
        System.out.println("\n========================================");
        System.out.println("=== LOADING DELIVERIES ===");
        System.out.println("========================================");
        System.out.println("Role: " + (isAdmin ? "ADMIN" : "COURIER"));
        System.out.println("User: " + user.getFullName());
        System.out.println("User ID: " + user.getIdUser());
        
        ArrayList<Delivery> deliveries;
        
        if (isAdmin) {
            deliveries = Delivery.getAllDeliveries();
            System.out.println("Query: SELECT * FROM delivery (ALL)");
        } else {
            Courier courier = (Courier) user;
            System.out.println("Courier ID being queried: " + courier.getIdCourier());
            deliveries = Delivery.getCourierDeliveries(courier.getIdCourier());
            System.out.println("Query: SELECT * FROM delivery WHERE idCourier = '" + courier.getIdCourier() + "'");
        }
        
        System.out.println("Deliveries found in database: " + deliveries.size());
        
        if (deliveries.isEmpty()) {
            System.out.println("⚠️ WARNING: No deliveries found!");
            if (!isAdmin) {
                System.out.println("⚠️ This courier has NO assigned deliveries");
                System.out.println("⚠️ Please check:");
                System.out.println("   1. Admin has assigned an order to this courier");
                System.out.println("   2. Courier ID matches exactly (case-sensitive)");
                System.out.println("   3. Database has delivery records");
            }
        }
        
        ArrayList<DeliveryDisplayData> displayData = new ArrayList<>();
        
        for (int i = 0; i < deliveries.size(); i++) {
            Delivery delivery = deliveries.get(i);
            System.out.println("\n--- Processing Delivery #" + (i + 1) + " ---");
            System.out.println("  Order ID: " + delivery.getIdOrder());
            System.out.println("  Courier ID: " + delivery.getIdCourier());
            System.out.println("  Delivery Status: " + delivery.getStatus());
            
            OrderHeader order = OrderHeader.getOrderHeader(delivery.getIdOrder());
            
            if (order == null) {
                System.err.println("  ✗ ERROR: OrderHeader not found for " + delivery.getIdOrder());
                continue;
            }
            
            System.out.println("  Customer ID: " + order.getIdCustomer());
            System.out.println("  Order Status: " + order.getStatus());
            
            String orderDate = "";
            if (order.getOrderAt() != null) {
                LocalDateTime ldt = order.getOrderAt().toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                orderDate = ldt.format(formatter);
                System.out.println("  Order Date: " + orderDate);
            }
            
            String courierName = "";
            if (isAdmin) {
                Courier courier = Courier.getCourier(delivery.getIdCourier());
                if (courier != null) {
                    courierName = courier.getFullName();
                    System.out.println("  Courier Name: " + courierName);
                }
            }
            
            String totalAmount = "";
            if (isAdmin) {
                totalAmount = "Rp " + String.format("%,.0f", order.getTotalAmount());
                System.out.println("  Total: " + totalAmount);
            }
            
            DeliveryDisplayData item = new DeliveryDisplayData(
                order.getIdOrder(),
                delivery.getIdCourier(),
                courierName,
                order.getIdCustomer(),
                delivery.getStatus(),
                orderDate,
                totalAmount
            );
            
            displayData.add(item);
            System.out.println("  ✓ Added to table");
        }
        
        System.out.println("\n========================================");
        System.out.println("SUMMARY:");
        System.out.println("  Database records: " + deliveries.size());
        System.out.println("  Table rows: " + displayData.size());
        System.out.println("========================================\n");
        
        table.setItems(FXCollections.observableArrayList(displayData));
        table.refresh();
    }
    
    private void markAsDelivered() {
        if (isAdmin) {
            return;
        }
        
        System.out.println("\n=== MARK AS DELIVERED ===");
        
        DeliveryDisplayData selected = table.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("Please select a delivery to mark as delivered", Alert.AlertType.WARNING);
            return;
        }
        
        System.out.println("Selected Order: " + selected.idOrder);
        System.out.println("Current Status: " + selected.status);
        
        if ("Delivered".equals(selected.status)) {
            showAlert("This delivery is already marked as delivered", Alert.AlertType.INFORMATION);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delivery");
        confirm.setHeaderText("Mark as Delivered");
        confirm.setContentText("Are you sure you want to mark this order as delivered?\n\n" +
                              "Order ID: " + selected.idOrder + "\n" +
                              "Customer: " + selected.idCustomer);
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Courier courier = (Courier) user;
                
                System.out.println("Updating delivery status...");
                System.out.println("  Order: " + selected.idOrder);
                System.out.println("  Courier: " + courier.getIdCourier());
                System.out.println("  New Status: Delivered");
                
                boolean deliveryUpdated = Delivery.updateDeliveryStatus(
                    selected.idOrder,
                    courier.getIdCourier(),
                    "Delivered"
                );
                
                System.out.println("Delivery update result: " + (deliveryUpdated ? "SUCCESS ✓" : "FAILED ✗"));
                
                if (deliveryUpdated) {
                    boolean orderUpdated = OrderHeader.updateOrderStatus(
                        selected.idOrder, 
                        "Delivered"
                    );
                    
                    System.out.println("Order update result: " + (orderUpdated ? "SUCCESS ✓" : "FAILED ✗"));
                    
                    if (orderUpdated) {
                        showAlert("✓ Order marked as delivered successfully!", Alert.AlertType.INFORMATION);
                        loadDeliveries();
                    } else {
                        showAlert("Delivery updated but failed to update order status", Alert.AlertType.WARNING);
                    }
                } else {
                    showAlert("Failed to update delivery status. Check console for details.", Alert.AlertType.ERROR);
                }
            }
        });
    }
    
    private void goBack() {
        if (isAdmin) {
            Admin admin = (Admin) user;
            AdminView dashboard = new AdminView(stage, admin);
            dashboard.show();
        } else {
            Courier courier = (Courier) user;
            CourierDashboard dashboard = new CourierDashboard(stage, courier);
            dashboard.show();
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
    
    private static class DeliveryDisplayData {
        String idOrder;
        String idCourier;
        String courierName;
        String idCustomer;
        String status;
        String orderDate;
        String totalAmount;
        
        DeliveryDisplayData(String idOrder, String idCourier, String courierName, 
                          String idCustomer, String status, String orderDate, String totalAmount) {
            this.idOrder = idOrder;
            this.idCourier = idCourier;
            this.courierName = courierName;
            this.idCustomer = idCustomer;
            this.status = status;
            this.orderDate = orderDate;
            this.totalAmount = totalAmount;
        }
    }
}