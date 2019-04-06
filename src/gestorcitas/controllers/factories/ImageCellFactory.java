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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Functional interface that given a TableColumn displaying instances
 * of Image creates instances of TableCell set to display said image using
 * a ImageView.
 */
public class ImageCellFactory<T> implements Callback<
        TableColumn<T,Image>,
        TableCell<T,Image>
    >
{
    @Override
    public TableCell<T,Image> call(TableColumn<T,Image> column) {
        return new TableCell<T,Image>() {
            private final ImageView view = new ImageView();
            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                if (empty || image == null) setGraphic(null);
                else {
                    view.setImage(image);
                    setGraphic(view);
                }
            }
        };
    }
}