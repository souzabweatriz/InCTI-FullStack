"""Limpa e padroniza os dados recebidos do backend."""

from __future__ import annotations

import re
import unicodedata
from dataclasses import dataclass, field

import pandas as pd


@dataclass
class ResultadoTransformacao:
    tratado: pd.DataFrame
    rejeitadas: pd.DataFrame
    padronizacoes: dict = field(default_factory=dict)
    segmentos_originais: list = field(default_factory=list)
    avisos: list = field(default_factory=list)


def _sem_acento(texto: str) -> str:
    return "".join(
        c for c in unicodedata.normalize("NFKD", texto)
        if not unicodedata.combining(c)
    )


def _texto(valor) -> str:
    if pd.isna(valor):
        return ""
    return re.sub(r"\s+", " ", str(valor)).strip()


def _numero(valor):
    if isinstance(valor, str):
        texto = _texto(valor).replace("R$", "").replace(" ", "")
        if "," in texto and "." in texto:
            texto = texto.replace(".", "").replace(",", ".")
        elif "," in texto:
            texto = texto.replace(",", ".")
        valor = texto
    return pd.to_numeric(valor, errors="coerce")


def _snake_case(coluna) -> str:
    """'codigoCliente', 'Codigo_Cliente' e 'Código Cliente' -> 'codigo_cliente'."""
    texto = _sem_acento(str(coluna).strip())
    texto = re.sub(r"(?<=[a-z0-9])(?=[A-Z])", "_", texto)
    texto = re.sub(r"[^A-Za-z0-9]+", "_", texto)
    return texto.strip("_").lower()


def _colunas_para_snake_case(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()
    df.columns = [_snake_case(coluna) for coluna in df.columns]
    return df


def _nome_servico(texto: str) -> str:
    """Capitaliza o serviço sem estragar siglas (BI, ERP, CRM)."""
    return " ".join(
        p if p.isupper() and len(p) <= 4 else p.capitalize()
        for p in texto.split()
    )


def tratar(bruto: pd.DataFrame) -> ResultadoTransformacao:
    """Limpa os dados e separa linhas válidas e inválidas."""

    df = _colunas_para_snake_case(bruto)

    aliases = {
        "codigo_cliente": "codigo_cliente",
        "nome_cliente": "nome_cliente",
        "consultor": "consultor",
        "segmento": "segmento",
        "nivel_cliente": "nivel_cliente",
        "faturamento_anual": "faturamento_anual",
        "servicos_contratados": "servicos_contratados",
        "data_contratacao": "data_contratacao",
        "cidade": "cidade",
        "uf": "uf",
    }

    df = df.rename(columns=aliases)

    colunas_esperadas = [
        "codigo_cliente",
        "nome_cliente",
        "consultor",
        "segmento",
        "nivel_cliente",
        "faturamento_anual",
        "servicos_contratados",
        "data_contratacao",
        "cidade",
        "uf",
    ]

    for coluna in colunas_esperadas:
        if coluna not in df.columns:
            df[coluna] = None

    rejeitadas = []
    validas = []
    padronizacoes = {
        "segmento": 0,
        "consultor": 0,
        "nivel_cliente": 0,
    }
    segmentos_originais = []

    for _, linha in df.iterrows():
        item = linha.to_dict()

        for coluna in df.columns:
            if isinstance(item.get(coluna), str):
                item[coluna] = _texto(item[coluna])

        codigo = _texto(item.get("codigo_cliente"))
        nome = _texto(item.get("nome_cliente"))
        faturamento = _numero(item.get("faturamento_anual"))

        if not codigo or not nome or pd.isna(faturamento):
            item["motivo_rejeicao"] = (
                "Código, nome ou faturamento inválido."
            )
            rejeitadas.append(item)
            continue

        item["codigo_cliente"] = codigo
        item["nome_cliente"] = nome.title()
        item["faturamento_anual"] = float(faturamento)

        consultor_original = _texto(item.get("consultor"))
        if consultor_original:
            item["consultor"] = consultor_original.title()
            padronizacoes["consultor"] += (
                item["consultor"] != consultor_original
            )

        # _texto trata NaN; sem isso, célula vazia vira o texto "Nan".
        item["cidade"] = _texto(item.get("cidade")).title() or None
        item["uf"] = _texto(item.get("uf")).upper() or None

        segmento = _texto(item.get("segmento"))
        if segmento:
            segmentos_originais.append(segmento)
            chave = _sem_acento(segmento).lower().replace(".", "")
            segmento_padronizado = segmento.title()
            if chave in ("ind", "industria"):
                segmento_padronizado = "Indústria"
            elif chave in ("comercio", "comercial"):
                segmento_padronizado = "Comércio"
            elif chave in ("servicos", "servico"):
                segmento_padronizado = "Serviços"
            elif chave in ("saude",):
                segmento_padronizado = "Saúde"
            elif chave in ("educacao",):
                segmento_padronizado = "Educação"
            elif chave in ("tecnologia",):
                segmento_padronizado = "Tecnologia"
            item["segmento"] = segmento_padronizado
            padronizacoes["segmento"] += segmento_padronizado != segmento

        nivel = _texto(item.get("nivel_cliente"))
        if nivel:
            item["nivel_cliente"] = nivel.upper()
            padronizacoes["nivel_cliente"] += item["nivel_cliente"] != nivel

        data = item.get("data_contratacao")
        if data:
            if isinstance(data, (int, float)) and 1 <= data <= 60000:
                data_convertida = pd.Timestamp("1899-12-30") + pd.to_timedelta(
                    data, unit="D"
                )
            else:
                data_convertida = pd.to_datetime(data, errors="coerce")
            item["data_contratacao"] = (
                data_convertida.date().isoformat()
                if not pd.isna(data_convertida)
                else None
            )

        servicos = _texto(item.get("servicos_contratados"))
        item["servicos"] = [
            _nome_servico(s.strip())
            for s in re.split(r"[;,|]", servicos) if s.strip()
        ]
        item["qtd_servicos"] = len(item["servicos"])
        item["mes"] = (
            item["data_contratacao"][:7]
            if item.get("data_contratacao")
            else None
        )

        validas.append(item)

    tratado = pd.DataFrame(validas)
    rejeitadas_df = pd.DataFrame(rejeitadas)

    return ResultadoTransformacao(
        tratado=tratado.reset_index(drop=True),
        rejeitadas=rejeitadas_df.reset_index(drop=True),
        padronizacoes=padronizacoes,
        segmentos_originais=segmentos_originais,
        avisos=[],
    )
