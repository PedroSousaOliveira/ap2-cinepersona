from sqlalchemy import select
from sqlalchemy.orm import Session

from ..models import Filme


def obter_por_id(db: Session, filme_id: int) -> Filme | None:
    return db.get(Filme, filme_id)


def listar(db: Session, q: str | None = None) -> list[Filme]:
    consulta = select(Filme).order_by(Filme.titulo)
    filmes = list(db.scalars(consulta).all())
    termo = q.strip().casefold() if q else ""
    if not termo:
        return filmes
    return [
        filme
        for filme in filmes
        if termo in filme.titulo.casefold() or termo in filme.diretor.casefold()
    ]
