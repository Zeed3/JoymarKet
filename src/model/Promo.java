package model;

import java.sql.*;
import java.util.ArrayList;

import utils.ConnectDB;

public class Promo {

	private String idPromo;
	private String code;
	private String headline;
	private double discountPercentage;
	
	public Promo(String idPromo, String code, String headline, double discountPercentage) {
		super();
		this.idPromo = idPromo;
		this.code = code;
		this.headline = headline;
		this.discountPercentage = discountPercentage;
	}

	public String getIdPromo() {
		return idPromo;
	}

	public void setIdPromo(String idPromo) {
		this.idPromo = idPromo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public double getDicountPercentage() {
		return discountPercentage;
	}

	public void setDicountPercentage(double dicountPercentage) {
		this.discountPercentage = dicountPercentage;
	}

	public static ArrayList<Promo> getAllPromos() {
        ArrayList<Promo> promos = new ArrayList<>();
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM promo ORDER BY code";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Promo promo = new Promo(
                    rs.getString("idPromo"),
                    rs.getString("code"),
                    rs.getString("headline"),
                    rs.getDouble("discountPercentage")
                );
                promos.add(promo);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return promos;
    }
    
    public static Promo getPromoByCode(String code) {
        Promo promo = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM promo WHERE code = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                promo = new Promo(
                    rs.getString("idPromo"),
                    rs.getString("code"),
                    rs.getString("headline"),
                    rs.getDouble("discountPercentage")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return promo;
    }
    
    public static Promo getPromo(String idPromo) {
        Promo promo = null;
        Connection conn = ConnectDB.getInstance().getConnection();
        
        String query = "SELECT * FROM promo WHERE idPromo = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idPromo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                promo = new Promo(
                    rs.getString("idPromo"),
                    rs.getString("code"),
                    rs.getString("headline"),
                    rs.getDouble("discountPercentage")
                );
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return promo;
    }
    
    public double calculateDiscount(double totalAmount) {
        return totalAmount * (discountPercentage / 100.0);
    }
    
    public double calculateFinalAmount(double totalAmount) {
        return totalAmount - calculateDiscount(totalAmount);
    }
	
}
