package org.observis.medreminder.client;
import javax.validation.Validator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

public class MyValidatorFactory extends AbstractGwtValidatorFactory {

    /**
     * Only the classes listed in the {@link GwtValidation} annotation can be validated.
     * In my example: Link.class can be validated
     */
    @GwtValidation(value = { PackageHolder.class ,PatientHolder.class,MedReminder.class, Delivery.class })
    public interface GwtValidator extends Validator {
    	
    }

    @Override
    public AbstractGwtValidator createValidator() {
        return GWT.create(GwtValidator.class);
    }

}