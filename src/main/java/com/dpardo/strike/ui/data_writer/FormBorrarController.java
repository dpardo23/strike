package com.dpardo.strike.ui.data_writer;

import com.dpardo.strike.repository.JugadorRepository;
import com.dpardo.strike.repository.PaisRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import java.sql.SQLException;
import java.util.Optional;

public class FormBorrarController {

    @FXML private ComboBox<String> paisComboBox;
    @FXML private Button borrarPaisButton;
    @FXML private ComboBox<String> jugadorCombBox;
    @FXML private Button borrarJugadorButton;

    private final PaisRepository paisRepository = new PaisRepository();
    private final JugadorRepository jugadorRepository = new JugadorRepository();

    @FXML
    public void initialize() {
        cargarPaises();
        cargarJugadores();
    }

    private void cargarPaises() {
        paisComboBox.setItems(FXCollections.observableArrayList(paisRepository.obtenerTodosLosCodigosFifa()));
    }

    private void cargarJugadores() {
        jugadorCombBox.setItems(FXCollections.observableArrayList(jugadorRepository.obtenerTodosLosNombresDeJugadores()));
    }

    @FXML
    private void handleBorrarPais() {
        String paisSeleccionado = paisComboBox.getValue();
        if (paisSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Vacía", "Por favor, seleccione un país para borrar.");
            return;
        }

        if (confirmarBorrado("país", paisSeleccionado)) {
            try {
                paisRepository.eliminarPais(paisSeleccionado);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El país '" + paisSeleccionado + "' ha sido eliminado.");
                cargarPaises(); // Recargar la lista
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo eliminar el país. Es posible que esté siendo usado por un jugador o equipo.\nError: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBorrarJugador() {
        String jugadorSeleccionado = jugadorCombBox.getValue();
        if (jugadorSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Vacía", "Por favor, seleccione un jugador para borrar.");
            return;
        }

        if (confirmarBorrado("jugador", jugadorSeleccionado)) {
            try {
                jugadorRepository.eliminarJugadorPorNombre(jugadorSeleccionado);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El jugador '" + jugadorSeleccionado + "' ha sido eliminado.");
                cargarJugadores(); // Recargar la lista
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "Ocurrió un error al eliminar el jugador: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean confirmarBorrado(String tipo, String nombre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar el " + tipo + " '" + nombre + "'?");
        alert.setContentText("Esta acción es irreversible.");
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}