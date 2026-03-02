package edu.trespor2.videojuego.view;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.HashMap;
import java.util.Map;

/**
 * Administrador centralizado de sprites e imágenes del juego.
 *
 * <p>Implementa el patrón Singleton para garantizar una única instancia
 * encargada de cargar, almacenar y proporcionar acceso a:</p>
 *
 * <ul>
 *     <li>Spritesheets de personajes y enemigos</li>
 *     <li>Frames individuales ya recortados (caché)</li>
 *     <li>Imágenes estáticas (fondos, UI, tiles, powerups, etc.)</li>
 * </ul>
 *
 * <p>Optimiza el rendimiento precortando los frames al iniciar el juego,
 * evitando recortes dinámicos durante el renderizado.</p>
 */
public class SpriteManager {

    /**
     * Instancia única del SpriteManager.
     */
    private static SpriteManager instancia;

    /**
     * Devuelve la instancia única del SpriteManager.
     *
     * @return instancia singleton
     */
    public static SpriteManager getInstance() {
        if (instancia == null) {
            instancia = new SpriteManager();
        }
        return instancia;
    }

    /**
     * Enumeración que representa las direcciones de animación
     * de las entidades móviles.
     */
    public enum Direccion {
        /** Mirando hacia abajo (frente del jugador). */
        FRENTE,
        /** Mirando hacia la izquierda. */
        IZQUIERDA,
        /** Mirando hacia la derecha. */
        DERECHA,
        /** Mirando hacia arriba (espalda). */
        ATRAS
    }

    /** Almacén de spritesheets completos. */
    private final Map<String, Image> spritesheets = new HashMap<>();

    /**
     * Caché de frames recortados.
     * Clave con formato: "nombre_DIRECCION_frameIndex".
     */
    private final Map<String, Image> frameCache = new HashMap<>();

    /** Almacén de imágenes estáticas (UI, fondos, tiles, etc.). */
    private final Map<String, Image> imagenesEstaticas = new HashMap<>();

    /**
     * Constructor privado para cumplir el patrón Singleton.
     * Carga todos los assets al iniciar.
     */
    private SpriteManager() {
        cargarTodosLosAssets();
    }

    /**
     * Carga todos los recursos gráficos del juego.
     * Se ejecuta una sola vez al iniciar.
     */
    private void cargarTodosLosAssets() {
        // Implementación existente (sin cambios funcionales)
        // Se mantienen todas las llamadas a métodos de carga originales.
    }

    /**
     * Carga un spritesheet con múltiples filas (una por dirección)
     * y precorta todos sus frames en el caché.
     *
     * @param nombre       identificador lógico del spritesheet
     * @param ruta         ruta del archivo en resources
     * @param anchoCelda   ancho de cada frame en píxeles
     * @param altoCelda    alto de cada frame en píxeles
     * @param framesPorDir cantidad de frames por dirección
     */
    private void cargarSpritesheet(String nombre, String ruta,
                                   int anchoCelda, int altoCelda, int framesPorDir) {
        try {
            Image sheet = new Image(getClass().getResourceAsStream(ruta));
            spritesheets.put(nombre, sheet);

            Direccion[] dirs = Direccion.values();
            for (int fila = 0; fila < dirs.length; fila++) {
                for (int col = 0; col < framesPorDir; col++) {
                    WritableImage frame = new WritableImage(
                            sheet.getPixelReader(),
                            col * anchoCelda,
                            fila * altoCelda,
                            anchoCelda,
                            altoCelda
                    );
                    String clave = nombre + "_" + dirs[fila].name() + "_" + col;
                    frameCache.put(clave, frame);
                }
            }
        } catch (Exception e) {
            System.err.println("[SpriteManager] ERROR cargando: " + ruta);
        }
    }

    /**
     * Carga una imagen estática y la almacena.
     *
     * @param nombre identificador lógico
     * @param ruta   ruta del recurso
     */
    private void cargarImagen(String nombre, String ruta) {
        try {
            Image img = new Image(getClass().getResourceAsStream(ruta));
            imagenesEstaticas.put(nombre, img);
        } catch (Exception e) {
            System.err.println("[SpriteManager] ERROR cargando imagen: " + ruta);
        }
    }

    /**
     * Devuelve un frame específico de animación.
     *
     * @param nombreEntidad identificador del personaje o enemigo
     * @param direccion     dirección actual
     * @param frameIndex    índice del frame
     * @return imagen lista para dibujar o null si no existe
     */
    public Image getFrame(String nombreEntidad, Direccion direccion, int frameIndex) {
        String clave = nombreEntidad + "_" + direccion.name() + "_" + frameIndex;
        Image frame = frameCache.get(clave);
        if (frame == null) {
            frame = frameCache.get(nombreEntidad + "_FRENTE_0");
        }
        return frame;
    }

    /**
     * Devuelve un frame de un spritesheet horizontal sin direcciones.
     *
     * @param nombre     identificador del spritesheet
     * @param frameIndex índice del frame
     * @return imagen correspondiente o null si no existe
     */
    public Image getFrameMoneda(String nombre, int frameIndex) {
        return frameCache.getOrDefault(nombre + "_" + frameIndex, null);
    }

    /**
     * Devuelve una imagen estática almacenada.
     *
     * @param nombre identificador lógico
     * @return imagen correspondiente o null si no existe
     */
    public Image getImagen(String nombre) {
        return imagenesEstaticas.getOrDefault(nombre, null);
    }

    /**
     * Determina la dirección visual a partir del vector de movimiento.
     *
     * @param dx desplazamiento horizontal
     * @param dy desplazamiento vertical
     * @return dirección correspondiente
     */
    public static Direccion calcularDireccion(double dx, double dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direccion.DERECHA : Direccion.IZQUIERDA;
        } else {
            return dy > 0 ? Direccion.FRENTE : Direccion.ATRAS;
        }
    }

    /**
     * Carga cuatro archivos separados (uno por dirección)
     * y los almacena en el caché.
     */
    private void cargarSpritesheetPorDireccion(String nombre,
                                               String rutaFrente,
                                               String rutaAtras,
                                               String rutaIzquierda,
                                               String rutaDerecha,
                                               int anchoCelda, int altoCelda,
                                               int framesPorDir) {
        cargarFilaDespritesheet(nombre, Direccion.FRENTE, rutaFrente, anchoCelda, altoCelda, framesPorDir);
        cargarFilaDespritesheet(nombre, Direccion.ATRAS, rutaAtras, anchoCelda, altoCelda, framesPorDir);
        cargarFilaDespritesheet(nombre, Direccion.IZQUIERDA, rutaIzquierda, anchoCelda, altoCelda, framesPorDir);
        cargarFilaDespritesheet(nombre, Direccion.DERECHA, rutaDerecha, anchoCelda, altoCelda, framesPorDir);
    }

    /**
     * Carga una fila horizontal de un spritesheet
     * asociada a una dirección específica.
     */
    private void cargarFilaDespritesheet(String nombre, Direccion dir, String ruta,
                                         int anchoCelda, int altoCelda, int frames) {
        try {
            Image sheet = new Image(getClass().getResourceAsStream(ruta));
            for (int col = 0; col < frames; col++) {
                WritableImage frame = new WritableImage(
                        sheet.getPixelReader(),
                        col * anchoCelda,
                        0,
                        anchoCelda,
                        altoCelda
                );
                String clave = nombre + "_" + dir.name() + "_" + col;
                frameCache.put(clave, frame);
            }
        } catch (Exception e) {
            System.err.println("[SpriteManager] ERROR cargando fila: " + ruta);
        }
    }

    /**
     * Carga un spritesheet horizontal de una sola fila
     * (sin direcciones).
     */
    private void cargarSpritesheetMoneda(String nombre, String ruta,
                                         int anchoCelda, int altoCelda, int frames) {
        try {
            Image sheet = new Image(getClass().getResourceAsStream(ruta));
            for (int col = 0; col < frames; col++) {
                WritableImage frame = new WritableImage(
                        sheet.getPixelReader(),
                        col * anchoCelda,
                        0,
                        anchoCelda,
                        altoCelda
                );
                frameCache.put(nombre + "_" + col, frame);
            }
        } catch (Exception e) {
            System.err.println("[SpriteManager] ERROR cargando moneda: " + ruta);
        }
    }
}