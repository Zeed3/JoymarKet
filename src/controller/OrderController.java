package controller;

import java.util.ArrayList;

import model.CartItem;
import model.Customer;
import model.OrderDetail;
import model.OrderHeader;
import model.Product;
import model.Promo;

/**
 * Controller class for order management.
 * Handles order creation, retrieval, and status updates.
 */

public class OrderController {
    
    public static OrderHeader createOrder(Customer customer, String idPromo, 
                                         ArrayList<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }
        
        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }
        
        if (idPromo != null && !idPromo.isEmpty()) {
            Promo promo = Promo.getPromo(idPromo);
            if (promo != null) {
                totalAmount = promo.calculateFinalAmount(totalAmount);
            }
        }
        
        if (!customer.hasSufficientBalance(totalAmount)) {
            return null;
        }
        
        OrderHeader order = OrderHeader.createOrderHeader(
            customer.getIdCustomer(), 
            idPromo, 
            cartItems
        );
        
        if (order != null) {
            customer.deductBalance(totalAmount);
            
            for (CartItem item : cartItems) {
                Product product = Product.getProduct(item.getIdProduct());
                if (product != null) {
                    product.reduceStock(item.getCount());
                }
            }
            
            CartItem.clearCart(customer.getIdCustomer());
        }
        
        return order;
    }
    
    public static OrderHeader getOrderHeader(String idOrder) {
        return OrderHeader.getOrderHeader(idOrder);
    }
    
    public static ArrayList<OrderHeader> getAllOrders() {
        return OrderHeader.getAllOrderHeaders();
    }
    
    public static ArrayList<OrderHeader> getCustomerOrders(String idCustomer) {
        return OrderHeader.getCustomerOrderHeaders(idCustomer);
    }
    
    public static ArrayList<OrderHeader> getOrdersByStatus(String status) {
        return OrderHeader.getOrderHeadersByStatus(status);
    }
    
    public static boolean updateOrderStatus(String idOrder, String newStatus) {
        return OrderHeader.updateOrderStatus(idOrder, newStatus);
    }
    
    public static ArrayList<OrderDetail> getOrderDetails(String idOrder) {
        return OrderDetail.getOrderDetails(idOrder);
    }
    
    public static double calculateOrderTotal(ArrayList<OrderDetail> orderDetails) {
        double total = 0;
        for (OrderDetail detail : orderDetails) {
            total += detail.getSubtotal();
        }
        return total;
    }
}