package controller;

import model.Customer;

public class CustomerController {
    
    public static Customer getCustomer(String idCustomer) {
        return Customer.getCustomer(idCustomer);
    }
    
    public static boolean updateProfile(Customer customer, String fullName, String email, 
                                       String password, String phone, String address) {
        if (fullName == null || fullName.isEmpty() || 
            email == null || email.isEmpty() || 
            phone == null || phone.isEmpty() || 
            address == null || address.isEmpty()) {
            return false;
        }
        
        if (!password.isEmpty() && password.length() < 6) {
            return false;
        }
        
        return customer.editProfile(fullName, email, password, phone, address);
    }
    
    public static boolean topUpBalance(Customer customer, double amount) {
        if (amount < 10000) {
            return false;
        }
        
        return customer.topUpBalance(amount);
    }
    
    public static double getBalance(Customer customer) {
        return customer.getBalance();
    }
    
    public static boolean hasInsufficientBalance(Customer customer, double amount) {
        return !customer.hasSufficientBalance(amount);
    }
    
    public static Customer refreshCustomer(String idCustomer) {
        return Customer.getCustomer(idCustomer);
    }
}