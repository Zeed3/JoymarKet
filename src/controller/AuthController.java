package controller;

import model.Customer;
import model.User;

/**
 * Controller class for managing user authentication and registration.
 * Includes login, registration, and validation methods.
 */

public class AuthController {
    
    public static User login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        
        if (!email.endsWith("@gmail.com")) {
            return null;
        }
        
        User user = User.getUserByEmail(email);
        
        if (user == null) {
            return null;
        }
        
        if (!user.getPassword().equals(password)) {
            return null;
        }
        
        return user;
    }
    
    public static Customer register(String fullName, String email, String password, 
                                   String phone, String address) {
        if (fullName == null || fullName.isEmpty() || 
            email == null || email.isEmpty() || 
            password == null || password.isEmpty() || 
            phone == null || phone.isEmpty() || 
            address == null || address.isEmpty()) {
            return null;
        }
        
        if (!email.endsWith("@gmail.com")) {
            return null;
        }
        
        if (User.emailExists(email)) {
            return null;
        }
        
        if (password.length() < 6) {
            return null;
        }
        
        if (phone.length() < 10 || phone.length() > 13) {
            return null;
        }
        
        return Customer.registerAccount(fullName, email, password, phone, address);
    }
    
    public static String getUserRole(String idUser) {
        User user = User.getUser(idUser);
        if (user != null) {
            return User.getUserByEmail(user.getEmail()).getRole();
        }
        return null;
    }
    
    public static boolean validateEmail(String email) {
        return email != null && email.endsWith("@gmail.com");
    }
    
    public static boolean validatePassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public static boolean validatePhone(String phone) {
        if (phone == null) return false;
        return phone.length() >= 10 && phone.length() <= 13;
    }
}
