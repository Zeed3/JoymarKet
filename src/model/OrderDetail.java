package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

public class OrderDetail {

	private String idOrder;
    private String idProduct;
    private int qty;
    private Product product;
    
    
    public OrderDetail(String idOrder, String idProduct, int qty) {
        this.idOrder = idOrder;
        this.idProduct = idProduct;
        this.qty = qty;
    }
    
    public String getIdOrder() {
        return idOrder;
    }
    
    public String getIdProduct() {
        return idProduct;
    }
    
    public int getQty() {
        return qty;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }
    
    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
    
    public void setQty(int qty) {
        this.qty = qty;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public static boolean createOrderDetail(String idOrder, String idProduct, int qty) {
        OrderDetail orderDetail = new OrderDetail(idOrder, idProduct, qty);
        return insertOrderDetail(orderDetail);
    }
    
    public static ArrayList<OrderDetail> getOrderDetails(String idOrder) {
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT od.*, p.name, p.price, p.stock, p.category " +
                      "FROM orderdetail od JOIN product p ON od.idProduct = p.idProduct " +
                      "WHERE od.idOrder = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idOrder);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                    rs.getString("idOrder"),
                    rs.getString("idProduct"),
                    rs.getInt("qty")
                );
                
                Product product = new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
                orderDetail.setProduct(product);
                orderDetails.add(orderDetail);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderDetails;
    }
    
    public static ArrayList<OrderDetail> getAllOrderDetails() {
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM orderdetail";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                    rs.getString("idOrder"),
                    rs.getString("idProduct"),
                    rs.getInt("qty")
                );
                orderDetails.add(orderDetail);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderDetails;
    }
    
    public static OrderDetail getCustomerOrderDetail(String idOrder, String idProduct) {
        return getOrderDetail(idOrder, idProduct);
    }
    
    public double getSubtotal() {
        if (product != null) {
            return product.getPrice() * qty;
        }
        return 0.0;
    }
    
    private static boolean insertOrderDetail(OrderDetail orderDetail) {
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "INSERT INTO orderdetail (idOrder, idProduct, qty) VALUES (?, ?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, orderDetail.getIdOrder());
            ps.setString(2, orderDetail.getIdProduct());
            ps.setInt(3, orderDetail.getQty());
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static OrderDetail getOrderDetail(String idOrder, String idProduct) {
        OrderDetail orderDetail = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        String query = "SELECT * FROM orderdetail WHERE idOrder = ? AND idProduct = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idOrder);
            ps.setString(2, idProduct);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                orderDetail = new OrderDetail(
                    rs.getString("idOrder"),
                    rs.getString("idProduct"),
                    rs.getInt("qty")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orderDetail;
    }

}
