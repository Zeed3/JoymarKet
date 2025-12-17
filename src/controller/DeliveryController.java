package controller;

import java.util.ArrayList;

import model.Delivery;

/**
 * Controller class for managing delivery operations.
 * Handles creation, assignment, and status updates of deliveries.
 */

public class DeliveryController {
    
    
    public static boolean createDelivery(String idOrder, String idCourier) {
        if (idOrder == null || idOrder.isEmpty() || 
            idCourier == null || idCourier.isEmpty()) {
            return false;
        }
        
        return Delivery.createDelivery(idOrder, idCourier);
    }
    
    
    public static boolean assignCourier(String idOrder, String idCourier) {
        return createDelivery(idOrder, idCourier);
    }
    
    
    public static Delivery getDelivery(String idOrder, String idCourier) {
        if (idOrder == null || idOrder.isEmpty() || 
            idCourier == null || idCourier.isEmpty()) {
            return null;
        }
        
        return Delivery.getDelivery(idOrder, idCourier);
    }
    
   
    public static ArrayList<Delivery> getAllDeliveries() {
        return Delivery.getAllDeliveries();
    }
    
    
    public static ArrayList<Delivery> getCourierDeliveries(String idCourier) {
        if (idCourier == null || idCourier.isEmpty()) {
            return new ArrayList<>();
        }
        
        return Delivery.getCourierDeliveries(idCourier);
    }
    
    
    public static boolean editDeliveryStatus(String idOrder, String idCourier, String newStatus) {
        if (idOrder == null || idOrder.isEmpty() || 
            idCourier == null || idCourier.isEmpty() || 
            newStatus == null || newStatus.isEmpty()) {
            return false;
        }
        
        return Delivery.editDeliveryStatus(idOrder, idCourier, newStatus);
    }
    
   
    public static Delivery getDeliveryByOrder(String idOrder) {
        if (idOrder == null || idOrder.isEmpty()) {
            return null;
        }
        
        return Delivery.getDeliveryByOrder(idOrder);
    }
}