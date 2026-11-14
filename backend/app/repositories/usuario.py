from sqlalchemy import func, select
from sqlalchemy.orm import Session

from ..models import Lente, Usuario


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


def atualizar_lente_atual(db: Session, usuario: Usuario, lente: Lente) -> Usuario:
    usuario.lente_atual = lente
    db.commit()
    db.refresh(usuario)
    return usuario
