package com.dpardo.strike.domain;

/**
 * Representa la entidad País con sus propiedades básicas.
 * Se utiliza un 'record' para un modelo de datos inmutable y conciso.
 *
 * @param nombre El nombre completo del país (ej: "Bolivia").
 * @param codigo El código FIFA de 3 letras del país (ej: "BOL").
 */
public record Pais(String nombre, String codigo) {
}