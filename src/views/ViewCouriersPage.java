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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import model.Admin;
import model.Courier;
import model.Delivery;

/**
 * View class for admin to view and manage couriers.
 * Allows viewing courier details and deleting couriers.
 */

public class ViewCouriersPage {
    
    private Stage stage;
    private Admin admin;
    private TableView<Courier> table;
    
    public ViewCouriersPage(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
        initialize();
    }
    
    private void initialize() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        
        Label title = new Label("All Couriers");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        table = new TableView<>();
        table.setPrefHeight(450);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No couriers available"));
        
        
        TableColumn<Courier, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getIdCourier())
        );
        idCol.setPrefWidth(100);
        
        
        TableColumn<Courier, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName())
        );
        nameCol.setPrefWidth(200);
        
        
        TableColumn<Courier, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone())
        );
        phoneCol.setPrefWidth(150);
        
        
        TableColumn<Courier, String> vehicleCol = new TableColumn<>("Vehicle");
        vehicleCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getVehicleType())
        );
        vehicleCol.setPrefWidth(100);
        
        
        TableColumn<Courier, String> plateCol = new TableColumn<>("Plate");
        plateCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getVehiclePlate())
        );
        plateCol.setPrefWidth(150);
        
        table.getColumns().add(idCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(phoneCol);
        table.getColumns().add(vehicleCol);
        table.getColumns().add(plateCol);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button deleteButton = new Button("Delete Courier");
        deleteButton.setPrefWidth(150);
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 14px;");
        deleteButton.setOnAction(e -> deleteCourier());
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setPrefWidth(150);
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        refreshButton.setOnAction(e -> loadCouriers());
        
        Button backButton = new Button("Back");
        backButton.setPrefWidth(150);
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setOnAction(e -> {
            AdminView dashboard = new AdminView(stage, admin);
            dashboard.show();
        });
        
        buttonBox.getChildren().addAll(deleteButton, refreshButton, backButton);
        
        container.getChildren().addAll(title, table, buttonBox);
        
        loadCouriers();
        
        BorderPane main = new BorderPane();
        main.setCenter(container);
        main.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(main, 900, 700);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - All Couriers");
    }
    
    private void loadCouriers() {
        ArrayList<Courier> couriers = Courier.getAllCouriers();
        table.setItems(FXCollections.observableArrayList(couriers));
        table.refresh();
    }
    private void deleteCourier() {
        Courier selected = table.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a courier to delete");
            alert.showAndWait();
            return;
        }
        
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Courier");
        confirm.setContentText("Are you sure you want to delete courier:\n" + 
                              selected.getFullName() + " (" + selected.getIdCourier() + ")?\n\n" +
                              "This action cannot be undone!");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                
                ArrayList<Delivery> deliveries = Delivery.getCourierDeliveries(selected.getIdCourier());
                
                if (!deliveries.isEmpty()) {
                    Alert warning = new Alert(Alert.AlertType.WARNING);
                    warning.setTitle("Cannot Delete");
                    warning.setHeaderText(null);
                    warning.setContentText("Cannot delete courier!\n" +
                                         "This courier has " + deliveries.size() + " delivery record(s).\n" +
                                         "Please reassign or complete deliveries first.");
                    warning.showAndWait();
                    return;
                }
                
                
                boolean success = Courier.deleteCourier(selected.getIdCourier());
                
                if (success) {
                    
                    loadCouriers(); 
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to delete courier. Please try again.");
                    alert.showAndWait();
                }
            }
        });
    }
    
    public void show() {
        stage.show();
    }
}