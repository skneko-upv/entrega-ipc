/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import gestorcitas.controllers.helpers.FormattedDateTimeCellFactory;
import gestorcitas.controllers.helpers.PersonCellFactory;
import gestorcitas.controllers.helpers.PersonSearchPredicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Person;

public class AppointmentsTabController extends TabController<Appointment> {

    @FXML protected Label tabTitle;
    @FXML protected TextField searchBox;
    
    @FXML protected TableView<Appointment> table;
    @FXML protected TableColumn<Appointment,LocalDateTime> dateColumn;
    @FXML protected TableColumn<Appointment,LocalDateTime> timeColumn;
    @FXML protected TableColumn<Appointment,Person> doctorColumn;
    @FXML protected TableColumn<Appointment,Person> patientColumn;
    
    @FXML private Button removeBtn;
    
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
        
        // Load items from DB
        ObservableList<Appointment> loadedItems = 
                FXCollections.observableArrayList(clinic.getAppointments());
        setItems(loadedItems);
        
        table.setItems(itemsFiltered);
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

    @FXML
    protected void onAdd(ActionEvent event) {
        // TODO
    }
    
}
