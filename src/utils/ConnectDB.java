package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing database connections using the Singleton pattern.
 * Provides methods for executing queries, updates, and managing MySQL connections.
 */

public class ConnectDB {
	private String USERNAME = "root";
	private String PASSWORD = "";
	private String DATABASE = "joymarket";
	private String HOST = "localhost:3306";
	private String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection conn;
	private Statement st;
	public PreparedStatement ps;
	public ResultSet rs;
	
	private static ConnectDB connectDB;
	
	public static ConnectDB getInstance() {

		if(connectDB == null) return new ConnectDB();
		return connectDB;
	}
	
	private ConnectDB() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = conn.createStatement();
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
                st = conn.createStatement();
            }
        } catch (SQLException e) {
            System.err.println("Failed to reconnect to database!");
            e.printStackTrace();
        }
        return conn;
    }
    
    public ResultSet execQuery(String query) {
        ResultSet rs = null;
        try {
            if (st == null || st.isClosed()) {
                st = conn.createStatement();
            }
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Query execution failed: " + query);
            e.printStackTrace();
        }
        return rs;
    }
    
    public int execUpdate(String query) {
        int rowsAffected = 0;
        try {
            if (st == null || st.isClosed()) {
                st = conn.createStatement();
            }
            rowsAffected = st.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Update execution failed: " + query);
            e.printStackTrace();
        }
        return rowsAffected;
    }
    
    public PreparedStatement prepareStatement(String query) {
        PreparedStatement ps = null;
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            }
            ps = conn.prepareStatement(query);
        } catch (SQLException e) {
            System.err.println("PreparedStatement creation failed: " + query);
            e.printStackTrace();
        }
        return ps;
    }
    
    public void closeConnection() {
        try {
            if (st != null && !st.isClosed()) {
                st.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection!");
            e.printStackTrace();
        }
    

		try {
			conn = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    }
	
	

