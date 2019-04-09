/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Patient;
import model.Person;

public class PatientsTabController extends PersonsTabController {
    
    @Override
    public String getSummary(Person person) {
        return rb.getString("generic.patient") + " " + person.getIdentifier();
    }

    /**
     * Initializes the controller class.
     *
     * @param url Current controller location.
     * @param rb Resources used to localize this controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url,rb);
        
        ObservableList<Patient> loadedItems = 
                FXCollections.observableArrayList(clinic.getPatients());
        setItems(loadedItems);
    }
    
    @FXML @Override
    protected void onShow(ActionEvent event) {
        // TODO
    }

    @FXML @Override
    protected void onAdd(ActionEvent event) {
        // TODO
    }
    
}
