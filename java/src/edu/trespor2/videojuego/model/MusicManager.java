package edu.trespor2.videojuego.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicManager {
    private static MusicManager instance;
    public static MusicManager getInstance() {
        if (instance == null) instance = new MusicManager();
        return instance;
    }

    // ── Pistas disponibles ────────────────────────────────────────────────
    public enum Pista {
        MENU,
        NIVEL,
        JEFE,
        CREDITOS
    }

    // ── Estado interno ────────────────────────────────────────────────────
    private MediaPlayer reproductorActual;
    private Pista       pistaActual = null;
    private double      volumen     = 0.5; // 0.0 → 1.0

    private MusicManager() {}

    // ── API pública ────────────────────────────────────────────────────────

    /** Reproduce la pista indicada en loop. Si ya suena esa misma, no hace nada. */
    public void reproducir(Pista pista) {
        if (pista == pistaActual) return; // ya está sonando, no reiniciar

        detener();

        String ruta = getRuta(pista);
        if (ruta == null) return;

        try {
            var url = getClass().getResource(ruta);
            if (url == null) {
                System.out.println("[MusicManager] Archivo no encontrado: " + ruta);
                return;
            }
            Media media = new Media(url.toExternalForm());
            reproductorActual = new MediaPlayer(media);
            reproductorActual.setVolume(volumen);
            reproductorActual.setCycleCount(MediaPlayer.INDEFINITE); // loop infinito
            reproductorActual.setOnEndOfMedia(() ->
                    reproductorActual.seek(Duration.ZERO)   // por si acaso
            );
            reproductorActual.play();
            pistaActual = pista;
        } catch (Exception e) {
            System.out.println("[MusicManager] Error al reproducir " + ruta + ": " + e.getMessage());
        }
    }

    /** Detiene la música actual. */
    public void detener() {
        if (reproductorActual != null) {
            reproductorActual.stop();
            reproductorActual.dispose();
            reproductorActual = null;
        }
        pistaActual = null;
    }

    /** Pausa sin perder la posición. */
    public void pausar() {
        if (reproductorActual != null) reproductorActual.pause();
    }

    /** Reanuda desde donde se pausó. */
    public void reanudar() {
        if (reproductorActual != null) reproductorActual.play();
    }

    /** Cambia el volumen (0.0 = silencio, 1.0 = máximo). */
    public void setVolumen(double v) {
        volumen = Math.max(0.0, Math.min(1.0, v));
        if (reproductorActual != null) reproductorActual.setVolume(volumen);
    }

    public double getVolumen() { return volumen; }

    // ── Rutas de los archivos ─────────────────────────────────────────────
    private String getRuta(Pista pista) {
        return switch (pista) {
            case MENU  -> "/assets/music/menu.mp3";
            case NIVEL -> "/assets/music/nivel.mp3";
            case JEFE  -> "/assets/music/jefe.mp3";
            case CREDITOS -> "/assets/music/creditos.mp3";
        };
    }
}