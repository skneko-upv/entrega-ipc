/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.helpers;

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
    
    private Image placeholder = null;
    
    public ImageCellFactory<T> withDefault(Image placeholder) {
        this.placeholder = placeholder;
        return this;
    }
    
    @Override
    public TableCell<T,Image> call(TableColumn<T,Image> column) {
        return new TableCell<T,Image>() {
            private final ImageView view = new ImageView();
            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                
                if (empty) {
                    setGraphic(null);
                    return;
                }
                
                Image toUse;
                if (image == null && placeholder != null) {
                    toUse = placeholder;
                } else {
                    toUse = image;
                }
                view.setPreserveRatio(true);
                view.fitWidthProperty().bind(column.widthProperty());
                view.setImage(toUse);
                setGraphic(view);
            }
        };
    }
}