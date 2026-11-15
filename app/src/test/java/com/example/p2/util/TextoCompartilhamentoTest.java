package com.example.p2.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextoCompartilhamentoTest {
    @Test
    public void geraTextoNoFormatoDoEscopo() {
        assertEquals("Meu match no CinePersona: Parasita (89%)",
                TextoCompartilhamento.gerar("Parasita", 89));
    }

    @Test
    public void preservaAcentosEDoisPontosNoTitulo() {
        assertEquals("Meu match no CinePersona: Rocky: Um Lutador (75%)",
                TextoCompartilhamento.gerar("Rocky: Um Lutador", 75));
        assertEquals("Meu match no CinePersona: Amnésia (99%)",
                TextoCompartilhamento.gerar("Amnésia", 99));
    }
}
