package com.dpardo.strike;

/**
 * Clase de lanzamiento para el 'Fat JAR'.
 * Sirve como un punto de entrada que luego llama al main() de la aplicación JavaFX principal.
 * Esto es necesario para solucionar problemas de compatibilidad con JavaFX modular.
 */
public class Launcher {
    public static void main(String[] args) {
        MainApplication.main(args);
    }
}