package com.example.p2.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidadoresTest {
    @Test
    public void validaNomeObrigatorioAteCemCaracteres() {
        assertTrue(Validadores.nomeValido("Ana"));
        assertTrue(Validadores.nomeValido("a".repeat(100)));
        assertFalse(Validadores.nomeValido("  "));
        assertFalse(Validadores.nomeValido(null));
        assertFalse(Validadores.nomeValido("a".repeat(101)));
    }

    @Test
    public void validaFormatoDeEmail() {
        assertTrue(Validadores.emailValido("ana@example.com"));
        assertFalse(Validadores.emailValido("ana.example.com"));
        assertFalse(Validadores.emailValido("ana @example.com"));
        assertFalse(Validadores.emailValido(null));
    }

    @Test
    public void validaNotaEntreUmECinco() {
        assertTrue(Validadores.notaValida(1));
        assertTrue(Validadores.notaValida(5));
        assertFalse(Validadores.notaValida(0));
        assertFalse(Validadores.notaValida(6));
    }
}
