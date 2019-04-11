/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
        launchForm(true, null);
    }
    
    @FXML @Override
    protected void onShow(ActionEvent event) {
        int index = itemsFiltered.getSourceIndex(
                table.getSelectionModel().getSelectedIndex()
        );
        launchForm(false, items.get(index));
    }
    
    private void launchForm(boolean editMode, Person prefill) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestorcitas/views/PatientForm.fxml"), rb);
            Parent formRoot = (Parent)loader.load();

            PatientFormController form = loader.<PatientFormController>getController();
            form.setData(editMode, prefill, items);
            
            Scene scene = new Scene(formRoot);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(rb.getString(
                    "modal.patientForm.title." + (editMode ? "add" : "show")
            ));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) { System.err.println(e); /* TODO */ }
    } 
    
}
