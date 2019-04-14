/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.helpers;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * Functional interface that given a TableColumn displaying instances
 * of DateTime creates instances of TableCell containing String resulting from
 * formatting said date with the given formatter.
 */
public class FormattedDateTimeCellFactory<T,R extends ChronoLocalDateTime<?>> implements Callback<
        TableColumn<T,R>,
        TableCell<T,R>
    >
{
    private final DateTimeFormatter formatter;
    
    public FormattedDateTimeCellFactory(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }
    
    @Override
    public TableCell<T,R> call(TableColumn<T,R> column) {
        return new TableCell<T,R>() {
            @Override
            protected void updateItem(R date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) setText(null);
                else {
                    setText(formatter.format(date));
                    if (date.isBefore(LocalDateTime.now())) {
                        setTextFill(Color.RED);
                    }
                }
            }
        };
    }
}