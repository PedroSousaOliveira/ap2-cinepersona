from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from ..models import Usuario
from ..repositories import usuario as usuario_repository
from ..schemas import UsuarioCriar


def obter_ou_criar(db: Session, req: UsuarioCriar) -> tuple[Usuario, bool]:
    usuario = usuario_repository.obter_por_email(db, req.email)
    if usuario is not None:
        return usuario, False
    try:
        return usuario_repository.criar(db, req.nome, req.email), True
    except IntegrityError:
        db.rollback()
        usuario = usuario_repository.obter_por_email(db, req.email)
        if usuario is None:
            raise
        return usuario, False
