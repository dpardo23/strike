module com.dpardo.strike {
    // Dependencias de JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Dependencias de librerías externas
    requires org.kordamp.ikonli.javafx;

    // --- AÑADE ESTA LÍNEA ---
    // Requerimos el módulo 'core' que define la interfaz IkonProvider
    requires org.kordamp.ikonli.core;

    requires java.sql;

    // Declara que la aplicación utilizará proveedores de iconos de Ikonli
    uses org.kordamp.ikonli.IkonProvider;

    // Permisos de acceso
    opens com.dpardo.strike.ui.login to javafx.fxml;
    opens com.dpardo.strike.ui.read_only to javafx.fxml;
    opens com.dpardo.strike.ui.data_writer to javafx.fxml;
    opens com.dpardo.strike.ui.super_user to javafx.fxml;
    opens com.dpardo.strike.domain to javafx.base;

    // Expone el paquete principal
    exports com.dpardo.strike;
}