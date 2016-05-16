package org.observis.medreminder.client;

import org.observis.medreminder.shared.FieldVerifier;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

public class CustomTextBox extends TextBox {
	HandlerRegistration boxValidationHandlerReg;

	BoxType boxType;

	public CustomTextBox(BoxType type) {
		switch (type) {
		case PHONEBOX:
			this.boxType = BoxType.PHONEBOX;
			break;
		case DAYBOX:
			this.boxType = BoxType.DAYBOX;
			break;
		case TEXTBOX:
			this.boxType = BoxType.TEXTBOX;
			break;
		case TITLEBOX:
			this.boxType = BoxType.TITLEBOX;
			break;
		case HOURBOX:
			this.boxType = BoxType.HOURBOX;
			break;
		case MINUTEBOX:
			this.boxType = BoxType.MINUTEBOX;
			break;
		}
	}

	void addValidationHandler() {
		if (boxValidationHandlerReg == null) {
			String toValidate = this.getText();
			switch (boxType) {
			case PHONEBOX:
				if (!FieldVerifier.isValidPhone(toValidate))
					this.setStyleName("error-validation");
				boxValidationHandlerReg = this
						.addKeyUpHandler(new KeyUpPhoneValidationHandler());
				break;
			case DAYBOX:
				if (!FieldVerifier.isValidDay(toValidate))
					this.setStyleName("error-validation");
				boxValidationHandlerReg = this
						.addKeyUpHandler(new KeyUpDayValidationHandler());
				break;
			case TEXTBOX:
				if (!FieldVerifier.isValidText(toValidate))
					this.setStyleName("error-validation");
				boxValidationHandlerReg = this
						.addKeyUpHandler(new KeyUpTextValidationHandler());
				break;
			case TITLEBOX:
				if (!FieldVerifier.isValidText(toValidate))
					this.setStyleName("error-validation");
				boxValidationHandlerReg = this
						.addKeyUpHandler(new KeyUpTextValidationHandler());
				break;
			case HOURBOX:
				if (!FieldVerifier.isValidHour(toValidate))
					this.setStyleName("error-validation");
				boxValidationHandlerReg = this
						.addKeyUpHandler(new KeyUpHourValidationHandler());
				break;
			case MINUTEBOX:
				if (!FieldVerifier.isValidMinute(toValidate))
					this.setStyleName("error-validation");
				boxValidationHandlerReg = this
						.addKeyUpHandler(new KeyUpMinuteValidationHandler());
				break;
			}
		}
	}

	void removeValidationHandler() {
		if (boxValidationHandlerReg != null) {
			boxValidationHandlerReg.removeHandler();
			boxValidationHandlerReg = null;
		}
	}
}
