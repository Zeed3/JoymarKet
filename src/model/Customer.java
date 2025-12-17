package model;

import java.sql.*;

import utils.ConnectDB;

public class Customer extends User {
    
    private double balance;
    
    public Customer() {
        super();
        this.role = "Customer";
    }
    
    public Customer(String idCustomer, String fullName, String email, String password,
                   String phone, String address, double balance) {
        super(idCustomer, fullName, email, password, phone, address, "Customer");
        this.balance = balance;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getIdCustomer() {
        return this.idUser;
    }
    
    public static Customer registerAccount(String fullName, String email, String password,
                                          String phone, String address) {
        String customerId = generateCustomerId();
        Customer customer = new Customer(customerId, fullName, email, password, 
                                        phone, address, 0.0);
        
        if (insertCustomer(customer)) {
            return customer;
        }
        return null;
    }
    
    public static Customer getCustomer(String idCustomer) {
        Customer customer = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT u.*, c.balance FROM userbase u " +
                      "JOIN customer c ON u.idUser = c.idCustomer " +
                      "WHERE u.idUser = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCustomer);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                customer = new Customer(
                    rs.getString("idUser"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getDouble("balance")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return customer;
    }
    
    public boolean topUpBalance(double amount) {
        this.balance += amount;
        return updateBalance(this.idUser, this.balance);
    }
    
    public boolean deductBalance(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return updateBalance(this.idUser, this.balance);
        }
        return false;
    }
    
    public boolean hasSufficientBalance(double amount) {
        return this.balance >= amount;
    }
    
    private static String generateCustomerId() {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT idUser FROM userbase WHERE idUser LIKE 'CU%' ORDER BY idUser DESC LIMIT 1";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String lastId = rs.getString("idUser");
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                rs.close();
                ps.close();
                return String.format("CU%03d", num);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "CU001";
    }
    
    private static boolean insertCustomer(Customer customer) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        try {
            String userQuery = "INSERT INTO userbase (idUser, fullName, email, password, phone, address) " +
                              "VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps1 = conn.prepareStatement(userQuery);
            ps1.setString(1, customer.getIdUser());
            ps1.setString(2, customer.getFullName());
            ps1.setString(3, customer.getEmail());
            ps1.setString(4, customer.getPassword());
            ps1.setString(5, customer.getPhone());
            ps1.setString(6, customer.getAddress());
            
            int rowsAffected1 = ps1.executeUpdate();
            ps1.close();
            
            if (rowsAffected1 > 0) {
                String customerQuery = "INSERT INTO customer (idCustomer, balance) VALUES (?, ?)";
                
                PreparedStatement ps2 = conn.prepareStatement(customerQuery);
                ps2.setString(1, customer.getIdUser());
                ps2.setDouble(2, customer.getBalance());
                
                int rowsAffected2 = ps2.executeUpdate();
                ps2.close();
                
                return rowsAffected2 > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private static boolean updateBalance(String idCustomer, double newBalance) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "UPDATE customer SET balance = ? WHERE idCustomer = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDouble(1, newBalance);
            ps.setString(2, idCustomer);
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}