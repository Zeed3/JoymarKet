package controller;

import java.util.ArrayList;

import model.Promo;

public class PromoController {
    
    public static ArrayList<Promo> getAllPromos() {
        return Promo.getAllPromos();
    }
    
    public static Promo getPromoByCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        
        return Promo.getPromoByCode(code);
    }
    
    public static Promo getPromo(String idPromo) {
        return Promo.getPromo(idPromo);
    }
    
    public static double calculateDiscount(Promo promo, double totalAmount) {
        if (promo == null) {
            return 0;
        }
        
        return promo.calculateDiscount(totalAmount);
    }
    
    public static double calculateFinalAmount(Promo promo, double totalAmount) {
        if (promo == null) {
            return totalAmount;
        }
        
        return promo.calculateFinalAmount(totalAmount);
    }
    
    public static boolean isPromoValid(String code) {
        return getPromoByCode(code) != null;
    }
}