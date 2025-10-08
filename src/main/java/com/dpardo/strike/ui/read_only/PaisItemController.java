package com.dpardo.strike.ui.read_only;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controlador para un ítem individual en una lista de países (pais-item.fxml).
 * Gestiona la visualización de la bandera y el nombre de un país.
 */
public class PaisItemController {

    //--- Componentes FXML ---
    @FXML private ImageView imagenBandera;
    @FXML private Label nombrePaisLabel;

    /**
     * Establece los datos para este ítem de la lista.
     * Este método es llamado desde el controlador principal para poblar cada celda.
     *
     * @param nombrePais El nombre del país a mostrar.
     * @param bandera Un objeto Image con la bandera del país.
     */
    public void setData(String nombrePais, Image bandera) {
        nombrePaisLabel.setText(nombrePais);
        imagenBandera.setImage(bandera);
    }
}