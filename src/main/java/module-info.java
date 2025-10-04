module com.dpardo.strike {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.dpardo.strike to javafx.fxml;
    opens com.dpardo.strike.ui.login to javafx.fxml;
    exports com.dpardo.strike;
    opens com.dpardo.strike.ui.home to javafx.fxml;
}