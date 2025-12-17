package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

/**
 * Model class representing the header of an order.
 * Includes order ID, customer, promo, status, timestamp, and order details.
 */

public class OrderHeader {

	private String idOrder;
    private String idCustomer;
    private String idPromo;
    private String status;
    private Timestamp orderAt;
    private Customer customer;
    private Promo promo;
    private ArrayList<OrderDetail> orderDetails;
    private double totalAmount;
    
    public OrderHeader() {
        this.orderDetails = new ArrayList<>();
    }
    
    public OrderHeader(String idOrder, String idCustomer, String idPromo, 
                      String status, Timestamp orderAt) {
        this.idOrder = idOrder;
        this.idCustomer = idCustomer;
        this.idPromo = idPromo;
        this.status = status;
        this.orderAt = orderAt;
        this.orderDetails = new ArrayList<>();
    }
    
    public String getIdOrder() {
        return idOrder;
    }
    
    public String getIdCustomer() {
        return idCustomer;
    }
    
    public String getIdPromo() {
        return idPromo;
    }
    
    public String getStatus() {
        return status;
    }
    
    public Timestamp getOrderAt() {
        return orderAt;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public Promo getPromo() {
        return promo;
    }
    
    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }
    
    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }
    
    public void setIdPromo(String idPromo) {
        this.idPromo = idPromo;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setOrderAt(Timestamp orderAt) {
        this.orderAt = orderAt;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setPromo(Promo promo) {
        this.promo = promo;
    }
    
    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public double calculateTotalAmount() {
    	double total = 0;
        ArrayList<OrderDetail> details = OrderDetail.getOrderDetails(this.idOrder);
        for (OrderDetail detail : details) {
            total += detail.getSubtotal();
        }
        if (this.idPromo != null && !this.idPromo.isEmpty()) {
            Promo promo = Promo.getPromo(this.idPromo);
            if (promo != null) {
                total = promo.calculateFinalAmount(total);
            }
        }
        
        return total;
    }
    
    public static OrderHeader createOrderHeader(String idCustomer, String idPromo, 
                                               ArrayList<CartItem> cartItems) {
        String orderId = generateOrderId();
        
        double totalAmount = 0.0;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }
        
        if (idPromo != null && !idPromo.isEmpty()) {
            Promo promo = Promo.getPromo(idPromo);
            if (promo != null) {
                totalAmount = promo.calculateFinalAmount(totalAmount);
            }
        }
        
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        OrderHeader orderHeader = new OrderHeader(orderId, idCustomer, idPromo, 
                                                  "Pending", currentTime);
        orderHeader.setTotalAmount(totalAmount);
        
        if (insertOrderHeader(orderHeader)) {
            for (CartItem item : cartItems) {
                OrderDetail.createOrderDetail(orderId, item.getIdProduct(), item.getCount());
            }
            return orderHeader;
        }
        
        return null;
    }
    
    public static OrderHeader getOrderHeader(String idOrder) {
        OrderHeader orderHeader = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM orderheader WHERE idOrder = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idOrder);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                orderHeader = new OrderHeader(
                    rs.getString("idOrder"),
                    rs.getString("idCustomer"),
                    rs.getString("idPromo"),
                    rs.getString("status"),
                    rs.getTimestamp("orderAt")
                );
                
                orderHeader.setOrderDetails(OrderDetail.getOrderDetails(idOrder));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderHeader;
    }
    
    public static ArrayList<OrderHeader> getAllOrderHeaders() {
        ArrayList<OrderHeader> orderHeaders = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM orderheader ORDER BY orderAt DESC";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderHeader orderHeader = new OrderHeader(
                    rs.getString("idOrder"),
                    rs.getString("idCustomer"),
                    rs.getString("idPromo"),
                    rs.getString("status"),
                    rs.getTimestamp("orderAt")
                );
                orderHeaders.add(orderHeader);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderHeaders;
    }
    
    public static ArrayList<OrderHeader> getCustomerOrderHeaders(String idCustomer) {
        ArrayList<OrderHeader> orderHeaders = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM orderheader WHERE idCustomer = ? ORDER BY orderAt DESC";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCustomer);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderHeader orderHeader = new OrderHeader(
                    rs.getString("idOrder"),
                    rs.getString("idCustomer"),
                    rs.getString("idPromo"),
                    rs.getString("status"),
                    rs.getTimestamp("orderAt")
                );
                
                orderHeader.setOrderDetails(OrderDetail.getOrderDetails(orderHeader.getIdOrder()));
                orderHeaders.add(orderHeader);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderHeaders;
    }
    
    public static ArrayList<OrderHeader> getOrderHeadersByStatus(String status) {
        ArrayList<OrderHeader> orderHeaders = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM orderheader WHERE status = ? ORDER BY orderAt DESC";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderHeader orderHeader = new OrderHeader(
                    rs.getString("idOrder"),
                    rs.getString("idCustomer"),
                    rs.getString("idPromo"),
                    rs.getString("status"),
                    rs.getTimestamp("orderAt")
                );
                orderHeaders.add(orderHeader);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderHeaders;
    }
    
    public static boolean updateOrderStatus(String idOrder, String newStatus) {
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "UPDATE orderheader SET status = ? WHERE idOrder = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, newStatus);
            ps.setString(2, idOrder);
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String generateOrderId() {
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT idOrder FROM orderheader ORDER BY idOrder DESC LIMIT 1";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String lastId = rs.getString("idOrder");
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                rs.close();
                ps.close();
                return String.format("OR%03d", num);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "OR001";
    }
    
    private static boolean insertOrderHeader(OrderHeader orderHeader) {
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "INSERT INTO orderheader (idOrder, idCustomer, idPromo, status, orderAt) VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, orderHeader.getIdOrder());
            ps.setString(2, orderHeader.getIdCustomer());
            ps.setString(3, orderHeader.getIdPromo());
            ps.setString(4, orderHeader.getStatus());
            ps.setTimestamp(5, orderHeader.getOrderAt());
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
