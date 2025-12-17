package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

/**
 * Model class representing a cart item in the shopping cart.
 * Contains customer ID, product ID, quantity, and associated product details.
 */

public class CartItem {

	private String idCustomer;
	private String idProduct;
	private int count;
	private Product product_item;
	
	public CartItem(String idCustomer, String idProduct, int count) {
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.count = count;
    }
    
    public String getIdCustomer() {
        return idCustomer;
    }
    
    public String getIdProduct() {
        return idProduct;
    }
    
    public int getCount() {
        return count;
    }
    
    public Product getProduct() {
        return product_item;
    }
    
    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }
    
    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public void setProduct(Product product_item) {
        this.product_item = product_item;
    }
    
    public static ArrayList<CartItem> getCartItems(String idCustomer) {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT c.*, p.name, p.price, p.stock, p.category " +
                      "FROM cartitem c JOIN product p ON c.idProduct = p.idProduct " +
                      "WHERE c.idCustomer = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCustomer);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CartItem cartItem = new CartItem(
                    rs.getString("idCustomer"),
                    rs.getString("idProduct"),
                    rs.getInt("count")
                );
                
                Product product = new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
                cartItem.setProduct(product);
                cartItems.add(cartItem);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cartItems;
    }
    
    public static CartItem getCartItem(String idCustomer, String idProduct) {
        CartItem cartItem = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM cartitem WHERE idCustomer = ? AND idProduct = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCustomer);
            ps.setString(2, idProduct);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                cartItem = new CartItem(
                    rs.getString("idCustomer"),
                    rs.getString("idProduct"),
                    rs.getInt("count")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cartItem;
    }
    
    public static boolean createCartItem(String idCustomer, String idProduct, int count) {
        CartItem existingItem = getCartItem(idCustomer, idProduct);
        
        if (existingItem != null) {
            return editCartItem(idCustomer, idProduct, existingItem.getCount() + count);
        } else {
            CartItem cartItem = new CartItem(idCustomer, idProduct, count);
            return insertCartItem(cartItem);
        }
    }
    
    public static boolean editCartItem(String idCustomer, String idProduct, int newCount) {
        return updateCartItem(idCustomer, idProduct, newCount);
    }
    
    public static boolean deleteCartItem(String idCustomer, String idProduct) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "DELETE FROM cartitem WHERE idCustomer = ? AND idProduct = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCustomer);
            ps.setString(2, idProduct);
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean clearCart(String idCustomer) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "DELETE FROM cartitem WHERE idCustomer = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCustomer);
            
            ps.executeUpdate();
            ps.close();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public double getSubtotal() {
        if (product_item != null) {
            return product_item.getPrice() * count;
        }
        return 0.0;
    }
    
    private static boolean insertCartItem(CartItem cartItem) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "INSERT INTO cartitem (idCustomer, idProduct, count) VALUES (?, ?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, cartItem.getIdCustomer());
            ps.setString(2, cartItem.getIdProduct());
            ps.setInt(3, cartItem.getCount());
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean updateCartItem(String idCustomer, String idProduct, int newCount) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "UPDATE cartitem SET count = ? WHERE idCustomer = ? AND idProduct = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, newCount);
            ps.setString(2, idCustomer);
            ps.setString(3, idProduct);
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
