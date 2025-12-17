package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

public class Courier extends User{

	private String vehicleType;
	private String vehiclePlate;
	
	public Courier() {
        super();
        this.role = "Courier";
    }
    
    public Courier(String idCourier, String fullName, String email, String password,
                  String phone, String address, String vehicleType, String vehiclePlate) {
        super(idCourier, fullName, email, password, phone, address, "Courier");
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public String getVehiclePlate() {
        return vehiclePlate;
    }
    
    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }
    
    public String getIdCourier() {
        return this.idUser;
    }
    
    public static Courier getCourier(String idCourier) {
        Courier courier = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT u.*, c.vehicleType, c.vehiclePlate FROM userbase u " +
                      "JOIN courier c ON u.idUser = c.idCourier " +
                      "WHERE u.idUser = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCourier);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                courier = new Courier(
                    rs.getString("idUser"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("vehicleType"),
                    rs.getString("vehiclePlate")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return courier;
    }
    
    public static ArrayList<Courier> getAllCouriers() {
        ArrayList<Courier> couriers = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT u.*, c.vehicleType, c.vehiclePlate FROM userbase u " +
                      "JOIN courier c ON u.idUser = c.idCourier " +
                      "ORDER BY u.fullName";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Courier courier = new Courier(
                    rs.getString("idUser"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("vehicleType"),
                    rs.getString("vehiclePlate")
                );
                couriers.add(courier);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return couriers;
    }
    
    public static boolean deleteCourier(String idCourier) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        try {
            
            conn.setAutoCommit(false);
            
            
            String deleteCourierQuery = "DELETE FROM courier WHERE idCourier = ?";
            PreparedStatement ps1 = conn.prepareStatement(deleteCourierQuery);
            ps1.setString(1, idCourier);
            ps1.executeUpdate();
            ps1.close();
            
            
            String deleteUserQuery = "DELETE FROM userbase WHERE idUser = ?";
            PreparedStatement ps2 = conn.prepareStatement(deleteUserQuery);
            ps2.setString(1, idCourier);
            int rowsAffected = ps2.executeUpdate();
            ps2.close();
            
            
            conn.commit();
            conn.setAutoCommit(true);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

}
