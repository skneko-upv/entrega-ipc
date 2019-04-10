/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import gestorcitas.controllers.helpers.ValidatedTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Patient;
import model.Person;

public class PatientFormController extends EditPrefillFormController<Person> {
    
    public static final int MAX_NAME_LENGTH = 20;
    public static final int MAX_SURNAME_LENGTH = 40;
    public static final int MAX_ID_LENGTH = 9;
    public static final int MAX_PHONE_LENGTH = 15;
    
    private ObservableList<Patient> patients;
    
    private ValidatedTextField name;
    private ValidatedTextField surname;
    private ValidatedTextField id;
    private ValidatedTextField phone;
    
    @FXML private TextField nameField;
    @FXML private Label nameErrorLabel;
    @FXML private TextField surnameField;
    @FXML private Label surnameErrorLabel;
    @FXML private TextField idField;
    @FXML private Label idErrorLabel;
    @FXML private TextField phoneField;
    @FXML private Label phoneErrorLabel;
    @FXML private ImageView photoPreview;
    @FXML private Button photoChangeBtn;
    
    /**
     * Initializes the controller class.
     *
     * @param url Current controller location.
     * @param rb Resources used to localize this controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        // Set up form field constraints
        name = new ValidatedTextField(nameField, nameErrorLabel, MAX_NAME_LENGTH);
        surname = new ValidatedTextField(surnameField, surnameErrorLabel, MAX_SURNAME_LENGTH);
        id = new ValidatedTextField(idField, idErrorLabel, MAX_ID_LENGTH);
        phone = new ValidatedTextField(phoneField, phoneErrorLabel, MAX_PHONE_LENGTH);
        
        name.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        surname.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        id.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        phone.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        phone.addValidator(value -> {
            if (!value.matches("[0-9]+")) {
                return rb.getString("form.person.error.phoneNotOnlyNumbers");
            } else return null;
        });
    }
    
    public void initialize(boolean editMode, Person prefill, ObservableList<Patient> patients) {
        super.initialize(editMode, prefill);
        this.patients = patients;
    }
    
    @Override
    protected void setClearAll() {
        name.setClear();
        surname.setClear();
        id.setClear();
        phone.setClear();
    }
    
    @Override
    protected boolean validateAll() {
        return name.validate()
                && surname.validate()
                && id.validate()
                && phone.validate();
    }
    
    @Override 
    protected void prefill() {
        name.setText(prefill.getName());
        surname.setText(prefill.getSurname());
        id.setText(prefill.getIdentifier());
        phone.setText(prefill.getTelephon());
    }
    
    @Override
    protected void setUneditableAll() {
        name.setEditable(false);
        surname.setEditable(false);
        id.setEditable(false);
        phone.setEditable(false);
        photoChangeBtn.setVisible(false);
    }
    
    @FXML @Override
    protected void onSaveValidated(ActionEvent event) {
        // TODO
    }
}
