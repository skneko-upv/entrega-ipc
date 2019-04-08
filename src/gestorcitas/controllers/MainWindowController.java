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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
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
        loadDB();

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
        doctorVisitDaysColumn.setCellFactory(column -> {
            TableCell<Doctor,ArrayList<Days>> cell = new TableCell<Doctor,ArrayList<Days>>() {
                @Override
                protected void updateItem(ArrayList<Days> available, boolean empty) {
                    if (empty || available == null) setText(null);
                    else {
                        StringBuilder result = new StringBuilder();
                        int i = 0;
                        for (Days day : Days.values()) {
                            if (available.contains(day)) { result.append(rb.getString("generic.weekday.initial." + i)); }
                            else { result.append("-"); }
                            i++;
                        }
                        setText(result.toString());
                    }
                }
            };
            cell.setStyle("-fx-font-family: monospace");
            return cell;
        });
        doctorVisitTimeColumn.setCellValueFactory(data -> {
            Doctor doctor = data.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
            String start = formatter.format(doctor.getVisitStartTime());
            String end = formatter.format(doctor.getVisitEndTime());
            return new SimpleStringProperty(start + "-" + end);
        });
        doctorRoomColumn.setCellValueFactory(new PropertyValueFactory<>("examinationRoom"));
        doctorRoomColumn.setCellFactory(column -> {
            TableCell<Doctor,ExaminationRoom> cell = new TableCell<Doctor,ExaminationRoom>() {
                @Override
                protected void updateItem(ExaminationRoom room, boolean empty) {
                    if (empty || room == null) setText(null);
                    else setText(String.valueOf(room.getIdentNumber()));
                }
            };
            return cell;
        });
    }
    
    public void saveDB() {
        boolean retry = false;
        do {
            Alert saveWait = new Alert(AlertType.INFORMATION);
            saveWait.setTitle(rb.getString("generic.wait"));
            saveWait.setHeaderText(rb.getString("modal.saveWait.header"));
            saveWait.setContentText(rb.getString("modal.saveWait.content"));
            saveWait.getButtonTypes().clear();
            saveWait.show();

            boolean success = clinic.saveDB();

            saveWait.setResult(ButtonType.OK);
            saveWait.close();
            if (!success) {
                ButtonType saveFailRetry = new ButtonType(rb.getString("generic.retry"), ButtonData.OK_DONE);
                ButtonType saveFailDesist = new ButtonType(rb.getString("generic.cancel"), ButtonData.CANCEL_CLOSE);
                Alert saveFail = new Alert(AlertType.ERROR, rb.getString("modal.saveFail.content"), saveFailRetry, saveFailDesist);
                saveFail.setTitle(rb.getString("generic.error"));
                saveFail.setHeaderText(rb.getString("modal.saveFail.header"));
                saveFail.getButtonTypes().setAll(saveFailRetry, saveFailDesist);
                
                Optional<ButtonType> saveFailResult = saveFail.showAndWait();
                retry = saveFailResult.isPresent() && saveFailResult.get() == saveFailRetry;
            }
        } while (retry);
    }

    public void saveDBAndQuit() {
        saveDB();
        Platform.exit();
        System.exit(0);
    }
    
    private void loadDB() {
        appointments = FXCollections.observableArrayList(clinic.getAppointments());
        appointmentTable.setItems(appointments);
        patients = FXCollections.observableArrayList(clinic.getPatients());
        patientTable.setItems(patients);
        doctors = FXCollections.observableArrayList(clinic.getDoctors());
        doctorTable.setItems(doctors);
    }
    
    private <T extends Person> void removePerson(ObservableList<T> list, int index, String identifier, Function<Appointment,T> getterFn) {
        try { 
            T toDelete = list.get(index);
            FilteredList<Appointment> conflicts = appointments.filtered(appointment -> {
                return getterFn.apply(appointment).getIdentifier().equals(toDelete.getIdentifier());
            });
            if (conflicts.size() > 0) {
                Alert removeConflict = new Alert(AlertType.INFORMATION, rb.getString("modal.removeConflict.content"));
                removeConflict.setTitle(rb.getString("modal.removeConflict.title"));
                removeConflict.setHeaderText(rb.getString("modal.removeConflict.title"));
                removeConflict.showAndWait();
            } else {
                removeWithConfirmation(list, index, rb.getString("generic." + identifier) + " " + toDelete.getIdentifier());
            }
        } catch (Exception e) { System.err.println(e); }
    }
    
    private <T> void removeWithConfirmation(ObservableList<T> list, int index, String identifier) {
        if (index >= 0 || index < list.size()) {
            Alert remove = new Alert(AlertType.WARNING, 
                    rb.getString("modal.remove.content") + "\n" + identifier, 
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> removeResult = remove.showAndWait();
            if (removeResult.isPresent() && removeResult.get() == ButtonType.YES) {
                list.remove(index);
            }
        }
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
        int index = appointmentTable.getSelectionModel().getSelectedIndex();
        try { 
            Appointment toDelete = appointments.get(index);
            removeWithConfirmation(appointments, index, 
                rb.getString("generic.appointment") + " "
                + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(toDelete.getAppointmentDateTime())
            );
        } catch (Exception e) {}
    }

    @FXML private void onShowPatient(ActionEvent event) {
    }

    @FXML private void onRemovePatient(ActionEvent event) {
        int index = patientTable.getSelectionModel().getSelectedIndex();
        removePerson(patients, index, "patient", Appointment::getPatient);
    }

    @FXML private void onShowDoctor(ActionEvent event) {
    }

    @FXML private void onRemoveDoctor(ActionEvent event) {
        int index = doctorTable.getSelectionModel().getSelectedIndex();
        removePerson(doctors, index, "doctor", Appointment::getDoctor);
    }

    @FXML private void onSave(ActionEvent event) {
        saveDB();
    }
    
    @FXML private void onDiscardChanges(ActionEvent event) {
        ButtonType discardYes = new ButtonType(rb.getString("modal.discard.btn.yes"), ButtonData.YES);
        Alert discard = new Alert(AlertType.WARNING, rb.getString("modal.discard.content"), discardYes, ButtonType.NO);
        discard.setTitle(rb.getString("modal.discard.title"));
        discard.setHeaderText(rb.getString("modal.discard.header"));
        
        Optional<ButtonType> discardResult = discard.showAndWait();
        if (discardResult.isPresent() && discardResult.get() == discardYes) {
            loadDB();
        }
    }

}
