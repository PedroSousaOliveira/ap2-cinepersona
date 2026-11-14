from fastapi import APIRouter, Depends, Response, status
from sqlalchemy.orm import Session

from ..database import get_db
from ..schemas import AvaliacaoHistoricoSaida, UsuarioCriar, UsuarioSaida
from ..services import avaliacoes as avaliacoes_service
from ..services import usuarios as usuarios_service


router = APIRouter(prefix="/usuarios", tags=["Usuários"])


@router.post(
    "",
    response_model=UsuarioSaida,
    status_code=status.HTTP_201_CREATED,
    responses={200: {"model": UsuarioSaida, "description": "Usuário já cadastrado."}},
    summary="Cadastrar ou recuperar usuário",
)
def criar_usuario(
    req: UsuarioCriar,
    response: Response,
    db: Session = Depends(get_db),
) -> UsuarioSaida:
    usuario, criado = usuarios_service.obter_ou_criar(db, req)
    if not criado:
        response.status_code = status.HTTP_200_OK
    return usuario


@router.get(
    "/{usuario_id}/avaliacoes",
    response_model=list[AvaliacaoHistoricoSaida],
    responses={404: {"description": "Usuário não encontrado."}},
    summary="Listar avaliações do usuário",
)
def listar_avaliacoes(
    usuario_id: int,
    db: Session = Depends(get_db),
) -> list[AvaliacaoHistoricoSaida]:
    return avaliacoes_service.listar_por_usuario(db, usuario_id)
