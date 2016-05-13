

package org.observis.medreminder.scheduleworker;
 
import java.util.ArrayList;
import java.util.List;
 
 
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
 
public class MedReminderWorker {
    /* Find your sid and token at twilio.com/user/account */
	static String ACCOUNT_SID = "AC7ccc251221e4bf99c4f156bbb02fadbc";
	static String AUTH_TOKEN = "3e7c5bc95be9a64cc8ac0c478e7a75ec";
	static String SRC_PHONE = "";
	
	public static void setGatewayAuth(String sid, String token, String phone){
		ACCOUNT_SID = sid;
		AUTH_TOKEN = token;
		SRC_PHONE = phone;
	}
    public static void sendSms(String text, String phone) throws TwilioRestException {
 
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
 
        Account account = client.getAccount();
 
        MessageFactory messageFactory = account.getMessageFactory();	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", phone)); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("From", SRC_PHONE)); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("Body",text));
        Message sms = messageFactory.create(params);
    }
}