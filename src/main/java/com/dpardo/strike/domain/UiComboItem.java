package com.dpardo.strike.domain;

/**
 * Representa un ítem de un componente de UI (Interfaz de Usuario) para ser mostrado en un ComboBox.
 * Asocia un ID y un código de componente con una descripción legible.
 *
 * @param id El identificador único del componente de UI.
 * @param codComponente El código programático del componente (ej: "superadminBorderPane").
 * @param descripcion El texto descriptivo que verá el usuario (ej: "Vista de Super Administrador").
 */
public record UiComboItem(int id, String codComponente, String descripcion) {

    /**
     * Define el formato de texto que se mostrará en la lista del ComboBox.
     * @return la descripción legible del componente de UI.
     */
    @Override
    public String toString() {
        return descripcion;
    }
}