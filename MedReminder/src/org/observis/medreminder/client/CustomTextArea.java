package org.observis.medreminder.client;

import org.observis.medreminder.shared.FieldVerifier;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextArea;

public class CustomTextArea extends TextArea{
	HandlerRegistration areaHandlerReg;
	
	public CustomTextArea(){
		this.setCharacterWidth(22);
		this.setVisibleLines(5);
	}
	
	void addValidation(){
		if(!FieldVerifier.isValidText(this.getText()))this.setStyleName("error-validation");
		if(areaHandlerReg == null){
			areaHandlerReg = this.addKeyUpHandler(new KeyUpTextValidationHandler());
		}
	}
	
	void reset(){
		if(areaHandlerReg!=null){
			areaHandlerReg.removeHandler();
			areaHandlerReg = null;
		}
		this.setText("");
	}
	
}
