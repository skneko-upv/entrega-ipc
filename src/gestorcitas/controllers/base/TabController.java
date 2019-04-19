/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.base;

import DBAccess.ClinicDBAccess;
import gestorcitas.controllers.MainWindowController;
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

    @FXML
    protected TableView<T> table;

    public abstract void setTitle(String title);

    public abstract String getSummary(T item);

    public abstract void loadDataFromDB();

    protected abstract boolean canDelete(T toDelete);

    @FXML
    public abstract void onAdd(ActionEvent event);

    @FXML
    protected abstract void onShow(ActionEvent event);

    @FXML
    protected abstract void onSearch(ActionEvent event);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }

    public void setMainController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @SuppressWarnings("unchecked")
    public void setItems(ObservableList<? extends T> items) {
        this.items = (ObservableList<T>) items;
        this.itemsFiltered = this.items.filtered(e -> true);
        table.setItems(this.itemsFiltered);
    }

    public ObservableList<T> getItems() {
        return this.items;
    }

    @FXML
    protected void onRemove(ActionEvent event) {
        int index = itemsFiltered.getSourceIndex(
                table.getSelectionModel().getSelectedIndex()
        );
        if (index < 0 || index >= items.size()) {
            return;
        }

        T toDelete = items.get(index);
        if (canDelete(toDelete)) {
            Alert remove = new Alert(Alert.AlertType.WARNING,
                    rb.getString("modal.remove.content")
                    + "\n\n\t" + getSummary(toDelete),
                    ButtonType.YES,
                    ButtonType.NO
            );
            Optional<ButtonType> removeResult = remove.showAndWait();
            removeResult.ifPresent(result -> {
                if (result == ButtonType.YES) {
                    items.remove(index);
                }
            });
        }
    }

}
