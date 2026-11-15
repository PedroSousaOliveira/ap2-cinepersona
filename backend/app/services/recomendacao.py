from sqlalchemy.orm import Session

from ..models import Lente
from ..repositories import filme as filme_repository
from ..repositories import usuario as usuario_repository
from ..schemas import (
    FilmeRecomendadoSaida,
    FilmeSaida,
    RecomendarEntrada,
    RecomendacaoSaida,
)
from .lentes import DESCRICOES_LENTES, NOMES_LENTES


PONTOS_MOMENTO = 40
PONTOS_CLIMA = 30
PONTOS_PRIORIDADE = 15
PONTUACAO_MAXIMA = 85
PERCENTUAL_TOTAL = 100
AJUSTE_ARREDONDAMENTO = 0.5
MATCH_BASE = 75
MATCH_VARIACAO = 24
BONUS_LADO_B = 5
MATCH_MINIMO = 75
MATCH_MAXIMO = 99


def calcular_pontuacao(
    momento: Lente, clima: Lente, prioridades: list[Lente]
) -> dict[Lente, int]:
    pontuacoes = {lente: 0 for lente in Lente}
    pontuacoes[momento] += PONTOS_MOMENTO
    pontuacoes[clima] += PONTOS_CLIMA
    for lente in prioridades:
        pontuacoes[lente] += PONTOS_PRIORIDADE
    return pontuacoes


def escolher_lente(pontuacoes: dict[Lente, int], momento: Lente) -> Lente:
    maior_pontuacao = max(pontuacoes.values())
    if pontuacoes[momento] == maior_pontuacao:
        return momento
    return max(pontuacoes, key=pontuacoes.get)


def calcular_reconhecimento(pontuacao: int) -> int:
    percentual = PERCENTUAL_TOTAL * pontuacao / PONTUACAO_MAXIMA
    return int(percentual + AJUSTE_ARREDONDAMENTO)


def calcular_match(pontuacao: int, is_cult: bool, modo_lado_b: bool) -> int:
    variacao = MATCH_VARIACAO * pontuacao / PONTUACAO_MAXIMA
    match = MATCH_BASE + int(variacao + AJUSTE_ARREDONDAMENTO)
    if is_cult == modo_lado_b:
        match += BONUS_LADO_B
    return min(MATCH_MAXIMO, max(MATCH_MINIMO, match))


def recomendar(db: Session, req: RecomendarEntrada) -> RecomendacaoSaida:
    usuario = usuario_repository.obter_por_id(db, req.usuario_id)
    if usuario is None:
        raise LookupError("Usuário não encontrado.")
    pontuacoes = calcular_pontuacao(req.momento, req.clima, req.prioridades)
    lente = escolher_lente(pontuacoes, req.momento)
    usuario_repository.atualizar_lente_atual(db, usuario, lente)
    filmes = _filmes_ordenados(db, lente, pontuacoes[lente], req.modo_lado_b)
    return RecomendacaoSaida(
        lente=lente,
        nome_lente=NOMES_LENTES[lente],
        descricao_lente=DESCRICOES_LENTES[lente],
        reconhecimento_percentual=calcular_reconhecimento(pontuacoes[lente]),
        filmes=filmes,
    )


def _filmes_ordenados(
    db: Session, lente: Lente, pontuacao: int, modo_lado_b: bool
) -> list[FilmeRecomendadoSaida]:
    recomendacoes = []
    for filme in filme_repository.listar(db):
        if filme.lente_alvo != lente:
            continue
        match = calcular_match(pontuacao, filme.is_cult, modo_lado_b)
        dados = FilmeSaida.model_validate(filme).model_dump()
        recomendacoes.append(FilmeRecomendadoSaida(**dados, match_percentual=match))
    return sorted(recomendacoes, key=lambda filme: (-filme.match_percentual, filme.titulo))
