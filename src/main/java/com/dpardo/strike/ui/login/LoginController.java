package com.dpardo.strike.ui.login;

import com.dpardo.strike.domain.SessionInfo;
import com.dpardo.strike.domain.SessionManager;
import com.dpardo.strike.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Controlador para la ventana de Login.
 * Gestiona la autenticación de usuarios y la navegación basada en roles.
 */
public class LoginController {

    //--- Componentes FXML ---
    @FXML private TextField tf_username;
    @FXML private PasswordField pf_password;
    @FXML private Button btnLogin;

    //--- Repositorios ---
    private final UserRepository userRepository = new UserRepository();

    /**
     * Método de inicialización. Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        if (btnLogin != null) {
            btnLogin.setOnAction(event -> handleLoginButtonAction());
        }
    }

    /**
     * Maneja el evento de clic en el botón de "Iniciar Sesión".
     */
    @FXML
    private void handleLoginButtonAction() {
        String username = tf_username.getText();
        String password = pf_password.getText();

        // Validación de entrada
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Entrada Inválida", "Por favor, ingrese su nombre de usuario y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Autenticación contra la BD y registro de sesión
            SessionInfo sessionInfo = userRepository.authenticateAndRegisterSession(username, password);

            if (sessionInfo != null && sessionInfo.roleName() != null) {
                // Guarda la sesión actual para que otras ventanas puedan acceder a ella
                SessionManager.setCurrentSession(sessionInfo);
                System.out.println("Login exitoso. Usuario: " + username + ", Rol: " + sessionInfo.roleName());

                // Navega a la ventana principal correspondiente al rol
                navigateToMainView(sessionInfo.roleName());
            } else {
                showAlert("Error de Autenticación", "Usuario, contraseña o rol incorrectos.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error de Base de Datos", "No se pudo conectar o procesar la solicitud.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    //--- Métodos Privados de Ayuda ---

    /**
     * Carga la ventana principal correspondiente al rol del usuario y cierra la ventana de login.
     * @param roleName El nombre del rol del usuario autenticado.
     */
    private void navigateToMainView(String roleName) {
        String fxmlPath;
        String windowTitle;

        // Determina la vista a cargar según el rol
        switch (roleName.toLowerCase()) {
            case "read_only":
                fxmlPath = "/com/dpardo/strike/ui/read_only/Home-view.fxml";
                windowTitle = "Strike - Vista de Lector";
                break;
            case "data_writer":
                fxmlPath = "/com/dpardo/strike/ui/data_writer/Home-admin.fxml";
                windowTitle = "Strike - Panel de Administración";
                break;
            case "super_user":
                fxmlPath = "/com/dpardo/strike/ui/super_user/Home-superadmin.fxml";
                windowTitle = "Strike - Panel de Super Administrador";
                break;
            default:
                showAlert("Error de Rol", "Rol no reconocido: " + roleName, Alert.AlertType.ERROR);
                return;
        }

        try {
            // Carga el FXML de la nueva ventana
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = new Stage();
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root));
            stage.show();

            // Cierra la ventana de login actual
            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            loginStage.close();

        } catch (IOException | NullPointerException e) {
            showAlert("Error de Carga", "No se pudo cargar la ventana principal para el rol: " + roleName, Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Muestra una ventana de alerta genérica.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}