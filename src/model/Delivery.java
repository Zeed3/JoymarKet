package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

/**
 * Model class representing delivery information for orders.
 * Includes order ID, courier ID, status, and delivery timestamp.
 */

public class Delivery {
    
    private String idOrder;
    private String idCourier;
    private String status;
    private Timestamp deliveredAt;
    
    public Delivery(String idOrder, String idCourier, String status, Timestamp deliveredAt) {
        this.idOrder = idOrder;
        this.idCourier = idCourier;
        this.status = status;
        this.deliveredAt = deliveredAt;
    }
    
    public String getIdOrder() {
        return idOrder;
    }
    
    public String getIdCourier() {
        return idCourier;
    }
    
    public String getStatus() {
        return status;
    }
    
    public Timestamp getDeliveredAt() {
        return deliveredAt;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setDeliveredAt(Timestamp deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
    
    // Create delivery when admin assigns courier
    public static boolean createDelivery(String idOrder, String idCourier) {
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "INSERT INTO delivery (idOrder, idCourier, status) VALUES (?, ?, 'Processed')";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idOrder);
            ps.setString(2, idCourier);
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            System.out.println("✓ Delivery INSERT: idOrder=" + idOrder + ", idCourier=" + idCourier + ", status=Processed");
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Delivery INSERT FAILED: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    public static ArrayList<Delivery> getCourierDeliveries(String idCourier) {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM delivery WHERE idCourier = ? ORDER BY idOrder DESC";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCourier);
            
            System.out.println(">>> EXECUTING SQL: " + query);
            System.out.println(">>> PARAMETER: idCourier = '" + idCourier + "'");
            
            ResultSet rs = ps.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                
                
                Timestamp deliveredAt = null;
                try {
                    deliveredAt = rs.getTimestamp("deliveredAt");
                } catch (SQLException e) {
                    
                    deliveredAt = null;
                }
                
                Delivery delivery = new Delivery(
                    rs.getString("idOrder"),
                    rs.getString("idCourier"),
                    rs.getString("status"),
                    deliveredAt
                );
                deliveries.add(delivery);
                
                System.out.println(">>> ROW " + count + ": Order=" + delivery.getIdOrder() + 
                                 ", Courier=" + delivery.getIdCourier() + 
                                 ", Status=" + delivery.getStatus());
            }
            
            System.out.println(">>> TOTAL ROWS: " + count);
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println(">>> SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        return deliveries;
    }
    
    
    public static Delivery getDeliveryByOrder(String idOrder) {
        Delivery delivery = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM delivery WHERE idOrder = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idOrder);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Timestamp deliveredAt = null;
                try {
                    deliveredAt = rs.getTimestamp("deliveredAt");
                } catch (SQLException e) {
                    deliveredAt = null;
                }
                
                delivery = new Delivery(
                    rs.getString("idOrder"),
                    rs.getString("idCourier"),
                    rs.getString("status"),
                    deliveredAt
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return delivery;
    }
    
    
    public static Delivery getDelivery(String idOrder, String idCourier) {
        Delivery delivery = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM delivery WHERE idOrder = ? AND idCourier = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idOrder);
            ps.setString(2, idCourier);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Timestamp deliveredAt = null;
                try {
                    deliveredAt = rs.getTimestamp("deliveredAt");
                } catch (SQLException e) {
                    deliveredAt = null;
                }
                
                delivery = new Delivery(
                    rs.getString("idOrder"),
                    rs.getString("idCourier"),
                    rs.getString("status"),
                    deliveredAt
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return delivery;
    }
    
    
    public static ArrayList<Delivery> getAllDeliveries() {
        ArrayList<Delivery> deliveries = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM delivery ORDER BY idOrder DESC";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Timestamp deliveredAt = null;
                try {
                    deliveredAt = rs.getTimestamp("deliveredAt");
                } catch (SQLException e) {
                    deliveredAt = null;
                }
                
                Delivery delivery = new Delivery(
                    rs.getString("idOrder"),
                    rs.getString("idCourier"),
                    rs.getString("status"),
                    deliveredAt
                );
                deliveries.add(delivery);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return deliveries;
    }
    
    
    public static boolean updateDeliveryStatus(String idOrder, String idCourier, String newStatus) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        
        boolean hasDeliveredAtColumn = false;
        try {
            ResultSet columns = conn.getMetaData().getColumns(null, null, "delivery", "deliveredAt");
            hasDeliveredAtColumn = columns.next();
            columns.close();
        } catch (SQLException e) {
            hasDeliveredAtColumn = false;
        }
        
        String query;
        if (hasDeliveredAtColumn) {
            query = "UPDATE delivery SET status = ?, deliveredAt = ? WHERE idOrder = ? AND idCourier = ?";
        } else {
            query = "UPDATE delivery SET status = ? WHERE idOrder = ? AND idCourier = ?";
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, newStatus);
            
            if (hasDeliveredAtColumn) {
                if ("Delivered".equals(newStatus)) {
                    ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                } else {
                    ps.setNull(2, Types.TIMESTAMP);
                }
                ps.setString(3, idOrder);
                ps.setString(4, idCourier);
            } else {
                ps.setString(2, idOrder);
                ps.setString(3, idCourier);
            }
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            System.out.println("✓ Delivery status updated to: " + newStatus);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("✗ Failed to update delivery: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    public static boolean editDeliveryStatus(String idOrder, String idCourier, String newStatus) {
        return updateDeliveryStatus(idOrder, idCourier, newStatus);
    }
}