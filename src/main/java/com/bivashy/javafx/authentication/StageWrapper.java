package com.bivashy.javafx.authentication;

import com.bivashy.javafx.authentication.controller.LoginController;
import com.bivashy.javafx.authentication.controller.RegisterController;
import com.bivashy.javafx.authentication.database.JDBCUserDatabase;
import com.bivashy.javafx.authentication.database.UserDatabase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.function.Supplier;

public class StageWrapper {

    private final UserDatabase userDatabase = new JDBCUserDatabase("jdbc:postgresql://localhost:5433/postgres", "username", "password");
    private final Stage stage;

    public StageWrapper(Stage stage) {
        this.stage = stage;
    }

    public void openLogin() {
        Parent root = loadFxml("view/main-view.fxml", () -> new LoginController(this));
        loadView(root, "Authentication");
    }

    public void openRegister() {
        Parent root = loadFxml("view/register-view.fxml", () -> new RegisterController(this));
        loadView(root, "Register");
    }

    public Stage getStage() {
        return stage;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

    private void loadView(Parent root, String title) {
        Scene scene = stage.getScene();
        if (scene != null) {
            scene.setRoot(root);
            stage.setTitle(title);
            return;
        }
        Scene newScene = new Scene(root);
        newScene.setFill(Color.TRANSPARENT);
        stage.setScene(newScene);
        stage.setTitle(title);
        if (!stage.isShowing()) {
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }
    }

    private Parent loadFxml(String name) {
        return loadFxml(name, null);
    }

    private Parent loadFxml(String name, Supplier<?> controllerSupplier) {
        FXMLLoader loader = new FXMLLoader(ResourceLoader.loadURL(name));
        if (controllerSupplier != null)
            loader.setControllerFactory(c -> controllerSupplier.get());
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
