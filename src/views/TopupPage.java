package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;

public class TopupPage {
    
	private Stage stage;
    private Scene scene;
    private BorderPane mainContainer;
    private VBox formContainer;
    private Customer customer;
    
    private Label titleLabel;
    private Label currentBalanceLabel;
    private Label amountLabel;
    private TextField amountField;
    private Button topUpButton;
    private Button backButton;
    
    public TopupPage(Stage stage, Customer customer) {
        this.stage = stage;
        this.customer = customer;
        initialize();
        setLayout();
        setEventHandlers();
    }
    
    private void initialize() {
        mainContainer = new BorderPane();
        formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(50));
        formContainer.setMaxWidth(400);
        
        titleLabel = new Label("Top Up Balance");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        currentBalanceLabel = new Label("Current Balance: Rp " + String.format("%,.0f", customer.getBalance()));
        currentBalanceLabel.setStyle("-fx-font-size: 16px;");
        
        amountLabel = new Label("Top Up Amount:");
        amountField = new TextField();
        amountField.setPromptText("Minimum Rp 10,000");
        
        topUpButton = new Button("Top Up");
        topUpButton.setPrefWidth(200);
        topUpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        
        backButton = new Button("Back");
        backButton.setPrefWidth(200);
        backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 14px;");
    }
    
    private void setLayout() {
        formContainer.getChildren().addAll(
            titleLabel,
            currentBalanceLabel,
            amountLabel,
            amountField,
            topUpButton,
            backButton
        );
        
        mainContainer.setCenter(formContainer);
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");
        
        scene = new Scene(mainContainer, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Top Up");
    }
    
    private void setEventHandlers() {
        topUpButton.setOnAction(e -> handleTopUp());
        
        backButton.setOnAction(e -> {
            Customer refreshedCustomer = Customer.getCustomer(customer.getIdCustomer());
            CustomerDashboard dashboard = new CustomerDashboard(stage, refreshedCustomer);
            dashboard.show();
        });
    }
    
    private void handleTopUp() {
        String amountText = amountField.getText().trim();
        
        if (amountText.isEmpty()) {
            showMessage("Please enter amount", true);
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountText);
            
            if (amount < 10000) {
                showMessage("Minimum top up is Rp 10,000", true);
                return;
            }
            
            boolean success = customer.topUpBalance(amount);
            
            if (success) {
                Customer refreshedCustomer = Customer.getCustomer(customer.getIdCustomer());
                currentBalanceLabel.setText("Current Balance: Rp " + String.format("%,.0f", refreshedCustomer.getBalance()));
                amountField.clear();
                showMessage("✓ Top up successful! New balance: Rp " + String.format("%,.0f", refreshedCustomer.getBalance()), false);
            } else {
                showMessage("Top up failed", true);
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid amount format", true);
        }
    }

    private void showMessage(String message, boolean isError) {
        
        Label messageLabel = new Label(message);
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