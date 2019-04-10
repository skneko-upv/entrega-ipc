/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers.helpers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextLimiterListener implements ChangeListener<String> {
    
    private TextField field;
    private int maxLength;
    
    public TextLimiterListener(TextField field, int maxLength) {
        this.field = field;
        this.maxLength = maxLength;
    }
    
    public void changed(ObservableValue<? extends String> val, String oldVal, String newVal) {
        if (newVal.length() > maxLength) {
            String s = newVal.substring(0, maxLength);
            field.setText(s);
        }
    }
}
