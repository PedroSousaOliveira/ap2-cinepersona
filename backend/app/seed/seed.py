import json
from pathlib import Path
from typing import Any

from sqlalchemy import func, select
from sqlalchemy.orm import Session

from ..database import SessionLocal, engine
from ..models import Base, Filme, Lente


def carregar_filmes() -> list[dict[str, Any]]:
    caminho = Path(__file__).with_name("filmes.json")
    with caminho.open(encoding="utf-8") as arquivo:
        return json.load(arquivo)


def contar_filmes(db: Session, lente: Lente | None = None) -> int:
    consulta = select(func.count(Filme.id))
    if lente is not None:
        consulta = consulta.where(Filme.lente_alvo == lente)
    return db.scalar(consulta) or 0


def seed_filmes(db: Session) -> int:
    total_atual = contar_filmes(db)
    if total_atual:
        return total_atual
    db.add_all(Filme(**filme) for filme in carregar_filmes())
    db.commit()
    return contar_filmes(db)


def imprimir_contagem(db: Session) -> None:
    for lente in Lente:
        print(f"{lente.value}: {contar_filmes(db, lente)}")
    print(f"Total: {contar_filmes(db)}")


def main() -> None:
    Base.metadata.create_all(bind=engine)
    with SessionLocal() as db:
        seed_filmes(db)
        imprimir_contagem(db)


if __name__ == "__main__":
    main()
