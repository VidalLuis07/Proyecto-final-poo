package edu.trespor2.videojuego.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Set<KeyCode> activeKeys = new HashSet<>();

    /**
     * Crea un manejador de entrada vinculado a la escena proporcionada.
     * Registra listeners para detectar teclas presionadas y liberadas en tiempo real.
     *
     * @param scene la escena de JavaFX sobre la que se escucharán los eventos de teclado
     */
    public InputHandler(Scene scene) {
        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    /**
     * Verifica si una tecla específica está actualmente presionada.
     *
     * @param code el código de la tecla a consultar
     * @return {@code true} si la tecla está presionada; {@code false} en caso contrario
     */
    public boolean isKeyPressed(KeyCode code) {
        return activeKeys.contains(code);
    }

    /**
     * Limpia todas las teclas activas registradas.
     * Útil para reiniciar el estado de entrada al cambiar de escena o pausar el juego.
     */
    public void clearKeys() {
        activeKeys.clear();
    }

}