from fastapi import HTTPException
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from ..models import Avaliacao
from ..repositories import avaliacao as avaliacao_repository
from ..repositories import filme as filme_repository
from ..repositories import usuario as usuario_repository
from ..schemas import AvaliacaoAtualizar, AvaliacaoCriar


def criar(db: Session, req: AvaliacaoCriar) -> Avaliacao:
    if usuario_repository.obter_por_id(db, req.usuario_id) is None:
        raise HTTPException(status_code=404, detail="Usuário não encontrado.")
    if filme_repository.obter_por_id(db, req.filme_id) is None:
        raise HTTPException(status_code=404, detail="Filme não encontrado.")
    try:
        return avaliacao_repository.criar(
            db, req.usuario_id, req.filme_id, req.nota, req.comentario
        )
    except IntegrityError:
        db.rollback()
        avaliacao = avaliacao_repository.obter_por_usuario_filme(
            db, req.usuario_id, req.filme_id
        )
        if avaliacao is None:
            raise
        raise HTTPException(status_code=409, detail="Avaliação já existe.") from None


def listar_por_usuario(db: Session, usuario_id: int) -> list[Avaliacao]:
    if usuario_repository.obter_por_id(db, usuario_id) is None:
        raise HTTPException(status_code=404, detail="Usuário não encontrado.")
    return avaliacao_repository.listar_por_usuario(db, usuario_id)


def atualizar(db: Session, avaliacao_id: int, req: AvaliacaoAtualizar) -> Avaliacao:
    avaliacao = avaliacao_repository.obter_por_id(db, avaliacao_id)
    if avaliacao is None:
        raise HTTPException(status_code=404, detail="Avaliação não encontrada.")
    return avaliacao_repository.atualizar(db, avaliacao, req.nota, req.comentario)


def excluir(db: Session, avaliacao_id: int) -> None:
    avaliacao = avaliacao_repository.obter_por_id(db, avaliacao_id)
    if avaliacao is None:
        raise HTTPException(status_code=404, detail="Avaliação não encontrada.")
    avaliacao_repository.excluir(db, avaliacao)
