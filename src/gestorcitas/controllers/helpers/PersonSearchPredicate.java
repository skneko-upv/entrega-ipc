/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.helpers;

import java.util.function.Predicate;
import model.Person;

public class PersonSearchPredicate<T extends Person> implements Predicate<T> {

    private String query;

    public PersonSearchPredicate(String query) {
        this.query = query.trim().toLowerCase();
    }

    @Override
    public boolean test(T p) {
        if (query.equals("")) {
            return true;
        } else {
            return p.getIdentifier().toLowerCase().contains(query)
                    || p.getName().toLowerCase().contains(query)
                    || p.getSurname().toLowerCase().contains(query)
                    || p.getTelephon().toLowerCase().contains(query);
        }
    }

}
