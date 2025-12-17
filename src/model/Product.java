package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

public class Product {

	private String idProduct;
	private String name;
	private double price;
	private int stock;
	private String category;
	
	public Product() {
		
	}
	
	public Product(String idProduct, String name, double price, int stock, String category) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
    
    public String getIdProduct() {
        return idProduct;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public static Product getProduct(String idProduct) {
        Product product = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM product WHERE idProduct = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idProduct);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                product = new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return product;
    }
    
    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM product ORDER BY name";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
                products.add(product);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
    
    public static ArrayList<Product> getAvailableProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM product WHERE stock > 0 ORDER BY name";
        
        System.out.println("Executing query: " + query);
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
                products.add(product);
                System.out.println("Loaded: " + product.getName());
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ERROR loading products:");
            e.printStackTrace();
        }
        
        System.out.println("Total products loaded: " + products.size());
        return products;
    }
    
    public static boolean addProduct(Product product) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "INSERT INTO product (idProduct, name, price, stock, category) VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, product.getIdProduct());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getCategory());
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean editProductStock(String idProduct, int newStock) {
        return updateProductStock(idProduct, newStock);
    }
    
    public boolean hasSufficientStock(int quantity) {
        return this.stock >= quantity;
    }
    
    public boolean reduceStock(int quantity) {
        if (hasSufficientStock(quantity)) {
            this.stock -= quantity;
            return updateProductStock(this.idProduct, this.stock);
        }
        return false;
    }
    
    public boolean increaseStock(int quantity) {
        this.stock += quantity;
        return updateProductStock(this.idProduct, this.stock);
    }
    
    private static boolean updateProductStock(String idProduct, int newStock) {
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "UPDATE product SET stock = ? WHERE idProduct = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, newStock);
            ps.setString(2, idProduct);
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    

}
