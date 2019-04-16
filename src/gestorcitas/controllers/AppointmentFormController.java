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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import utils.SlotAppointmentsWeek;
import utils.SlotWeek;

public class AppointmentFormController extends AbstractFormController<Appointment> {

    protected ObservableList<Patient> patients;
    protected ObservableList<Appointment> appointments;
    protected ObservableList<Doctor> doctors;
    
    protected Patient selectedPatient = null;
    protected Doctor selectedDoctor = null;
    protected ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(null);

    @FXML private Label weekLabel;

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
        
        // Set up form
        patientSelector.valueProperty().addListener((val, oldVal, newVal) -> { this.selectedPatient = newVal; });
        doctorSelector.valueProperty().addListener((val, oldVal, newVal) -> { this.selectedDoctor = newVal; });
        doctorSelector.valueProperty().addListener((val, oldVal, newVal) -> { tryBuildTable(); });
        visitDayPicker.valueProperty().addListener((val, oldVal, newVal) -> { this.selectedDate.setValue(newVal); });
        visitDayPicker.valueProperty().addListener((val, oldVal, newVal) -> { tryBuildTable(); });

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
        
        weekTable.getSelectionModel().setCellSelectionEnabled(true);
        
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
        errorLabel.setVisible(false);
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
    
    protected void tryBuildTable() {
        if (selectedDoctor != null && selectedDate.getValue() != null) {
            buildTable(selectedDoctor, selectedDate.getValue());
        }
    }
    
    protected void buildTable(Doctor doctor, LocalDate date) {
        WeekFields weekFields = WeekFields.of(rb.getLocale());
        LocalDate mondayDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int selectedWeek = date.get(weekFields.weekOfWeekBasedYear());
        LocalDate today = LocalDate.now();

        setupColumn(mondayColumn, today, mondayDate, 0);
        setupColumn(tuesdayColumn, today, mondayDate, 1);
        setupColumn(wednesdayColumn, today, mondayDate, 2);
        setupColumn(thursdayColumn, today, mondayDate, 3);
        setupColumn(fridayColumn, today, mondayDate, 4);
        setupColumn(saturdayColumn, today, mondayDate, 5);
        setupColumn(sundayColumn, today, mondayDate, 6);
        
        weekTable.setItems(
                FXCollections.observableArrayList(
                        SlotAppointmentsWeek.getAppointmentsWeek(
                                selectedWeek,
                                selectedDoctor.getVisitDays(),
                                selectedDoctor.getVisitStartTime(),
                                selectedDoctor.getVisitEndTime(),
                                new ArrayList<>(appointments)
                        )
                )
        );
    }
    
    protected void setupColumn(TableColumn column, LocalDate today, LocalDate mondayDate, int offset) {
        LocalDate current = mondayDate.plusDays(offset);
        column.setText(String.valueOf(current.getDayOfMonth()));
        if (current.isBefore(today)) {
            column.getStyleClass().add("cell-old");
        } else {
            column.getStyleClass().remove("cell-old");
        }
    }
    
    @FXML
    protected void onNextWeek(ActionEvent event) {
        if (this.selectedDate.getValue() != null) {
            this.selectedDate.setValue(this.selectedDate.getValue().plusWeeks(1));
        }
        tryBuildTable();
    }
    
    @FXML
    protected void onPrevWeek(ActionEvent event) {
        if (this.selectedDate.getValue() != null) {
            this.selectedDate.setValue(this.selectedDate.getValue().minusWeeks(1));
        }
        tryBuildTable();
    }

}
