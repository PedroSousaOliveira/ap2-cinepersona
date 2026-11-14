from sqlalchemy import select
from sqlalchemy.orm import Session, joinedload

from ..models import Avaliacao


def obter_por_id(db: Session, avaliacao_id: int) -> Avaliacao | None:
    return db.get(Avaliacao, avaliacao_id)


def obter_por_usuario_filme(
    db: Session, usuario_id: int, filme_id: int
) -> Avaliacao | None:
    consulta = select(Avaliacao).where(
        Avaliacao.usuario_id == usuario_id,
        Avaliacao.filme_id == filme_id,
    )
    return db.scalar(consulta)


def listar_por_usuario(db: Session, usuario_id: int) -> list[Avaliacao]:
    consulta = (
        select(Avaliacao)
        .options(joinedload(Avaliacao.filme))
        .where(Avaliacao.usuario_id == usuario_id)
        .order_by(Avaliacao.id)
    )
    return list(db.scalars(consulta).all())


def criar(db: Session, usuario_id: int, filme_id: int, nota: int, comentario: str | None) -> Avaliacao:
    avaliacao = Avaliacao(
        usuario_id=usuario_id, filme_id=filme_id, nota=nota, comentario=comentario
    )
    db.add(avaliacao)
    db.commit()
    db.refresh(avaliacao)
    return avaliacao


def atualizar(db: Session, avaliacao: Avaliacao, nota: int, comentario: str | None) -> Avaliacao:
    avaliacao.nota = nota
    avaliacao.comentario = comentario
    db.commit()
    db.refresh(avaliacao)
    return avaliacao


def excluir(db: Session, avaliacao: Avaliacao) -> None:
    db.delete(avaliacao)
    db.commit()
