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
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Patient;
import model.Person;

public class PatientsTabController extends PersonsTabController {
    
    @Override
    public String getSummary(Person person) {
        return rb.getString("generic.patient") + " " + person.getIdentifier();
    }
    
    @Override
    public void loadDataFromDB() {
        ObservableList<Patient> loadedItems = 
                FXCollections.observableArrayList(clinic.getPatients());
        setItems(loadedItems);
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
        
        loadDataFromDB();
    }
    
    @Override
    protected Function<Appointment,Person> getAppointmentValueFactory() {
        return Appointment::getPatient;
    }
    
    @FXML @Override
    public void onAdd(ActionEvent event) {
        // TODO
    }
    
    @FXML @Override
    protected void onShow(ActionEvent event) {
        // TODO
    }
    
}
