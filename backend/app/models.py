from enum import Enum

from sqlalchemy import CheckConstraint, Enum as SqlEnum, ForeignKey, String, Text, UniqueConstraint
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship


class Lente(str, Enum):
    ROTEIRO_DE_VIRADA = "ROTEIRO_DE_VIRADA"
    PLANO_SEQUENCIA_AFETIVO = "PLANO_SEQUENCIA_AFETIVO"
    CORTE_SECO_ADRENALINA = "CORTE_SECO_ADRENALINA"
    FORA_DE_QUADRO = "FORA_DE_QUADRO"


class Base(DeclarativeBase):
    pass


def lente_column() -> SqlEnum:
    return SqlEnum(
        Lente,
        name="lente",
        values_callable=lambda enum_type: [item.value for item in enum_type],
    )


class Usuario(Base):
    __tablename__ = "usuarios"

    id: Mapped[int] = mapped_column(primary_key=True)
    nome: Mapped[str] = mapped_column(String(100), nullable=False)
    email: Mapped[str] = mapped_column(String(254, collation="NOCASE"), unique=True, nullable=False)
    lente_atual: Mapped[Lente | None] = mapped_column(lente_column(), nullable=True)
    avaliacoes: Mapped[list["Avaliacao"]] = relationship(
        back_populates="usuario", cascade="all, delete-orphan"
    )


class Filme(Base):
    __tablename__ = "filmes"

    id: Mapped[int] = mapped_column(primary_key=True)
    titulo: Mapped[str] = mapped_column(String(200), nullable=False, unique=True)
    ano: Mapped[int] = mapped_column(nullable=False)
    diretor: Mapped[str] = mapped_column(String(200), nullable=False)
    genero: Mapped[str] = mapped_column(String(100), nullable=False)
    lente_alvo: Mapped[Lente] = mapped_column(lente_column(), nullable=False)
    is_cult: Mapped[bool] = mapped_column(nullable=False)
    motivo_reconhecimento: Mapped[str] = mapped_column(Text, nullable=False)
    sinopse: Mapped[str] = mapped_column(Text, nullable=False)
    avaliacoes: Mapped[list["Avaliacao"]] = relationship(back_populates="filme")


class Avaliacao(Base):
    __tablename__ = "avaliacoes"
    __table_args__ = (
        UniqueConstraint("usuario_id", "filme_id", name="uq_avaliacao_usuario_filme"),
        CheckConstraint("nota >= 1 AND nota <= 5", name="ck_avaliacao_nota"),
    )

    id: Mapped[int] = mapped_column(primary_key=True)
    usuario_id: Mapped[int] = mapped_column(ForeignKey("usuarios.id"), nullable=False)
    filme_id: Mapped[int] = mapped_column(ForeignKey("filmes.id"), nullable=False)
    nota: Mapped[int] = mapped_column(nullable=False)
    comentario: Mapped[str | None] = mapped_column(Text, nullable=True)
    usuario: Mapped[Usuario] = relationship(back_populates="avaliacoes")
    filme: Mapped[Filme] = relationship(back_populates="avaliacoes")
