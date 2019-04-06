/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.application;

import gestorcitas.controllers.MainWindowController;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestorCitas extends Application {
    
    public final double MIN_STAGE_HEIGHT = 350;
    public final double MIN_STAGE_WIDTH = 410;
    
    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = Locale.getDefault(); // TODO: handle invalid locales
        ResourceBundle rb = ResourceBundle.getBundle("gestorcitas.resources.locales.strings", locale);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestorcitas/views/MainWindowView.fxml"), rb);
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        
        stage.setTitle(rb.getString("window.main.title"));
        stage.setMinHeight(MIN_STAGE_HEIGHT);
        stage.setMinWidth(MIN_STAGE_WIDTH);
        stage.setScene(scene);
        stage.show();
        
        // Save database on application close
        stage.setOnCloseRequest((event) -> {
            ((MainWindowController)loader.getController()).saveDBAndQuit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
