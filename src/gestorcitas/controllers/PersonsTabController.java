/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

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
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
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
    
    @FXML 
    protected abstract void onShow(ActionEvent event);

    @FXML 
    protected abstract void onAdd(ActionEvent event);
    
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
    
    @Override
    protected boolean canDelete(Person toDelete) {
        FilteredList<Appointment> conflicts = mainWindowController.getRemoveConflicts(
                toDelete, Appointment::getPatient
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
