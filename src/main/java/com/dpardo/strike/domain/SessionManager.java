package com.dpardo.strike.domain;

/**
 * Clase de utilidad para gestionar la información de la sesión activa de forma global.
 * Actúa como un singleton para mantener una única instancia de la sesión actual
 * accesible desde cualquier parte de la aplicación.
 */
public final class SessionManager {

    // Almacena la información de la sesión del usuario que ha iniciado sesión.
    private static SessionInfo currentSession;

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private SessionManager() {
        // Esta clase no debe ser instanciada.
    }

    /**
     * Establece la sesión activa actual. Se llama después de un login exitoso.
     * @param sessionInfo El objeto con la información de la sesión a guardar.
     */
    public static void setCurrentSession(SessionInfo sessionInfo) {
        currentSession = sessionInfo;
    }

    /**
     * Obtiene la sesión activa actual.
     * @return El objeto SessionInfo de la sesión activa, o null si no hay ninguna.
     */
    public static SessionInfo getCurrentSession() {
        return currentSession;
    }

    // VvVvV MÉTODO AÑADIDO VvVvV
    /**
     * Obtiene el ID del usuario de la sesión actual.
     * Este método es utilizado por DatabaseConnection para "marcar" cada nueva conexión.
     *
     * @return El ID del usuario si hay una sesión activa, de lo contrario null.
     */
    public static Integer getCurrentUserId() {
        if (currentSession != null) {
            return currentSession.userId();
        }
        return null;
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}