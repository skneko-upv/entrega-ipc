/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import DBAccess.ClinicDBAccess;
import gestorcitas.controllers.helpers.DatabaseHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;

public class MainWindowController implements Initializable {
    
    private ResourceBundle rb;
    private ClinicDBAccess clinic = ClinicDBAccess.getSingletonClinicDBAccess();
    
    @FXML private AppointmentsTabController appointmentsTabController;
    @FXML private PatientsTabController patientsTabController;
    @FXML private DoctorsTabController doctorsTabController;

    @FXML
    private ToggleGroup language;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab appointmentTab;
    @FXML
    private Tab patientTab;
    @FXML
    private Tab doctorTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        // TODO
    }
    
    public void saveDBAndQuit() {
        DatabaseHelper.save(clinic, rb);
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void onChangeName(ActionEvent event) {
    }

    @FXML
    private void onSave(ActionEvent event) {
    }

    @FXML
    private void onDiscardChanges(ActionEvent event) {
    }

    @FXML
    private void onQuit(ActionEvent event) {
    }

    @FXML
    private void onAddAppointment(ActionEvent event) {
    }

    @FXML
    private void onAddPatient(ActionEvent event) {
    }

    @FXML
    private void onAddDoctor(ActionEvent event) {
    }

    @FXML
    private void onAbout(ActionEvent event) {
    }
    
}
