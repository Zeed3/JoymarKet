package controller;

import java.util.ArrayList;

import model.CartItem;
import model.Product;

/**
 * Controller class for handling shopping cart operations.
 * Manages adding, updating, removing items, and calculating totals.
 */

public class CartController {
    
    public static ArrayList<CartItem> getCartItems(String idCustomer) {
        return CartItem.getCartItems(idCustomer);
    }
    
    public static boolean addToCart(String idCustomer, String idProduct, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        Product product = Product.getProduct(idProduct);
        if (product == null) {
            return false;
        }
        
        if (quantity > product.getStock()) {
            return false;
        }
        
        return CartItem.createCartItem(idCustomer, idProduct, quantity);
    }
    
    public static boolean updateCartItem(String idCustomer, String idProduct, int newQuantity) {
        if (newQuantity <= 0) {
            return false;
        }
        
        Product product = Product.getProduct(idProduct);
        if (product == null) {
            return false;
        }
        
        if (newQuantity > product.getStock()) {
            return false;
        }
        
        return CartItem.editCartItem(idCustomer, idProduct, newQuantity);
    }
    
    public static boolean removeFromCart(String idCustomer, String idProduct) {
        return CartItem.deleteCartItem(idCustomer, idProduct);
    }
    
    public static boolean clearCart(String idCustomer) {
        return CartItem.clearCart(idCustomer);
    }
    
    public static double calculateTotal(ArrayList<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    public static boolean isCartEmpty(String idCustomer) {
        ArrayList<CartItem> items = CartItem.getCartItems(idCustomer);
        return items.isEmpty();
    }
}