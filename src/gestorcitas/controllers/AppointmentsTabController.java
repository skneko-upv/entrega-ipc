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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class AppointmentsTabController implements Initializable {

    @FXML
    private Label appointmentTabTitle;
    @FXML
    private TextField appointmentSearchBox;
    @FXML
    private TableView<?> appointmentTable;
    @FXML
    private TableColumn<?, ?> appointmentDateColumn;
    @FXML
    private TableColumn<?, ?> appointmentTimeColumn;
    @FXML
    private TableColumn<?, ?> appointmentDoctorColumn;
    @FXML
    private TableColumn<?, ?> appointmentPatientColumn;
    @FXML
    private Button removeAppointmentBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onSearch(ActionEvent event) {
    }

    @FXML
    private void onAddAppointment(ActionEvent event) {
    }

    @FXML
    private void onRemoveAppointment(ActionEvent event) {
    }
    
}
