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

public class DoctorsTabController implements Initializable {

    @FXML
    private Label doctorTabTitle;
    @FXML
    private TextField doctorSearchBox;
    @FXML
    private TableView<?> doctorTable;
    @FXML
    private TableColumn<?, ?> doctorPhotoColumn;
    @FXML
    private TableColumn<?, ?> doctorIdColumn;
    @FXML
    private TableColumn<?, ?> doctorNameColumn;
    @FXML
    private TableColumn<?, ?> doctorPhoneColumn;
    @FXML
    private TableColumn<?, ?> doctorVisitDaysColumn;
    @FXML
    private TableColumn<?, ?> doctorVisitTimeColumn;
    @FXML
    private TableColumn<?, ?> doctorRoomColumn;
    @FXML
    private Button showDoctorBtn;
    @FXML
    private Button removeDoctorBtn;

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
    private void onShowDoctor(ActionEvent event) {
    }

    @FXML
    private void onAddDoctor(ActionEvent event) {
    }

    @FXML
    private void onRemoveDoctor(ActionEvent event) {
    }
    
}
