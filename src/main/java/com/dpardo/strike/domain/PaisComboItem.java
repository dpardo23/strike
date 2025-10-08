package com.dpardo.strike.domain;

/**
 * Representa un ítem simplificado de un País para ser mostrado en un ComboBox.
 * Contiene el código FIFA y el nombre del país.
 *
 * @param codFifa El código FIFA de 3 letras del país (ej: "BOL").
 * @param nombre El nombre completo del país (ej: "Bolivia").
 */
public record PaisComboItem(String codFifa, String nombre) {

    /**
     * Define el formato de texto que se mostrará en la lista del ComboBox.
     * @return una cadena con el formato "Código - Nombre" (ej: "BOL - Bolivia").
     */
    @Override
    public String toString() {
        return codFifa + " - " + nombre;
    }
}