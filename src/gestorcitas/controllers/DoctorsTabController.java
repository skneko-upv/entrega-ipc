/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers;

import gestorcitas.controllers.base.PersonsTabController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;

public class DoctorsTabController extends PersonsTabController<Doctor> {

    @FXML
    private TableColumn<Doctor, ArrayList<Days>> visitDaysColumn;
    @FXML
    private TableColumn<Doctor, String> visitTimeColumn;
    @FXML
    private TableColumn<Doctor, ExaminationRoom> roomColumn;

    @Override
    public String getSummary(Doctor doctor) {
        return rb.getString("generic.doctor") + " " + doctor.getIdentifier();
    }

    @Override
    public void loadDataFromDB() {
        ObservableList<Doctor> loadedItems
                = FXCollections.observableArrayList(clinic.getDoctors());
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
        visitDaysColumn.setCellValueFactory(new PropertyValueFactory<>("visitDays"));
        visitDaysColumn.setCellFactory(column -> {
            TableCell<Doctor, ArrayList<Days>> cell = new TableCell<Doctor, ArrayList<Days>>() {
                @Override
                protected void updateItem(ArrayList<Days> available, boolean empty) {
                    if (empty || available == null) {
                        setText(null);
                    } else {
                        StringBuilder result = new StringBuilder();
                        int i = 0;
                        for (Days day : Days.values()) {
                            if (available.contains(day)) {
                                result.append(rb.getString("generic.weekday.initial." + i));
                            } else {
                                result.append("-");
                            }
                            i++;
                        }
                        setText(result.toString());
                    }
                }
            };
            cell.setStyle("-fx-font-family: monospace");
            return cell;
        });

        visitTimeColumn.setCellValueFactory(data -> {
            Doctor doctor = data.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
            String start = formatter.format(doctor.getVisitStartTime());
            String end = formatter.format(doctor.getVisitEndTime());
            return new SimpleStringProperty(start + "-" + end);
        });
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("examinationRoom"));
        roomColumn.setCellFactory(column -> {
            TableCell<Doctor, ExaminationRoom> cell = new TableCell<Doctor, ExaminationRoom>() {
                @Override
                protected void updateItem(ExaminationRoom room, boolean empty) {
                    if (empty || room == null) {
                        setText(null);
                    } else {
                        setText(String.valueOf(room.getIdentNumber()));
                    }
                }
            };
            return cell;
        });

        loadDataFromDB();
    }

    @FXML
    @Override
    public void onAdd(ActionEvent event) {
        launchForm(true, null);
    }

    @Override
    protected Function<Appointment, Doctor> getAppointmentValueFactory() {
        return Appointment::getDoctor;
    }

    @FXML
    @Override
    protected void onShow(ActionEvent event) {
        int index = itemsFiltered.getSourceIndex(
                table.getSelectionModel().getSelectedIndex()
        );
        launchForm(false, items.get(index));
    }

    private void launchForm(boolean editMode, Doctor prefill) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestorcitas/views/DoctorForm.fxml"), rb);
            Parent formRoot = (Parent) loader.load();

            DoctorFormController form = loader.<DoctorFormController>getController();
            form.setup(editMode, prefill, items, clinic.getExaminationRooms());

            Scene scene = new Scene(formRoot);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(rb.getString(
                    "modal.doctorForm.title." + (editMode ? "add" : "show")
            ));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
