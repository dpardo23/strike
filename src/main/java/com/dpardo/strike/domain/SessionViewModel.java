package com.dpardo.strike.domain;

import java.sql.Timestamp;

/**
 * Representa el modelo de datos para una fila en la TableView de la vista de Super Administrador.
 * Contiene una vista detallada de una sesión activa, combinando información de varias tablas.
 *
 * @param pid El ID del proceso de la sesión de PostgreSQL.
 * @param nombreUsuario El nombre de usuario asociado a la sesión.
 * @param correo El correo electrónico del usuario.
 * @param fecCreacionUsuario La fecha en que la cuenta del usuario fue creada.
 * @param nombreRol El nombre del rol activo del usuario.
 * @param codComponenteUi El código del componente de UI principal asociado al rol.
 * @param direccionIp La dirección IP desde la que se conectó el usuario.
 * @param puerto El puerto del cliente.
 * @param fechaAsignacionRol La fecha en que se asignó el rol actual al usuario.
 * @param rolActivo El estado de actividad de la asignación del rol.
 */
public record SessionViewModel(
        int pid,
        String nombreUsuario,
        String correo,
        Timestamp fecCreacionUsuario,
        String nombreRol,
        String codComponenteUi,
        String direccionIp,
        int puerto,
        Timestamp fechaAsignacionRol,
        boolean rolActivo
) {}