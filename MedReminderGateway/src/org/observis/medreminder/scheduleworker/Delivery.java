package org.observis.medreminder.scheduleworker;

public class Delivery {
	/**
	 * 
	 */
	public String patientPhone = "";
	public String text = "";
	public String date = "";
	public String time = "";
	public String sent =  "";

	public Delivery(String patientPhone, String text, String date, String time, String sent){
		this.patientPhone = patientPhone;
		this.text = text;
		this.date = date;
		this.time = time;
		this.sent = sent;
	}
}
