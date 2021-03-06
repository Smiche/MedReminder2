package org.observis.medreminder.client;

import org.observis.medreminder.shared.FieldVerifier;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class KeyUpTextValidationHandler implements KeyUpHandler{

	@Override
	public void onKeyUp(KeyUpEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() instanceof CustomTextBox) {
			CustomTextBox cur = (CustomTextBox) event.getSource();
			if (FieldVerifier.isValidText(cur.getText())) {
				((CustomTextBox) event.getSource()).setStyleName("gwt-TextBox");
			} else {
				((CustomTextBox) event.getSource())
						.setStyleName("error-validation");
			}
		}
		if (event.getSource() instanceof CustomTextArea) {
			CustomTextArea cur = (CustomTextArea) event.getSource();
			if (FieldVerifier.isValidText(cur.getText())) {
				((CustomTextArea) event.getSource()).setStyleName("gwt-TextBox");
			} else {
				((CustomTextArea) event.getSource())
						.setStyleName("error-validation");
			}
		}
	}

}
