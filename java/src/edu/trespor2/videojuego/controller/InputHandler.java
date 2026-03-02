package edu.trespor2.videojuego.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase encargada de manejar la entrada del teclado.
 * Registra las teclas presionadas y liberadas dentro de la escena.
 */
public class InputHandler {

    /**
     * Conjunto que almacena las teclas actualmente presionadas.
     */
    private final Set<KeyCode> activeKeys = new HashSet<>();

    /**
     * Constructor de la clase InputHandler.
     * Configura los eventos de teclado para detectar
     * cuando una tecla es presionada o liberada.
     *
     * @param scene Escena donde se capturarán los eventos de teclado.
     */
    public InputHandler(Scene scene) {
        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    /**
     * Verifica si una tecla específica está actualmente presionada.
     *
     * @param code Código de la tecla a verificar.
     * @return true si la tecla está presionada,
     *         false en caso contrario.
     */
    public boolean isKeyPressed(KeyCode code) {
        return activeKeys.contains(code);
    }

    /**
     * Limpia el registro de teclas activas.
     * Se utiliza para reiniciar el estado del teclado.
     */
    public void clearKeys() {
        activeKeys.clear();
    }
}
