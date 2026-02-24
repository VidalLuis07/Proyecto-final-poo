package edu.trespor2.videojuego.model.items;

public class ShopItem {
    private String nombre;
    private int costo;
    private String tipoMejora;

    public ShopItem(String nombre, int costo, String tipoMejora) {
        this.nombre = nombre;
        this.costo = costo;
        this.tipoMejora = tipoMejora;
    }

    public String getNombre() { return nombre; }
    public int getCosto() { return costo; }
    public String getTipoMejora() { return tipoMejora; }

}    // Agregar los sprites de los items
