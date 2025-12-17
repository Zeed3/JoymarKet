package views;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;
import javafx.collections.FXCollections;
import model.OrderDetail;
import model.OrderHeader;
import model.Product;
import model.Promo;

public class OrderHistory {
    
    private Stage stage;
    private Customer customer;
    private TableView<OrderHeader> orderTable;
    
    public OrderHistory(Stage stage, Customer customer) {
        this.stage = stage;
        this.customer = customer;
        initialize();
    }
    
    private void initialize() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label title = new Label("Order History");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        orderTable = new TableView<>();
        orderTable.setPrefHeight(450);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        orderTable.setPlaceholder(new Label("No orders yet"));
        
        
        TableColumn<OrderHeader, String> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getIdOrder())
        );
        idCol.setPrefWidth(150);
        
        
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
        statusCol.setCellFactory(column -> {
            return new TableCell<OrderHeader, String>() {
                @Override
                protected void updateItem(String status, boolean empty) {
                    super.updateItem(status, empty);
                    if (empty || status == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(status);
                        // Color code based on status
                        if ("Delivered".equals(status)) {
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-alignment: CENTER;");
                        } else if ("Pending".equals(status)) {
                            setStyle("-fx-text-fill: orange; -fx-font-weight: bold; -fx-alignment: CENTER;");
                        } else if ("Processed".equals(status) || "In Progress".equals(status)) {
                            setStyle("-fx-text-fill: blue; -fx-font-weight: bold; -fx-alignment: CENTER;");
                        } else {
                            setStyle("-fx-alignment: CENTER;");
                        }
                    }
                }
            };
        });
        
        
        TableColumn<OrderHeader, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data -> {
            OrderHeader order = data.getValue();
            
            
            ArrayList<OrderDetail> details = OrderDetail.getOrderDetails(order.getIdOrder());
            double subtotal = 0;
            for (OrderDetail detail : details) {
                subtotal += detail.getSubtotal();
            }
            
            // Apply promo discount if exists
            double finalTotal = subtotal;
            if (order.getIdPromo() != null && !order.getIdPromo().isEmpty()) {
                Promo promo = Promo.getPromo(order.getIdPromo());
                if (promo != null) {
                    finalTotal = promo.calculateFinalAmount(subtotal);
                }
            }
            
            return new javafx.beans.property.SimpleStringProperty(
                "Rp " + String.format("%,.0f", finalTotal)
            );
        });
        totalCol.setPrefWidth(150);
        
        orderTable.getColumns().add(idCol);
        orderTable.getColumns().add(dateCol);
        orderTable.getColumns().add(statusCol);
        orderTable.getColumns().add(totalCol);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setPrefWidth(150);
        viewDetailsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        viewDetailsButton.setOnAction(e -> viewOrderDetails());
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setPrefWidth(150);
        refreshButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setOnAction(e -> loadOrders());
        
        Button backButton = new Button("Back");
        backButton.setPrefWidth(150);
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setOnAction(e -> {
            Customer refreshedCustomer = Customer.getCustomer(customer.getIdCustomer());
            CustomerDashboard dashboard = new CustomerDashboard(stage, refreshedCustomer);
            dashboard.show();
        });
        
        buttonBox.getChildren().addAll(viewDetailsButton, refreshButton, backButton);
        
        container.getChildren().addAll(title, orderTable, buttonBox);
        
        loadOrders();
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, 900, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Order History");
    }
    
    private void loadOrders() {
        ArrayList<OrderHeader> orders = OrderHeader.getCustomerOrderHeaders(customer.getIdCustomer());
        orderTable.setItems(FXCollections.observableArrayList(orders));
        orderTable.refresh();
    }
    
    private void viewOrderDetails() {
        OrderHeader selected = orderTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select an order to view details");
            alert.showAndWait();
            return;
        }
        
        ArrayList<OrderDetail> details = OrderDetail.getOrderDetails(selected.getIdOrder());
        
        if (details.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Details");
            alert.setHeaderText("Order: " + selected.getIdOrder());
            alert.setContentText("No items found in this order");
            alert.showAndWait();
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(selected.getIdOrder()).append("\n");
        
        // Format date without time
        Timestamp orderDate = selected.getOrderAt();
        if (orderDate != null) {
            LocalDateTime ldt = orderDate.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            sb.append("Date: ").append(ldt.format(formatter)).append("\n");
        }
        
        sb.append("Status: ").append(selected.getStatus()).append("\n\n");
        sb.append("Items\t\t Qty\t \tPrice\n");
        sb.append("----------------------------------------\n");
        
        double subtotal = 0;
        for (OrderDetail detail : details) {
            Product product = detail.getProduct();
            if (product != null) {
                sb.append(String.format("%-20s x%-3d\t\t Rp %,.0f\n", 
                    product.getName(), 
                    detail.getQty(),
                    detail.getSubtotal()));
                subtotal += detail.getSubtotal();
            }
        }
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("Subtotal: Rp %,.0f\n", subtotal));
        
        // Show promo discount if applied
        if (selected.getIdPromo() != null && !selected.getIdPromo().isEmpty()) {
            Promo promo = Promo.getPromo(selected.getIdPromo());
            if (promo != null) {
                double discount = promo.calculateDiscount(subtotal);
				double finalTotal = promo.calculateFinalAmount(subtotal);
                sb.append(String.format("Promo: %s (-%,.0f%%)\n", promo.getCode(), promo.getDicountPercentage()));
                sb.append(String.format("Discount: -Rp %,.0f\n", discount));
                sb.append("----------------------------------------\n");
                
            } else {
                sb.append("----------------------------------------\n");
            }
        } 
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText(null);
        alert.setContentText(sb.toString());
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinWidth(500);
        
        alert.showAndWait();
    }
    
    public void show() {
        stage.show();
    }
}