/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import gestorcitas.controllers.base.TabController;
import gestorcitas.controllers.helpers.FormattedDateTimeCellFactory;
import gestorcitas.controllers.helpers.PersonCellFactory;
import gestorcitas.controllers.helpers.PersonSearchPredicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.Appointment;
import model.Doctor;
import model.ExaminationRoom;
import model.Person;
import utils.SlotAppointmentsWeek;
import utils.SlotWeek;

public class AppointmentsTabController extends TabController<Appointment> {

    @FXML protected Label tabTitle;
    @FXML protected TextField searchBox;
    
    @FXML protected TableColumn<Appointment,LocalDateTime> dateColumn;
    @FXML protected TableColumn<Appointment,LocalDateTime> timeColumn;
    @FXML protected TableColumn<Appointment,Person> doctorColumn;
    @FXML protected TableColumn<Appointment,Person> patientColumn;
    
    @FXML private Button removeBtn;
    
    @FXML protected Label dateTimeLabel;
    @FXML protected Label dateTimeDiffLabel;
    @FXML protected Label patientLabel;
    @FXML protected Label doctorLabel;
    @FXML protected Label roomIdLabel;
    @FXML protected Label roomDescLabel;
    @FXML protected ImageView photoPreview;
    
    @Override
    public void setTitle(String title) {
        tabTitle.setText(title);
    }
    
    @Override
    public String getSummary(Appointment appointment) {
        String formattedDate = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .format(appointment.getAppointmentDateTime());
        return rb.getString("generic.appointment") + " " + formattedDate; 
    }
    
    @Override
    public void loadDataFromDB() {
        ObservableList<Appointment> loadedItems = 
                FXCollections.observableArrayList(clinic.getAppointments());
        setItems(loadedItems);
    }

    /**
     * Initializes the controller class.
     *
     * @param url Current controller location.
     * @param rb Resources used to localize this controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        // Set up table
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        dateColumn.setCellFactory(
                new FormattedDateTimeCellFactory<>(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                )
        );
        
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        timeColumn.setCellFactory(
                new FormattedDateTimeCellFactory<>(
                        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                )
        );
        
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        doctorColumn.setCellFactory(new PersonCellFactory<>());
        
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        patientColumn.setCellFactory(new PersonCellFactory<>());
        
        // Set up event handlers
        table.getSelectionModel().selectedItemProperty().addListener((val, oldVal, newVal) -> {
            boolean noneSelected = newVal == null;
            removeBtn.setDisable(noneSelected);
        });
        
        table.getSelectionModel().selectedItemProperty().addListener((val, oldVal, newVal) -> {
            if (newVal != null) {
                dateTimeLabel.setText(
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(
                                newVal.getAppointmentDateTime()
                        )
                );
                Duration diff = Duration.between(LocalDateTime.now(), newVal.getAppointmentDateTime());
                if (diff.isNegative()) {
                    dateTimeDiffLabel.setText(
                            rb.getString("tab.appointments.summary.deadlineExceeded")
                    );
                } else {
                    dateTimeDiffLabel.setText(
                            rb.getString("tab.appointments.summary.inTimeSpan") + " " 
                                    + diff.toDays() + rb.getString("tab.appointments.summary.days") + " "
                                    + diff.toHours() + rb.getString("tab.appointments.summary.hours") + " "
                                    + diff.toMinutes() + rb.getString("tab.appointments.summary.mins")
                    );
                }
                
                
                patientLabel.setText(
                        newVal.getPatient().getSurname() + ", " 
                                + newVal.getPatient().getName() + " ("
                                + newVal.getPatient().getIdentifier() + ")"
                );
                doctorLabel.setText(
                        newVal.getDoctor().getSurname() + ", " 
                                + newVal.getDoctor().getName() + " ("
                                + newVal.getDoctor().getIdentifier() + ")"
                );
                
                ExaminationRoom room = newVal.getDoctor().getExaminationRoom();
                roomIdLabel.setText(
                        String.valueOf(room.getIdentNumber())
                );
                roomDescLabel.setText(room.getEquipmentDescription());
                
                photoPreview.setImage(newVal.getPatient().getPhoto());
            }
        });
        
        // Load items from DB
        loadDataFromDB();
        
        table.setItems(itemsFiltered);
    }
    
    @FXML @Override
    public void onShow(ActionEvent event) {
        throw new UnsupportedOperationException();
    }
    
    @FXML @Override
    public void onAdd(ActionEvent event) {
        Doctor test = clinic.getDoctors().get(0);
        ArrayList<SlotWeek> slots = SlotAppointmentsWeek.getAppointmentsWeek(
                13, 
                test.getVisitDays(), 
                test.getVisitStartTime(), 
                test.getVisitEndTime(), 
                new ArrayList<>(items)
        );
        for (SlotWeek slot : slots) {
            System.out.printf(
                    "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                    slot.getSlot(),
                    slot.getMondayAvailability(),
                    slot.getTuesdayAvailability(),
                    slot.getWednesdayAvailability(),
                    slot.getThursdayAvailability(),
                    slot.getFridayAvailability(),
                    slot.getSaturdayAvailability(),
                    slot.getSundayAvailability()
            );
        }
    }
    
    @Override
    protected boolean canDelete(Appointment toDelete) {
        return true;
    }

    @FXML @Override
    protected void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        itemsFiltered.setPredicate(e -> {
                Predicate<Person> predicate = new PersonSearchPredicate(query);
                return predicate.test(e.getDoctor()) || predicate.test(e.getPatient());
        });
    }

}
