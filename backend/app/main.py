from collections.abc import AsyncIterator, Callable
from contextlib import asynccontextmanager

from fastapi import FastAPI
from sqlalchemy.engine import Engine
from sqlalchemy.orm import Session

from .database import SessionLocal, configurar_chaves_estrangeiras, engine
from .models import Base
from .routers import avaliacoes, filmes, recomendacoes, usuarios
from .seed.seed import seed_filmes


def criar_app(
    database_engine: Engine = engine,
    session_factory: Callable[[], Session] = SessionLocal,
) -> FastAPI:
    if database_engine is not engine and database_engine.dialect.name == "sqlite":
        configurar_chaves_estrangeiras(database_engine)

    @asynccontextmanager
    async def lifespan(_app: FastAPI) -> AsyncIterator[None]:
        Base.metadata.create_all(bind=database_engine)
        with session_factory() as db:
            seed_filmes(db)
        yield

    app = FastAPI(
        title="CinePersona API",
        description="API de recomendações de filmes e diário pessoal de avaliações.",
        version="1.0.0",
        lifespan=lifespan,
    )
    app.include_router(usuarios.router)
    app.include_router(filmes.router)
    app.include_router(recomendacoes.router)
    app.include_router(avaliacoes.router)
    return app


app = criar_app()
