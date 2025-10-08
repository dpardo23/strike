package com.dpardo.strike.domain;

/**
 * Representa un ítem para un ComboBox de selección de sexo.
 * Asocia un valor de carácter (ej: 'M') con una descripción legible (ej: "Masculino").
 *
 * @param valor El carácter que representa el sexo (ej: 'M' o 'F').
 * @param descripcion El texto descriptivo que verá el usuario (ej: "Masculino").
 */
public record SexoComboItem(char valor, String descripcion) {

    /**
     * Define el formato de texto que se mostrará en la lista del ComboBox.
     * @return la descripción legible del sexo (ej: "Masculino" o "Femenino").
     */
    @Override
    public String toString() {
        return descripcion;
    }
}