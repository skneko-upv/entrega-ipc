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
        int start = s.lastIndexOf('(');
        int end = s.lastIndexOf(')');
        String id = s.substring(start + 1, end);
        
        return list.stream()
                .filter(e -> e.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString(T item) {
        return item.getSurname() + ", " + item.getName()
                + " (" + item.getIdentifier() + ")";
    }
}
