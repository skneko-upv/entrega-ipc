/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.application;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestorCitas extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = Locale.getDefault(); // TODO: handle invalid locales
        ResourceBundle rb = ResourceBundle.getBundle("gestorcitas.resources.locales", locale);
        
        Parent root = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"), rb);
        Scene scene = new Scene(root);
        
        stage.setTitle(rb.getString("window.main.title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
