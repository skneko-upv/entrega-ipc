/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers;

import gestorcitas.controllers.base.BasePersonFormController;
import gestorcitas.controllers.helpers.StringUtils;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;

public class DoctorFormController extends BasePersonFormController<Doctor> {

    public static final int FIRST_VISIT_HOUR = 7;
    public static final int LAST_VISIT_HOUR = 23;
    public static final List<Integer> VISIT_HOURS = new ArrayList<>();

    public static final Integer[] VISIT_MINUTES = new Integer[]{0, 15, 30, 45};

    static {
        for (int hour = FIRST_VISIT_HOUR; hour < LAST_VISIT_HOUR; hour++) {
            VISIT_HOURS.add(hour);
        }
    }
    
    protected LocalTime startTime;
    protected LocalTime endTime;
    protected ArrayList<Days> selectedDays;

    @FXML
    protected CheckBox mondayBox;
    @FXML
    protected CheckBox tuesdayBox;
    @FXML
    protected CheckBox wednesdayBox;
    @FXML
    protected CheckBox thursdayBox;
    @FXML
    protected CheckBox fridayBox;
    @FXML
    protected CheckBox saturdayBox;
    @FXML
    protected CheckBox sundayBox;
    @FXML
    protected ChoiceBox<Integer> startTimeHourSelector;
    @FXML
    protected ChoiceBox<Integer> startTimeMinSelector;
    @FXML
    protected ChoiceBox<Integer> endTimeHourSelector;
    @FXML
    protected ChoiceBox<Integer> endTimeMinSelector;
    @FXML
    protected ChoiceBox<ExaminationRoom> roomSelector;
    @FXML
    protected Label roomDescriptionLabel;
    @FXML
    protected Label timeErrorLabel;
    @FXML
    protected Button saveBtn;
    @FXML
    protected Button cancelBtn;

    protected final StringConverter<Integer> minStringConverter = new StringConverter<Integer>() {
        @Override
        public Integer fromString(String s) {
            return Integer.parseInt(s);
        }

        @Override
        public String toString(Integer i) {
            String res = String.valueOf(i);
            if (i < 10) {
                res = "0" + res;
            }
            return res;
        }
    };

    public void setup(
            boolean editMode,
            Doctor prefill,
            ObservableList<Doctor> persons,
            ArrayList<ExaminationRoom> rooms
    ) {
        // Set up choice boxes
        ObservableList<Integer> hoursList = FXCollections.observableArrayList(VISIT_HOURS);
        ObservableList<Integer> minsList = FXCollections.observableArrayList(VISIT_MINUTES);
        startTimeHourSelector.setItems(hoursList);
        startTimeMinSelector.setItems(minsList);
        endTimeHourSelector.setItems(hoursList);
        endTimeMinSelector.setItems(minsList);
        roomSelector.setItems(FXCollections.observableArrayList(rooms));

        startTimeMinSelector.setConverter(minStringConverter);
        endTimeMinSelector.setConverter(minStringConverter);
        roomSelector.setConverter(new StringConverter<ExaminationRoom>() {
            @Override
            public ExaminationRoom fromString(String s) {
                int id = Integer.parseInt(s);
                ExaminationRoom room = null;
                for (int i = 0; i < rooms.size(); i++) {
                    if (rooms.get(i).getIdentNumber() == id) {
                        room = rooms.get(i);
                        break;
                    }
                }
                return room;
            }

            @Override
            public String toString(ExaminationRoom e) {
                return String.valueOf(e.getIdentNumber());
            }
        });

        // Set up listeners
        roomSelector.valueProperty().addListener((val, oldVal, newVal) -> {
            roomDescriptionLabel.setText(
                    StringUtils.prettifyEnumeration(newVal.getEquipmentDescription())
            );
        });
        
        super.setup(editMode, prefill, persons);
    }

    @Override
    protected void onSaveValidated(ActionEvent event) {
        Doctor toAdd = new Doctor(
                roomSelector.getValue(),
                Days.Monday,
                LocalTime.of(
                        startTimeHourSelector.getValue(),
                        startTimeMinSelector.getValue()
                ),
                LocalTime.of(
                        endTimeHourSelector.getValue(),
                        endTimeMinSelector.getValue()
                ),
                id.getText(),
                name.getText(),
                surname.getText(),
                phone.getText(),
                photo.getValue()
        );
        toAdd.setVisitDays(selectedDays);
        persons.add(toAdd);
    }
    
    @Override
    protected boolean validateAll() {
        if (!super.validateAll()) return false;
        
        selectedDays = new ArrayList<>();
        if (mondayBox.isSelected()) {
            selectedDays.add(Days.Monday);
        }
        if (tuesdayBox.isSelected()) {
            selectedDays.add(Days.Tuesday);
        }
        if (wednesdayBox.isSelected()) {
            selectedDays.add(Days.Wednesday);
        }
        if (thursdayBox.isSelected()) {
            selectedDays.add(Days.Thursday);
        }
        if (fridayBox.isSelected()) {
            selectedDays.add(Days.Friday);
        }
        if (saturdayBox.isSelected()) {
            selectedDays.add(Days.Saturday);
        }
        if (sundayBox.isSelected()) {
            selectedDays.add(Days.Sunday);
        }
        if (selectedDays.isEmpty()) return false;

        if (
            startTimeHourSelector.getValue() == null
                || startTimeMinSelector.getValue() == null
                || endTimeHourSelector.getValue() == null
                || endTimeMinSelector.getValue() == null
        ) return false;
        
        startTime = LocalTime.of(
                startTimeHourSelector.getValue(),
                startTimeMinSelector.getValue()
        );
        endTime = LocalTime.of(
                endTimeHourSelector.getValue(),
                endTimeMinSelector.getValue()
        );
        
        if (!startTime.isBefore(endTime)) {
            timeErrorLabel.setText(rb.getString("form.doctor.error.endTimeBefore"));
            timeErrorLabel.setVisible(true);
            return false;
        }
        
        if (roomSelector.getValue() == null) return false;
        
        return true;
    }

    @Override
    protected void prefill() {
        super.prefill();

        ArrayList<Days> visitDays = prefill.getVisitDays();
        if (visitDays.contains(Days.Monday)) {
            mondayBox.setSelected(true);
        }
        if (visitDays.contains(Days.Tuesday)) {
            tuesdayBox.setSelected(true);
        }
        if (visitDays.contains(Days.Wednesday)) {
            wednesdayBox.setSelected(true);
        }
        if (visitDays.contains(Days.Thursday)) {
            thursdayBox.setSelected(true);
        }
        if (visitDays.contains(Days.Friday)) {
            fridayBox.setSelected(true);
        }
        if (visitDays.contains(Days.Saturday)) {
            saturdayBox.setSelected(true);
        }
        if (visitDays.contains(Days.Sunday)) {
            sundayBox.setSelected(true);
        }

        LocalTime start = prefill.getVisitStartTime();
        LocalTime end = prefill.getVisitEndTime();
        startTimeHourSelector.setValue(start.getHour());
        startTimeMinSelector.setValue(start.getMinute());
        endTimeHourSelector.setValue(end.getHour());
        endTimeMinSelector.setValue(end.getMinute());

        ExaminationRoom room = prefill.getExaminationRoom();
        roomSelector.setItems(FXCollections.observableArrayList(room));
        roomSelector.setValue(room);
    }

    @Override
    protected void setUneditableAll() {
        super.setUneditableAll();

        mondayBox.setDisable(true);
        tuesdayBox.setDisable(true);
        wednesdayBox.setDisable(true);
        thursdayBox.setDisable(true);
        fridayBox.setDisable(true);
        saturdayBox.setDisable(true);
        sundayBox.setDisable(true);

        startTimeHourSelector.setDisable(true);
        startTimeMinSelector.setDisable(true);
        endTimeHourSelector.setDisable(true);
        endTimeMinSelector.setDisable(true);

        roomSelector.setDisable(true);
    }

}
