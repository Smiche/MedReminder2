package org.observis.medreminder.client;

import org.observis.medreminder.shared.FieldVerifier;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class KeyUpHourValidationHandler implements KeyUpHandler{

	@Override
	public void onKeyUp(KeyUpEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() instanceof CustomTextBox) {
			CustomTextBox cur = (CustomTextBox) event.getSource();
			if (FieldVerifier.isValidHour(cur.getText())) {
				((CustomTextBox) event.getSource()).setStyleName("gwt-TextBox");
			} else {
				((CustomTextBox) event.getSource())
						.setStyleName("error-validation");
			}
		}
	}

}
