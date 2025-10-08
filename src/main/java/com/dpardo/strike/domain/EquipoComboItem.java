package com.dpardo.strike.domain;

/**
 * Representa un ítem simplificado de un Equipo para ser mostrado en un ComboBox.
 * Contiene el ID y el nombre del equipo.
 *
 * @param id El identificador único del equipo.
 * @param nombre El nombre del equipo.
 */
public record EquipoComboItem(int id, String nombre) {

    /**
     * Define el formato de texto que se mostrará en la lista del ComboBox.
     * @return una cadena con el formato "ID - Nombre" (ej: "101 - Real Madrid").
     */
    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}