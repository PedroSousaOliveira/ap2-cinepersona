from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..database import get_db
from ..schemas import FilmeSaida
from ..services import filmes as filmes_service


router = APIRouter(prefix="/filmes", tags=["Filmes"])


@router.get(
    "",
    response_model=list[FilmeSaida],
    summary="Listar e buscar filmes",
)
def listar_filmes(
    q: str | None = None,
    db: Session = Depends(get_db),
) -> list[FilmeSaida]:
    return filmes_service.listar(db, q)
