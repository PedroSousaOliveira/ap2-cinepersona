from app.models import Lente
from app.services.recomendacao import (
    calcular_match,
    calcular_pontuacao,
    escolher_lente,
)


def test_calcular_pontuacao_soma_os_pesos():
    pontuacoes = calcular_pontuacao(
        Lente.ROTEIRO_DE_VIRADA,
        Lente.PLANO_SEQUENCIA_AFETIVO,
        [Lente.PLANO_SEQUENCIA_AFETIVO, Lente.FORA_DE_QUADRO],
    )
    assert pontuacoes == {
        Lente.ROTEIRO_DE_VIRADA: 40,
        Lente.PLANO_SEQUENCIA_AFETIVO: 45,
        Lente.CORTE_SECO_ADRENALINA: 0,
        Lente.FORA_DE_QUADRO: 15,
    }


def test_empate_favorece_lente_do_momento():
    momento = Lente.PLANO_SEQUENCIA_AFETIVO
    pontuacoes = {
        Lente.ROTEIRO_DE_VIRADA: 40,
        momento: 40,
        Lente.CORTE_SECO_ADRENALINA: 0,
        Lente.FORA_DE_QUADRO: 0,
    }
    assert escolher_lente(pontuacoes, momento) == momento


def test_escolhe_lente_com_maior_pontuacao():
    pontuacoes = {
        Lente.ROTEIRO_DE_VIRADA: 40,
        Lente.PLANO_SEQUENCIA_AFETIVO: 45,
        Lente.CORTE_SECO_ADRENALINA: 0,
        Lente.FORA_DE_QUADRO: 0,
    }
    assert escolher_lente(pontuacoes, Lente.ROTEIRO_DE_VIRADA) == Lente.PLANO_SEQUENCIA_AFETIVO


def test_match_fica_entre_75_e_99():
    minimo = calcular_match(0, False, True)
    maximo = calcular_match(85, True, True)
    assert minimo == 75
    assert maximo == 99


def test_lado_b_da_bonus_a_filmes_cult():
    com_lado_b = calcular_match(0, True, True)
    sem_lado_b = calcular_match(0, True, False)
    assert com_lado_b - sem_lado_b == 5
