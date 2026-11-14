from sqlalchemy import func, select
from sqlalchemy.orm import Session

from ..models import Usuario


def obter_por_id(db: Session, usuario_id: int) -> Usuario | None:
    return db.get(Usuario, usuario_id)


def obter_por_email(db: Session, email: str) -> Usuario | None:
    consulta = select(Usuario).where(func.lower(Usuario.email) == email.lower())
    return db.scalar(consulta)


def criar(db: Session, nome: str, email: str) -> Usuario:
    usuario = Usuario(nome=nome, email=email)
    db.add(usuario)
    db.commit()
    db.refresh(usuario)
    return usuario
