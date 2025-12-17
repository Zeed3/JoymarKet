package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import model.Courier;
import model.Customer;
import model.User;

public class LoginView {
    
    private Stage stage;
    private Scene scene;
    private BorderPane mainContainer;
    private VBox formContainer;
    
    private Label titleLabel;
    private Label emailLabel;
    private Label passwordLabel;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Hyperlink registerLink;
    private Label messageLabel;
    
    public LoginView(Stage stage) {
        this.stage = stage;
        initialize();
        setLayout();
        setEventHandlers();
    }
    
    private void initialize() {
        mainContainer = new BorderPane();
        formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(50));
        formContainer.setMaxWidth(400);
        
        titleLabel = new Label("JoymarKet Login");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        
        emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        
        passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        
        loginButton = new Button("Login");
        loginButton.setPrefWidth(200);
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        
        registerLink = new Hyperlink("Don't have an account? Register here");
        
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
    }
    
    private void setLayout() {
        formContainer.getChildren().addAll(
            titleLabel,
            emailLabel,
            emailField,
            passwordLabel,
            passwordField,
            loginButton,
            registerLink,
            messageLabel
        );
        
        mainContainer.setCenter(formContainer);
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        scene = new Scene(mainContainer, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Login");
    }
    
    private void setEventHandlers() {
        loginButton.setOnAction(e -> handleLogin());
        
        registerLink.setOnAction(e -> {
            RegisterView registerPage = new RegisterView(stage);
            registerPage.show();
        });
        
        passwordField.setOnAction(e -> handleLogin());
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields", true);
            return;
        }
        
        if (!email.endsWith("@gmail.com")) {
            showMessage("Email must end with @gmail.com", true);
            return;
        }
        
        User user = User.getUserByEmail(email);
        
        if (user == null) {
            showMessage("Invalid email or password", true);
            return;
        }
        
        if (!user.getPassword().equals(password)) {
            showMessage("Invalid email or password", true);
            return;
        }
        
        String role = user.getRole();
        
        if ("Customer".equals(role)) {
            Customer customer = Customer.getCustomer(user.getIdUser());
            if (customer != null) {
                CustomerDashboard dashboard = new CustomerDashboard(stage, customer);
                dashboard.show();
            }
        } else if ("Admin".equals(role)) {
            Admin admin = Admin.getAdmin(user.getIdUser());
            if (admin != null) {
                AdminView dashboard = new AdminView(stage, admin);
                dashboard.show();
            }
        } else if ("Courier".equals(role)) {
            Courier courier = Courier.getCourier(user.getIdUser());
            if (courier != null) {
                CourierDashboard dashboard = new CourierDashboard(stage, courier);
                dashboard.show();
            }
        } else {
            showMessage("Unknown user role", true);
        }
    }
    
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }
    }
    
    public void show() {
        stage.show();
    }
}