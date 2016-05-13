package org.observis.medreminder.shared;

import java.util.ArrayList;

import org.observis.medreminder.client.Delivery;
import org.observis.medreminder.client.Message;

import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client-side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * In this example, we only require that the name is at least four
	 * characters. In your application, you can use more complex checks to ensure
	 * that usernames, passwords, email addresses, URLs, and other fields have the
	 * proper syntax.
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 3;
	}

	public static boolean isValidPass(String pass) {
		if (pass == null) {
			return false;
		}
		return pass.length() > 3;
	}
	public static boolean isValidText(String text){
		if(text.length()<1 || text.length()>200){
			return false;
		} else {
			return true;
		}
		
	}
	public static boolean isValidDate(String date){
		
		String[] toTest;
		toTest = date.split("-");
		String regex = "[0-9]+";
		String pureDate = date.replace("-", "");

		if(!pureDate.matches(regex)){
		return false;
		}
		if(toTest.length!=3){
			return false;
		}
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for(int i=0;i<3;i++){
			nums.add(Integer.parseInt(toTest[i]));
		}
		for(Integer n:nums){
			if(n==null){
				return false;
			}
		}
		return true;
	}
	public static boolean isValidPhone(String phone){
		String pluslessPhone = phone.replaceAll("\\+", "");
		String regex = "[0-9]+";
		if(phone!=null && pluslessPhone.matches(regex) &&pluslessPhone.length()==12 && phone.startsWith("+")){
			return true;
		}
			return false;
	}
	public static boolean isValidTime(String time){
		
		String[] toTest;
		toTest = time.split(":");
		String pureTime = time.replace(":", "");
		String regex = "[0-9]+";
		if(!pureTime.matches(regex)){
			return false;
			
		}
		if(toTest.length!=2){
			return false;
		}
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for(int i = 0;i<2;i++){
			nums.add(Integer.parseInt(toTest[i]));
		}
		for(Integer n:nums){
			if(n==null){
				return false;
			}
			
		}return true;
	}
	public static boolean isValidDay(String day){
		String regex = "[0-9]+";
		Integer dayNum = Integer.parseInt(day);
		if(day.matches(regex) && dayNum<8){
			return true;
		}else{
		return false;
		}
	}
	public static boolean isValidHour(String hour){
		String regex = "[0-9]+";
		Integer hourNum = Integer.parseInt(hour);
		if(hour.matches(regex) && hourNum<25){
			return true;
		}else{
		return false;
		}
		
	}
	public static boolean isValidMinute(String minute){
		String regex = "[0-9]+";
		Integer minNum = Integer.parseInt(minute);
		if(minute.matches(regex) && minNum<61){
			return true;
		}else{
		return false;
		}
	}
	
	public static boolean isValidMessage(Message msg){
		if(msg== null || !FieldVerifier.isValidDay(msg.day) || !FieldVerifier.isValidTime(msg.time) || !FieldVerifier.isValidText(msg.title) || !FieldVerifier.isValidText(msg.text))
		return false;
		
		return true;
	}
	
	public static boolean isValidDelivery(Delivery del){
		return false;
	}
	
}
