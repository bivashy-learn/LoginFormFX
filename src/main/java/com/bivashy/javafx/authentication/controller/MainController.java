package com.bivashy.javafx.authentication.controller;

import com.bivashy.javafx.authentication.ResourceLoader;
import com.bivashy.javafx.authentication.StageWrapper;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BasicController {

    @FXML
    private StackPane logoContainer;

    public MainController(StageWrapper stageWrapper) {
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

}