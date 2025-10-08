package com.dpardo.strike.ui.data_writer;

import com.dpardo.strike.repository.EquipoRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador para el formulario de inserción y gestión de Equipos.
 */
public class FormEquipoController {

    //--- Componentes FXML ---
    @FXML private TextField identificadorField;
    @FXML private TextField nombreEquipoField;
    @FXML private ComboBox<String> paisComboBox;
    @FXML private TextField ciudadField;
    @FXML private TextField fechaFundacionField;
    @FXML private TextField directorTecnicoField;
    @FXML private Button guardarEquipoButton;

    //--- Repositorios ---
    private final EquipoRepository equipoRepository = new EquipoRepository();

    /**
     * Método de inicialización. Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        cargarPaisesEnComboBox();
    }

    /**
     * Maneja el evento de clic en el botón "Guardar".
     * Valida la entrada, procesa los datos y los inserta en la base de datos.
     */
    @FXML
    void handleGuardarEquipo() {
        // Validación de campos obligatorios
        if (identificadorField.getText().trim().isEmpty() || nombreEquipoField.getText().trim().isEmpty() ||
                paisComboBox.getValue() == null || ciudadField.getText().trim().isEmpty() ||
                fechaFundacionField.getText().trim().isEmpty() || directorTecnicoField.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos son obligatorios.");
            return;
        }

        try {
            // Conversión de tipos de dato desde los campos de texto
            int id = Integer.parseInt(identificadorField.getText());
            String nombre = nombreEquipoField.getText();
            String pais = paisComboBox.getValue();
            String ciudad = ciudadField.getText();
            LocalDate fechaFundacion = LocalDate.parse(fechaFundacionField.getText()); // Asume formato AAAA-MM-DD
            String dt = directorTecnicoField.getText();

            // Llamada al repositorio para ejecutar la inserción en la BD
            equipoRepository.insertarEquipo(id, nombre, pais, ciudad, fechaFundacion, dt);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El equipo '" + nombre + "' ha sido guardado correctamente.");
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El identificador debe ser un número entero.");
        } catch (DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "La fecha de fundación debe tener el formato AAAA-MM-DD.");
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error al guardar el equipo.");
            e.printStackTrace();
        }
    }

    //--- Métodos Privados de Ayuda ---

    /**
     * Carga la lista de códigos de países desde la BD y los añade al ComboBox.
     */
    private void cargarPaisesEnComboBox() {
        try {
            // Consulta a BD para obtener los códigos de país
            List<String> paises = equipoRepository.obtenerCodigosPaises();
            paisComboBox.setItems(FXCollections.observableArrayList(paises));
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar los países desde la base de datos.");
            e.printStackTrace();
        }
    }

    /**
     * Limpia todos los campos del formulario después de una inserción exitosa.
     */
    private void limpiarFormulario() {
        identificadorField.clear();
        nombreEquipoField.clear();
        paisComboBox.getSelectionModel().clearSelection();
        ciudadField.clear();
        fechaFundacionField.clear();
        directorTecnicoField.clear();
    }

    /**
     * Muestra una ventana de alerta genérica.
     * @param tipo El tipo de alerta (ERROR, INFORMATION, etc.).
     * @param titulo El título de la ventana de alerta.
     * @param mensaje El mensaje a mostrar.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}