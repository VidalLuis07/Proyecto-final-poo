package edu.trespor2.videojuego.model;

/**
 * Interfaz que deben implementar todas las entidades que pueden recibir daño y morir.
 */
public interface IDamageable {

    /**
     * Aplica la cantidad de daño indicada a la entidad.
     *
     * @param cantidad puntos de daño a recibir
     */
    void recibirDano(int cantidad);

    /**
     * Indica si la entidad ha muerto, es decir, si su vida ha llegado a 0 o menos.
     *
     * @return {@code true} si la entidad está muerta; {@code false} en caso contrario
     */
    boolean estaMuerto();
}