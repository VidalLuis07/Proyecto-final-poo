package edu.trespor2.videojuego.model;

/**
 * Gestiona el idioma actual del juego.
 *
 * Implementa el patrón Singleton para garantizar que exista
 * una única instancia global encargada del control del idioma.
 * Permite alternar entre español (ES) e inglés (EN).
 */
public class IdiomaManager {

    /**
     * Idiomas disponibles en el juego.
     */
    public enum Idioma { EN, ES }

    private static IdiomaManager instancia;
    private Idioma idiomaActual = Idioma.ES;

    /**
     * Constructor privado para evitar instanciación externa.
     */
    private IdiomaManager() {}

    /**
     * Obtiene la instancia única del gestor de idioma.
     *
     * @return Instancia singleton de IdiomaManager.
     */
    public static IdiomaManager getInstance() {
        if (instancia == null) {
            instancia = new IdiomaManager();
        }
        return instancia;
    }

    /**
     * Obtiene el idioma actualmente seleccionado.
     *
     * @return Idioma actual.
     */
    public Idioma getIdioma() {
        return idiomaActual;
    }

    /**
     * Indica si el idioma actual es español.
     *
     * @return true si el idioma es ES, false si es EN.
     */
    public boolean esEspanol() {
        return idiomaActual == Idioma.ES;
    }

    /**
     * Establece el idioma actual.
     *
     * @param idioma Nuevo idioma a configurar.
     */
    public void setIdioma(Idioma idioma) {
        this.idiomaActual = idioma;
    }

    /**
     * Alterna el idioma actual entre inglés (EN) y español (ES).
     */
    public void toggleIdioma() {
        idiomaActual = (idiomaActual == Idioma.ES) ? Idioma.EN : Idioma.ES;
    }

    /**
     * Devuelve una cadena en el idioma actual.
     *
     * @param claveEN Texto en inglés.
     * @param claveES Texto en español.
     * @return Texto correspondiente al idioma seleccionado.
     */
    public String clave(String claveEN, String claveES) {
        return (idiomaActual == Idioma.EN) ? claveEN : claveES;
    }
}