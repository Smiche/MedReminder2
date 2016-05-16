package org.observis.medreminder.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

public class CustomTextBox extends TextBox{
	HandlerRegistration boxValidationHandlerReg;
	public static enum BoxEnum {
	   PHONEBOX,DAYBOX,TEXTBOX,TITLEBOX,HOURBOX,MINUTEBOX 
	}
	
	BoxEnum boxType;
	
	public CustomTextBox(BoxEnum type){
		switch(type){
		case PHONEBOX:
			this.boxType = BoxEnum.PHONEBOX;
			break;
		case DAYBOX:
			this.boxType = BoxEnum.DAYBOX;
			break;
		case TEXTBOX:
			this.boxType = BoxEnum.TEXTBOX;
			break;
		case TITLEBOX:
			this.boxType = BoxEnum.TITLEBOX;
			break;
		case HOURBOX: 
			this.boxType = BoxEnum.HOURBOX;
			break;
		case MINUTEBOX:
			this.boxType = BoxEnum.MINUTEBOX;
			break;
		}
	}
	
	void addValidationHandler(){
		if(boxValidationHandlerReg == null)
		switch(boxType){
		case PHONEBOX:
				boxValidationHandlerReg = this.addKeyUpHandler(new KeyUpPhoneValidationHandler());
			break;
		case DAYBOX:
			boxValidationHandlerReg = this.addKeyUpHandler(new KeyUpDayValidationHandler());
			break;
		case TEXTBOX:
			boxValidationHandlerReg = this.addKeyUpHandler(new KeyUpTextValidationHandler());
			break;
		case TITLEBOX:
			boxValidationHandlerReg = this.addKeyUpHandler(new KeyUpTextValidationHandler());
			break;
		case HOURBOX:
			boxValidationHandlerReg = this.addKeyUpHandler(new KeyUpHourValidationHandler());
			break;
		case MINUTEBOX:
			boxValidationHandlerReg = this.addKeyUpHandler(new KeyUpMinuteValidationHandler());
			break;
		}
	}
	
	void removeValidationHandler(){
		if(boxValidationHandlerReg!=null){
			boxValidationHandlerReg.removeHandler();
			boxValidationHandlerReg = null;
		}
	}
}
