/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.factories;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import model.Person;


/**
 * Functional interface that given a TableColumn displaying instances
 * of Person creates instances of TableCell containing the surname and name
 * of each Person separated by a comma.
 */
public class PersonCellFactory<T> implements Callback<
        TableColumn<T,Person>,
        TableCell<T,Person>
    >
{
    @Override
    public TableCell<T,Person> call(TableColumn<T,Person> column) {
        return new TableCell<T,Person>() {
            @Override
            protected void updateItem(Person person, boolean empty) {
                super.updateItem(person, empty);
                if (empty || person == null) setText(null);
                else setText(person.getSurname() + ", " + person.getName());
            }
        };
    }
}
