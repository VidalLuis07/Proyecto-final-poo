package edu.trespor2.videojuego.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    // Tamaño de la ventana
    public static final double ANCHO  = 1280;
    public static final double ALTO   = 720;

    @Override
    public void start(Stage stage) {
        // Creamos el Canva
        Canvas canvas = new Canvas(ANCHO, ALTO);

        //Scene de JavaFX
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, ANCHO, ALTO);

        //Arrancamos el GameLoop
        GameLoop gameLoop = new GameLoop(canvas, scene);
        gameLoop.start();

        //Configuramos la ventana
        stage.setTitle("Dungeon Crawler — POO 2025");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}