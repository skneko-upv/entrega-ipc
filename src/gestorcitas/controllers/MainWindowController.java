/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers;

import gestorcitas.controllers.helpers.*;
import DBAccess.ClinicDBAccess;
import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.*;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import model.*;

/**
 * FXML Controller class
 */
public class MainWindowController implements Initializable {
    private ResourceBundle rb;
    private final ClinicDBAccess clinic = ClinicDBAccess.getSingletonClinicDBAccess();

    private ObservableList<Appointment> appointments;
    private FilteredList<Appointment> appointmentsFiltered;
    
    private ObservableList<Patient> patients;
    private FilteredList<Patient> patientsFiltered;
    
    private ObservableList<Doctor> doctors;
    private FilteredList<Doctor> doctorsFiltered;

    @FXML private ToggleGroup language;
    @FXML private TabPane mainTabPane;
    @FXML private Tab appointmentTab;
    @FXML private Tab patientTab;
    @FXML private Tab doctorTab;
    @FXML private TextField appointmentSearchBox;
    @FXML private TextField patientSearchBox;
    @FXML private TextField doctorSearchBox;
    @FXML private Button showPatientBtn;
    @FXML private Button showDoctorBtn;
    @FXML private Button removeAppointmentBtn;
    @FXML private Button removePatientBtn;
    @FXML private Button removeDoctorBtn;
    @FXML private Label appointmentTabTitle;
    @FXML private Label patientTabTitle;
    @FXML private Label doctorTabTitle;

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment,LocalDateTime> appointmentDateColumn;
    @FXML private TableColumn<Appointment,LocalDateTime> appointmentTimeColumn;
    @FXML private TableColumn<Appointment,Person> appointmentDoctorColumn;
    @FXML private TableColumn<Appointment,Person> appointmentPatientColumn;

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Person,Image> patientPhotoColumn;
    @FXML private TableColumn<Person,String> patientIdColumn;
    @FXML private TableColumn<Person,Person> patientNameColumn;
    @FXML private TableColumn<Person,String> patientPhoneColumn;

    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Person,Image> doctorPhotoColumn;
    @FXML private TableColumn<Person,String> doctorIdColumn;
    @FXML private TableColumn<Person,Person> doctorNameColumn;
    @FXML private TableColumn<Person,String> doctorPhoneColumn;
    @FXML private TableColumn<Doctor,ArrayList<Days>> doctorVisitDaysColumn;
    @FXML private TableColumn<Doctor,String> doctorVisitTimeColumn;
    @FXML private TableColumn<Doctor,ExaminationRoom> doctorRoomColumn;

    /**
     * Initializes the controller class.
     *
     * @param url Current controller location.
     * @param rb Resources used to localize this controller.
     */
    @Override
    public void initialize(@SuppressWarnings("unused") URL url, ResourceBundle rb) {
        this.rb = rb;

        // Initialize labels
        refreshName();

        // Retrieve data from database
        loadFromDB();

        // Set up event listeners
        appointmentTable.getSelectionModel().selectedItemProperty().addListener((val, oldVal, newVal) -> {
            boolean noneSelected = newVal == null;
            removeAppointmentBtn.setDisable(noneSelected);
        });
        patientTable.getSelectionModel().selectedItemProperty().addListener((val, oldVal, newVal) -> {
            boolean noneSelected = newVal == null;
            showPatientBtn.setDisable(noneSelected);
            removePatientBtn.setDisable(noneSelected);
        });
        doctorTable.getSelectionModel().selectedItemProperty().addListener((val, oldVal, newVal) -> {
            boolean noneSelected = newVal == null;
            showDoctorBtn.setDisable(noneSelected);
            removeDoctorBtn.setDisable(noneSelected);
        });

        // Set up tables
        TableViewHelper.setupAppointmentTable(
                appointmentDateColumn, 
                appointmentTimeColumn, 
                appointmentDoctorColumn, 
                appointmentPatientColumn
        );
        TableViewHelper.setupPatientTable(
                patientPhotoColumn, 
                patientIdColumn, 
                patientNameColumn, 
                patientPhoneColumn
        );
        TableViewHelper.setupDoctorTable(
                doctorPhotoColumn,
                doctorIdColumn,
                doctorNameColumn,
                doctorPhoneColumn,
                doctorVisitDaysColumn,
                doctorVisitTimeColumn,
                doctorRoomColumn,
                rb
        );
        
    }

    public void saveDBAndQuit() {
        DatabaseHelper.save(clinic, rb);
        Platform.exit();
        System.exit(0);
    }
    
    private void refreshName() {
        String clinicName = clinic.getClinicName();
        appointmentTabTitle.setText(clinicName);
        patientTabTitle.setText(clinicName);
        doctorTabTitle.setText(clinicName);
    }
    
    private void loadFromDB() {
        appointments = FXCollections.observableArrayList(clinic.getAppointments());
        appointmentsFiltered = appointments.filtered(e -> true);
        appointmentTable.setItems(appointmentsFiltered);
        
        patients = FXCollections.observableArrayList(clinic.getPatients());
        patientsFiltered = patients.filtered(e -> true);
        patientTable.setItems(patientsFiltered);
        
        doctors = FXCollections.observableArrayList(clinic.getDoctors());
        doctorsFiltered = doctors.filtered(e -> true);
        doctorTable.setItems(doctorsFiltered);
    }

    @FXML private void onChangeName(ActionEvent event) {
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

    @FXML private void onQuit(ActionEvent event) {
        saveDBAndQuit();
    }

    @FXML private void onAddAppointment(ActionEvent event) {
    }

    @FXML private void onAddPatient(ActionEvent event) {
    }

    @FXML private void onAddDoctor(ActionEvent event) {
    }

    @FXML private void onAbout(ActionEvent event) {
        Alert about = new Alert(AlertType.INFORMATION);
        about.setTitle(rb.getString("modal.about.title"));
        about.setHeaderText(null); about.setGraphic(null);
        about.setContentText(rb.getString("modal.about.content"));
        about.showAndWait();
    }

    @FXML private void onRemoveAppointment(ActionEvent event) {
        int index = appointmentTable.getSelectionModel().getSelectedIndex();
        ItemRemoveHelper.removeAppointment(appointments, index, rb);
    }

    @FXML private void onShowPatient(ActionEvent event) {
    }

    @FXML private void onRemovePatient(ActionEvent event) {
        int index = patientTable.getSelectionModel().getSelectedIndex();
        ItemRemoveHelper.removePatient(appointments, patients, index, rb);
    }

    @FXML private void onShowDoctor(ActionEvent event) {
    }

    @FXML private void onRemoveDoctor(ActionEvent event) {
        int index = doctorTable.getSelectionModel().getSelectedIndex();
        ItemRemoveHelper.removeDoctor(appointments, doctors, index, rb);
    }

    @FXML private void onSave(ActionEvent event) {
        DatabaseHelper.save(clinic, rb);
    }
    
    @FXML private void onDiscardChanges(ActionEvent event) {
        ButtonType discardYes = new ButtonType(rb.getString("modal.discard.btn.yes"), ButtonData.YES);
        Alert discard = new Alert(AlertType.WARNING, rb.getString("modal.discard.content"), discardYes, ButtonType.NO);
        discard.setTitle(rb.getString("modal.discard.title"));
        discard.setHeaderText(rb.getString("modal.discard.header"));
        
        Optional<ButtonType> discardResult = discard.showAndWait();
        if (discardResult.isPresent() && discardResult.get() == discardYes) {
            loadFromDB();
        }
    }
    
    @FXML private void onSearch(ActionEvent event) {
        Tab selected = mainTabPane.getSelectionModel().getSelectedItem();
        String query;
        if (selected == appointmentTab) {
            query = appointmentSearchBox.getText();
            appointmentsFiltered.setPredicate(e -> {
                    Predicate<Person> predicate = new PersonSearchPredicate(query);
                    return predicate.test(e.getDoctor()) || predicate.test(e.getPatient());
            });
        } else if (selected == patientTab) {
            query = patientSearchBox.getText();
            patientsFiltered.setPredicate(new PersonSearchPredicate(query));
        } else if (selected == doctorTab) {
            query = doctorSearchBox.getText();
            doctorsFiltered.setPredicate(new PersonSearchPredicate(query));
        }
    }

}
