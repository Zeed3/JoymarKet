package controller;

/**
 * Controller class for input validation.
 * Provides validation methods for various fields and error messages.
 */

public class ValidationController {
    
    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.endsWith("@gmail.com");
    }
    
    public static boolean validatePassword(String password) {
        if (password == null) {
            return false;
        }
        return password.length() >= 6;
    }
    
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        
        if (phone.length() < 10 || phone.length() > 13) {
            return false;
        }
        
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean validateQuantity(int quantity) {
        return quantity > 0;
    }
    
    public static boolean validateStock(int stock) {
        return stock >= 0;
    }
    
    public static boolean validateAmount(double amount) {
        return amount > 0;
    }
    
    public static boolean validateTopUpAmount(double amount) {
        return amount >= 10000;
    }
    
    public static boolean validateRequiredField(String field) {
        return field != null && !field.trim().isEmpty();
    }
    
    public static String getEmailErrorMessage() {
        return "Email must end with @gmail.com";
    }
    
    public static String getPasswordErrorMessage() {
        return "Password must be at least 6 characters";
    }
    
    public static String getPhoneErrorMessage() {
        return "Phone must be 10-13 digits";
    }
    
    public static String getQuantityErrorMessage() {
        return "Quantity must be greater than 0";
    }
    
    public static String getStockErrorMessage() {
        return "Stock cannot be negative";
    }
    
    public static String getTopUpErrorMessage() {
        return "Minimum top up is Rp 10,000";
    }
    
    public static String getRequiredFieldErrorMessage() {
        return "Please fill in all required fields";
    }
}
