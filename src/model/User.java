package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

/**
 * Base model class representing a user in the JoymarKet system.
 * Contains common user attributes and methods, extended by specific user types.
 */

public class User {
	
	protected String idUser;
    protected String fullName;
    protected String email;
    protected String password;
    protected String phone;
    protected String address;
    protected String role;
    
    
    public User(String idUser, String fullName, String email, String password, 
                String phone, String address, String role) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
    
    public User() {
		// TODO Auto-generated constructor stub
	}

	public String getIdUser() {
        return idUser;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public static User getUser(String idUser) {
        User user = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM userbase WHERE idUser = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User(
                    rs.getString("idUser"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    ""
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    public static User getUserByEmail(String email) {
        User user = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM userbase WHERE email = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String idUser = rs.getString("idUser");
                user = new User(
                    idUser,
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    ""
                );
                
                user.setRole(determineUserRole(idUser));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    private static String determineUserRole(String idUser) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT idCustomer FROM customer WHERE idCustomer = ?");
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return "Customer";
            }
            rs.close();
            ps.close();
            
            ps = conn.prepareStatement("SELECT idAdmin FROM admin WHERE idAdmin = ?");
            ps.setString(1, idUser);
            rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return "Admin";
            }
            rs.close();
            ps.close();
            
            ps = conn.prepareStatement("SELECT idCourier FROM courier WHERE idCourier = ?");
            ps.setString(1, idUser);
            rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return "Courier";
            }
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "Unknown";
    }
    
    public boolean editProfile(String fullName, String email, String password, 
                               String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
        this.phone = phone;
        this.address = address;
        
        return updateUser(this);
    }
    public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM userbase";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Create empty user object
                User user = new User();
                
                // Set fields manually using setters
                user.idUser = rs.getString("idUser");
                user.fullName = rs.getString("fullName");
                user.email = rs.getString("email");
                user.password = rs.getString("password");
                user.phone = rs.getString("phone");
                user.address = rs.getString("address");
                
                users.add(user);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    private static boolean updateUser(User user) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "UPDATE userbase SET fullName = ?, email = ?, password = ?, " +
                      "phone = ?, address = ? WHERE idUser = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getIdUser());
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean emailExists(String email) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT COUNT * FROM userbase WHERE email = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                ps.close();
                return count > 0;
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

}
