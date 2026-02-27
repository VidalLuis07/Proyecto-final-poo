package edu.trespor2.videojuego.view;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private static SpriteManager instancia;

    public static SpriteManager getInstance() {
        if (instancia == null) {
            instancia = new SpriteManager();
        }
        return instancia;
    }

    // ── Enum de dirección (conecta con dx/dy del jugador/enemigo) ──────────
    public enum Direccion {
        FRENTE,     // fila 0 del spritesheet (bajando, mirando al jugador)
        IZQUIERDA,  // fila 1
        DERECHA,    // fila 2
        ATRAS       // fila 3
    }

    // ── Almacén de spritesheets completos ──────────────────────────────────
    private final Map<String, Image> spritesheets = new HashMap<>();

    // ── Almacén de frames ya recortados (caché para no recortar cada frame) ─
    // Clave: "nombre_DIRECCION_frameIndex"  Ej: "jugador_FRENTE_0"
    private final Map<String, Image> frameCache = new HashMap<>();

    // ── Imágenes estáticas (fondos, UI, tiles) ─────────────────────────────
    private final Map<String, Image> imagenesEstaticas = new HashMap<>();

    private SpriteManager() {
        cargarTodosLosAssets();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  CARGA INICIAL — Se llama una sola vez al arrancar el juego
    // ══════════════════════════════════════════════════════════════════════
    private void cargarTodosLosAssets() {
        System.out.println("Buscando recursos en: " +
                getClass().getResource("/assets/images/carlosFrente.png"));

        // ── Personajes jugables (archivos separados por dirección) ─────────
        cargarSpritesheetPorDireccion(
                "carlos",
                "/assets/images/carlosFrente.png",
                "/assets/images/carlosDetras.png",
                "/assets/images/carlosIzquierda.png",
                "/assets/images/carlosDerecha.png",
                32, 32, 4);

        // ── Animaciones de ataque con daga ─────────────────────────────────
        // Frente y atrás: cada frame mide 32x40 (3 frames = 96px de ancho)
        // Izquierda y derecha: cada frame mide 40x32 (3 frames = 120px de ancho)
        cargarFilaDespritesheet("carlos_ataque", Direccion.FRENTE,    "/assets/images/carlosDagaFrente.png",    32, 32, 3);
        cargarFilaDespritesheet("carlos_ataque", Direccion.ATRAS,     "/assets/images/carloDagaDetras.png",     32, 32, 3);
        cargarFilaDespritesheet("carlos_ataque", Direccion.IZQUIERDA, "/assets/images/carlosDagaIzquierda.png", 32, 32, 3);
        cargarFilaDespritesheet("carlos_ataque", Direccion.DERECHA,   "/assets/images/carlosDagaDerecha.png",   32, 32, 3);

        // ── Sprite de la daga (proyectil) ──────────────────────────────────
        cargarImagen("daga", "/assets/images/daga.png");

        // ── Enemigos (spritesheet único con 4 filas) ───────────────────────
        cargarSpritesheet("zombie", "/assets/images/chatFrente.png", 40, 40, 4);
        cargarSpritesheet("boss",   "/assets/images/boss.png",   64, 64, 6);

        //MENU
        cargarImagen("menu_fondo",   "/assets/images/1.png");
        // ── Botones de MENÚ con soporte de idioma ─────────────────────────
        cargarImagen("boton_start", "/assets/Idioma/start.png");
        cargarImagen("boton_exit", "/assets/Idioma/exit.png");

        // ── Botones de MENÚ con soporte de idioma ─────────────────────────
        cargarImagen("boton_iniciar", "/assets/Idioma/iniciar.png");  // ES
        cargarImagen("boton_salir", "/assets/Idioma/salir.png");    // ES

        // ── Botón de cambio de idioma ──────────────────────────────────────
        // Muestra ESP cuando el juego está en español (para cambiar a inglés)
        // Muestra ENG cuando el juego está en inglés  (para cambiar a español)
        cargarImagen("boton_lang_esp", "/assets/Idioma/esp.png");     // se muestra cuando idioma=ES
        cargarImagen("boton_lang_eng", "/assets/Idioma/eng.png");     // se muestra cuando idioma=EN

        // ── Game Over ──────────────────────────────────────────────────────
        cargarImagen("gameover_en", "/assets/Idioma/GameOver.png");
        cargarImagen("gameover_es", "/assets/Idioma/perdiste.png");

        // ── Botones YES / NO / SÍ ─────────────────────────────────────────
        cargarImagen("boton_yes", "/assets/Idioma/yes.png");
        cargarImagen("boton_no", "/assets/Idioma/no.png");
        cargarImagen("boton_si", "/assets/Idioma/si.png");
        // ── Tiles del mapa ─────────────────────────────────────────────────
        cargarImagen("tile_vacio",  "/assets/sprites/mapa/tile_vacio.png");
        cargarImagen("tile_piso",   "/assets/sprites/mapa/tile_piso.png");
        cargarImagen("tile_pared",  "/assets/sprites/mapa/tile_pared.png");

        //Corazones
        cargarImagen("corazon_lleno", "/assets/images/corazon.png");
        cargarImagen("corazon_vacio", "/assets/images/corazonVacio.png");

        // ── Cofres ────────────────────────────────────────────────────────
        cargarImagen("cofre_cerrado", "/assets/images/Cofre cerradooo.png");
        cargarImagen("cofre_abierto", "/assets/images/Cofre abiertoo.png");

        // ── Moneda ────────────────────────────────────────────────────────
        cargarImagen("moneda", "/assets/Idioma/monedas.png");

        // ── Jefe Final ────────────────────────────────────────────────────
        // Solo tenemos sprite de derecha e izquierda.
        // FRENTE y ATRAS usan la imagen de DERECHA como fallback.
        cargarFilaDespritesheet("jefe", Direccion.DERECHA,   "/assets/images/jefeFinalDerecha.png", 32, 32, 1);
        cargarFilaDespritesheet("jefe", Direccion.IZQUIERDA, "/assets/images/jefeFinalLeft.png",    32, 32, 1);
        cargarFilaDespritesheet("jefe", Direccion.FRENTE,    "/assets/images/jefeFinalDerecha.png", 32, 32, 1);
        cargarFilaDespritesheet("jefe", Direccion.ATRAS,     "/assets/images/jefeFinalDerecha.png", 32, 32, 1);

        // ── Powerups (tienda) ─────────────────────────────────────────────
        cargarImagen("powerup_fuego_mortal",  "/assets/powerups/fuegoMortal.png");
        cargarImagen("powerup_fuego_veloz",   "/assets/powerups/fuegoVeloz.png");
        cargarImagen("powerup_fire_velocity", "/assets/powerups/fireVelocity.png");
        cargarImagen("powerup_mortal_fire",   "/assets/powerups/mortalFire.png");
        cargarImagen("powerup_lloros",        "/assets/powerups/lloros.png");
        cargarImagen("powerup_cries",         "/assets/powerups/cries.png");
    }

    /**
     * Carga un spritesheet y pre-corta todos sus frames en el caché.
     *
     * @param nombre       Identificador (ej: "jugador")
     * @param ruta         Ruta dentro de resources
     * @param anchoCelda   Ancho en px de cada frame
     * @param altoCelda    Alto en px de cada frame
     * @param framesPorDir Cuántas columnas (frames) hay por dirección
     */
    private void cargarSpritesheet(String nombre, String ruta,
                                   int anchoCelda, int altoCelda, int framesPorDir) {
        try {
            Image sheet = new Image(getClass().getResourceAsStream(ruta));
            spritesheets.put(nombre, sheet);

            // Pre-cortamos todos los frames de todas las direcciones
            Direccion[] dirs = Direccion.values();
            for (int fila = 0; fila < dirs.length; fila++) {
                for (int col = 0; col < framesPorDir; col++) {
                    WritableImage frame = new WritableImage(
                            sheet.getPixelReader(),
                            col * anchoCelda,   // X en el spritesheet
                            fila * altoCelda,   // Y en el spritesheet
                            anchoCelda,
                            altoCelda
                    );
                    String clave = nombre + "_" + dirs[fila].name() + "_" + col;
                    frameCache.put(clave, frame);
                }
            }
            System.out.println("[SpriteManager] Cargado: " + nombre);
        } catch (Exception e) {
            System.err.println("[SpriteManager] ERROR cargando: " + ruta + " → " + e.getMessage());
        }
    }

    private void cargarImagen(String nombre, String ruta) {
        try {
            Image img = new Image(getClass().getResourceAsStream(ruta));
            imagenesEstaticas.put(nombre, img);
        } catch (Exception e) {
            System.err.println("[SpriteManager] ERROR cargando imagen estática: " + ruta);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  API PÚBLICA — Lo que usan GameRenderer y las Screens
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Devuelve un frame específico de animación.
     *
     * @param nombreEntidad  "jugador", "zombie", "boss"
     * @param direccion      Hacia dónde mira el personaje
     * @param frameIndex     Índice del frame (0, 1, 2, …)
     * @return               La imagen recortada lista para dibujar
     */
    public Image getFrame(String nombreEntidad, Direccion direccion, int frameIndex) {
        String clave = nombreEntidad + "_" + direccion.name() + "_" + frameIndex;
        Image frame = frameCache.get(clave);
        if (frame == null) {
            System.err.println("[SpriteManager] Frame no encontrado: " + clave);
            // Devuelve el primer frame como fallback
            frame = frameCache.get(nombreEntidad + "_FRENTE_0");
        }
        return frame;
    }

    /**
     * Devuelve una imagen estática (fondos, UI).
     */
    public Image getImagen(String nombre) {
        return imagenesEstaticas.getOrDefault(nombre, null);
    }

    /**
     * Convierte dx/dy del personaje a una Direccion para elegir la animación.
     * Conecta directamente con los valores de EntidadMovible.
     */
    public static Direccion calcularDireccion(double dx, double dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direccion.DERECHA : Direccion.IZQUIERDA;
        } else {
            return dy > 0 ? Direccion.FRENTE : Direccion.ATRAS;
        }
    }
    /**
     * Carga 4 archivos separados (uno por dirección) y los mete al caché
     * con el mismo formato que usa getFrame().
     */
    private void cargarSpritesheetPorDireccion(String nombre,
                                               String rutaFrente,
                                               String rutaAtras,
                                               String rutaIzquierda,
                                               String rutaDerecha,
                                               int anchoCelda, int altoCelda,
                                               int framesPorDir) {
        cargarFilaDespritesheet(nombre, Direccion.FRENTE,     rutaFrente,     anchoCelda, altoCelda, framesPorDir);
        cargarFilaDespritesheet(nombre, Direccion.ATRAS,      rutaAtras,      anchoCelda, altoCelda, framesPorDir);
        cargarFilaDespritesheet(nombre, Direccion.IZQUIERDA,  rutaIzquierda,  anchoCelda, altoCelda, framesPorDir);
        cargarFilaDespritesheet(nombre, Direccion.DERECHA,    rutaDerecha,    anchoCelda, altoCelda, framesPorDir);
    }

    private void cargarFilaDespritesheet(String nombre, Direccion dir, String ruta,
                                         int anchoCelda, int altoCelda, int frames) {
        try {
            Image sheet = new Image(getClass().getResourceAsStream(ruta));
            for (int col = 0; col < frames; col++) {
                WritableImage frame = new WritableImage(
                        sheet.getPixelReader(),
                        col * anchoCelda, 0,   // siempre fila 0 porque cada archivo es una sola fila
                        anchoCelda, altoCelda
                );
                String clave = nombre + "_" + dir.name() + "_" + col;
                frameCache.put(clave, frame);
            }
            System.out.println("[SpriteManager] OK: " + nombre + " " + dir.name());
        } catch (Exception e) {
            System.err.println("[SpriteManager] ERROR: " + ruta + " → " + e.getMessage());
        }
    }
}