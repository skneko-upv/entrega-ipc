/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import gestorcitas.controllers.base.AbstractFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Appointment;
import model.Patient;
import utils.SlotWeek;

public class AppointmentFormController extends AbstractFormController<Appointment> {
    
    public static final String FREE = "Free";
    public static final String NOT_AVAILABLE = "Not Available";
    
    enum CellWeekKind { Free, NotAvailable, Taken }
    
    class CellWeek {
        CellWeekKind kind;
        Patient patient;
        
        CellWeek(String summary) {
            patient = null;
            switch (summary) {
                case FREE:
                    kind = CellWeekKind.Free;
                    break;
                case NOT_AVAILABLE:
                    kind = CellWeekKind.NotAvailable;
                    break;
                default:
                    kind = CellWeekKind.Taken;
                    // TODO: search patient
                    break;
            }
        }
    }
    
    protected ObservableList<Appointment> items;

    @FXML private Button prevWeekBtn;
    @FXML private Label weekLabel;
    @FXML private Button nextWeekBtn;
    
    @FXML private TableView<SlotWeek> weekTable;
    @FXML private TableColumn<SlotWeek,LocalTime> slotColumn;
    @FXML private TableColumn<SlotWeek,CellWeek> mondayColumn;
    @FXML private TableColumn<SlotWeek,CellWeek> tuesdayColumn;
    @FXML private TableColumn<SlotWeek,CellWeek> wednesdayColumn;
    @FXML private TableColumn<SlotWeek,CellWeek> thursdayColumn;
    @FXML private TableColumn<SlotWeek,CellWeek> fridayColumn;
    @FXML private TableColumn<SlotWeek,CellWeek> saturdayColumn;
    @FXML private TableColumn<SlotWeek,CellWeek> sundayColumn;
    
    @FXML private DatePicker visitDayPicker;
    @FXML private Label visitTimeLabel;
    @FXML private ChoiceBox<?> roomSelector;
    @FXML private Label roomDescriptionLabel;
    @FXML private ChoiceBox<?> patientSelector;
    @FXML private ChoiceBox<?> doctorSelector;
    @FXML private Label errorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        
        // TODO
    }
    
    public void setup(ObservableList<Appointment> items) {
        super.setup(true, null);
        this.items = items;
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
