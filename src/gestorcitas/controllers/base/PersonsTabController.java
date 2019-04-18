/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers.base;

import static gestorcitas.controllers.MainWindowController.DEFAULT_PHOTO;
import gestorcitas.controllers.helpers.ImageCellFactory;
import gestorcitas.controllers.helpers.PersonCellFactory;
import gestorcitas.controllers.helpers.PersonSearchPredicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import model.Appointment;
import model.Person;

public abstract class PersonsTabController<T extends Person> 
        extends TabController<T> {
    
    @FXML protected Label tabTitle;
    @FXML protected TextField searchBox;
    
    @FXML protected TableColumn<T,Image> photoColumn;
    @FXML protected TableColumn<T,String> idColumn;
    @FXML protected TableColumn<T,Person> nameColumn;
    @FXML protected TableColumn<T,String> phoneColumn;
    
    @FXML protected Button showBtn;
    @FXML protected Button removeBtn;
    
    protected abstract Function<Appointment,T> getAppointmentValueFactory();
    
    @Override
    public void setTitle(String title) {
        tabTitle.setText(title);
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
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        photoColumn.setCellFactory(new ImageCellFactory<T>().withDefault(
                new Image(getClass().getResource(DEFAULT_PHOTO).toString())
        ));
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        
        nameColumn.setCellValueFactory(data -> 
                new SimpleObjectProperty<>(data.getValue())
        );
        nameColumn.setCellFactory(new PersonCellFactory<>());
        
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephon"));
        
        // Set up event handlers
        table.getSelectionModel().selectedItemProperty().addListener((val, oldVal, newVal) -> {
            boolean noneSelected = newVal == null;
            showBtn.setDisable(noneSelected);
            removeBtn.setDisable(noneSelected);
        });
    }

    @FXML @Override
    protected void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        if (query.equals("")) {
            itemsFiltered.setPredicate(e -> true);
        } else {
            itemsFiltered.setPredicate(new PersonSearchPredicate<>(query));
        }
    }
    
    @Override
    protected boolean canDelete(T toDelete) {
        FilteredList<Appointment> conflicts = mainWindowController.getRemoveConflicts(
                toDelete, getAppointmentValueFactory()
        );
        
        boolean canDelete = conflicts.isEmpty();
        if (!canDelete) {
            Alert removeConflict = new Alert(
                    Alert.AlertType.INFORMATION, 
                    rb.getString("modal.removeConflict.content")
            );
            removeConflict.setTitle(rb.getString("modal.removeConflict.title"));
            removeConflict.setHeaderText(rb.getString("modal.removeConflict.title"));
            removeConflict.showAndWait();
        }
        return canDelete;
    }
    
}
