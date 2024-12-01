package com.bivashy.javafx.authentication.controller;

import com.bivashy.javafx.authentication.StageWrapper;
import com.bivashy.javafx.authentication.database.UserAuthentication;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BasicController {

    @FXML
    private MFXTextField loginField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private StackPane logoContainer;

    public LoginController(StageWrapper stageWrapper) {
        super(stageWrapper);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        loadIcon(logoContainer, "asset/logo.png", 64, 64);
    }

    @FXML
    private void onRegisterButtonClick() throws IOException {
        stageWrapper.openRegister();
    }

    public void login(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passwordField.getText();
        UserAuthentication userAuthentication = stageWrapper.getUserAuthentication();
        if (!userAuthentication.login(login, password)) {
            showError("Try again!", "Invalid login or password.");
        } else {
            showInfo("Success!", "You've logged in.");
        }
    }

}