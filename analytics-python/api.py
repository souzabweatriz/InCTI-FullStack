"""API HTTP usada pelo backend para executar o ETL."""

from __future__ import annotations

import os

import pandas as pd
from fastapi import FastAPI, Header, HTTPException
from pydantic import BaseModel, Field

from analise import calcular
from banco import criar_engine
from limpeza import tratar
from main import processar_lote

app = FastAPI(title="InCTI Analytics", version="1.0.0")


class EtlRequest(BaseModel):
    loteId: int = Field(gt=0)
    recebidas: int = Field(ge=0)
    linhas: list[dict] = Field(default_factory=list)


def _validar_chave(chave: str | None) -> None:
    esperada = os.getenv("ETL_API_KEY")
    if esperada and chave != esperada:
        raise HTTPException(status_code=401, detail="Chave ETL inválida.")


@app.get("/saude")
def saude() -> dict[str, str]:
    return {"status": "ok"}


@app.post("/etl")
def executar_etl(
    pedido: EtlRequest,
    x_etl_key: str | None = Header(default=None, alias="X-ETL-Key"),
) -> dict[str, object]:
    _validar_chave(x_etl_key)
    if pedido.recebidas != len(pedido.linhas):
        raise HTTPException(
            status_code=422,
            detail="recebidas deve corresponder à quantidade de linhas.",
        )

    sucesso = processar_lote(
        criar_engine(),
        pedido.loteId,
        pedido.linhas,
        pedido.recebidas,
    )
    if not sucesso:
        raise HTTPException(
            status_code=409,
            detail="O lote não pôde ser processado.",
        )
    return {"status": "PROCESSADO", "loteId": pedido.loteId}


@app.post("/testar")
def testar(
    linhas: list[dict],
    x_etl_key: str | None = Header(default=None, alias="X-ETL-Key"),
) -> dict:
    _validar_chave(x_etl_key)
    resultado = tratar(pd.DataFrame(linhas))
    if resultado.tratado.empty:
        raise HTTPException(
            status_code=422,
            detail="A lista não contém clientes válidos.",
        )
    return calcular(resultado, len(linhas))
