package org.observis.medreminder.client;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class KeyUpValidationHandler implements KeyUpHandler{

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if(event.getSource() instanceof TextBox){
		TextBox ourBox  =  (TextBox)event.getSource();
		((TextBox)event.getSource()).setStyleName("error-validation");
		}
	}
	
	
	
}
