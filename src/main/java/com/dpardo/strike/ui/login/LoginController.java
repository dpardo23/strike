package com.dpardo.strike.ui.login;

import com.dpardo.strike.repository.UserRepository; // Importamos el Repositorio
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class LoginController {


    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField pf_password;

    @FXML
    private Button btnLogin;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    public void initialize() {
        if (btnLogin != null) {
            btnLogin.setOnAction(event -> handleLoginButtonAction());
        }
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = tf_username.getText();
        String password = pf_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Alerta", "Por favor, ingrese su nombre de usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean authenticated = userRepository.authenticateUser(username, password);

            if (authenticated) {
                showAlert("Éxito de Autenticación",
                        "¡Credenciales correctas! Bienvenido, " + username + ".",
                        Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error de Login", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            showAlert("Error de Conexión a DB",
                    "Fallo al conectar con la base de datos.\nAsegúrese de que el servicio de Docker esté activo.",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}