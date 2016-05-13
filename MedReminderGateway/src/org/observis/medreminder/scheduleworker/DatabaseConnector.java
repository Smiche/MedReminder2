package org.observis.medreminder.scheduleworker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.twilio.sdk.TwilioRestException;


public class DatabaseConnector {
	
	// JDBC driver name and database URL
		static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		static final String DB_URL = "jdbc:mysql://192.168.8.102:3306/patients";
		static Connection conn = null;
		static Statement stmt = null;
		// Database credentials
		static final String USER = "root";
		static final String PASS = "rootroot";
		public static void main(String... args){
			Thread checker = new Thread(new Runnable(){
				@Override
				public void run() {
					while(true){
					openConnection();
						//select all deliveries with sent == 0
						//check if their date <current date
					//get details -> MedReminderWorker.sendSms
						
					timeCheck();
						
					
					closeConnection();
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
				
			});
			
			checker.run();
		}
		
		public static void openConnection() { // opens connection to the server
			try {
				// STEP 2: Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				// STEP 3: Open a connection
				// System.out.println("Connecting to a selected database...");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				// System.out.println("Connected database successfully...");

			} catch (SQLException se) {
				// Handle errors for JDBC
				se.printStackTrace();
			} catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
			} finally {
			}

		}// end main
		
		public static void closeConnection() { // close connection to the server
			try {
				if (conn != null)
					conn.close();
				// System.out.println("Connection is closed");
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}
		
		public static ArrayList<Delivery> deliveryListSent(){
			ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();
			String sqlSelect = "SELECT text FROM delivery WHERE sent = '0'";
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sqlSelect);
				while(rs.next()){
					deliveryList.add(new Delivery(rs.getString("number"),rs.getString("text"),rs.getString("time"),rs.getString("date"),rs.getString("sent")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closeConnection();
			return deliveryList;
			
		}

		public static void timeCheck(){
			//String values needed to store results from DB
			String userDate = "";
			String deliveryID = "";
			String phone = "";
			String text = "";
			Date compareDate= null;
			String sqlTime = "SELECT delivery.date, delivery.time, delivery.delivery_id, delivery.text, patients.patient_id, patients.number FROM delivery LEFT JOIN patients on delivery.patient_id WHERE delivery.sent = 0 and patients.patient_id = delivery.patient_id";
			ResultSet rs = null;
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm");
			Calendar curCal = Calendar.getInstance();
			Date curDate = new Date(); // getting current Date value for curDate
			curCal.setTime(curDate); //getting current Date value for curCal
			
			//System.out.println(dateFormat.format(curCal.getTime())); 
			
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sqlTime);
				while(rs.next()){
					phone = rs.getString("number");
					text = rs.getString("text");
					deliveryID = rs.getString("delivery_id");
					userDate = rs.getString("date")+" "+rs.getString("time");
					//System.out.println(userDate);
					try {
						compareDate = dateFormat.parse(userDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(compareDate.before(curDate)){
						try {
							MedReminderWorker.sendSms(text, phone);	
							stmt = conn.createStatement();
							stmt.executeUpdate("UPDATE delivery SET sent = '1' WHERE delivery_id = '"+deliveryID+"'");
						} catch (TwilioRestException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						System.out.println(deliveryID+""+text+""+phone);
						
					}
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
			
     
			
		}
}
