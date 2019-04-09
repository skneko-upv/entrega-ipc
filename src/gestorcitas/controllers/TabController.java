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
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public abstract class TabController<T> implements Initializable {
    
    protected MainWindowController mainWindowController;
    
    protected ResourceBundle rb;
    protected final ClinicDBAccess clinic = ClinicDBAccess.getSingletonClinicDBAccess();
    
    protected ObservableList<T> items;
    protected FilteredList<T> itemsFiltered;
    
    @FXML protected abstract void onSearch(ActionEvent event);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }
    
    public void initialize(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
    
    public void setItems(ObservableList<? extends T> items) {
        this.items = (ObservableList<T>)items;
        this.itemsFiltered = this.items.filtered(e -> true);
    }
    
    public ObservableList<T> getItems() {
        return this.items;
    }
}
