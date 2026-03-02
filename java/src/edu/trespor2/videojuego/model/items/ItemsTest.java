package edu.trespor2.videojuego.model.items;

public class ItemsTest {

    private static int passed = 0;
    private static int failed = 0;

    static void assertTrue(String nombre, boolean condicion) {
        if (condicion) {
            System.out.println("  [OK]   " + nombre);
            passed++;
        } else {
            System.out.println("  [FAIL] " + nombre);
            failed++;
        }
    }

    static void assertEquals(String nombre, Object esperado, Object actual) {
        boolean igual = esperado == null ? actual == null : esperado.equals(actual);
        assertTrue(nombre + " (esperado=" + esperado + ", actual=" + actual + ")", igual);
    }

    // ── Coins ─────────────────────────────────────────────────────────────────

    static void testCoinsGetValor() {
        Coins c = new Coins(0, 0, 5);
        assertEquals("getValor() = 5", 5, c.getValor());
    }

    static void testCoinsSetValor() {
        Coins c = new Coins(0, 0, 5);
        c.setValor(10);
        assertEquals("getValor() = 10 tras setValor(10)", 10, c.getValor());
    }

    static void testCoinsUpdateAvanzaFrame() {
        Coins c = new Coins(0, 0, 1);
        int frameInicial = c.getFrameActual();
        for (int i = 0; i < 6; i++) c.update(); // VELOCIDAD_ANIM = 6
        assertEquals("frame avanza 1 tras 6 updates", (frameInicial + 1) % 10, c.getFrameActual());
    }

    static void testCoinsFrameCicla() {
        Coins c = new Coins(0, 0, 1);
        for (int i = 0; i < 60; i++) c.update(); // 10 frames × 6 ticks = vuelve a 0
        assertEquals("frame vuelve a 0 tras un ciclo completo", 0, c.getFrameActual());
    }

    // ── Chest ─────────────────────────────────────────────────────────────────

    static void testChestCerradoAlCrearse() {
        Chest cofre = new Chest(0, 0);
        assertTrue("cofre cerrado al crearse", !cofre.isAbierto());
    }

    static void testChestAbrirCambiaEstado() {
        Chest cofre = new Chest(0, 0);
        cofre.abrir();
        assertTrue("cofre abierto tras llamar a abrir()", cofre.isAbierto());
    }

    static void testChestAbrirRetornaMonedasValidas() {
        Chest cofre = new Chest(0, 0);
        int monedas = cofre.abrir();
        assertTrue("monedas generadas entre 8 y 15 (fue " + monedas + ")",
                monedas >= 8 && monedas <= 15);
    }

    static void testChestAbrirYaAbiertoRetornaCero() {
        Chest cofre = new Chest(0, 0);
        cofre.abrir();
        int segundaVez = cofre.abrir();
        assertEquals("abrir cofre ya abierto retorna 0", 0, segundaVez);
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== ItemsTest (Coins + Chest) ===");
        testCoinsGetValor();
        testCoinsSetValor();
        testCoinsUpdateAvanzaFrame();
        testCoinsFrameCicla();
        testChestCerradoAlCrearse();
        testChestAbrirCambiaEstado();
        testChestAbrirRetornaMonedasValidas();
        testChestAbrirYaAbiertoRetornaCero();
        System.out.println("Resultado: " + passed + " ok, " + failed + " fallidos\n");
    }
}