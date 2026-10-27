"""Orquestra o processamento de um lote recebido do backend."""

from __future__ import annotations

import logging

import pandas as pd
from sqlalchemy.engine import Engine

from analise import calcular
from banco import gravar_resultado, marcar_erro, reservar_lote
from limpeza import tratar

log = logging.getLogger("analytics")


def processar_lote(
    engine: Engine,
    lote_id: int,
    linhas: list[dict],
    recebidas: int,
) -> bool:
    """Reserva, transforma, analisa e grava um lote."""
    if not reservar_lote(engine, lote_id, so_pendente=True):
        return False

    try:
        resultado = tratar(pd.DataFrame(linhas))
        if resultado.tratado.empty:
            raise ValueError("O lote não contém linhas válidas.")

        estatisticas = calcular(resultado, recebidas)
        gravar_resultado(
            engine,
            lote_id,
            resultado.tratado,
            estatisticas,
            recebidas,
            len(resultado.rejeitadas),
        )
        return True
    except Exception as erro:
        log.exception("Falha ao processar o lote %s.", lote_id)
        marcar_erro(engine, lote_id, str(erro))
        return False
