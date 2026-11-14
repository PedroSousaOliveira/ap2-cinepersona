from sqlalchemy.orm import Session

from ..models import Filme
from ..repositories import filme as filme_repository


def listar(db: Session, q: str | None = None) -> list[Filme]:
    termo = q.strip() if q and q.strip() else None
    return filme_repository.listar(db, termo)
