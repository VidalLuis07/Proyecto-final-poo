package edu.trespor2.videojuego.model.items;

/**
 * StoreItem — Ítem vendible en la tienda.
 * Esta versión es funcional y compilable de inmediato.
 * Cuando el equipo implemente la lógica de efectos (buffs, etc.) se expande esta clase.
 */
public class StoreItem {

    private final String name;
    private final String description;
    private final int price;

    public StoreItem(String name, String description, int price) {
        this.name        = name;
        this.description = description;
        this.price       = price;
    }

    public String getName()        { return name;        }
    public String getDescription() { return description; }
    public int    getPrice()       { return price;       }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}