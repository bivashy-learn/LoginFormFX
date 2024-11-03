package com.bivashy.javafx.authentication.controller;

import com.bivashy.javafx.authentication.ResourceLoader;
import com.bivashy.javafx.authentication.StageWrapper;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BasicController implements Initializable {

    protected final StageWrapper stageWrapper;
    protected final Stage stage;
    private double xOffset;
    private double yOffset;
    @FXML
    protected HBox windowHeader;
    @FXML
    protected MFXFontIcon closeIcon;
    @FXML
    protected MFXFontIcon minimizeIcon;
    @FXML
    protected MFXFontIcon alwaysOnTopIcon;
    @FXML
    protected AnchorPane rootPane;

    public BasicController(StageWrapper stageWrapper) {
        this.stageWrapper = stageWrapper;
        this.stage = stageWrapper.getStage();
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
    }

    protected void loadIcon(StackPane logoContainer, String path, int width, int height) {
        Image image = new Image(ResourceLoader.load(path), width, height, true, true);
        ImageView logo = new ImageView(image);
        Circle clip = new Circle(30);
        clip.centerXProperty().bind(Bindings.createDoubleBinding(() -> logo.getLayoutBounds().getCenterX(), logo.layoutBoundsProperty()));
        clip.centerYProperty().bind(Bindings.createDoubleBinding(() -> logo.getLayoutBounds().getCenterY(), logo.layoutBoundsProperty()));
        logo.setClip(clip);
        logoContainer.getChildren().add(logo);
    }

}
