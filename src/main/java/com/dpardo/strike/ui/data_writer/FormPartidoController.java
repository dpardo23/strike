package com.dpardo.strike.ui.data_writer;

import com.dpardo.strike.domain.EquipoComboItem;
import com.dpardo.strike.domain.LigaComboItem;
import com.dpardo.strike.repository.EquipoRepository;
import com.dpardo.strike.repository.LigaRepository;
import com.dpardo.strike.repository.PartidoRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FormPartidoController {

    @FXML private ComboBox<EquipoComboItem> equipoLocalComboBox;
    @FXML private ComboBox<EquipoComboItem> equipoVisitanteComboBox;
    @FXML private TextField fechaField;
    @FXML private TextField HoraField;
    @FXML private ComboBox<LigaComboItem> ligaComboBox;
    @FXML private Button guardarPartidoButton;
    @FXML private TextField historialField;

    private final EquipoRepository equipoRepository = new EquipoRepository();
    private final LigaRepository ligaRepository = new LigaRepository();
    private final PartidoRepository partidoRepository = new PartidoRepository();

    @FXML
    public void initialize() {
        cargarEquipos();
        cargarLigas();
    }

    private void cargarEquipos() {
        try {
            List<EquipoComboItem> equipos = equipoRepository.obtenerEquiposParaCombo();
            equipoLocalComboBox.setItems(FXCollections.observableArrayList(equipos));
            equipoVisitanteComboBox.setItems(FXCollections.observableArrayList(equipos));
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar los equipos.");
        }
    }

    private void cargarLigas() {
        try {
            List<LigaComboItem> ligas = ligaRepository.obtenerLigasParaCombo();
            ligaComboBox.setItems(FXCollections.observableArrayList(ligas));
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar las ligas.");
        }
    }

    @FXML
    void handleGuardarPartido() {
        if (equipoLocalComboBox.getValue() == null || equipoVisitanteComboBox.getValue() == null ||
                ligaComboBox.getValue() == null || fechaField.getText().trim().isEmpty() ||
                HoraField.getText().trim().isEmpty() || historialField.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos son obligatorios.");
            return;
        }

        try {
            int equipoLocalId = equipoLocalComboBox.getValue().id();
            int equipoVisitanteId = equipoVisitanteComboBox.getValue().id();
            int ligaId = ligaComboBox.getValue().id();
            LocalDate fecha = LocalDate.parse(fechaField.getText()); // Formato AAAA-MM-DD
            LocalTime hora = LocalTime.parse(HoraField.getText()); // Formato HH:MM o HH:MM:SS
            int historial = Integer.parseInt(historialField.getText());

            partidoRepository.insertarPartido(equipoLocalId, equipoVisitanteId, fecha, hora, ligaId, historial);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El partido ha sido guardado correctamente.");
            limpiarFormulario();

        } catch (DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "La fecha debe ser AAAA-MM-DD y la hora HH:MM.");
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El historial debe ser un número entero.");
        } catch (SQLException e) {
            // Aquí se mostrará cualquier error de la base de datos, como duplicados.
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", e.getMessage());
        }
    }

    private void limpiarFormulario() {
        equipoLocalComboBox.getSelectionModel().clearSelection();
        equipoVisitanteComboBox.getSelectionModel().clearSelection();
        ligaComboBox.getSelectionModel().clearSelection();
        fechaField.clear();
        HoraField.clear();
        historialField.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}