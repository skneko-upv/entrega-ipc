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

    @FXML
    private CheckBox mondayBox;
    @FXML
    private CheckBox tuesdayBox;
    @FXML
    private CheckBox wednesdayBox;
    @FXML
    private CheckBox thursdayBox;
    @FXML
    private CheckBox fridayBox;
    @FXML
    private CheckBox saturdayBox;
    @FXML
    private CheckBox sundayBox;
    @FXML
    private ChoiceBox<Integer> startTimeHourSelector;
    @FXML
    private ChoiceBox<Integer> startTimeMinSelector;
    @FXML
    private ChoiceBox<Integer> endTimeHourSelector;
    @FXML
    private ChoiceBox<Integer> endTimeMinSelector;
    @FXML
    private ChoiceBox<ExaminationRoom> roomSelector;
    @FXML
    private Label roomDescriptionLabel;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    private final StringConverter<Integer> minStringConverter = new StringConverter<Integer>() {
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

        ArrayList<Days> visitDays = new ArrayList<>();
        if (mondayBox.isSelected()) {
            visitDays.add(Days.Monday);
        }
        if (tuesdayBox.isSelected()) {
            visitDays.add(Days.Tuesday);
        }
        if (wednesdayBox.isSelected()) {
            visitDays.add(Days.Wednesday);
        }
        if (thursdayBox.isSelected()) {
            visitDays.add(Days.Thursday);
        }
        if (fridayBox.isSelected()) {
            visitDays.add(Days.Friday);
        }
        if (saturdayBox.isSelected()) {
            visitDays.add(Days.Saturday);
        }
        if (sundayBox.isSelected()) {
            visitDays.add(Days.Sunday);
        }
        toAdd.setVisitDays(visitDays);

        persons.add(toAdd);
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
