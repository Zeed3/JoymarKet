package model;

import java.sql.*;

import utils.ConnectDB;

public class Admin extends User{

	private String emergencyContact;
	
	public Admin() {
        super();
        this.role = "Admin";
    }
    
    public Admin(String idAdmin, String fullName, String email, String password,
                String phone, String address, String emergencyContact) {
        super(idAdmin, fullName, email, password, phone, address, "Admin");
        this.emergencyContact = emergencyContact;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getIdAdmin() {
        return this.idUser;
    }
    
    public static Admin getAdmin(String idAdmin) {
        Admin admin = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT u.*, a.emergencyContact FROM userbase u " +
                      "JOIN admin a ON u.idUser = a.idAdmin " +
                      "WHERE u.idUser = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idAdmin);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                admin = new Admin(
                    rs.getString("idUser"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("emergencyContact")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return admin;
    }

}
