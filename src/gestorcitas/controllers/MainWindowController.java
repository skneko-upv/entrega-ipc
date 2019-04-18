/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers;

import DBAccess.ClinicDBAccess;
import gestorcitas.application.GestorCitas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Appointment;
import model.Doctor;
import model.Patient;
import model.Person;

public class MainWindowController implements Initializable {

    public static final String DEFAULT_PHOTO = "/gestorcitas/resources/img/default.png";

    private ResourceBundle rb;
    private final ClinicDBAccess clinic = ClinicDBAccess.getSingletonClinicDBAccess();

    @FXML
    private BorderPane root;

    @FXML
    private AppointmentsTabController appointmentsTabController;
    @FXML
    private PatientsTabController patientsTabController;
    @FXML
    private DoctorsTabController doctorsTabController;

    @FXML
    private ToggleGroup language;
    @FXML
    private RadioMenuItem langES;
    @FXML
    private RadioMenuItem langEN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;

        appointmentsTabController.setMainController(this);
        patientsTabController.setMainController(this);
        doctorsTabController.setMainController(this);

        refreshName();

        // Set up language change
        Locale locale = rb.getLocale();
        if (locale.equals(new Locale("en", "us"))) {
            language.selectToggle(langEN);
        } else if (locale.equals(new Locale("es", "es"))) {
            language.selectToggle(langES);
        }
        language.selectedToggleProperty().addListener((val, oldVal, newVal) -> {
            if (newVal != null && (oldVal == null || !oldVal.equals(newVal))) {
                if (newVal == langEN) {
                    restartWithLocale(new Locale("en", "us"));
                } else if (newVal == langES) {
                    restartWithLocale(new Locale("es", "es"));
                }
            }
        });
    }

    // TODO: add comment about why we're not using clinic.getPatientAppointments(), etc.
    public <T extends Person> FilteredList<Appointment> getRemoveConflicts(
            T toDelete,
            Function<Appointment, T> personValueFactory
    ) {
        ObservableList<Appointment> appointments = appointmentsTabController.getItems();
        return appointments.filtered(appointment -> {
            return personValueFactory.apply(appointment)
                    .getIdentifier()
                    .equals(toDelete.getIdentifier());
        });
    }

    public ObservableList<Appointment> getAppointments() {
        return appointmentsTabController.getItems();
    }

    public ObservableList<Patient> getPatients() {
        return patientsTabController.getItems();
    }

    public ObservableList<Doctor> getDoctors() {
        return doctorsTabController.getItems();
    }

    public void saveDB() {
        clinic.getAppointments().clear();
        clinic.getAppointments().addAll(appointmentsTabController.getItems());
        clinic.getPatients().clear();
        clinic.getPatients().addAll(patientsTabController.getItems());
        clinic.getDoctors().clear();
        clinic.getDoctors().addAll(doctorsTabController.getItems());

        boolean retry = false;
        do {
            Alert saveWait = new Alert(Alert.AlertType.INFORMATION);
            saveWait.setTitle(rb.getString("generic.wait"));
            saveWait.setHeaderText(rb.getString("modal.saveWait.header"));
            saveWait.setContentText(rb.getString("modal.saveWait.content"));
            saveWait.getButtonTypes().clear();
            saveWait.show();

            boolean success = clinic.saveDB();

            saveWait.setResult(ButtonType.OK);
            saveWait.close();
            if (!success) {
                ButtonType saveFailRetry = new ButtonType(
                        rb.getString("generic.retry"),
                        ButtonBar.ButtonData.OK_DONE
                );
                ButtonType saveFailDesist = new ButtonType(
                        rb.getString("generic.cancel"),
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

                Alert saveFail = new Alert(
                        Alert.AlertType.ERROR,
                        rb.getString("modal.saveFail.content"),
                        saveFailRetry,
                        saveFailDesist
                );
                saveFail.setTitle(rb.getString("generic.error"));
                saveFail.setHeaderText(rb.getString("modal.saveFail.header"));
                saveFail.getButtonTypes().setAll(saveFailRetry, saveFailDesist);

                Optional<ButtonType> saveFailResult = saveFail.showAndWait();
                retry = saveFailResult.isPresent()
                        && saveFailResult.get() == saveFailRetry;
            }
        } while (retry);
    }

    public void saveDBAndQuit() {
        saveDB();
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
        setName.setHeaderText(null);
        setName.setGraphic(null);
        setName.setContentText(rb.getString("modal.setName.content"));
        setName.getEditor().setPromptText(unnamed);

        Optional<String> setNameResult = setName.showAndWait();
        setNameResult.ifPresent(name -> {
            if (!setNameResult.get().equals("")) {
                clinic.setClinicName(name);
            } else {
                clinic.setClinicName(unnamed);
            }
            refreshName();
        });
    }

    @FXML
    private void onSave(ActionEvent event) {
        saveDB();
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
        saveDBAndQuit();
    }

    @FXML
    private void onAddAppointment(ActionEvent event) {
        appointmentsTabController.onAdd(event);
    }

    @FXML
    private void onAddPatient(ActionEvent event) {
        patientsTabController.onAdd(event);
    }

    @FXML
    private void onAddDoctor(ActionEvent event) {
        doctorsTabController.onAdd(event);
    }

    @FXML
    private void onAbout(ActionEvent event) {
        Alert about = new Alert(AlertType.INFORMATION);
        about.setTitle(rb.getString("modal.about.title"));
        about.setHeaderText(null);
        about.setGraphic(null);
        about.setContentText(rb.getString("modal.about.content"));
        about.showAndWait();
    }

    private void restartWithLocale(Locale locale) {
        saveDB();
        ((Stage) root.getScene().getWindow()).close();
        Platform.runLater(() -> {
            try {
                new GestorCitas().start(new Stage(), locale);
            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }

}
