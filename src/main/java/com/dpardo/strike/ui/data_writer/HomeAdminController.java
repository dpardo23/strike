package com.dpardo.strike.ui.data_writer;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
    @FXML private Button estadisticasButton;
    @FXML private Button equiposButton;
    @FXML private Button partidoButton;
    @FXML private Button ligaButton;
    @FXML private StackPane formContainer;

    /**
     * Método de inicialización. Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        // Carga el formulario de países por defecto al iniciar
        loadForm("/com/dpardo/strike/ui/data_writer/form-pais.fxml");
    }

    /**
     * Maneja los eventos de clic de los botones del menú lateral.
     * Carga el formulario correspondiente al botón presionado.
     */
    @FXML
    void handleMenuClick(ActionEvent event) {
        Object source = event.getSource();

        // Determina qué formulario cargar según el botón presionado
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
        } else {
            // Si el botón no tiene un formulario asociado, limpia el contenedor
            clearFormContainer();
            System.out.println("Formulario aún no implementado...");
        }
    }

    //--- Métodos Privados de Ayuda ---

    /**
     * Carga un archivo FXML en el contenedor central con una animación de transición.
     * @param fxmlPath La ruta al archivo FXML a cargar.
     */
    private void loadForm(String fxmlPath) {
        try {
            Node newFormNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));

            if (!formContainer.getChildren().isEmpty()) {
                // Si ya hay un formulario, aplica animación de salida (fade-out)
                Node oldFormNode = formContainer.getChildren().get(0);
                FadeTransition fadeOut = new FadeTransition(Duration.millis(250), oldFormNode);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);

                // Al terminar el fade-out, reemplaza el nodo y aplica fade-in al nuevo
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
                // Si el contenedor está vacío, solo aplica animación de entrada (fade-in)
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

    /**
     * Limpia el contenedor de formularios con una animación de salida.
     */
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