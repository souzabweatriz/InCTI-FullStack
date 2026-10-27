"""Conexão e gravação de dados no PostgreSQL."""

from __future__ import annotations

import json
import logging
from datetime import datetime, timezone

import pandas as pd
from sqlalchemy import create_engine, text
from sqlalchemy.engine import Engine

from config import database_url

log = logging.getLogger("analytics")


def criar_engine() -> Engine:
    return create_engine(database_url(), pool_pre_ping=True)


def agora_utc() -> datetime:
    return datetime.now(timezone.utc).replace(tzinfo=None)


def reservar_lote(engine: Engine, lote_id: int, so_pendente: bool) -> bool:
    filtro = (
        "status = 'AGUARDANDO_ETL'"
        if so_pendente
        else "status NOT IN ('PROCESSANDO', 'REJEITADO')"
    )

    with engine.begin() as conn:
        resultado = conn.execute(
            text(
                "UPDATE upload_lote "
                "SET status = 'PROCESSANDO', mensagem = 'ETL em andamento...' "
                f"WHERE id = :id AND {filtro}"
            ),
            {"id": lote_id},
        )
        return resultado.rowcount == 1


def marcar_erro(engine: Engine, lote_id: int, mensagem: str) -> None:
    with engine.begin() as conn:
        conn.execute(
            text(
                "UPDATE upload_lote "
                "SET status = 'ERRO', mensagem = :m WHERE id = :id"
            ),
            {"id": lote_id, "m": mensagem[:480]},
        )


def gravar_resultado(
    engine: Engine,
    lote_id: int,
    tratado: pd.DataFrame,
    estatisticas: dict,
    recebidas: int,
    rejeitadas_etl: int = 0,
) -> None:
    """Grava clientes, contratos e estatísticas do lote."""

    with engine.begin() as conn:
        conn.execute(
            text("DELETE FROM estatistica_lote WHERE lote_id = :id"),
            {"id": lote_id},
        )

        for _, linha in tratado.iterrows():
            cliente = linha.to_dict()

            cliente_id = conn.execute(
                text("""
                    INSERT INTO cliente (
                        lote_id, codigo_cliente, nome_cliente, segmento,
                        nivel_cliente, faturamento_anual, cidade, uf,
                        consultor_id
                    )
                    VALUES (
                        :lote_id, :codigo_cliente, :nome_cliente, :segmento,
                        :nivel_cliente, :faturamento_anual, :cidade, :uf,
                        :consultor_id
                    )
                    RETURNING id
                """),
                {
                    "lote_id": lote_id,
                    "codigo_cliente": cliente.get("codigo_cliente"),
                    "nome_cliente": cliente.get("nome_cliente"),
                    "segmento": cliente.get("segmento"),
                    "nivel_cliente": cliente.get("nivel_cliente"),
                    "faturamento_anual": cliente.get("faturamento_anual"),
                    "cidade": cliente.get("cidade"),
                    "uf": cliente.get("uf"),
                    "consultor_id": cliente.get("consultor_id"),
                },
            ).scalar_one()

            for servico in cliente.get("servicos", []):
                conn.execute(
                    text("""
                        INSERT INTO contrato (
                            cliente_id, servico, data_contratacao, ativo
                        )
                        VALUES (
                            :cliente_id, :servico, :data_contratacao, true
                        )
                    """),
                    {
                        "cliente_id": cliente_id,
                        "servico": servico,
                        "data_contratacao": cliente.get("data_contratacao"),
                    },
                )

        conn.execute(
            text("""
                INSERT INTO estatistica_lote (lote_id, resultado, criado_em)
                VALUES (:lote_id, CAST(:resultado AS jsonb), :criado_em)
            """),
            {
                "lote_id": lote_id,
                "resultado": json.dumps(
                    estatisticas, ensure_ascii=False, default=str
                ),
                "criado_em": agora_utc(),
            },
        )

        mensagem = f"ETL concluído: {len(tratado)} clientes tratados"

        if rejeitadas_etl:
            mensagem += f", {rejeitadas_etl} linhas recusadas na limpeza"

        conn.execute(
            text("""
                UPDATE upload_lote
                SET status = 'PROCESSADO',
                    total_linhas = :recebidas,
                    linhas_validas = :validas,
                    linhas_invalidas = :invalidas,
                    mensagem = :mensagem
                WHERE id = :id
            """),
            {
                "id": lote_id,
                "recebidas": recebidas,
                "validas": len(tratado),
                "invalidas": recebidas - len(tratado),
                "mensagem": mensagem + ".",
            },
        )

    log.info("Lote %s gravado com sucesso.", lote_id)
