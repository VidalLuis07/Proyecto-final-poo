package edu.trespor2.videojuego.model;

/**
 * Define el comportamiento de las entidades que pueden recibir daño
 * y tener un estado de vida dentro del juego.
 *
 * Las clases que implementen esta interfaz deben gestionar su
 * propia lógica de salud y determinar cuándo han sido derrotadas.
 */
public interface IDamageable {

    /**
     * Aplica una cantidad de daño a la entidad.
     *
     * @param cantidad Cantidad de daño a recibir.
     */
    void recibirDano(int cantidad);

    /**
     * Indica si la entidad ha sido derrotada.
     *
     * @return true si la entidad está muerta, false en caso contrario.
     */
    boolean estaMuerto();

}