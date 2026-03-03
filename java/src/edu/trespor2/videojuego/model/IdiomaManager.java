package edu.trespor2.videojuego.model;

/**
 * Singleton que gestiona el idioma activo del juego.
 * Permite alternar entre inglés ({@code EN}) y español ({@code ES})
 * y obtener textos localizados según el idioma actual.
 */
public class IdiomaManager {
    public enum Idioma { EN, ES }
    private static IdiomaManager instancia;

    /**
     * Retorna la única instancia de {@code IdiomaManager}, creándola si no existe.
     *
     * @return instancia única de {@code IdiomaManager}
     */
    public static IdiomaManager getInstance() {
        if (instancia == null) {
            instancia = new IdiomaManager();
        }
        return instancia;
    }

    private Idioma idiomaActual = Idioma.ES;

    private IdiomaManager() {}

    /**
     * Retorna el idioma actualmente seleccionado.
     *
     * @return idioma actual ({@code EN} o {@code ES})
     */
    public Idioma getIdioma() {
        return idiomaActual;
    }

    /**
     * Indica si el idioma actual es español.
     *
     * @return {@code true} si el idioma es español; {@code false} si es inglés
     */
    public boolean esEspanol() {
        return idiomaActual == Idioma.ES;
    }

    /**
     * Establece el idioma activo del juego.
     *
     * @param idioma nuevo idioma ({@code EN} o {@code ES})
     */
    public void setIdioma(Idioma idioma) {
        this.idiomaActual = idioma;
    }

    /** Alterna entre EN y ES */
    /**
     * Alterna el idioma activo entre español e inglés.
     */
    public void toggleIdioma() {
        idiomaActual = (idiomaActual == Idioma.ES) ? Idioma.EN : Idioma.ES;
    }

    /**
     * Retorna el texto correspondiente al idioma actual entre dos opciones dadas.
     *
     * @param claveEN texto en inglés
     * @param claveES texto en español
     * @return {@code claveEN} si el idioma es inglés; {@code claveES} si es español
     */
    public String clave(String claveEN, String claveES) {
        return (idiomaActual == Idioma.EN) ? claveEN : claveES;
    }
}