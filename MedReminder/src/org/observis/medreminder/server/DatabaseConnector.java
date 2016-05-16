package org.observis.medreminder.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.observis.medreminder.client.Delivery;
import org.observis.medreminder.client.Message;

public class DatabaseConnector {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://192.168.8.102:3306/patients";
	static Connection conn = null;
	static Statement stmt = null;
	// Database credentials
	static final String USER = "root";
	static final String PASS = "rootroot";
	

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
			stmt = null;
			// System.out.println("Connection is closed");
		} catch (SQLException se) {
			se.printStackTrace();
		}

	}

	public static String returnPatient(String doctorName) { // returns patients
															// numbers for exact
															// // doctor name
		openConnection();
		ResultSet rs = null;
		String doctorID = "";
		String resultString = "";
		PreparedStatement doctorSql;
		try {
			doctorSql = conn.prepareStatement("SELECT doctor_id FROM doctors WHERE username LIKE ?");
			doctorSql.setString(1, doctorName);
			stmt = conn.createStatement();

			// get doctor_id from doctors database to find patients numbers for
			// this exact doctor
			rs = doctorSql.executeQuery();
			while (rs.next()) {
				doctorID = rs.getString("doctor_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PreparedStatement getNumbers;
		try {
			getNumbers = conn.prepareStatement("SELECT number FROM patients WHERE doctor_id LIKE ? ORDER BY number");
			getNumbers.setString(1, doctorID);
			rs = getNumbers.executeQuery();
			while (rs.next()) {
				if (resultString.length() < 2) {
					resultString += rs.getString("number");
				} else {
					resultString += "," + rs.getString("number");
				}
			}
			// System.out.println(resultString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
		return resultString;

	}

	public static void addPatientRecord(String name, String phone, String username) {
		ResultSet rs = null;
		openConnection();
		String doctorID = "";
		PreparedStatement getDoctorID;
		PreparedStatement addPatientSQL;
		try {
			getDoctorID = conn.prepareStatement("SELECT doctor_id FROM doctors WHERE username LIKE ?");
			getDoctorID.setString(1, username);
			stmt = conn.createStatement();
			// get doctor_id from doctors database to find patients numbers for
			// this exact doctor
			rs = getDoctorID.executeQuery();
			while (rs.next()) {
				doctorID = rs.getString("doctor_id");
			}
			addPatientSQL = conn.prepareStatement("INSERT INTO patients (doctor_id, number,name) VALUES (?,?,?)");
			addPatientSQL.setString(1, doctorID);
			addPatientSQL.setString(2, phone);
			addPatientSQL.setString(3,name);
			addPatientSQL.executeUpdate();
			// new patient added
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection();
		return;
	}

	public static boolean checkLogin(String username, String password) {
		openConnection();
		String result = "";
		ResultSet rs = null;
		PreparedStatement checkLoginSQL;
		try {
			checkLoginSQL = conn.prepareStatement("SELECT password FROM doctors WHERE username LIKE ?");
			checkLoginSQL.setString(1, username);
			stmt = conn.createStatement();

			rs = checkLoginSQL.executeQuery();
			if (rs == null) {
				return false;

			}
			while (rs.next()) {
				result = rs.getString("password");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();

		// System.out.println(MD5(password)+" "+result);
		if (result.equals(MD5(password))) {
			return true;
		} else {
			return false;
		}
	}

	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	public static void insertSchedule(Message msg, String patientPhone) {
		openConnection();
		Date curDate = new Date();
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(curDate);
		curCal.add(Calendar.DATE, Integer.parseInt(msg.day) - 1);
		ResultSet rs;
		String patient_id = "";
		PreparedStatement sqlSelect;

		String day = "" + curCal.get(Calendar.DAY_OF_MONTH);
		String month = "" + (curCal.get(Calendar.MONTH) + 1);
		String year = "" + curCal.get(Calendar.YEAR);
		String date = day + "-" + month + "-" + year;
		try {
			sqlSelect = conn.prepareStatement("SELECT patient_id FROM patients WHERE number LIKE ?");
			sqlSelect.setString(1, patientPhone);
			stmt = conn.createStatement();
			rs = sqlSelect.executeQuery();
			while (rs.next()) {
				patient_id = rs.getString("patient_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rs = null;
		PreparedStatement sqlInsert;
		// String sqlInsert = "INSERT INTO `delivery`(patient_id, text, date,
		// time, sent) VALUES ('" + patient_id
		// + "', '" + msg.text + "', '" + date + "', '" + msg.time + "', '0') ";

		try {
			sqlInsert = conn.prepareStatement(
					"INSERT INTO delivery (patient_id, text, date, time, sent) VALUES (?, ?, ?, ?, ?)");
			sqlInsert.setString(1, patient_id);
			sqlInsert.setString(2, msg.text);
			sqlInsert.setString(3, date);
			sqlInsert.setString(4, msg.time);
			sqlInsert.setString(5, "0");
			stmt = conn.createStatement();
			sqlInsert.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Insert into db delivery -> delivered -> false, phone -> patientPhone,
		// text -> msg.text time -> msg.time, patient_id -> from patient phone
		// with query
		// date -> cal.get(Calendar.DAY or DATE or DATE_OF_YEAR + YEAR..
		closeConnection();

	}

	public static String getPackagesDB() {
		openConnection();
		String packageList = "";
		PreparedStatement sqlQuery;
		ResultSet rs = null;
		try {
			sqlQuery = conn.prepareStatement("SELECT title FROM packages ORDER BY title");
			stmt = conn.createStatement();
			rs = sqlQuery.executeQuery();
			while (rs.next()) {
				if (packageList.length() < 2) {
					packageList += rs.getString("title");
				} else {
					packageList += "," + rs.getString("title");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
		return packageList;

	}

	public static ArrayList<Message> getSinglePackage(String title) {
		openConnection();
		ArrayList<Message> messageList = new ArrayList<Message>();
		PreparedStatement sqlSelect;
		ResultSet rs = null;
		try {
			sqlSelect = conn.prepareStatement(
					"SELECT messages.id, messages.title, messages.time, messages.day, messages.text FROM packages Left join messages ON packages.id = messages.package_id WHERE packages.title LIKE ? ORDER BY day, time");
			sqlSelect.setString(1, title);
			stmt = conn.createStatement();
			rs = sqlSelect.executeQuery();
			while (rs.next()) {
				if (rs.getString("title") != null && rs.getString("text") != null && rs.getString("time") != null
						&& rs.getString("day") != null)
					messageList.add(new Message(rs.getString("title"), rs.getString("text"), rs.getString("time"),
							rs.getString("day")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
		return messageList;
	}

	public static void addPackagetoDB(String title) {
		openConnection();
		PreparedStatement sqlInsert;
		try {
			sqlInsert = conn.prepareStatement("INSERT INTO packages (title) VALUES (?)");
			sqlInsert.setString(1, title);
			stmt = conn.createStatement();
			sqlInsert.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();

	}

	public static void addMessagetoDB(Message msg, String package_title) {
		openConnection();
		String package_id = "";
		PreparedStatement sqlSelect;
		PreparedStatement sqlInsert;
		ResultSet rs = null;

		try {
			sqlSelect = conn.prepareStatement("SELECT id FROM packages WHERE title LIKE ?");
			sqlSelect.setString(1, package_title);
			stmt = conn.createStatement();
			rs = sqlSelect.executeQuery();
			while (rs.next()) {
				package_id = rs.getString("id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sqlInsert = conn
					.prepareStatement("INSERT INTO messages(title,text,time,day,package_id) VALUES (?,?,?,?,?)");
			sqlInsert.setString(1, msg.title);
			sqlInsert.setString(2, msg.text);
			sqlInsert.setString(3, msg.time);
			sqlInsert.setString(4, msg.day);
			sqlInsert.setString(5, package_id);
			stmt = conn.createStatement();
			sqlInsert.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
	}

	public static void removeMessageDB(String title, String text) {
		openConnection();
		PreparedStatement sqlRemove;
		try {
			sqlRemove = conn.prepareStatement("DELETE FROM messages WHERE title = ? AND text = ?");
			sqlRemove.setString(1, title);
			sqlRemove.setString(2, text);
			stmt = conn.createStatement();
			sqlRemove.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
	}

	public static void removePackageDB(String title) {
		openConnection();
		PreparedStatement sqlRemove;
		try {
			sqlRemove = conn.prepareStatement("DELETE FROM packages WHERE title = ?");
			sqlRemove.setString(1, title);
			stmt = conn.createStatement();
			sqlRemove.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();

	}

	public static void updateMessageDB(String oldTitle, String newTitle, String newText, String newTime,
			String newDay) {
		openConnection();
		PreparedStatement sqlUpdate;

		try {
			sqlUpdate = conn.prepareStatement("UPDATE messages SET title = (?, ?, ?, ?) WHERE title LIKE ?");
			sqlUpdate.setString(1, newTitle);
			sqlUpdate.setString(2, newText);
			sqlUpdate.setString(3, newTime);
			sqlUpdate.setString(4, newDay);
			sqlUpdate.setString(5, oldTitle);

			stmt = conn.createStatement();
			sqlUpdate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
	}

	public static void updatePackageDB(String oldTitle, String newTitle) {
		openConnection();
		PreparedStatement sqlUpdate;
		try {
			sqlUpdate = conn.prepareStatement("UPDATE packages SET title=? WHERE title LIKE ?");
			sqlUpdate.setString(1, newTitle);
			sqlUpdate.setString(2, oldTitle);

			stmt = conn.createStatement();

			sqlUpdate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();

	}

	public static ArrayList<Delivery> returnDeliveryDB(String phone) {
		openConnection();
		ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();
		String patient_id = "";
		PreparedStatement sqlGetId;
		PreparedStatement sqlSelect;

		ResultSet rs = null;
		try {
			sqlGetId = conn.prepareStatement("SELECT patient_id,number FROM patients WHERE number = ?");
			sqlGetId.setString(1, phone);

			stmt = conn.createStatement();
			rs = sqlGetId.executeQuery();
			while (rs.next())
				patient_id = rs.getString("patient_id");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sqlSelect = conn.prepareStatement(
					"SELECT  text, date, time, sent FROM delivery WHERE patient_id = ? ORDER BY date, time");
			sqlSelect.setString(1, patient_id);
			rs = sqlSelect.executeQuery();
			while (rs.next()) {
				deliveryList.add(new Delivery(phone, rs.getString("text"), rs.getString("date"), rs.getString("time"),
						rs.getString("sent")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection();
		return deliveryList;
	}

	public static void removePatientDB(String phone) {
		openConnection();
		PreparedStatement sqlRemove;
		// String sqlRemove = "DELETE FROM patients WHERE number = '"+phone+"'";
		try {
			sqlRemove = conn.prepareStatement("DELETE FROM patients WHERE number = ?");
			sqlRemove.setString(1, phone);
			stmt = conn.createStatement();
			sqlRemove.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();

	}

	public static void removeDeliveryDB(Delivery chosenDelivery) {
		openConnection();
		ResultSet rs = null;
		String patient_id = "";
		String delivery_id = "";
		PreparedStatement sqlSelectPatient;
		PreparedStatement sqlSelectDelivery;
		PreparedStatement sqlRemove;

		try {
			sqlSelectPatient = conn.prepareStatement("SELECT patient_id FROM patients WHERE number = ?");
			sqlSelectPatient.setString(1, chosenDelivery.patientPhone);

			stmt = conn.createStatement();
			rs = sqlSelectPatient.executeQuery();
			while (rs.next()) {
				patient_id = rs.getString("patient_id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rs = null;

		try {
			sqlSelectDelivery = conn.prepareStatement(
					"SELECT delivery_id FROM delivery WHERE patient_id = ? AND text = ? AND date = ? AND time = ? ");
			sqlSelectDelivery.setString(1, patient_id);
			sqlSelectDelivery.setString(2, chosenDelivery.text);
			sqlSelectDelivery.setString(3, chosenDelivery.date);
			sqlSelectDelivery.setString(4, chosenDelivery.time);
			// sqlSelectDelivery.setString(5, chosenDelivery.sent);
			stmt = conn.createStatement();
			rs = sqlSelectDelivery.executeQuery();
			while (rs.next()) {
				delivery_id = rs.getString("delivery_id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sqlRemove = conn.prepareStatement("DELETE FROM delivery WHERE delivery_id = ?");
			sqlRemove.setString(1, delivery_id);
			stmt = conn.createStatement();
			sqlRemove.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection();
	}

	public static void addDeliveryDB(Delivery delivery, String phone) {
		openConnection();
		String patient_id = "";
		ResultSet rs = null;
		PreparedStatement sqlSelect;
		PreparedStatement sqlUpdate;

		try {
			sqlSelect = conn.prepareStatement("SELECT patient_id FROM patients WHERE number = ?");
			sqlSelect.setString(1, delivery.patientPhone);
			stmt = conn.createStatement();
			rs = sqlSelect.executeQuery();
			while (rs.next()) {
				patient_id = rs.getString("patient_id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sqlUpdate = conn.prepareStatement(
					"INSERT INTO delivery (patient_id, text, date, time, sent) VALUES (?, ?, ?, ?, ?)");
			sqlUpdate.setString(1, patient_id);
			sqlUpdate.setString(2, delivery.text);
			sqlUpdate.setString(3, delivery.date);
			sqlUpdate.setString(4, delivery.time);
			sqlUpdate.setString(5, "0");

			stmt = conn.createStatement();
			sqlUpdate.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection();
	}

	public static void updateDeliveryDB(Delivery oldDelivery, Delivery changedDelivery) {
		openConnection();

		System.out
				.println("Old delivery: " + oldDelivery.text + " " + oldDelivery.patientPhone + " " + oldDelivery.time);
		System.out.println("Old delivery: " + changedDelivery.text + " " + changedDelivery.patientPhone + " "
				+ changedDelivery.time);
		String patient_id = "";
		String delivery_id = "";
		PreparedStatement sqlSelect;
		PreparedStatement sqlSelectDelivery;
		PreparedStatement sqlUpdate;
		ResultSet rs = null;
		// String sqlSelect = "SELECT patient_id FROM patients WHERE number =
		// '"+changedDelivery.patientPhone+"'";

		try {
			sqlSelect = conn.prepareStatement("SELECT patient_id FROM patients WHERE number = ?");
			sqlSelect.setString(1, changedDelivery.patientPhone);
			stmt = conn.createStatement();
			rs = sqlSelect.executeQuery();
			while (rs.next()) {
				patient_id = rs.getString("patient_id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rs = null;

		// String sqlSelectDelivery = "SELECT delivery_id FROM delivery WHERE
		// patient_id = '"+patient_id+"' "
		// + "AND text = '"+oldDelivery.text+"' AND date =
		// '"+oldDelivery.date+"' AND time = '"+oldDelivery.time+"' AND sent =
		// '"+oldDelivery.sent+"'";
		try {
			sqlSelectDelivery = conn.prepareStatement(
					"SELECT delivery_id FROM delivery WHERE patient_id = ? AND text = ? AND date = ? AND time = ? AND sent = ?");
			sqlSelectDelivery.setString(1, patient_id);
			sqlSelectDelivery.setString(2, oldDelivery.text);
			sqlSelectDelivery.setString(3, oldDelivery.date);
			sqlSelectDelivery.setString(4, oldDelivery.time);
			sqlSelectDelivery.setString(5, oldDelivery.sent);
			stmt = conn.createStatement();
			rs = sqlSelectDelivery.executeQuery();
			while (rs.next())
				delivery_id = rs.getString("delivery_id");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sqlUpdate = conn.prepareStatement("UPDATE delivery SET text = ?, date = ?, time = ? WHERE delivery_id = ?");
			sqlUpdate.setString(1, changedDelivery.text);
			sqlUpdate.setString(2, changedDelivery.date);
			sqlUpdate.setString(3, changedDelivery.time);
			sqlUpdate.setString(4, delivery_id);
			stmt = conn.createStatement();
			sqlUpdate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection();
	}

	public static void removeAllDeliveryDB(String phone) {
		openConnection();
		
		String patient_id = "";
		PreparedStatement sqlSelect;
		PreparedStatement sqlDelete;
		ResultSet rs = null;
		try {
			sqlSelect = conn.prepareStatement("SELECT patient_id FROM patients WHERE number = ?");
			sqlSelect.setString(1, phone);
			stmt = conn.createStatement();
			rs = sqlSelect.executeQuery();
			while (rs.next()) {
				patient_id = rs.getString("patient_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sqlDelete = conn.prepareStatement("DELETE FROM delivery WHERE patient_id = ?");
			sqlDelete.setString(1, patient_id);

			stmt = conn.createStatement();
			sqlDelete.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
	}

}
