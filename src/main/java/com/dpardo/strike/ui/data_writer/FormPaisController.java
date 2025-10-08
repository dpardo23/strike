package com.dpardo.strike.ui.data_writer;

import com.dpardo.strike.repository.PaisRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

/**
 * Controlador para el formulario de inserción y gestión de Países.
 */
public class FormPaisController {

    //--- Componentes FXML ---
    @FXML private TextField codigoFifaField;
    @FXML private TextField nombrePaisField;
    @FXML private ComboBox<String> continenteComboBox;
    @FXML private Button guardarPaisButton;
    @FXML private Button elegirBanderaButton;
    @FXML private Label nombreBanderaLabel;

    //--- Repositorios ---
    private final PaisRepository paisRepository = new PaisRepository();

    /**
     * Método de inicialización. Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        // Carga las opciones estáticas en el ComboBox de continentes
        ObservableList<String> opcionesContinente = FXCollections.observableArrayList(
                "Sudamérica (CONMEBOL)", "Norteamérica (CONCACAF)", "Europa (UEFA)",
                "África (CAF)", "Asia (AFC)", "Oceanía (OFC)",
                "Centroamérica (CONCACAF)", "Caribe (CONCACAF)"
        );
        continenteComboBox.setItems(opcionesContinente);
    }

    //--- Manejadores de Eventos FXML ---

    /**
     * Abre un FileChooser para seleccionar una bandera y la copia a la carpeta de recursos del proyecto.
     */
    @FXML
    void handleElegirBandera() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Bandera");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) elegirBanderaButton.getScene().getWindow();
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            try {
                // Define la ruta de destino dentro de la carpeta de recursos del proyecto
                Path rutaDestino = Paths.get("src/main/resources/images/flags/" + archivoSeleccionado.getName());

                // Copia el archivo seleccionado a la carpeta de recursos
                Files.copy(archivoSeleccionado.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

                nombreBanderaLabel.setText(archivoSeleccionado.getName());
                System.out.println("Bandera copiada exitosamente a: " + rutaDestino.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error al copiar el archivo de la bandera.");
                e.printStackTrace();
                nombreBanderaLabel.setText("Error al copiar archivo.");
            }
        }
    }

    /**
     * Maneja el evento de clic en el botón "Guardar".
     * Valida los campos y guarda el nuevo país en la base de datos.
     */
    @FXML
    void handleGuardarPais() {
        String codigo = codigoFifaField.getText();
        String nombre = nombrePaisField.getText();
        String continente = continenteComboBox.getValue();

        // Validación de campos obligatorios
        if (codigo == null || codigo.trim().isEmpty() ||
                nombre == null || nombre.trim().isEmpty() ||
                continente == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
            return;
        }

        try {
            // Llamada al repositorio para insertar en la BD
            paisRepository.insertarPais(codigo, nombre, continente);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El país '" + nombre + "' ha sido guardado correctamente.");
            limpiarFormulario();
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    //--- Métodos Privados de Ayuda ---

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarFormulario() {
        codigoFifaField.clear();
        nombrePaisField.clear();
        continenteComboBox.getSelectionModel().clearSelection();
        nombreBanderaLabel.setText("Ningún archivo seleccionado...");
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
}