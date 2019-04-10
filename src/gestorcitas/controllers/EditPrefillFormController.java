/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class EditPrefillFormController<T> implements Initializable {
    
    protected boolean editMode;
    protected T prefill;
    
    protected ResourceBundle rb;
    
    @FXML Button saveBtn;
    @FXML Button cancelBtn;
    
    protected void initialize(boolean editMode, T prefill) {
        this.editMode = editMode;
        this.prefill = prefill;
        if (this.prefill != null) prefill();
        if (!this.editMode) {
            setUneditableAll();
            saveBtn.setVisible(false);
            cancelBtn.setText(rb.getString("generic.ok"));
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }
    
    protected abstract void setClearAll();
    protected abstract void setUneditableAll();
    protected abstract void prefill();
    protected abstract boolean validateAll();
    
    protected abstract void onSaveValidated(ActionEvent event);
    
    @FXML
    private void onSave(ActionEvent event) {
        if (validateAll()) onSaveValidated(event);
    }
    
    @FXML
    protected void onCancel(ActionEvent event) {
        ((Stage)cancelBtn.getScene().getWindow()).close();
    }
}
