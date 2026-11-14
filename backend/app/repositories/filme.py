from sqlalchemy import select
from sqlalchemy.orm import Session

from ..models import Filme


def obter_por_id(db: Session, filme_id: int) -> Filme | None:
    return db.get(Filme, filme_id)


def listar(db: Session, q: str | None = None) -> list[Filme]:
    consulta = select(Filme)
    termo = q.strip() if q else ""
    if termo:
        padrao = f"%{termo}%"
        consulta = consulta.where(Filme.titulo.ilike(padrao) | Filme.diretor.ilike(padrao))
    return list(db.scalars(consulta.order_by(Filme.titulo)).all())
