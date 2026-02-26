package edu.trespor2.videojuego.model;

public class IdiomaManager {
    public enum Idioma { EN, ES }
    private static IdiomaManager instancia;

    public static IdiomaManager getInstance() {
        if (instancia == null) {
            instancia = new IdiomaManager();
        }
        return instancia;
    }
    private Idioma idiomaActual = Idioma.ES;

    private IdiomaManager() {}


    public Idioma getIdioma() {
        return idiomaActual;
    }

    public boolean esEspanol() {
        return idiomaActual == Idioma.ES;
    }

    public void setIdioma(Idioma idioma) {
        this.idiomaActual = idioma;
    }

    /** Alterna entre EN y ES */
    public void toggleIdioma() {
        idiomaActual = (idiomaActual == Idioma.ES) ? Idioma.EN : Idioma.ES;
    }
    public String clave(String claveEN, String claveES) {
        return (idiomaActual == Idioma.EN) ? claveEN : claveES;
    }
}