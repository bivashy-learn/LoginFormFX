module com.bivashy.bukkit.library.authentication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.bivashy.javafx.authentication to javafx.fxml;
    exports com.bivashy.javafx.authentication;
}