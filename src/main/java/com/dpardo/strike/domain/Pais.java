package com.dpardo.strike.domain;

/**
 * Esta clase es un 'record', una forma moderna en Java para crear
 * una clase simple que solo sirve para guardar datos.
 * Automáticamente nos da un constructor y los métodos para obtener los datos.
 */
public record Pais(String nombre, String codigo) {
}