package com.dpardo.strike.ui.home;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PaisItemController {

    @FXML
    private ImageView imagenBandera;

    @FXML
    private Label nombrePaisLabel;

    // Este método lo usaremos después para poner los datos reales
    public void setData(String nombrePais, Image bandera) {
        nombrePaisLabel.setText(nombrePais);
        imagenBandera.setImage(bandera);
    }
}