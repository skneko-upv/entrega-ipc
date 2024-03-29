/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package gestorcitas.controllers.base;

import static gestorcitas.controllers.MainWindowController.DEFAULT_PHOTO;
import gestorcitas.controllers.helpers.ValidatedTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Person;

public abstract class BasePersonFormController<T extends Person>
        extends AbstractFormController<T> {

    public static final int MAX_NAME_LENGTH = 20;
    public static final int MAX_SURNAME_LENGTH = 40;
    public static final int MAX_ID_LENGTH = 9;
    public static final int MAX_PHONE_LENGTH = 9;
    public static final int MAX_PHOTO_WIDTH = 400;
    public static final int MAX_PHOTO_HEIGHT = 400;

    protected ObservableList<T> persons;

    protected ValidatedTextField name;
    protected ValidatedTextField surname;
    protected ValidatedTextField id;
    protected ValidatedTextField phone;
    protected final ObjectProperty<Image> photo = new SimpleObjectProperty<>();

    @FXML
    protected TextField nameField;
    @FXML
    protected Label nameErrorLabel;
    @FXML
    protected TextField surnameField;
    @FXML
    protected Label surnameErrorLabel;
    @FXML
    protected TextField idField;
    @FXML
    protected Label idErrorLabel;
    @FXML
    protected TextField phoneField;
    @FXML
    protected Label phoneErrorLabel;
    @FXML
    protected ImageView photoPreview;
    @FXML
    protected Button photoChangeBtn;
    @FXML
    protected Label photoErrorLabel;
    @FXML
    protected CheckBox photoConsentBox;

    public void setup(boolean editMode, T prefill, ObservableList<T> persons) {
        this.persons = persons;

        // Set up form field constraints
        name = new ValidatedTextField(nameField, nameErrorLabel, MAX_NAME_LENGTH, editMode);
        surname = new ValidatedTextField(surnameField, surnameErrorLabel, MAX_SURNAME_LENGTH, editMode);
        id = new ValidatedTextField(idField, idErrorLabel, MAX_ID_LENGTH, editMode);
        phone = new ValidatedTextField(phoneField, phoneErrorLabel, MAX_PHONE_LENGTH, editMode);

        name.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        surname.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        id.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        id.addValidator(value -> {
            if (!value.matches("(?:\\d{8}\\w)|(?:[x-zX-Z]\\d{7}\\w)")) {  // DNI: 00000000A 
                // or NIE: X0000000A 
                // (where 0 = [0-9], A = [a-zA-Z], X = [x-zX-Z])
                return rb.getString("form.person.error.dniPattern");
            } else {
                return null;
            }
        });
        phone.addValidator(ValidatedTextField.getNotEmptyValidator(rb));
        phone.addValidator(value -> {
            if (!value.matches("[0-9]+")) {
                return rb.getString("form.person.error.phoneNotOnlyNumbers");
            } else {
                return null;
            }
        });

        // Set up listeners
        photo.addListener((image, oldImage, newImage) -> {
            if (newImage != null) {
                photoPreview.setImage(newImage);
            }
        });

        photoChangeBtn.disableProperty().bind(
                Bindings.not(photoConsentBox.selectedProperty())
        );

        super.setup(editMode, prefill);
    }

    @Override
    protected void setClearAll() {
        name.setClear();
        surname.setClear();
        id.setClear();
        phone.setClear();
        photoErrorLabel.setVisible(false);
    }

    @Override
    protected boolean validateAll() {
        return name.validate()
                & surname.validate()
                & id.validate()
                & phone.validate();
    }

    @Override
    protected void prefill() {
        name.setText(prefill.getName());
        surname.setText(prefill.getSurname());
        id.setText(prefill.getIdentifier());
        phone.setText(prefill.getTelephon());

        Image savedPhoto = prefill.getPhoto();
        if (savedPhoto != null) {
            photoPreview.setImage(savedPhoto);
        } else {
            Image placeholder = new Image(
                    getClass().getResource(DEFAULT_PHOTO).toString()
            );
            photoPreview.setImage(placeholder);
        }
        photoConsentBox.setSelected(prefill.getPhoto() != null);
    }

    @Override
    protected void setUneditableAll() {
        name.setEditable(false);
        surname.setEditable(false);
        id.setEditable(false);
        phone.setEditable(false);
        photoChangeBtn.setVisible(false);
        photoConsentBox.setDisable(true);
    }

    @FXML
    protected void onChangePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("modal.photoSelect.title"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        rb.getString("modal.photoSelect.filter.img"),
                        "*.png", "*.jpg", "*.jpeg"
                )
        );
        File selectedFile = fileChooser.showOpenDialog(
                ((Node) event.getSource()).getScene().getWindow()
        );
        if (selectedFile != null) {
            try {
                Image loaded = new Image(new FileInputStream(selectedFile.getAbsolutePath()));
                if (loaded.getWidth() > MAX_PHOTO_WIDTH
                        || loaded.getHeight() > MAX_PHOTO_HEIGHT) {
                    photoErrorLabel.setText(
                            rb.getString("form.img.error.tooBig")
                            + MAX_PHOTO_WIDTH + "x"
                            + MAX_PHOTO_HEIGHT + "."
                    );
                    photoErrorLabel.setVisible(true);
                } else {
                    photo.setValue(loaded);
                    photoErrorLabel.setVisible(false);
                }
            } catch (FileNotFoundException e) {
                photoErrorLabel.setText(rb.getString("generic.error.fileNotFound"));
                photoErrorLabel.setVisible(true);
            }
        }
    }
}
