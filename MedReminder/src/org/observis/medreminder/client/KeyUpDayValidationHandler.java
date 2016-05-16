package org.observis.medreminder.client;

import org.observis.medreminder.shared.FieldVerifier;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class KeyUpDayValidationHandler implements KeyUpHandler{

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getSource() instanceof CustomTextBox) {
			CustomTextBox cur = (CustomTextBox) event.getSource();
			if (FieldVerifier.isValidDay(cur.getText())) {
				((CustomTextBox) event.getSource()).setStyleName("gwt-TextBox");
			} else {
				((CustomTextBox) event.getSource())
						.setStyleName("error-validation");
			}
		}
		
	}

}
