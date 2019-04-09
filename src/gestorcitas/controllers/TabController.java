/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import DBAccess.ClinicDBAccess;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

public abstract class TabController<T> implements Initializable {
    
    protected MainWindowController mainWindowController;
    
    protected ResourceBundle rb;
    protected final ClinicDBAccess clinic = ClinicDBAccess.getSingletonClinicDBAccess();
    
    protected ObservableList<T> items;
    protected FilteredList<T> itemsFiltered;
    
    @FXML protected TableView<T> table;
    
    public abstract void setTitle(String title);
    public abstract String getSummary(T item);
    public abstract void loadDataFromDB();
    
    protected abstract boolean canDelete(T toDelete);
    
    @FXML
    protected abstract void onSearch(ActionEvent event);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }
    
    public void setMainController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
    
    public void setItems(ObservableList<? extends T> items) {
        this.items = (ObservableList<T>)items;
        this.itemsFiltered = this.items.filtered(e -> true);
    }
    
    public ObservableList<T> getItems() {
        return this.items;
    }
    
    @FXML
    protected void onRemove(ActionEvent event) {
        int index = table.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= itemsFiltered.size()) return;
        
        T toDelete = itemsFiltered.get(index);
        if (canDelete(toDelete)) {
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
