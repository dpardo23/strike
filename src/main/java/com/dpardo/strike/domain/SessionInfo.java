package com.dpardo.strike.domain;

/**
 * Representa la información de una sesión de base de datos activa.
 * Creado como un 'record' de Java para una clase de datos inmutable y concisa.
 *
 * @param userId El ID del usuario de la tabla 'user'.
 * @param pid El Process ID de la sesión en el servidor de PostgreSQL.
 * @param clientAddress La dirección IP desde la que se conecta el cliente.
 * @param clientPort El puerto desde el que se conecta el cliente.
 */

public record SessionInfo(int userId, int pid, String clientAddress, int clientPort) {
}