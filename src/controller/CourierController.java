package controller;

import java.util.ArrayList;

import model.Courier;
import model.Delivery;

public class CourierController {
    
    public static Courier getCourier(String idCourier) {
        return Courier.getCourier(idCourier);
    }
    
    public static ArrayList<Courier> getAllCouriers() {
        return Courier.getAllCouriers();
    }
    
    public static ArrayList<Delivery> getMyDeliveries(String idCourier) {
        return Delivery.getCourierDeliveries(idCourier);
    }
    
    public static boolean updateDeliveryStatus(String idOrder, String idCourier, String newStatus) {
        if (!isValidStatus(newStatus)) {
            return false;
        }
        
        return Delivery.editDeliveryStatus(idOrder, idCourier, newStatus);
    }
    
    public static ArrayList<Delivery> getPendingDeliveries(String idCourier) {
        ArrayList<Delivery> allDeliveries = Delivery.getCourierDeliveries(idCourier);
        ArrayList<Delivery> pendingDeliveries = new ArrayList<>();
        
        for (Delivery delivery : allDeliveries) {
            if ("Pending".equals(delivery.getStatus())) {
                pendingDeliveries.add(delivery);
            }
        }
        
        return pendingDeliveries;
    }
    
    public static ArrayList<Delivery> getInProgressDeliveries(String idCourier) {
        ArrayList<Delivery> allDeliveries = Delivery.getCourierDeliveries(idCourier);
        ArrayList<Delivery> inProgressDeliveries = new ArrayList<>();
        
        for (Delivery delivery : allDeliveries) {
            if ("In Progress".equals(delivery.getStatus())) {
                inProgressDeliveries.add(delivery);
            }
        }
        
        return inProgressDeliveries;
    }
    
    public static ArrayList<Delivery> getCompletedDeliveries(String idCourier) {
        ArrayList<Delivery> allDeliveries = Delivery.getCourierDeliveries(idCourier);
        ArrayList<Delivery> completedDeliveries = new ArrayList<>();
        
        for (Delivery delivery : allDeliveries) {
            if ("Delivered".equals(delivery.getStatus())) {
                completedDeliveries.add(delivery);
            }
        }
        
        return completedDeliveries;
    }
    
    private static boolean isValidStatus(String status) {
        return "Pending".equals(status) || 
               "In Progress".equals(status) || 
               "Delivered".equals(status);
    }
}