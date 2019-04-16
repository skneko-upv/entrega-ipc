/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import DBAccess.ClinicDBAccess;
import gestorcitas.controllers.base.AbstractFormController;
import gestorcitas.controllers.helpers.SlotDayCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Doctor;
import model.ExaminationRoom;
import model.Patient;
import utils.SlotWeek;

public class AppointmentFormController extends AbstractFormController<Appointment> {

    protected ObservableList<Patient> patients;
    protected ObservableList<Appointment> appointments;
    protected ObservableList<Doctor> doctors;

    @FXML private Button prevWeekBtn;
    @FXML private Label weekLabel;
    @FXML private Button nextWeekBtn;

    @FXML private TableView<SlotWeek> weekTable;
    @FXML private TableColumn<SlotWeek,LocalTime> slotColumn;
    @FXML private TableColumn<SlotWeek,String> mondayColumn;
    @FXML private TableColumn<SlotWeek,String> tuesdayColumn;
    @FXML private TableColumn<SlotWeek,String> wednesdayColumn;
    @FXML private TableColumn<SlotWeek,String> thursdayColumn;
    @FXML private TableColumn<SlotWeek,String> fridayColumn;
    @FXML private TableColumn<SlotWeek,String> saturdayColumn;
    @FXML private TableColumn<SlotWeek,String> sundayColumn;

    @FXML private DatePicker visitDayPicker;
    @FXML private Label visitTimeLabel;
    @FXML private ChoiceBox<ExaminationRoom> roomSelector;
    @FXML private Label roomDescriptionLabel;
    @FXML private ChoiceBox<Patient> patientSelector;
    @FXML private ChoiceBox<Doctor> doctorSelector;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        // Set up table
        slotColumn.setCellValueFactory(new PropertyValueFactory<>("slot"));
        slotColumn.setCellFactory(data -> {
            return new TableCell<SlotWeek,LocalTime>() {
                @Override
                protected void updateItem(LocalTime date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) setText(null);
                    else {
                        setText(
                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                        .format(date)
                        );
                    }
                }
            };
        });

        mondayColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getMondayAvailability())
        );
        mondayColumn.setCellFactory(new SlotDayCellFactory(patients, rb));

        tuesdayColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getTuesdayAvailability())
        );
        tuesdayColumn.setCellFactory(new SlotDayCellFactory(patients, rb));

        wednesdayColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getWednesdayAvailability())
        );
        wednesdayColumn.setCellFactory(new SlotDayCellFactory(patients, rb));

        thursdayColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getThursdayAvailability())
        );
        thursdayColumn.setCellFactory(new SlotDayCellFactory(patients, rb));

        fridayColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getFridayAvailability())
        );
        fridayColumn.setCellFactory(new SlotDayCellFactory(patients, rb));

        saturdayColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getSaturdayAvailability())
        );
        saturdayColumn.setCellFactory(new SlotDayCellFactory(patients, rb));

        sundayColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getSundayAvailability())
        );
        sundayColumn.setCellFactory(new SlotDayCellFactory(patients, rb));

    }

    public void setup(
            ObservableList<Appointment> appointments,
            ObservableList<Patient> patients,
            ObservableList<Doctor> doctors
    ) {
        super.setup(true, null);
        this.appointments = appointments;
        this.patients = patients;
        this.doctors = doctors;

        patientSelector.setItems(patients);
        doctorSelector.setItems(doctors);
        roomSelector.setItems(
                FXCollections.observableArrayList(
                        ClinicDBAccess.getSingletonClinicDBAccess().getExaminationRooms()
                )
        );
    }

    @Override
    protected void prefill() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void setUneditableAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void setClearAll() {
        // TODO
    }

    @Override
    protected boolean validateAll() {
        // TODO
        return false;
    }

    @Override
    protected void onSaveValidated(ActionEvent event) {
        // TODO
    }

}
