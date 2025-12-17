package views;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;
import model.User;

public class EditProfile {
    
    private Stage stage;
    private Customer customer;
    private TextField nameField, emailField, phoneField;
    private PasswordField passwordField;
    private TextArea addressArea;
    private Label messageLabel;
    
    public EditProfile(Stage stage, Customer customer) {
        this.stage = stage;
        this.customer = customer;
        initialize();
    }
    
    private void initialize() {
        
        BorderPane mainContainer = new BorderPane();
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30));
        container.setMaxWidth(450);
        
        Label title = new Label("Edit Profile");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        
        VBox formFields = new VBox(10);
        formFields.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-font-weight: bold;");
        nameField = new TextField(customer.getFullName());
        nameField.setPrefWidth(400);
        
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold;");
        emailField = new TextField(customer.getEmail());
        emailField.setPrefWidth(400);
        emailField.setStyle("-fx-opacity: 0.6;");
        
        Label passwordLabel = new Label("New Password:");
        passwordLabel.setStyle("-fx-font-weight: bold;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Insert New Password");
        passwordField.setPrefWidth(400);
        
        Label phoneLabel = new Label("Phone:");
        phoneLabel.setStyle("-fx-font-weight: bold;");
        phoneField = new TextField(customer.getPhone());
        phoneField.setPrefWidth(400);
        
        Label addressLabel = new Label("Address:");
        addressLabel.setStyle("-fx-font-weight: bold;");
        addressArea = new TextArea(customer.getAddress());
        addressArea.setPrefRowCount(3);
        addressArea.setPrefWidth(400);
        
        formFields.getChildren().addAll(
            nameLabel, nameField,
            emailLabel, emailField,
            passwordLabel, passwordField,
            phoneLabel, phoneField,
            addressLabel, addressArea
        );
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button saveButton = new Button("Save Changes");
        saveButton.setPrefWidth(150);
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        saveButton.setOnAction(e -> saveChanges());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(150);
        cancelButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        cancelButton.setOnAction(e -> goBackToDashboard());
        
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        
        
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        messageLabel.setAlignment(Pos.CENTER);
        
        container.getChildren().addAll(
            title,
            formFields,
            buttonBox,
            messageLabel
        );
        
        
        mainContainer.setCenter(container);
        
        Scene scene = new Scene(mainContainer, 900, 750);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Edit Profile");
    }
    
    private void saveChanges() {
        String newName = nameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newPassword = passwordField.getText();
        String newPhone = phoneField.getText().trim();
        String newAddress = addressArea.getText().trim();
        
        
        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
            showMessage("Please fill all required fields", true);
            return;
        }
        
        // Validation: Email format
        if (!newEmail.endsWith("@gmail.com")) {
            showMessage("Email must end with @gmail.com", true);
            return;
        }
        
        // Validation: Password length (if provided)
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            showMessage("Password must be at least 6 characters", true);
            return;
        }
        
        // Validation: Phone format
        if (!newPhone.matches("\\d+")) {
            showMessage("Phone must contain only numbers", true);
            return;
        }
        
        if (newPhone.length() < 10 || newPhone.length() > 13) {
            showMessage("Phone must be 10-13 digits", true);
            return;
        }
        
        
        
        ArrayList<User> allUsers = User.getAllUsers();
        
        for (User existingUser : allUsers) {
            
            if (existingUser.getIdUser().equals(customer.getIdCustomer())) {
                continue;
            }
            
           
            if (!newName.equalsIgnoreCase(customer.getFullName())) {
                if (existingUser.getFullName().equalsIgnoreCase(newName)) {
                    showMessage("Error: Full name '" + newName + "' is already registered.\nPlease use a different name.", true);
                    return;
                }
            }
            
            
            if (!newEmail.equalsIgnoreCase(customer.getEmail())) {
                if (existingUser.getEmail().equalsIgnoreCase(newEmail)) {
                    showMessage("Error: Email '" + newEmail + "' is already registered.\nPlease use a different email.", true);
                    return;
                }
            }
            
            
            if (!newPhone.equals(customer.getPhone())) {
                if (existingUser.getPhone().equals(newPhone)) {
                    showMessage("Error: Phone number '" + newPhone + "' is already registered.\nPlease use a different phone number.", true);
                    return;
                }
            }
            
            
            if (!newPassword.isEmpty()) {
                if (existingUser.getPassword().equals(newPassword)) {
                    showMessage("Error: This password is already used by another user.\nPlease use a different password.", true);
                    return;
                }
            }
        }
        
        
        String finalPassword = newPassword.isEmpty() ? customer.getPassword() : newPassword;
        
        // Update profile
        boolean success = customer.editProfile(newName, newEmail, finalPassword, newPhone, newAddress);
        
        if (success) {
            showMessage("Profile updated successfully!", false);
            
            
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(() -> goBackToDashboard());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        } else {
            showMessage("Failed to update profile. Please try again.", true);
            return;
        }
    }
    
    private void goBackToDashboard() {
        Customer refreshedCustomer = Customer.getCustomer(customer.getIdCustomer());
        CustomerDashboard dashboard = new CustomerDashboard(stage, refreshedCustomer);
        dashboard.show();
    }
    
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14px;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 14px;");
        }
    }
    
    public void show() {
        stage.show();
    }
}