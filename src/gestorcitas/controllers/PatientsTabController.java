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

public class PatientsTabController implements Initializable {

    @FXML
    private Label patientTabTitle;
    @FXML
    private TextField patientSearchBox;
    @FXML
    private TableView<?> patientTable;
    @FXML
    private TableColumn<?, ?> patientPhotoColumn;
    @FXML
    private TableColumn<?, ?> patientIdColumn;
    @FXML
    private TableColumn<?, ?> patientNameColumn;
    @FXML
    private TableColumn<?, ?> patientPhoneColumn;
    @FXML
    private Button showPatientBtn;
    @FXML
    private Button removePatientBtn;

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
    private void onShowPatient(ActionEvent event) {
    }

    @FXML
    private void onAddPatient(ActionEvent event) {
    }

    @FXML
    private void onRemovePatient(ActionEvent event) {
    }
    
}
