package com.bivashy.javafx.authentication.controller;

import com.bivashy.javafx.authentication.StageWrapper;
import com.bivashy.javafx.authentication.database.UserAuthentication;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends BasicController {

    @FXML
    private Label registerLabel;
    @FXML
    private MFXIconWrapper returnIcon;
    @FXML
    private MFXTextField loginInput;
    @FXML
    private MFXTextField passwordInput;
    @FXML
    private MFXTextField passwordRepeatInput;

    public RegisterController(StageWrapper stageWrapper) {
        super(stageWrapper);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        registerLabel.setTextFill(Color.color(0, 0, 0));

        returnIcon.setIcon(new MaterialIconView(MaterialIcon.CHEVRON_LEFT, "24"));
        returnIcon.defaultRippleGeneratorBehavior();
    }

    public void onReturn(MouseEvent mouseEvent) {
        stageWrapper.openLogin();
    }

    public void register(ActionEvent actionEvent) {
        String login = loginInput.getText();
        String password = passwordInput.getText();
        if (!password.equals(passwordRepeatInput.getText())) {
            showError("Password doesn't match!", "Your passwords doesn't match.");
            return;
        }
        UserAuthentication userAuthentication = stageWrapper.getUserAuthentication();
        if (!userAuthentication.register(login, password)) {
            showError("Error!", "Unexpected error occurred, probably such account exits already.");
        } else {
            showInfo("Success!", "You've created account.");
        }
    }

}
