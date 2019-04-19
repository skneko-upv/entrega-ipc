/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.application;

import com.sun.javafx.css.StyleManager;
import gestorcitas.controllers.MainWindowController;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestorCitas extends Application {

    public final Locale[] ALLOWED_LOCALES = new Locale[]{
        new Locale("en", "us"),
        new Locale("es", "es")
    };
    public static final Locale DEFAULT_LOCALE = new Locale("en", "us");

    public final double MIN_STAGE_HEIGHT = 350;
    public final double MIN_STAGE_WIDTH = 410;

    @Override
    public void start(Stage stage) throws Exception {
        start(stage, Locale.getDefault());
    }

    public void start(Stage stage, Locale locale) throws Exception {
        if (Arrays.asList(ALLOWED_LOCALES).contains(locale)) {
            startWithLocale(stage, locale);
        } else {
            startWithLocale(stage, DEFAULT_LOCALE);
        }
    }

    private void startWithLocale(Stage stage, Locale locale) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("gestorcitas.resources.locales.strings", locale);

        Application.setUserAgentStylesheet(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestorcitas/views/MainWindow.fxml"), rb);
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);

        stage.setTitle(rb.getString("app.title"));
        stage.setMinHeight(MIN_STAGE_HEIGHT);
        stage.setMinWidth(MIN_STAGE_WIDTH);
        stage.setScene(scene);
        stage.show();

        StyleManager.getInstance().addUserAgentStylesheet("/gestorcitas/resources/css/style.css");
        StyleManager.getInstance().addUserAgentStylesheet("/gestorcitas/resources/css/custom.css");

        // Save database on application close
        stage.setOnCloseRequest((event) -> {
            ((MainWindowController) loader.getController()).saveDBAndQuit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
