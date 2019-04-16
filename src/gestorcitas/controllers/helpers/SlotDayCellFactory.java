/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.helpers;

import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import model.Patient;
import utils.SlotWeek;

public class SlotDayCellFactory implements Callback<
        TableColumn<SlotWeek,String>,
        TableCell<SlotWeek,String>
    >
{
    public static final String FREE = "Free";
    public static final String UNAVAILABLE = "Not Available";
    
    private final ObservableList<Patient> patients;
    private final ResourceBundle rb;
    
    public SlotDayCellFactory(ObservableList<Patient> patients, ResourceBundle rb) {
        this.patients = patients;
        this.rb = rb;
    }
    
    @Override
    public TableCell<SlotWeek,String> call(TableColumn<SlotWeek,String> column) {
        return new TableCell<SlotWeek,String>() {
            @Override
            protected void updateItem(String day, boolean empty) {
                super.updateItem(day, empty);
                getStyleClass().remove("cell-free");
                getStyleClass().remove("cell-unavailable");
                getStyleClass().remove("cell-taken");
                if (empty || day == null) setText(null);
                else {
                    if (day.equals(FREE)) {
                        getStyleClass().add("cell-free");
                        setText(rb.getString("modal.appointmentForm.cell.free"));
                    } else if (day.equals(UNAVAILABLE)) {
                        getStyleClass().add("cell-unavailable");
                        setText(rb.getString("modal.appointmentForm.cell.unavailable"));
                    } else {
                        getStyleClass().add("cell-taken");
                        Patient found = patients.stream()
                                .filter(p -> p.getIdentifier().equals(day))
                                .findFirst()
                                .orElse(null);

                        String text = rb.getString("modal.appointmentForm.cell.taken");
                        if (found != null) {
                            text += "\n" + found.getSurname() + ", " + found.getName() 
                                    + "(" + found.getIdentifier() + ")";
                        }
                        setText(text);
                    }
                }
            }
        };
    }
}
