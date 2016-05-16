package org.observis.medreminder.client;

import org.observis.medreminder.shared.FieldVerifier;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class KeyUpPhoneValidationHandler implements KeyUpHandler {

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getSource() instanceof CustomTextBox) {
			if (FieldVerifier.isValidPhone(((CustomTextBox) event.getSource())
					.getText())) {
				((CustomTextBox) event.getSource())
				.setStyleName("gwt-TextBox");
			} else {
				((CustomTextBox) event.getSource())
						.setStyleName("error-validation");
			}
		}		
	}

}
