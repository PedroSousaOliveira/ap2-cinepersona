package com.example.p2.util;

import com.example.p2.domain.model.Lente;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class QuestionarioRegrasTest {
    @Test
    public void convertePosicoesDeMomentoNaOrdemDasLentes() {
        assertEquals(Lente.ROTEIRO_DE_VIRADA, QuestionarioRegras.lenteDaPosicao(0));
        assertEquals(Lente.PLANO_SEQUENCIA_AFETIVO, QuestionarioRegras.lenteDaPosicao(1));
        assertEquals(Lente.CORTE_SECO_ADRENALINA, QuestionarioRegras.lenteDaPosicao(2));
        assertEquals(Lente.FORA_DE_QUADRO, QuestionarioRegras.lenteDaPosicao(3));
    }

    @Test
    public void spinnerNaPosicaoInicialNaoRepresentaUmaLente() {
        assertThrows(IllegalArgumentException.class, () -> QuestionarioRegras.lenteDoClima(0));
        assertEquals(Lente.ROTEIRO_DE_VIRADA, QuestionarioRegras.lenteDoClima(1));
        assertEquals(Lente.FORA_DE_QUADRO, QuestionarioRegras.lenteDoClima(4));
    }

    @Test
    public void habilitaBotaoSomenteComMomentoEClimaValidos() {
        assertFalse(QuestionarioRegras.botaoHabilitado(-1, 0));
        assertFalse(QuestionarioRegras.botaoHabilitado(0, 0));
        assertFalse(QuestionarioRegras.botaoHabilitado(-1, 1));
        assertTrue(QuestionarioRegras.botaoHabilitado(3, 4));
    }
}
