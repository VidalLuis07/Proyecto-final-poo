package edu.trespor2.videojuego.model.entidades.personajes;

public class JefeTest {

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

    // ── tests ────────────────────────────────────────────────────────────────

    static void testNoEstaMuertoAlCrearse() {
        Jefe jefe = new Jefe(100, 100, 64, 64, 1.0, 200);
        assertTrue("jefe no está muerto al crearse", !jefe.estaMuerto());
    }

    static void testListaZombiesVaciaAlInicio() {
        Jefe jefe = new Jefe(100, 100, 64, 64, 1.0, 200);
        assertTrue("lista de zombies invocados vacía al inicio", jefe.getZombiesInvocados().isEmpty());
    }

    static void testFase1PersigueAlJugador() {
        Jefe jefe = new Jefe(0, 0, 64, 64, 1.0, 200);
        Jugador j  = new Jugador(300, 300, 64, 64, 1.0, 100, "carlos");
        jefe.perseguir(j);
        assertTrue("jefe se mueve hacia el jugador en fase 1 (dx o dy != 0)",
                jefe.getDx() != 0 || jefe.getDy() != 0);
    }

    static void testCambioAFase2Al180Frames() {
        Jefe jefe = new Jefe(0, 0, 64, 64, 1.0, 200);
        Jugador j  = new Jugador(300, 300, 64, 64, 1.0, 100, "carlos");
        // 181 llamadas → supera los 180 frames y entra en fase 2
        for (int i = 0; i < 181; i++) jefe.perseguir(j);
        assertTrue("jefe quieto en fase 2 (dx=0)", jefe.getDx() == 0.0);
        assertTrue("jefe quieto en fase 2 (dy=0)", jefe.getDy() == 0.0);
    }

    static void testInvocaZombiesAlEntrarEnFase2() {
        Jefe jefe = new Jefe(0, 0, 64, 64, 1.0, 200);
        Jugador j  = new Jugador(300, 300, 64, 64, 1.0, 100, "carlos");
        for (int i = 0; i < 181; i++) jefe.perseguir(j);
        assertTrue("jefe invoca zombies al pasar a fase 2", !jefe.getZombiesInvocados().isEmpty());
    }

    static void testLimpiarZombiesVaciaLista() {
        Jefe jefe = new Jefe(0, 0, 64, 64, 1.0, 200);
        Jugador j  = new Jugador(300, 300, 64, 64, 1.0, 100, "carlos");
        for (int i = 0; i < 181; i++) jefe.perseguir(j);
        jefe.limpiarZombiesInvocados();
        assertTrue("lista vacía tras limpiarZombiesInvocados()", jefe.getZombiesInvocados().isEmpty());
    }

    static void testAtaqueReduceVidaJugador() {
        Jefe jefe = new Jefe(0, 0, 64, 64, 1.0, 200);
        Jugador j  = new Jugador(0, 0, 64, 64, 1.0, 100, "carlos");
        int vidaAntes = j.getVidaActual();
        jefe.atacar(j);
        assertTrue("vida del jugador baja tras el ataque del jefe", j.getVidaActual() < vidaAntes);
    }

    static void testSinInvulnerabilidad() {
        Jefe jefe = new Jefe(0, 0, 64, 64, 1.0, 200);
        jefe.recibirDano(10);
        jefe.recibirDano(10);
        assertEquals("jefe pierde 20 de vida sin invulnerabilidad", 180, jefe.getVidaActual());
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== JefeTest ===");
        testNoEstaMuertoAlCrearse();
        testListaZombiesVaciaAlInicio();
        testFase1PersigueAlJugador();
        testCambioAFase2Al180Frames();
        testInvocaZombiesAlEntrarEnFase2();
        testLimpiarZombiesVaciaLista();
        testAtaqueReduceVidaJugador();
        testSinInvulnerabilidad();
        System.out.println("Resultado: " + passed + " ok, " + failed + " fallidos\n");
    }
}