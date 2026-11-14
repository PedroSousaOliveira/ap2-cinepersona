from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from ..database import get_db
from ..schemas import RecomendarEntrada, RecomendacaoSaida
from ..services import recomendacao as recomendacao_service


router = APIRouter(tags=["Recomendações"])


@router.post(
    "/recomendar",
    response_model=RecomendacaoSaida,
    responses={404: {"description": "Usuário não encontrado."}},
    summary="Recomendar filmes para a sessão",
)
def recomendar(
    req: RecomendarEntrada,
    db: Session = Depends(get_db),
) -> RecomendacaoSaida:
    try:
        return recomendacao_service.recomendar(db, req)
    except LookupError as erro:
        raise HTTPException(status_code=404, detail=str(erro)) from erro
