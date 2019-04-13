/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import gestorcitas.controllers.base.BasePersonFormController;
import javafx.event.ActionEvent;
import model.Patient;

public class PatientFormController extends BasePersonFormController<Patient> {
 
    @Override
    protected void onSaveValidated(ActionEvent event) {
        persons.add(new Patient(
                id.getText(),
                name.getText(),
                surname.getText(),
                phone.getText(),
                photo.getValue()
        ));
    }
    
}
