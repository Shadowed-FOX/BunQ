module com.blackfox.bunq {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.blackfox.bunq to javafx.fxml;
    exports com.blackfox.bunq;
}