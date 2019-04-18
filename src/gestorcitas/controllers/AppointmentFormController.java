/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers;

import gestorcitas.controllers.base.AbstractFormController;
import gestorcitas.controllers.helpers.PersonStringConverter;
import gestorcitas.controllers.helpers.SlotDayCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Doctor;
import model.Patient;
import utils.SlotAppointmentsWeek;
import utils.SlotWeek;

public class AppointmentFormController extends AbstractFormController<Appointment> {

    protected ObservableList<Patient> patients;
    protected ObservableList<Appointment> appointments;
    protected ObservableList<Doctor> doctors;

    protected Patient selectedPatient = null;
    protected Doctor selectedDoctor = null;
    protected SimpleObjectProperty<LocalDateTime> selectedSlot = new SimpleObjectProperty<>(null);

    protected ObservableList<SlotWeek> tableContent;
    protected LocalDate today = LocalDate.now();
    protected LocalDate mondayDate = null;

    @FXML
    private Label weekLabel;
    @FXML
    private Button prevWeekBtn;

    @FXML
    private TableView<SlotWeek> weekTable;
    @FXML
    private TableColumn<SlotWeek, LocalTime> slotColumn;
    @FXML
    private TableColumn<SlotWeek, String> mondayColumn;
    @FXML
    private TableColumn<SlotWeek, String> tuesdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> wednesdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> thursdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> fridayColumn;
    @FXML
    private TableColumn<SlotWeek, String> saturdayColumn;
    @FXML
    private TableColumn<SlotWeek, String> sundayColumn;

    @FXML
    private DatePicker visitDayPicker;
    @FXML
    private Label visitTimeLabel;
    @FXML
    private ChoiceBox<Patient> patientSelector;
    @FXML
    private ChoiceBox<Doctor> doctorSelector;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        // Set up table
        slotColumn.setCellValueFactory(new PropertyValueFactory<>("slot"));
        slotColumn.setCellFactory(data -> {
            return new TableCell<SlotWeek, LocalTime>() {
                @Override
                protected void updateItem(LocalTime date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(
                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                        .format(date)
                        );
                    }
                }
            };
        });
        mondayColumn.setCellValueFactory(new PropertyValueFactory<>("mondayAvailability"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("tuesdayAvailability"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("wednesdayAvailability"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<>("thursdayAvailability"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<>("fridayAvailability"));
        saturdayColumn.setCellValueFactory(new PropertyValueFactory<>("saturdayAvailability"));
        sundayColumn.setCellValueFactory(new PropertyValueFactory<>("sundayAvailability"));

        weekTable.getSelectionModel().setCellSelectionEnabled(true);

        ObservableList<TablePosition> selectedCells = weekTable.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                LocalDate date = mondayDate;
                TableColumn column = selectedCells.get(0).getTableColumn();
                if (column == tuesdayColumn) {
                    date.plusDays(1);
                } else if (column == wednesdayColumn) {
                    date = date.plusDays(2);
                } else if (column == thursdayColumn) {
                    date = date.plusDays(3);
                } else if (column == fridayColumn) {
                    date = date.plusDays(4);
                } else if (column == saturdayColumn) {
                    date = date.plusDays(5);
                } else if (column == sundayColumn) {
                    date = date.plusDays(6);
                }

                int rowIndex = selectedCells.get(0).getRow();
                LocalTime time = tableContent.get(rowIndex).getSlot();
                this.selectedSlot.setValue(LocalDateTime.of(date, time));
            }
        });

        // Set up form
        patientSelector.valueProperty().addListener((val, oldVal, newVal) -> {
            this.selectedPatient = newVal;
        });

        doctorSelector.valueProperty().addListener((val, oldVal, newVal) -> {
            this.selectedDoctor = newVal;
            buildTable();
        });

        visitDayPicker.valueProperty().addListener((val, oldVal, newVal) -> {
            if (visitDayPicker.isDisable()) {
                return;
            }
            mondayDate = newVal.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            buildTable();
        });
        visitDayPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        visitDayPicker.setValue(today);

        selectedSlot.addListener((val, oldVal, newVal) -> {
            if (newVal != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
                visitTimeLabel.setText(
                        formatter.format(newVal)
                        + "-"
                        + formatter.format(newVal.plusMinutes(15))
                );
                visitDayPicker.setDisable(true);
                visitDayPicker.setValue(newVal.toLocalDate());
                visitDayPicker.setDisable(false);
            } else {
                visitTimeLabel.setText("");
            }
        });

        patientSelector.setConverter(new PersonStringConverter<>(patients));
        doctorSelector.setConverter(new PersonStringConverter<>(doctors));
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
        if (selectedDoctor == null
                || selectedPatient == null) {
            errorLabel.setVisible(true);
            errorLabel.setText(rb.getString("form.generic.error.notComplete"));
            return false;
        } else if (selectedSlot.getValue() == null) {
            errorLabel.setVisible(true);
            errorLabel.setText(rb.getString("form.appointment.error.noSlot"));
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onSaveValidated(ActionEvent event) {
        appointments.add(
                new Appointment(
                        selectedSlot.getValue(),
                        selectedDoctor,
                        selectedPatient
                )
        );
    }

    protected void buildTable() {
        selectedSlot.setValue(null);
        if (mondayDate != null && selectedDoctor != null) {
            WeekFields weekFields = WeekFields.of(rb.getLocale());
            int selectedWeek = mondayDate.get(weekFields.weekOfWeekBasedYear());

            setupColumn(mondayColumn, 0);
            setupColumn(tuesdayColumn, 1);
            setupColumn(wednesdayColumn, 2);
            setupColumn(thursdayColumn, 3);
            setupColumn(fridayColumn, 4);
            setupColumn(saturdayColumn, 5);
            setupColumn(sundayColumn, 6);

            prevWeekBtn.setDisable(mondayDate.minusDays(1).isBefore(today));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ww, MMMM yyyy", rb.getLocale());
            weekLabel.setText(rb.getString("generic.week") + " " + formatter.format(mondayDate));

            tableContent = FXCollections.observableArrayList(
                    SlotAppointmentsWeek.getAppointmentsWeek(
                            selectedWeek,
                            selectedDoctor.getVisitDays(),
                            selectedDoctor.getVisitStartTime(),
                            selectedDoctor.getVisitEndTime(),
                            new ArrayList<>(appointments)
                    )
            );

            weekTable.setItems(tableContent);
        } else {
            tableContent = null;
            weekTable.setItems(null);
        }
    }

    protected void setupColumn(TableColumn column, int offset) {
        LocalDate current = mondayDate.plusDays(offset);
        column.setText(String.valueOf(current.getDayOfMonth()));
        if (current.isBefore(today)) {
            column.setCellFactory(col -> new TableCell<SlotWeek, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    getStyleClass().remove("cell-old");
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        getStyleClass().add("cell-old");
                        setDisable(true);
                    }
                }
            });
        } else {
            column.setCellFactory(new SlotDayCellFactory(patients, rb));
        }
    }

    @FXML
    protected void onNextWeek(ActionEvent event) {
        if (tableContent != null) {
            mondayDate = mondayDate.plusWeeks(1);
            buildTable();
        }
    }

    @FXML
    protected void onPrevWeek(ActionEvent event) {
        if (mondayDate.minusDays(1).isBefore(today)) {
            return;
        }
        if (tableContent != null) {
            mondayDate = mondayDate.minusWeeks(1);
            buildTable();
        }
    }

}
