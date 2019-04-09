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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import model.Appointment;
import model.Person;

public class MainWindowController implements Initializable {
    
    private ResourceBundle rb;
    private final ClinicDBAccess clinic = ClinicDBAccess.getSingletonClinicDBAccess();
    
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        
        appointmentsTabController.setMainController(this);
        patientsTabController.setMainController(this);
        doctorsTabController.setMainController(this);
        
        refreshName();
    }
    
    public <T extends Person> FilteredList<Appointment> getRemoveConflicts(
            T toDelete,
            Function<Appointment,T> personValueFactory
    ) {
        ObservableList<Appointment> appointments = appointmentsTabController.getItems();
        return appointments.filtered(appointment -> {
            return personValueFactory.apply(appointment)
                    .getIdentifier()
                    .equals(toDelete.getIdentifier());
        });
    }
    
    public void saveDBAndQuit() {
        DatabaseHelper.save(clinic, rb);
        Platform.exit();
        System.exit(0);
    }
    
    private void refreshName() {
        String clinicName = clinic.getClinicName();
        appointmentsTabController.setTitle(clinicName);
        patientsTabController.setTitle(clinicName);
        doctorsTabController.setTitle(clinicName);
    }

    @FXML 
    private void onChangeName(ActionEvent event) {
        String unnamed = rb.getString("app.default.clinicName");
        
        TextInputDialog setName = new TextInputDialog();
        setName.setTitle(rb.getString("modal.setName.title"));
        setName.setHeaderText(null); setName.setGraphic(null);
        setName.setContentText(rb.getString("modal.setName.content"));
        setName.getEditor().setPromptText(unnamed);
        
        Optional<String> setNameResult = setName.showAndWait();
        setNameResult.ifPresent(name -> {
            if (!setNameResult.get().equals("")) { clinic.setClinicName(name); }
            else { clinic.setClinicName(unnamed); }
            refreshName();
        }); 
    }

    @FXML
    private void onSave(ActionEvent event) {
        DatabaseHelper.save(clinic, rb);
    }

    @FXML 
    private void onDiscardChanges(ActionEvent event) {
        ButtonType discardYes = new ButtonType(rb.getString("modal.discard.btn.yes"), ButtonData.YES);
        Alert discard = new Alert(AlertType.WARNING, rb.getString("modal.discard.content"), discardYes, ButtonType.NO);
        discard.setTitle(rb.getString("modal.discard.title"));
        discard.setHeaderText(rb.getString("modal.discard.header"));
        
        Optional<ButtonType> discardResult = discard.showAndWait();
        if (discardResult.isPresent() && discardResult.get() == discardYes) {
            appointmentsTabController.loadDataFromDB();
            patientsTabController.loadDataFromDB();
            doctorsTabController.loadDataFromDB();
        }
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
