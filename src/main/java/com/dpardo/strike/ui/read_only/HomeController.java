package com.dpardo.strike.ui.read_only;

// --- Imports existentes ---
import com.dpardo.strike.domain.Pais;
import com.dpardo.strike.repository.PaisRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

// --- Imports AÑADIDOS para la nueva funcionalidad ---
import com.dpardo.strike.domain.SessionInfo;
import com.dpardo.strike.domain.SessionManager;
import com.dpardo.strike.domain.UiComboItem;
import com.dpardo.strike.repository.SuperAdminRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class HomeController {

    // --- Componentes FXML del menú superior (ya estaban) ---
    @FXML private Button jugadoresButton;
    @FXML private Button estadisticasButton;
    @FXML private Button equiposButton;
    @FXML private Button partidosButton;
    @FXML private Button clasificacionButton;
    @FXML private Button resumenButton;
    @FXML private Button ligaButton;

    // --- Componentes FXML del cuerpo (ya estaban) ---
    @FXML private BorderPane mainBorderPane;
    @FXML private VBox paisContenedor;

    // --- VvVvV COMPONENTES AÑADIDOS PARA EL HEADER VvVvV ---
    @FXML private ComboBox<UiComboItem> viewHomeComboBox;
    @FXML private Button userinfoHomeButton;
    @FXML private Tooltip usernameAdminTooltip; // Usamos el fx:id que mencionaste

    // --- Repositorios ---
    private final PaisRepository paisRepository = new PaisRepository();
    // VvVvV REPOSITORIO AÑADIDO VvVvV
    private final SuperAdminRepository superAdminRepo = new SuperAdminRepository();

    // VvVvV UTILIDADES AÑADIDAS VvVvV
    private final Map<String, String> uiPathMap = new HashMap<>();


    /**
     * Este método se ejecuta automáticamente cuando la vista se carga.
     */
    @FXML
    public void initialize() {
        // Tu lógica original para cargar países
        cargarPaises();

        // --- VvVvV LÓGICA AÑADIDA PARA CONFIGURAR EL HEADER VvVvV ---
        setupUserInfo();
        setupComboBox();
        // --- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ---
    }

    /**
     * Carga los países desde la base de datos y los muestra en la barra lateral.
     * (Este método se queda tal como estaba)
     */
    public void cargarPaises() {
        if (paisContenedor != null) {
            paisContenedor.getChildren().clear();
        }
        List<Pais> listaDePaises = paisRepository.obtenerTodosLosPaises();
        System.out.println("Países obtenidos de la BD: " + listaDePaises.size());
        for (Pais pais : listaDePaises) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dpardo/strike/ui/read_only/Pais-view.fxml"));
                Node nodoPaisItem = loader.load();
                PaisItemController paisController = loader.getController();
                String rutaImagen = "/images/flags/" + pais.codigo() + ".png";
                Image bandera = new Image(getClass().getResourceAsStream(rutaImagen));
                paisController.setData(pais.nombre(), bandera);
                paisContenedor.getChildren().add(nodoPaisItem);
            } catch (IOException | NullPointerException e) {
                System.err.println("Error al cargar item para " + pais.nombre() + ". ¿Falta la imagen " + pais.codigo() + ".png en resources?");
                e.printStackTrace();
            }
        }
    }


    // --- VvVvV MÉTODOS AÑADIDOS PARA NAVEGACIÓN Y SESIÓN (Iguales a HomeAdminController) VvVvV ---

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
        UiComboItem selectedUi = viewHomeComboBox.getValue();
        if (selectedUi != null) {
            String fxmlPath = uiPathMap.get(selectedUi.codComponente());
            if (fxmlPath != null) {
                openNewWindow(fxmlPath, selectedUi.descripcion());
            } else {
                System.err.println("No se encontró la ruta para: " + selectedUi.codComponente());
            }
            Platform.runLater(() -> viewHomeComboBox.getSelectionModel().clearSelection());
        }
    }

    /**
     * Configura la información del usuario en el Tooltip del botón.
     */
    private void setupUserInfo() {
        SessionInfo currentSession = SessionManager.getCurrentSession();
        if (currentSession != null) {
            usernameAdminTooltip.setText("Usuario ID: " + currentSession.userId());
        } else {
            userinfoHomeButton.setVisible(false);
        }
    }

    /**
     * Carga las vistas disponibles desde la BD en el ComboBox de navegación.
     */
    private void setupComboBox() {
        uiPathMap.put("homeBorderPane", "/com/dpardo/strike/ui/read_only/Home-view.fxml");
        uiPathMap.put("adminBorderPane", "/com/dpardo/strike/ui/data_writer/Home-admin.fxml");
        uiPathMap.put("superadminBorderPane", "/com/dpardo/strike/ui/super_user/Home-superadmin.fxml");

        try {
            SessionInfo currentSession = SessionManager.getCurrentSession();
            if (currentSession != null) {
                int userId = currentSession.userId();
                viewHomeComboBox.setItems(FXCollections.observableArrayList(superAdminRepo.obtenerUis(userId)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        viewHomeComboBox.setOnAction(event -> handleViewSelection());
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
    // --- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ---
}