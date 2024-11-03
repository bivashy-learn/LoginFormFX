package com.bivashy.javafx.authentication.controller;

import com.bivashy.javafx.authentication.ResourceLoader;
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
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final Stage stage;
    private double xOffset;
    private double yOffset;
    @FXML
    private HBox windowHeader;
    @FXML
    private MFXFontIcon closeIcon;
    @FXML
    private MFXFontIcon minimizeIcon;
    @FXML
    private MFXFontIcon alwaysOnTopIcon;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label registerLabel;
    @FXML
    private StackPane logoContainer;

    public MainController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());
        minimizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> ((Stage) rootPane.getScene().getWindow()).setIconified(true));
        alwaysOnTopIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            boolean newVal = !stage.isAlwaysOnTop();
            alwaysOnTopIcon.pseudoClassStateChanged(PseudoClass.getPseudoClass("always-on-top"), newVal);
            stage.setAlwaysOnTop(newVal);
        });

        windowHeader.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        windowHeader.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        loadIcon("asset/logo.png", 64, 64);
    }

    @FXML
    private void onRegisterButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL("view/register-view.fxml"));
        loader.setControllerFactory(controller -> new RegisterController(stage));
        Parent registerRoot = loader.load();

        Stage registerStage = new Stage();
        registerStage.initModality(Modality.APPLICATION_MODAL);
        registerStage.initOwner(stage);
        registerStage.setTitle("Register");
        registerStage.setScene(new Scene(registerRoot));
        registerStage.show();
    }

    private void loadIcon(String path, int width, int height) {
        Image image = new Image(ResourceLoader.load(path), width, height, true, true);
        ImageView logo = new ImageView(image);
        Circle clip = new Circle(30);
        clip.centerXProperty().bind(Bindings.createDoubleBinding(() -> logo.getLayoutBounds().getCenterX(), logo.layoutBoundsProperty()));
        clip.centerYProperty().bind(Bindings.createDoubleBinding(() -> logo.getLayoutBounds().getCenterY(), logo.layoutBoundsProperty()));
        logo.setClip(clip);
        logoContainer.getChildren().add(logo);
    }

}