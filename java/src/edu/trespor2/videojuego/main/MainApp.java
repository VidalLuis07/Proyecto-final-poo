package edu.trespor2.videojuego.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    // Tamaño de la ventana — ajusta si tu mapa necesita más espacio
    public static final double ANCHO  = 1280;
    public static final double ALTO   = 720;

    @Override
    public void start(Stage stage) {
        // 1) Creamos el Canvas donde se dibuja todo
        Canvas canvas = new Canvas(ANCHO, ALTO);

        // 2) Lo metemos en un contenedor y creamos la Scene de JavaFX
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, ANCHO, ALTO);

        // 3) Creamos y arrancamos el GameLoop, pasándole todo lo que necesita
        GameLoop gameLoop = new GameLoop(canvas, scene);
        gameLoop.start(); // Esto activa el AnimationTimer

        // 4) Configuramos la ventana
        stage.setTitle("Dungeon Crawler — POO 2025");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Punto de entrada real de JavaFX
    }
}