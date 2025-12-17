package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;
import model.User;

public class RegisterView {
    
    private Stage stage;
    private Scene scene;
    private BorderPane mainContainer;
    private VBox formContainer;
    
    private Label titleLabel;
    private TextField fullNameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField phoneField;
    private TextArea addressArea;
    private Button registerButton;
    private Hyperlink loginLink;
    private Label messageLabel;
    
    public RegisterView(Stage stage) {
        this.stage = stage;
        initialize();
        setLayout();
        setEventHandlers();
    }
    
    private void initialize() {
        mainContainer = new BorderPane();
        formContainer = new VBox(10);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setMaxWidth(450);
        
        titleLabel = new Label("Register New Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        
        emailField = new TextField();
        emailField.setPromptText("Email (must end with @gmail.com)");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Password (min 6 characters)");
        
        phoneField = new TextField();
        phoneField.setPromptText("Phone (10-13 digits)");
        
        addressArea = new TextArea();
        addressArea.setPromptText("Address");
        addressArea.setPrefRowCount(3);
        
        registerButton = new Button("Register");
        registerButton.setPrefWidth(200);
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        
        loginLink = new Hyperlink("Already have an account? Login here");
        
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: red;");
    }
    
    private void setLayout() {
        formContainer.getChildren().addAll(
            titleLabel,
            new Label("Full Name:"),
            fullNameField,
            new Label("Email:"),
            emailField,
            new Label("Password:"),
            passwordField,
            new Label("Phone:"),
            phoneField,
            new Label("Address:"),
            addressArea,
            registerButton,
            loginLink,
            messageLabel
        );
        
        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        mainContainer.setCenter(scrollPane);
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        scene = new Scene(mainContainer, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Register");
    }
    
    private void setEventHandlers() {
        registerButton.setOnAction(e -> handleRegister());
        
        loginLink.setOnAction(e -> {
            LoginView loginPage = new LoginView(stage);
            loginPage.show();
        });
    }
    
    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || 
            phone.isEmpty() || address.isEmpty()) {
            showMessage("Please fill in all fields");
            return;
        }
        
        if (!email.endsWith("@gmail.com")) {
            showMessage("Email must end with @gmail.com");
            return;
        }
        
        if (User.emailExists(email)) {
            showMessage("Email already registered");
            return;
        }
        
        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters");
            return;
        }
        
        if (phone.length() < 10 || phone.length() > 13) {
            showMessage("Phone must be 10-13 digits");
            return;
        }
        
        Customer newCustomer = Customer.registerAccount(fullName, email, password, phone, address);
        
        if (newCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Registration successful! Please login.");
            alert.showAndWait();
            
            LoginView loginPage = new LoginView(stage);
            loginPage.show();
        } else {
            showMessage("Registration failed");
        }
    }
    
    private void showMessage(String message) {
        messageLabel.setText(message);
    }
    
    public void show() {
        stage.show();
    }
}