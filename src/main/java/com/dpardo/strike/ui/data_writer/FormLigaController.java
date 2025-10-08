package com.dpardo.strike.ui.data_writer;

import com.dpardo.strike.repository.DatabaseConnection;
import com.dpardo.strike.repository.LigaRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para el formulario de inserción y gestión de Ligas.
 */
public class FormLigaController {

    //--- Componentes FXML ---
    @FXML private TextField identificadorField;
    @FXML private TextField nombreLigaField;
    @FXML private ComboBox<String> paisComboBox;
    @FXML private ComboBox<String> ligaComboBox;
    @FXML private Button guardarLigaButton;

    //--- Repositorios ---
    private final LigaRepository ligaRepository = new LigaRepository();

    /**
     * Método de inicialización. Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        cargarPaisesEnComboBox();
        cargarTiposDeLigaEnComboBox();
    }

    /**
     * Maneja el evento de clic en el botón "Guardar".
     * Valida la entrada, procesa los datos y los inserta en la base de datos.
     */
    @FXML
    void handleGuardarLiga() {
        // Validación de campos obligatorios
        if (identificadorField.getText().trim().isEmpty() || nombreLigaField.getText().trim().isEmpty() ||
                paisComboBox.getValue() == null || ligaComboBox.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos son obligatorios.");
            return;
        }

        try {
            // Conversión de tipos de dato desde los campos de texto
            int id = Integer.parseInt(identificadorField.getText());
            String nombre = nombreLigaField.getText();
            String pais = paisComboBox.getValue();
            String tipo = ligaComboBox.getValue();

            // Llamada al repositorio para ejecutar la inserción en la BD
            ligaRepository.insertarLiga(id, nombre, pais, tipo);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La liga '" + nombre + "' ha sido guardada correctamente.");
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El identificador debe ser un número entero.");
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error al guardar la liga.");
            e.printStackTrace();
        }
    }

    //--- Métodos Privados de Ayuda ---

    /**
     * Carga las opciones de países en el ComboBox correspondiente.
     */
    private void cargarPaisesEnComboBox() {
        try {
            List<String> paises = obtenerCodigosPaises();
            paisComboBox.setItems(FXCollections.observableArrayList(paises));
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar los países.");
            e.printStackTrace();
        }
    }

    /**
     * Carga una lista estática de tipos de liga en el ComboBox correspondiente.
     */
    private void cargarTiposDeLigaEnComboBox() {
        ObservableList<String> tipos = FXCollections.observableArrayList(
                "Profesional", "Semi-profesional", "Amateur",
                "Con sistema de divisiones y ascensos/descensos",
                "Con playoffs o eliminatorias", "Regional/Local"
        );
        ligaComboBox.setItems(tipos);
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarFormulario() {
        identificadorField.clear();
        nombreLigaField.clear();
        paisComboBox.getSelectionModel().clearSelection();
        ligaComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Muestra una ventana de alerta genérica.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Obtiene los códigos FIFA de los países desde la base de datos.
     */
    private List<String> obtenerCodigosPaises() throws SQLException {
        List<String> codigosPaises = new ArrayList<>();
        String sql = "SELECT * FROM obtener_codigos_paises()"; // Ejecución del query

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                codigosPaises.add(rs.getString("codigo_fifa"));
            }
        }
        return codigosPaises;
    }
}