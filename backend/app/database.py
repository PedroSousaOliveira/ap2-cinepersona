import os
from pathlib import Path
from typing import Generator

from dotenv import load_dotenv
from sqlalchemy import create_engine, event
from sqlalchemy.engine import Engine
from sqlalchemy.orm import Session, sessionmaker

from .models import Base


load_dotenv(Path(__file__).resolve().parents[1] / ".env")
DEFAULT_DATABASE_PATH = Path(__file__).resolve().parents[1] / "cinepersona.db"
DATABASE_URL = os.getenv("DATABASE_URL", f"sqlite:///{DEFAULT_DATABASE_PATH.as_posix()}")
ENGINE_OPTIONS = {"connect_args": {"check_same_thread": False}} if DATABASE_URL.startswith("sqlite") else {}
engine = create_engine(DATABASE_URL, **ENGINE_OPTIONS)
SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False)


def configurar_chaves_estrangeiras(db_engine: Engine) -> None:
    @event.listens_for(db_engine, "connect")
    def ativar_chaves_estrangeiras(connection, _record) -> None:
        cursor = connection.cursor()
        cursor.execute("PRAGMA foreign_keys=ON")
        cursor.close()


if DATABASE_URL.startswith("sqlite"):
    configurar_chaves_estrangeiras(engine)


def get_db() -> Generator[Session, None, None]:
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
