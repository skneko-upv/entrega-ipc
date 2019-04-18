/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.helpers;

import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import model.Person;

public class PersonStringConverter<T extends Person> extends StringConverter<T> {

    private final ObservableList<T> list;

    public PersonStringConverter(ObservableList<T> list) {
        this.list = list;
    }

    @Override
    public T fromString(String s) {
        return list.filtered(new PersonSearchPredicate<>(s)).get(0);
    }

    @Override
    public String toString(T item) {
        return item.getSurname() + ", " + item.getName()
                + " (" + item.getIdentifier() + ")";
    }
}
