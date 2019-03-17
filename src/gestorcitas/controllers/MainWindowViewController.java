/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 */
public class MainWindowViewController implements Initializable {

    private ResourceBundle rb;

    /**
     * Initializes the controller class.
     * 
     * @param url Current controller location.
     * @param rb Resources used to localize this controller.
     */
    @Override
    public void initialize(@SuppressWarnings("unused") URL url, ResourceBundle rb) {
        this.rb = rb;
    }
    
}
