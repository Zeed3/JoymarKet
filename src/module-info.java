/**
 * 
 */
/**
 * 
 */
module JoymarKet {
	opens main;
<<<<<<< HEAD
	opens utils;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	requires java.sql;
=======
	requires javafx.graphics;
	requires java.sql;
	requires javafx.controls;
	opens utils;
	opens views;
	opens model;
	opens controller;

>>>>>>> update_version
}