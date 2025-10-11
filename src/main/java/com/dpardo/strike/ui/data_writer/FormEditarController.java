package com.dpardo.strike.ui.data_writer;

import com.dpardo.strike.repository.PaisRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.util.List;

public class FormEditarController {

    @FXML private ComboBox<String> codFifaComboBox;
    @FXML private TextField nombrePaisEditField;
    @FXML private Button guardarEditPaisButton;

    private final PaisRepository paisRepository = new PaisRepository();

    @FXML
    public void initialize() {
        cargarCodigosFifa();
    }

    private void cargarCodigosFifa() {
        List<String> codigos = paisRepository.obtenerTodosLosCodigosFifa();
        codFifaComboBox.setItems(FXCollections.observableArrayList(codigos));
    }

    @FXML
    private void handleGuardarEditPais() {
        String codFifaSeleccionado = codFifaComboBox.getValue();
        String nuevoNombre = nombrePaisEditField.getText();

        // Validaciones
        if (codFifaSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Debe seleccionar un código FIFA.");
            return;
        }
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "El campo de nuevo nombre no puede estar vacío.");
            return;
        }

        try {
            // Llamada al repositorio para ejecutar el UPDATE
            paisRepository.actualizarNombrePais(codFifaSeleccionado, nuevoNombre);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El país con código '" + codFifaSeleccionado + "' ha sido actualizado.");

            // Limpiar formulario
            nombrePaisEditField.clear();
            codFifaComboBox.getSelectionModel().clearSelection();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "Ocurrió un error al actualizar el país: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}