package com.dpardo.strike.domain;

/**
 * Representa un ítem simplificado de una Liga para ser mostrado en un ComboBox.
 * Contiene el ID y el nombre de la liga.
 *
 * @param id El identificador único de la liga.
 * @param nombre El nombre de la liga.
 */
public record LigaComboItem(int id, String nombre) {

    /**
     * Define el formato de texto que se mostrará en la lista del ComboBox.
     * @return una cadena con el formato "ID - Nombre" (ej: "1 - La Liga").
     */
    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}