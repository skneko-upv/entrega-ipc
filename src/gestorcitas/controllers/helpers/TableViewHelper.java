/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;
import model.Person;

public class TableViewHelper {
    
    public static void setupAppointmentTable(
            TableColumn<Appointment,LocalDateTime> appointmentDateColumn,
            TableColumn<Appointment,LocalDateTime> appointmentTimeColumn,
            TableColumn<Appointment,Person> appointmentDoctorColumn,
            TableColumn<Appointment,Person> appointmentPatientColumn
    ) {
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        appointmentDateColumn.setCellFactory(
                new FormattedDateTimeCellFactory<>(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                )
        );
        
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        appointmentTimeColumn.setCellFactory(
                new FormattedDateTimeCellFactory<>(
                        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                )
        );
        
        appointmentDoctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        appointmentDoctorColumn.setCellFactory(new PersonCellFactory<>());
        
        appointmentPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        appointmentPatientColumn.setCellFactory(new PersonCellFactory<>());
    }
    
    public static void setupPatientTable(
            TableColumn<Person,Image> patientPhotoColumn,
            TableColumn<Person,String> patientIdColumn,
            TableColumn<Person,Person> patientNameColumn,
            TableColumn<Person,String> patientPhoneColumn
    ) {
        setupPersonTable(
                patientPhotoColumn,
                patientIdColumn,
                patientNameColumn,
                patientPhoneColumn
        );
    }
    
    public static void setupDoctorTable(
            TableColumn<Person,Image> doctorPhotoColumn,
            TableColumn<Person,String> doctorIdColumn,
            TableColumn<Person,Person> doctorNameColumn,
            TableColumn<Person,String> doctorPhoneColumn,
            TableColumn<Doctor,ArrayList<Days>> doctorVisitDaysColumn,
            TableColumn<Doctor,String> doctorVisitTimeColumn,
            TableColumn<Doctor,ExaminationRoom> doctorRoomColumn,
            ResourceBundle rb
    ) {
        setupPersonTable(
                doctorPhotoColumn,
                doctorIdColumn,
                doctorNameColumn,
                doctorPhoneColumn
        );
        
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
                            if (available.contains(day)) {
                                result.append(rb.getString("generic.weekday.initial." + i));
                            }
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
    
    private static void setupPersonTable(
            TableColumn<Person,Image> photoColumn,
            TableColumn<Person,String> idColumn,
            TableColumn<Person,Person> nameColumn,
            TableColumn<Person,String> phoneColumn
    ) {
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        photoColumn.setCellFactory(new ImageCellFactory<>());
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        
        nameColumn.setCellValueFactory(data -> 
                new SimpleObjectProperty<>(data.getValue())
        );
        nameColumn.setCellFactory(new PersonCellFactory<>());
        
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephon"));
    }
}
