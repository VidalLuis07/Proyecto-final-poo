package edu.trespor2.videojuego.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public InputHandler(Scene scene) {
        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    public boolean isKeyPressed(KeyCode code) {
        return activeKeys.contains(code);
    }

    public void clearKeys() {
        activeKeys.clear();
    }
}
