package controller;

import java.util.ArrayList;

import model.Product;

/**
 * Controller class for product management.
 * Provides methods for retrieving and updating product information and stock.
 */

public class ProductController {
    
    public static ArrayList<Product> getAllProducts() {
        return Product.getAllProducts();
    }
    
    public static ArrayList<Product> getAvailableProducts() {
        return Product.getAvailableProducts();
    }
    
    public static Product getProduct(String idProduct) {
        return Product.getProduct(idProduct);
    }
    
    public static boolean updateProductStock(String idProduct, int newStock) {
        if (newStock < 0) {
            return false;
        }
        
        return Product.editProductStock(idProduct, newStock);
    }
    
    public static boolean checkStock(Product product, int quantity) {
        return product.hasSufficientStock(quantity);
    }
    
    public static boolean reduceStock(Product product, int quantity) {
        return product.reduceStock(quantity);
    }
    
    public static boolean increaseStock(Product product, int quantity) {
        return product.increaseStock(quantity);
    }
}