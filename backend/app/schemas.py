import re
from typing import Annotated

from pydantic import BaseModel, ConfigDict, Field, field_validator

from .models import Lente


class SchemaBase(BaseModel):
    model_config = ConfigDict(from_attributes=True)


class UsuarioCriar(SchemaBase):
    nome: Annotated[str, Field(min_length=1, max_length=100)]
    email: Annotated[str, Field(min_length=5, max_length=254)]

    @field_validator("nome")
    @classmethod
    def validar_nome(cls, valor: str) -> str:
        nome = valor.strip()
        if not nome:
            raise ValueError("O nome não pode ficar vazio.")
        return nome

    @field_validator("email")
    @classmethod
    def validar_email(cls, valor: str) -> str:
        email = valor.strip().lower()
        if not re.fullmatch(r"[^@\s]+@[^@\s]+\.[^@\s]+", email):
            raise ValueError("Informe um e-mail válido.")
        return email


class UsuarioSaida(SchemaBase):
    id: int
    nome: str
    email: str
    lente_atual: Lente | None


class FilmeSaida(SchemaBase):
    id: int
    titulo: str
    ano: int
    diretor: str
    genero: str
    lente_alvo: Lente
    is_cult: bool
    motivo_reconhecimento: str
    sinopse: str


class RecomendarEntrada(SchemaBase):
    usuario_id: int
    momento: Lente
    clima: Lente
    prioridades: Annotated[list[Lente], Field(max_length=4)] = Field(default_factory=list)
    modo_lado_b: bool

    @field_validator("prioridades")
    @classmethod
    def validar_prioridades(cls, valores: list[Lente]) -> list[Lente]:
        if len(valores) != len(set(valores)):
            raise ValueError("Cada lente pode ser selecionada uma única vez.")
        return valores


class FilmeRecomendadoSaida(FilmeSaida):
    match_percentual: int = Field(ge=75, le=99)


class RecomendacaoSaida(SchemaBase):
    lente: Lente
    nome_lente: str
    descricao_lente: str
    reconhecimento_percentual: int = Field(ge=0, le=100)
    filmes: list[FilmeRecomendadoSaida]


class AvaliacaoCriar(SchemaBase):
    usuario_id: int
    filme_id: int
    nota: int = Field(ge=1, le=5)
    comentario: str | None = None


class AvaliacaoAtualizar(SchemaBase):
    nota: int = Field(ge=1, le=5)
    comentario: str | None = None


class AvaliacaoSaida(SchemaBase):
    id: int
    usuario_id: int
    filme_id: int
    nota: int
    comentario: str | None


class AvaliacaoHistoricoSaida(AvaliacaoSaida):
    filme: FilmeSaida
