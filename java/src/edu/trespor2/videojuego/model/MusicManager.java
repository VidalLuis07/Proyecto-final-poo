package edu.trespor2.videojuego.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Gestiona la reproducción de música del juego.
 *
 * Implementa el patrón Singleton para asegurar una única instancia
 * encargada del control de audio. Permite reproducir, pausar,
 * reanudar y detener pistas musicales, así como controlar el volumen.
 */
public class MusicManager {

    private static MusicManager instance;

    /**
     * Obtiene la instancia única del gestor de música.
     *
     * @return Instancia singleton de MusicManager.
     */
    public static MusicManager getInstance() {
        if (instance == null) instance = new MusicManager();
        return instance;
    }

    /**
     * Pistas musicales disponibles en el juego.
     */
    public enum Pista {
        MENU,
        NIVEL,
        JEFE,
        CREDITOS
    }

    // ── Estado interno ────────────────────────────────────────────────────
    private MediaPlayer reproductorActual;
    private Pista pistaActual = null;
    private double volumen = 0.5; // Rango: 0.0 (silencio) → 1.0 (máximo)

    /**
     * Constructor privado para evitar instanciación externa.
     */
    private MusicManager() {}

    // ── API pública ────────────────────────────────────────────────────────

    /**
     * Reproduce la pista indicada en bucle infinito.
     * Si la pista ya se encuentra reproduciéndose, no reinicia la reproducción.
     *
     * @param pista Pista a reproducir.
     */
    public void reproducir(Pista pista) {
        if (pista == pistaActual) return;

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
            reproductorActual.setCycleCount(MediaPlayer.INDEFINITE);

            reproductorActual.setOnEndOfMedia(() ->
                    reproductorActual.seek(Duration.ZERO)
            );

            reproductorActual.play();
            pistaActual = pista;

        } catch (Exception e) {
            System.out.println("[MusicManager] Error al reproducir " + ruta + ": " + e.getMessage());
        }
    }

    /**
     * Detiene completamente la reproducción actual
     * y libera los recursos asociados.
     */
    public void detener() {
        if (reproductorActual != null) {
            reproductorActual.stop();
            reproductorActual.dispose();
            reproductorActual = null;
        }
        pistaActual = null;
    }

    /**
     * Pausa la reproducción actual sin perder la posición.
     */
    public void pausar() {
        if (reproductorActual != null) reproductorActual.pause();
    }

    /**
     * Reanuda la reproducción desde la posición pausada.
     */
    public void reanudar() {
        if (reproductorActual != null) reproductorActual.play();
    }

    /**
     * Establece el volumen de reproducción.
     *
     * @param v Valor entre 0.0 (silencio) y 1.0 (máximo).
     *          Valores fuera de rango se ajustan automáticamente.
     */
    public void setVolumen(double v) {
        volumen = Math.max(0.0, Math.min(1.0, v));
        if (reproductorActual != null) reproductorActual.setVolume(volumen);
    }

    /**
     * Obtiene el volumen actual.
     *
     * @return Nivel de volumen (0.0 – 1.0).
     */
    public double getVolumen() {
        return volumen;
    }

    /**
     * Obtiene la ruta del archivo de audio asociada a una pista.
     *
     * @param pista Pista solicitada.
     * @return Ruta relativa del recurso de audio.
     */
    private String getRuta(Pista pista) {
        return switch (pista) {
            case MENU      -> "/assets/music/menu.mp3";
            case NIVEL     -> "/assets/music/nivel.mp3";
            case JEFE      -> "/assets/music/jefe.mp3";
            case CREDITOS  -> "/assets/music/creditos.mp3";
        };
    }
}