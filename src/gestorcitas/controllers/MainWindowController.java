/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers;

import gestorcitas.controllers.factories.ImageCellFactory;
import gestorcitas.controllers.factories.FormattedDateTimeCellFactory;
import gestorcitas.controllers.factories.PersonCellFactory;
import DBAccess.ClinicDBAccess;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;
import model.Patient;
import model.Person;

/**
 * FXML Controller class
 */
public class MainWindowController implements Initializable {

    private ResourceBundle rb;
    private final ClinicDBAccess clinic = ClinicDBAccess.getSingletonClinicDBAccess();

    private ObservableList<Patient> patients;
    private ObservableList<Appointment> appointments;
    private ObservableList<Doctor> doctors;

    @FXML private ToggleGroup language;
    @FXML private TabPane mainTabPane;
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
    @FXML private TableColumn<Person,String> patientNameColumn;
    @FXML private TableColumn<Person,String> patientPhoneColumn;

    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Person,Image> doctorPhotoColumn;
    @FXML private TableColumn<Person,String> doctorIdColumn;
    @FXML private TableColumn<Person,String> doctorNameColumn;
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
        String clinicName = clinic.getClinicName();
        appointmentTabTitle.setText(clinicName);
        patientTabTitle.setText(clinicName);
        doctorTabTitle.setText(clinicName);

        // Retrieve data from database
        appointments = FXCollections.observableArrayList(clinic.getAppointments());
        appointmentTable.setItems(appointments);
        patients = FXCollections.observableArrayList(clinic.getPatients());
        patientTable.setItems(patients);
        doctors = FXCollections.observableArrayList(clinic.getDoctors());
        doctorTable.setItems(doctors);

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
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        appointmentDateColumn.setCellFactory(new FormattedDateTimeCellFactory<>(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        appointmentTimeColumn.setCellFactory(new FormattedDateTimeCellFactory<>(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        appointmentDoctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        appointmentDoctorColumn.setCellFactory(new PersonCellFactory<>());
        appointmentPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        appointmentPatientColumn.setCellFactory(new PersonCellFactory<>());

        patientPhotoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        patientPhotoColumn.setCellFactory(new ImageCellFactory<>());
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephon"));

        doctorPhotoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        doctorPhotoColumn.setCellFactory(new ImageCellFactory<>());
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        doctorPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephon"));
        doctorVisitDaysColumn.setCellValueFactory(new PropertyValueFactory<>("visitDays"));
        doctorVisitTimeColumn.setCellValueFactory(column -> {
            Doctor doctor = column.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
            String start = formatter.format(doctor.getVisitStartTime());
            String end = formatter.format(doctor.getVisitEndTime());
            return new SimpleStringProperty(start + "-" + end);
        });
    }

    public void saveDBAndQuit() {
        clinic.saveDB(); // TODO: Handle error
        Platform.exit();
        System.exit(0);
    }

    @FXML private void onChangeName(ActionEvent event) {
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
    }

    @FXML private void onRemoveAppointment(ActionEvent event) {
    }

    @FXML private void onShowPatient(ActionEvent event) {
    }

    @FXML private void onRemovePatient(ActionEvent event) {
    }

    @FXML private void onShowDoctor(ActionEvent event) {
    }

    @FXML private void onRemoveDoctor(ActionEvent event) {
    }

    @FXML private void onSave(ActionEvent event) {
    }

}
