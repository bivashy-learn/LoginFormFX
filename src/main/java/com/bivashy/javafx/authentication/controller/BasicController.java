package com.bivashy.javafx.authentication.controller;

import com.bivashy.javafx.authentication.ResourceLoader;
import com.bivashy.javafx.authentication.StageWrapper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
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
    protected MFXGenericDialog dialogContent;
    protected MFXStageDialog dialog;

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

        this.dialogContent = MFXGenericDialogBuilder.build()
                .makeScrollable(true)
                .get();
        this.dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(stage)
                .initModality(Modality.APPLICATION_MODAL)
                .setDraggable(true)
                .setTitle("Login")
                .setOwnerNode(rootPane)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        dialogContent.addActions(
                Map.entry(new MFXButton("Ok"), event -> dialog.close()),
                Map.entry(new MFXButton("Cancel"), event -> dialog.close())
        );

        dialogContent.setMaxSize(128, 64);
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

    protected void showError(String header, String content) {
        showDialog(new MFXFontIcon("fas-circle-xmark", 18), header, content, "mfx-error-dialog");
    }

    protected void showInfo(String header, String content) {
        showDialog(new MFXFontIcon("fas-circle-info", 18), header, content, "mfx-info-dialog");
    }

    protected void showDialog(MFXFontIcon icon, String header, String content, String style) {
        dialogContent.setHeaderIcon(icon);
        dialogContent.setHeaderText(header);
        dialogContent.setContentText(content);
        convertDialogTo(style);
        dialog.showDialog();
    }

    protected void convertDialogTo(String styleClass) {
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog") || s.equals("mfx-error-dialog")
        );

        if (styleClass != null)
            dialogContent.getStyleClass().add(styleClass);
    }

}
