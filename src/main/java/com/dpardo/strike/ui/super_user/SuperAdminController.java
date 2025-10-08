package com.dpardo.strike.ui.super_user;

import com.dpardo.strike.domain.SessionInfo;
import com.dpardo.strike.domain.SessionManager;
import com.dpardo.strike.domain.SessionViewModel;
import com.dpardo.strike.domain.UiComboItem;
import com.dpardo.strike.repository.SuperAdminRepository;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Controlador para la ventana de Super Administrador.
 * Muestra sesiones activas en tiempo real y permite la navegación a otras vistas.
 */
public class SuperAdminController {

    //--- Componentes FXML ---
    @FXML private ComboBox<UiComboItem> viewSelectorComboBox;
    @FXML private Button userInfoButton;
    @FXML private Tooltip usernameTooltip;
    @FXML private TableView<SessionViewModel> mainTableView;
    @FXML private TableColumn<SessionViewModel, Integer> pidColumn;
    @FXML private TableColumn<SessionViewModel, String> userColumn;
    @FXML private TableColumn<SessionViewModel, String> correoColumn;
    @FXML private TableColumn<SessionViewModel, Timestamp> fecCreacionColumn;
    @FXML private TableColumn<SessionViewModel, String> rolColumn;
    @FXML private TableColumn<SessionViewModel, String> uiColumn;
    @FXML private TableColumn<SessionViewModel, String> direccionIpColumn;
    @FXML private TableColumn<SessionViewModel, Integer> puertoColumn;
    @FXML private TableColumn<SessionViewModel, Timestamp> fecAsignacionColumn;
    @FXML private TableColumn<SessionViewModel, Boolean> activoColumn;

    //--- Repositorios y Servicios ---
    private final SuperAdminRepository repository = new SuperAdminRepository();
    private ScheduledService<ObservableList<SessionViewModel>> sessionUpdateService;
    private final Map<String, String> uiPathMap = new HashMap<>();

    /**
     * Método de inicialización. Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        setupUserInfo();
        setupComboBox();
        setupTableView();
        startSessionUpdateService();
    }

    /**
     * Detiene los servicios en segundo plano al cerrar la ventana para liberar recursos.
     */
    public void stop() {
        if (sessionUpdateService != null) {
            sessionUpdateService.cancel();
            System.out.println("Servicio de actualización de sesiones detenido.");
        }
    }

    //--- Manejadores de Eventos FXML ---

    /**
     * Abre una nueva ventana según la selección del ComboBox.
     */
    @FXML
    private void handleViewSelection() {
        UiComboItem selectedUi = viewSelectorComboBox.getValue();
        if (selectedUi != null) {
            String fxmlPath = uiPathMap.get(selectedUi.codComponente());
            if (fxmlPath != null) {
                openNewWindow(fxmlPath, selectedUi.descripcion());
            } else {
                System.err.println("No se encontró la ruta para: " + selectedUi.codComponente());
            }
            // Limpia la selección para permitir reabrir la misma ventana
            Platform.runLater(() -> viewSelectorComboBox.getSelectionModel().clearSelection());
        }
    }

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

    //--- Métodos Privados de Configuración ---

    /**
     * Configura la información del usuario en el Tooltip del botón.
     */
    private void setupUserInfo() {
        SessionInfo currentSession = SessionManager.getCurrentSession();
        if (currentSession != null) {
            usernameTooltip.setText("Usuario: " + currentSession.userId());
        }
    }

    /**
     * Carga las vistas disponibles desde la BD en el ComboBox de navegación.
     */
    private void setupComboBox() {
        // Mapeo de componentes a rutas FXML
        uiPathMap.put("homeBorderPane", "/com/dpardo/strike/ui/read_only/Home-view.fxml");
        uiPathMap.put("adminBorderPane", "/com/dpardo/strike/ui/data_writer/Home-admin.fxml");
        uiPathMap.put("superadminBorderPane", "/com/dpardo/strike/ui/super_user/Home-superadmin.fxml");

        try {
            // Consulta a BD para obtener las UIs
            viewSelectorComboBox.setItems(FXCollections.observableArrayList(repository.obtenerUis()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        viewSelectorComboBox.setOnAction(event -> handleViewSelection());
    }

    /**
     * Configura las columnas de la TableView para enlazarlas al modelo de datos.
     */
    private void setupTableView() {
        pidColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().pid()).asObject());
        userColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreUsuario()));
        correoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().correo()));
        fecCreacionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().fecCreacionUsuario()));
        rolColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nombreRol()));
        uiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().codComponenteUi()));
        direccionIpColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().direccionIp()));
        puertoColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().puerto()).asObject());
        fecAsignacionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().fechaAsignacionRol()));
        activoColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().rolActivo()).asObject());
    }

    /**
     * Inicia un servicio que actualiza la tabla de sesiones periódicamente.
     */
    private void startSessionUpdateService() {
        sessionUpdateService = new ScheduledService<>() {
            @Override
            protected Task<ObservableList<SessionViewModel>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<SessionViewModel> call() throws Exception {
                        // Ejecuta la consulta en un hilo de fondo
                        return FXCollections.observableArrayList(repository.obtenerSesionesActivas());
                    }
                };
            }
        };

        sessionUpdateService.setPeriod(Duration.seconds(3)); // Frecuencia de actualización
        sessionUpdateService.setOnSucceeded(event -> mainTableView.setItems(sessionUpdateService.getValue()));
        sessionUpdateService.setOnFailed(event -> sessionUpdateService.getException().printStackTrace());
        sessionUpdateService.start();
    }

    /**
     * Carga y muestra una nueva ventana FXML con una animación de entrada.
     */
    private void openNewWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            // Aplica animación de entrada
            root.setOpacity(0.0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            stage.show();
            fadeIn.play();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}