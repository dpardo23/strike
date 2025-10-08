package com.dpardo.strike.domain;

/**
 * Representa la información de una sesión de usuario después de una autenticación exitosa.
 * Se utiliza un 'record' para un modelo de datos inmutable y conciso.
 *
 * @param userId El ID del usuario que ha iniciado sesión.
 * @param pid El ID del proceso de backend de PostgreSQL que atiende la conexión.
 * @param clientAddress La dirección IP del cliente conectado.
 * @param clientPort El puerto del cliente conectado.
 * @param roleName El nombre del rol principal del usuario.
 */
public record SessionInfo(int userId, int pid, String clientAddress, int clientPort, String roleName) {
}