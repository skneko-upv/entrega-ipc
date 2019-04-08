/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers.helpers;

import DBAccess.ClinicDBAccess;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class DatabaseHelper { 
    
    public static void save(ClinicDBAccess db, ResourceBundle rb) {
        boolean retry = false;
        do {
            Alert saveWait = new Alert(Alert.AlertType.INFORMATION);
            saveWait.setTitle(rb.getString("generic.wait"));
            saveWait.setHeaderText(rb.getString("modal.saveWait.header"));
            saveWait.setContentText(rb.getString("modal.saveWait.content"));
            saveWait.getButtonTypes().clear();
            saveWait.show();

            boolean success = db.saveDB();

            saveWait.setResult(ButtonType.OK);
            saveWait.close();
            if (!success) {
                ButtonType saveFailRetry = new ButtonType(
                        rb.getString("generic.retry"), 
                        ButtonBar.ButtonData.OK_DONE
                );
                ButtonType saveFailDesist = new ButtonType(
                        rb.getString("generic.cancel"), 
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );
                
                Alert saveFail = new Alert(
                        Alert.AlertType.ERROR,
                        rb.getString("modal.saveFail.content"),
                        saveFailRetry,
                        saveFailDesist
                );
                saveFail.setTitle(rb.getString("generic.error"));
                saveFail.setHeaderText(rb.getString("modal.saveFail.header"));
                saveFail.getButtonTypes().setAll(saveFailRetry, saveFailDesist);
                
                Optional<ButtonType> saveFailResult = saveFail.showAndWait();
                retry = saveFailResult.isPresent() 
                        && saveFailResult.get() == saveFailRetry;
            }
        } while (retry);
    }
    
}
