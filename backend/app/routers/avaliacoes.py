from fastapi import APIRouter, Depends, Response, status
from sqlalchemy.orm import Session

from ..database import get_db
from ..schemas import AvaliacaoAtualizar, AvaliacaoCriar, AvaliacaoSaida
from ..services import avaliacoes as avaliacoes_service


router = APIRouter(prefix="/avaliacoes", tags=["Avaliações"])


@router.post(
    "",
    response_model=AvaliacaoSaida,
    status_code=status.HTTP_201_CREATED,
    responses={
        404: {"description": "Usuário ou filme não encontrado."},
        409: {"description": "Usuário já avaliou este filme."},
    },
    summary="Criar avaliação de filme",
)
def criar_avaliacao(
    req: AvaliacaoCriar,
    db: Session = Depends(get_db),
) -> AvaliacaoSaida:
    return avaliacoes_service.criar(db, req)


@router.put(
    "/{avaliacao_id}",
    response_model=AvaliacaoSaida,
    responses={404: {"description": "Avaliação não encontrada."}},
    summary="Atualizar avaliação",
)
def atualizar_avaliacao(
    avaliacao_id: int,
    req: AvaliacaoAtualizar,
    db: Session = Depends(get_db),
) -> AvaliacaoSaida:
    return avaliacoes_service.atualizar(db, avaliacao_id, req)


@router.delete(
    "/{avaliacao_id}",
    status_code=status.HTTP_204_NO_CONTENT,
    response_model=None,
    responses={404: {"description": "Avaliação não encontrada."}},
    summary="Remover avaliação",
)
def excluir_avaliacao(
    avaliacao_id: int,
    db: Session = Depends(get_db),
) -> Response:
    avaliacoes_service.excluir(db, avaliacao_id)
    return Response(status_code=status.HTTP_204_NO_CONTENT)
