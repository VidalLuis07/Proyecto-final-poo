package edu.trespor2.videojuego.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Singleton que gestiona la reproducción de música del juego.
 * Controla las pistas disponibles, el volumen y el estado del reproductor actual.
 */
public class MusicManager {
    private static MusicManager instance;

    /**
     * Retorna la única instancia de {@code MusicManager}, creándola si no existe.
     *
     * @return instancia única de {@code MusicManager}
     */
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
    /**
     * Reproduce la pista indicada en loop infinito.
     * Si la pista ya está sonando, no la reinicia.
     * Si hay otra pista activa, la detiene antes de reproducir la nueva.
     *
     * @param pista la {@code Pista} a reproducir
     */
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

    /**
     * Detiene la música actual y libera los recursos del reproductor.
     */
    /** Detiene la música actual. */
    public void detener() {
        if (reproductorActual != null) {
            reproductorActual.stop();
            reproductorActual.dispose();
            reproductorActual = null;
        }
        pistaActual = null;
    }

    /**
     * Pausa la reproducción sin perder la posición actual.
     */
    /** Pausa sin perder la posición. */
    public void pausar() {
        if (reproductorActual != null) reproductorActual.pause();
    }

    /**
     * Reanuda la reproducción desde donde fue pausada.
     */
    /** Reanuda desde donde se pausó. */
    public void reanudar() {
        if (reproductorActual != null) reproductorActual.play();
    }

    /**
     * Cambia el volumen de reproducción, aplicándolo también al reproductor activo si existe.
     * El valor se limita automáticamente al rango [0.0, 1.0].
     *
     * @param v nuevo volumen (0.0 = silencio, 1.0 = máximo)
     */
    /** Cambia el volumen (0.0 = silencio, 1.0 = máximo). */
    public void setVolumen(double v) {
        volumen = Math.max(0.0, Math.min(1.0, v));
        if (reproductorActual != null) reproductorActual.setVolume(volumen);
    }

    /**
     * Retorna el volumen actual de reproducción.
     *
     * @return volumen entre 0.0 y 1.0
     */
    public double getVolumen() { return volumen; }

    // ── Rutas de los archivos ─────────────────────────────────────────────

    /**
     * Retorna la ruta del archivo de audio correspondiente a la pista dada.
     *
     * @param pista la {@code Pista} cuya ruta se quiere obtener
     * @return ruta del archivo de audio como {@code String}
     */
    private String getRuta(Pista pista) {
        return switch (pista) {
            case MENU  -> "/assets/music/menu.mp3";
            case NIVEL -> "/assets/music/nivel.mp3";
            case JEFE  -> "/assets/music/jefe.mp3";
            case CREDITOS -> "/assets/music/creditos.mp3";
        };
    }
}