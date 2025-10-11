package com.dpardo.strike.ui.data_writer;

// --- Imports para la nueva funcionalidad ---
import com.dpardo.strike.domain.SessionInfo;
import com.dpardo.strike.domain.SessionManager;
import com.dpardo.strike.domain.UiComboItem;
import com.dpardo.strike.repository.SuperAdminRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// --- Imports que ya tenías ---
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.control.Tooltip;
import java.io.IOException;
import java.util.Objects;

/**
 * Controlador para la ventana principal del rol "data_writer" (Home-admin).
 * Gestiona la navegación entre los diferentes formularios de inserción.
 */
public class HomeAdminController {

    //--- Componentes FXML ---
    @FXML private Button paisButton;
    @FXML private Button jugadorButton;
    @FXML private Button equiposButton;
    @FXML private Button partidoButton;
    @FXML private Button ligaButton;
    @FXML private StackPane formContainer;
    @FXML private Button borrarButton;
    @FXML private Button editarButton;

    // --- Componentes del Header ---
    @FXML private Button userInfoadminButton;
    @FXML private Tooltip usernameAdminTooltip;
    @FXML private ComboBox<UiComboItem> viewSelectorAdminComboBox; // El tipo debe ser UiComboItem

    //--- Repositorios y utilidades ---
    private final SuperAdminRepository repository = new SuperAdminRepository();
    private final Map<String, String> uiPathMap = new HashMap<>();

    /**
     * Método de inicialización. Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        // Carga el formulario de países por defecto al iniciar
        loadForm("/com/dpardo/strike/ui/data_writer/form-pais.fxml");

        // Configura las funcionalidades del header
        setupUserInfo();
        setupComboBox();
    }

    //--- Manejador del menú lateral (Tu código original) ---
    @FXML
    void handleMenuClick(ActionEvent event) {
        Object source = event.getSource();
        if (source == paisButton) {
            loadForm("/com/dpardo/strike/ui/data_writer/form-pais.fxml");
        } else if (source == equiposButton) {
            loadForm("/com/dpardo/strike/ui/data_writer/Form-equipo.fxml");
        } else if (source == ligaButton) {
            loadForm("/com/dpardo/strike/ui/data_writer/Form-liga.fxml");
        } else if (source == partidoButton) {
            loadForm("/com/dpardo/strike/ui/data_writer/Form-partido.fxml");
        } else if (source == jugadorButton) {
            loadForm("/com/dpardo/strike/ui/data_writer/Form-jugador.fxml");
        } else if (source ==editarButton) {
            loadForm("/com/dpardo/strike/ui/data_writer/Form-editar.fxml");
        }else if(source ==borrarButton){
            loadForm("/com/dpardo/strike/ui/data_writer/Form-borrar.fxml");
        }else {
            clearFormContainer();
            System.out.println("Formulario aún no implementado...");
        }
    }

    //--- VvVvV MÉTODOS PORTADOS DE SUPERADMINCONTROLLER VvVvV ---

    /**
     * Muestra un pop-up con detalles de la sesión del usuario actual.
     */
    @FXML
    private void handleUserInfoClick() {
        SessionInfo currentSession = SessionManager.getCurrentSession();
        if (currentSession == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información del Usuario");
        alert.setHeaderText("Detalles de la Sesión Actual");

        String content = String.format(
                "ID de Usuario: %d\n" +
                        "Rol: %s\n" +
                        "PID de la Sesión: %d\n" +
                        "IP Cliente: %s\n" +
                        "Puerto Cliente: %d",
                currentSession.userId(),
                currentSession.roleName(),
                currentSession.pid(),
                currentSession.clientAddress(),
                currentSession.clientPort()
        );
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Abre una nueva ventana según la selección del ComboBox de navegación.
     */
    @FXML
    private void handleViewSelection() {
        UiComboItem selectedUi = viewSelectorAdminComboBox.getValue();
        if (selectedUi != null) {
            String fxmlPath = uiPathMap.get(selectedUi.codComponente());
            if (fxmlPath != null) {
                openNewWindow(fxmlPath, selectedUi.descripcion());
            } else {
                System.err.println("No se encontró la ruta para: " + selectedUi.codComponente());
            }
            Platform.runLater(() -> viewSelectorAdminComboBox.getSelectionModel().clearSelection());
        }
    }

    /**
     * Configura la información del usuario en el Tooltip del botón de la derecha.
     */
    private void setupUserInfo() {
        SessionInfo currentSession = SessionManager.getCurrentSession();
        if (currentSession != null) {
            usernameAdminTooltip.setText("Usuario ID: " + currentSession.userId());
        } else {
            userInfoadminButton.setVisible(false);
        }
    }

    /**
     * Carga las vistas disponibles desde la BD en el ComboBox de navegación de la izquierda.
     */

    private void setupComboBox() {
        uiPathMap.put("homeBorderPane", "/com/dpardo/strike/ui/read_only/Home-view.fxml");
        uiPathMap.put("adminBorderPane", "/com/dpardo/strike/ui/data_writer/Home-admin.fxml");
        uiPathMap.put("superadminBorderPane", "/com/dpardo/strike/ui/super_user/Home-superadmin.fxml");

        try {
            // Obtenemos la sesión actual para saber qué usuario está logueado
            SessionInfo currentSession = SessionManager.getCurrentSession();
            if (currentSession != null) {
                // Llamamos al nuevo método del repositorio pasándole el ID del usuario
                int userId = currentSession.userId();
                viewSelectorAdminComboBox.setItems(FXCollections.observableArrayList(repository.obtenerUis(userId)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        viewSelectorAdminComboBox.setOnAction(event -> handleViewSelection());
    }

    /**
     * Carga y muestra una nueva ventana FXML.
     */
    private void openNewWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    //--- Tus métodos originales para cargar formularios (preservados) ---

    private void loadForm(String fxmlPath) {
        try {
            Node newFormNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            if (!formContainer.getChildren().isEmpty()) {
                Node oldFormNode = formContainer.getChildren().get(0);
                FadeTransition fadeOut = new FadeTransition(Duration.millis(250), oldFormNode);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(event -> {
                    newFormNode.setOpacity(0.0);
                    formContainer.getChildren().setAll(newFormNode);
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(250), newFormNode);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                });
                fadeOut.play();
            } else {
                newFormNode.setOpacity(0.0);
                formContainer.getChildren().add(newFormNode);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(250), newFormNode);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Error fatal al cargar el formulario: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void clearFormContainer() {
        if (!formContainer.getChildren().isEmpty()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(250), formContainer.getChildren().get(0));
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> formContainer.getChildren().clear());
            fadeOut.play();
        }
    }
}