/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers.helpers;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Appointment;
import model.Doctor;
import model.Patient;
import model.Person;

public class ItemRemoveHelper {
    
    public static void removeAppointment(
            ObservableList<Appointment> appointments,
            int index,
            ResourceBundle rb
    ) {
        if (index < 0 || index >= appointments.size()) return;
        
        Appointment toDelete = appointments.get(index);
        String summary = rb.getString("generic.appointment")
                + " "
                + DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.MEDIUM)
                        .format(toDelete.getAppointmentDateTime());
        removeWithConfirmation(
                appointments,
                index,
                rb,
                summary
        );
    }
    
    public static void removePatient(
            ObservableList<Appointment> appointments,
            ObservableList<Patient> list,
            int index,
            ResourceBundle rb
    ) {
        if (index < 0 || index >= list.size()) return;
        
        Patient toDelete = list.get(index);
        FilteredList<Appointment> conflicts = getRemoveConflicts(
                appointments, toDelete, Appointment::getPatient
        );
        if (conflicts.size() > 0) showConflictAlert(rb);
        else {
            removeWithConfirmation(
                    list,
                    index,
                    rb,
                    rb.getString("generic.patient") + " " + toDelete.getIdentifier()
            );
        }
    }
    
    public static void removeDoctor(
            ObservableList<Appointment> appointments,
            ObservableList<Doctor> list,
            int index,
            ResourceBundle rb
    ) {
        if (index < 0 || index >= list.size()) return;
        
        Doctor toDelete = list.get(index);
        FilteredList<Appointment> conflicts = getRemoveConflicts(
                appointments, toDelete, Appointment::getDoctor
        );
        if (conflicts.size() > 0) showConflictAlert(rb);
        else {
            removeWithConfirmation(
                    list,
                    index,
                    rb,
                    rb.getString("generic.doctor") + " " + toDelete.getIdentifier()
            );
        }
    }
    
    private static <T extends Person> FilteredList<Appointment> getRemoveConflicts(
            ObservableList<Appointment> appointments,
            T toDelete,
            Function<Appointment,T> personValueFactory
    ) {
        return appointments.filtered(appointment -> {
            return personValueFactory.apply(appointment)
                    .getIdentifier()
                    .equals(toDelete.getIdentifier());
        });
    }
    
    private static void showConflictAlert(ResourceBundle rb) {
        Alert removeConflict = new Alert(
                Alert.AlertType.INFORMATION, 
                rb.getString("modal.removeConflict.content")
        );
        removeConflict.setTitle(rb.getString("modal.removeConflict.title"));
        removeConflict.setHeaderText(rb.getString("modal.removeConflict.title"));
        removeConflict.showAndWait();
    }
    
    private static <T> void removeWithConfirmation(
            ObservableList<T> list,
            int index,
            ResourceBundle rb,
            String summary
    ) {
        if (index >= 0 || index < list.size()) {
            Alert remove = new Alert(Alert.AlertType.WARNING, 
                    rb.getString("modal.remove.content") + "\n\n\t" + summary, 
                    ButtonType.YES, 
                    ButtonType.NO
            );
            Optional<ButtonType> removeResult = remove.showAndWait();
            if (removeResult.isPresent() && removeResult.get() == ButtonType.YES) {
                list.remove(index);
            }
        }
    }
}
