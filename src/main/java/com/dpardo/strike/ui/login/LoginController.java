package com.dpardo.strike.ui.login;

import com.dpardo.strike.domain.SessionInfo;
import com.dpardo.strike.repository.UserRepository;
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
            showAlert("Entrada Inválida", "Por favor, ingrese su nombre de usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        try {
            SessionInfo sessionInfo = userRepository.authenticateAndRegisterSession(username, password);
            if (sessionInfo != null) {
                String successMessage = String.format(
                        """
                                ¡Bienvenido, %s! (ID de Usuario: %d)
                                
                                Sesión registrada con éxito en el servidor:
                                ID de Proceso (PID): %d
                                Dirección IP Cliente: %s
                                Puerto Cliente: %d""",
                        username,
                        sessionInfo.userId(),
                        sessionInfo.pid(),
                        sessionInfo.clientAddress(),
                        sessionInfo.clientPort()
                );

                showAlert("Éxito de Autenticación", successMessage, Alert.AlertType.INFORMATION);

                // TODO: Aquí deberías añadir la lógica para cerrar la ventana de login
                // y abrir la ventana principal de tu aplicación.

            } else {
                showAlert("Error de Autenticación", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            showAlert("Error de Base de Datos",
                    "No se pudo conectar o procesar la solicitud.\n\nError técnico: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace(); // Importante para ver el error completo en la consola de desarrollo.
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