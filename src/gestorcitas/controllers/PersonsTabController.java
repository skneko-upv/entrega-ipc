/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import DBAccess.ClinicDBAccess;
import gestorcitas.controllers.helpers.ImageCellFactory;
import gestorcitas.controllers.helpers.PersonCellFactory;
import gestorcitas.controllers.helpers.PersonSearchPredicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import model.Appointment;
import model.Person;

public abstract class PersonsTabController extends TabController<Person> {
    
    @FXML protected Label tabTitle;
    @FXML protected TextField searchBox;
    
    @FXML protected TableView<Person> table;
    @FXML protected TableColumn<Person,Image> photoColumn;
    @FXML protected TableColumn<Person,String> idColumn;
    @FXML protected TableColumn<Person,Person> nameColumn;
    @FXML protected TableColumn<Person,String> phoneColumn;
    
    @FXML protected Button showBtn;
    @FXML protected Button removeBtn;
    
    public abstract String getSummary(Person person);
    
    @FXML protected abstract void onShow(ActionEvent event);

    @FXML protected abstract void onAdd(ActionEvent event);

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
        photoColumn.setCellFactory(new ImageCellFactory<>());
        
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
    
    @Override
    public void setItems(ObservableList<? extends Person> items) {
        super.setItems(items);
        table.setItems(this.itemsFiltered);
    }

    @FXML @Override
    protected void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        if (query.equals("")) {
            itemsFiltered.setPredicate(e -> true);
        } else {
            itemsFiltered.setPredicate(new PersonSearchPredicate(query));
        }
    }

    @FXML
    protected void onRemove(ActionEvent event) {
        int index = table.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= itemsFiltered.size()) return;
        
        Person toDelete = itemsFiltered.get(index);
        FilteredList<Appointment> conflicts = mainWindowController.getRemoveConflicts(
                toDelete, Appointment::getPatient
        );
        
        if (conflicts.size() > 0) {
            Alert removeConflict = new Alert(
                    Alert.AlertType.INFORMATION, 
                    rb.getString("modal.removeConflict.content")
            );
            removeConflict.setTitle(rb.getString("modal.removeConflict.title"));
            removeConflict.setHeaderText(rb.getString("modal.removeConflict.title"));
            removeConflict.showAndWait();
        } else {
            Alert remove = new Alert(Alert.AlertType.WARNING, 
                    rb.getString("modal.remove.content") 
                            + "\n\n\t" + getSummary(toDelete), 
                    ButtonType.YES, 
                    ButtonType.NO
            );
            Optional<ButtonType> removeResult = remove.showAndWait();
            removeResult.ifPresent(result -> {
                if (result == ButtonType.YES) items.remove(index);
            });
        }
    }
    
}
