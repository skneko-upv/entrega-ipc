/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ValidatedTextField {
    
    public static final TextFieldValidator getNotEmptyValidator(ResourceBundle rb) {
        return value -> {
            if (value.equals("")) return rb.getString("form.generic.error.empty");
            else return null;
        };
    }
    
    private static final String CSS_ERROR_CLASS = "input-error";
    
    private final TextField field;
    private final Label errorLabel;
    
    private boolean isError;
    private List<TextFieldValidator> validators;
    
    @FunctionalInterface
    public interface TextFieldValidator extends Function<String,String> {}
    
    public ValidatedTextField(TextField field, Label errorLabel) {
        this(field, errorLabel, 0);
    }
    
    public ValidatedTextField(TextField field, Label errorLabel, int maxLength) {
        this(field, errorLabel, maxLength, true);
    }
    
    public ValidatedTextField(TextField field, Label errorLabel, int maxLength, boolean autoValidate) {
        this.field = field;
        this.errorLabel = errorLabel;
        this.validators = new ArrayList<>();
        
        if (maxLength > 0) {
            field.textProperty().addListener(new TextLimiterListener(field, maxLength));
        }
        if (autoValidate) {
            field.focusedProperty().addListener((val, oldVal, newVal) -> {
                if (oldVal && !newVal) {  // exited focus
                    validate();
                }
            });
        }
    }
    
    public TextField getField() { return field; }
    
    public Label getErrorLabel() { return errorLabel; }
    
    public String getText() { return field.getText(); }
    
    public void setText(String text) { field.setText(text); }
    
    public void setEditable(boolean editable) { field.setEditable(editable); }
    
    public boolean isError() { return isError; }
    
    public void addValidator(TextFieldValidator validator) { this.validators.add(validator); }
    
    public boolean validate() {
        return validate(true);
    }
    
    public boolean validate(boolean setErrorOnFail) {
        String error = null;
        for (TextFieldValidator validator : validators) {
            error = validator.apply(getText());
            if (error != null) break;
        }
        if (error != null) {
            if (setErrorOnFail) setError(error);
            return false;
        } else {
            setClear();
            return true;
        }
    }
    
    public void setError(String message) {
        isError = true;
        field.getStyleClass().add(CSS_ERROR_CLASS);
        errorLabel.setVisible(true);
        errorLabel.setText(message);
    }
    
    public void setClear() {
        isError = false;
        field.getStyleClass().remove("input-error");
        errorLabel.setVisible(false);
    }
}
