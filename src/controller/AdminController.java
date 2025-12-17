package controller;

import java.util.ArrayList;

import model.Admin;
import model.Courier;
import model.OrderHeader;
import model.Product;

public class AdminController {
    
    public static Admin getAdmin(String idAdmin) {
        return Admin.getAdmin(idAdmin);
    }
    
    public static ArrayList<Product> getAllProducts() {
        return Product.getAllProducts();
    }
    
    public static boolean updateProductStock(String idProduct, int newStock) {
        if (newStock < 0) {
            return false;
        }
        
        return Product.editProductStock(idProduct, newStock);
    }
    
    public static ArrayList<Courier> getAllCouriers() {
        return Courier.getAllCouriers();
    }
    
    public static ArrayList<OrderHeader> getAllOrders() {
        return OrderHeader.getAllOrderHeaders();
    }
    
    public static boolean assignCourierToOrder(String idOrder, String idCourier) {
        return DeliveryController.assignCourier(idOrder, idCourier);
    }
    
    public static ArrayList<OrderHeader> getPendingOrders() {
        return OrderHeader.getOrderHeadersByStatus("Pending");
    }
    
    public static ArrayList<OrderHeader> getProcessedOrders() {
        return OrderHeader.getOrderHeadersByStatus("Processed");
    }
    
    public static ArrayList<OrderHeader> getDeliveredOrders() {
        return OrderHeader.getOrderHeadersByStatus("Delivered");
    }
}