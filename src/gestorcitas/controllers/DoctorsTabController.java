/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import model.Doctor;
import model.Person;

public class DoctorsTabController extends PersonsTabController {

    @FXML
    private Label tabTitle;
    @FXML
    private TextField searchBox;
    @FXML
    private TableView<?> table;
    
    @FXML
    private TableColumn<?, ?> photoColumn;
    @FXML
    private TableColumn<?, ?> idColumn;
    @FXML
    private TableColumn<?, ?> nameColumn;
    @FXML
    private TableColumn<?, ?> phoneColumn;
    @FXML
    private TableColumn<?, ?> visitDaysColumn;
    @FXML
    private TableColumn<?, ?> visitTimeColumn;
    @FXML
    private TableColumn<?, ?> roomColumn;
    
    @FXML
    private Button showBtn;
    @FXML
    private Button removeBtn;
    
    @Override
    public String getSummary(Person doctor) {
        return rb.getString("generic.doctor") + " " + doctor.getIdentifier();
    }
    
    @Override
    public void loadDataFromDB() {
        ObservableList<Doctor> loadedItems = 
                FXCollections.observableArrayList(clinic.getDoctors());
        setItems(loadedItems);
    }

    /**
     * Initializes the controller class.
     *
     * @param url Current controller location.
     * @param rb Resources used to localize this controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url,rb);
        
        loadDataFromDB();
    }    

    @FXML
    protected void onShow(ActionEvent event) {
    }

    @FXML
    protected void onAdd(ActionEvent event) {
    }
    
}
