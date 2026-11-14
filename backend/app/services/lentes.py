from ..models import Lente


NOMES_LENTES = {
    Lente.ROTEIRO_DE_VIRADA: "Roteiro de Virada",
    Lente.PLANO_SEQUENCIA_AFETIVO: "Plano-Sequência Afetivo",
    Lente.CORTE_SECO_ADRENALINA: "Corte Seco & Adrenalina",
    Lente.FORA_DE_QUADRO: "Fora de Quadro",
}

DESCRICOES_LENTES = {
    Lente.ROTEIRO_DE_VIRADA: (
        "Para quem está iniciando um novo ciclo, enfrentando metas difíceis ou precisando de energia e perspectiva. "
        "Reúne histórias de superação, propósito e recomeço."
    ),
    Lente.PLANO_SEQUENCIA_AFETIVO: (
        "Para quem está reflexivo ou nostálgico, valoriza relações, família e amizade, ou precisa de acolhimento. "
        "Reúne histórias de memória, laços e emoção."
    ),
    Lente.CORTE_SECO_ADRENALINA: (
        "Para quem está com a mente inquieta ou analítica, lida com pressão ou busca histórias intensas. "
        "Reúne tensão, mistério, estratégia e reviravoltas."
    ),
    Lente.FORA_DE_QUADRO: (
        "Para quem quer fugir da rotina, questionar padrões ou explorar universos criativos. "
        "Reúne histórias de imaginário, crítica e descoberta."
    ),
}
