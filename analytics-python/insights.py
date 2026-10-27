"""Insights: transforma os números da análise em frases em português."""

from __future__ import annotations


def brl(valor) -> str:
    if valor is None:
        return "—"
    s = f"{valor:,.2f}".replace(",", "X").replace(".", ",").replace("X", ".")
    return f"R$ {s}"


def _virg(x) -> str:
    return str(x).replace(".", ",")


def gerar_insights(
    resumo,
    por_segmento,
    por_nivel,
    por_consultor,
    por_servico,
    por_mes,
    correlacao,
) -> list[str]:
    ins: list[str] = []

    if por_segmento:
        top = por_segmento[0]
        ins.append(
            f"O segmento {top['segmento']} concentra a maior carteira: "
            f"{top['clientes']} clientes ({_virg(top['percentual'])}%)."
        )

    media = resumo.get("media")
    mediana = resumo.get("mediana")

    if media is not None and mediana is not None:
        if mediana > 0 and media > mediana * 1.1:
            ins.append(
                f"A média de faturamento ({brl(media)}) é maior que a "
                f"mediana ({brl(mediana)}): poucos clientes muito grandes "
                "puxam a média para cima, então a mediana descreve melhor "
                "o cliente típico."
            )
        else:
            ins.append(
                f"Média ({brl(media)}) e mediana ({brl(mediana)}) "
                "são próximas: a carteira é equilibrada."
            )

    if por_nivel:
        nivel_top = por_nivel[0]
        ins.append(
            f"O nível {nivel_top.get('nivel', 'não informado')} "
            f"possui {nivel_top.get('clientes', 0)} clientes."
        )

    if por_consultor:
        consultor_top = por_consultor[0]
        ins.append(
            f"O consultor {consultor_top.get('consultor', 'não informado')} "
            f"tem a maior quantidade de clientes na análise."
        )

    if por_servico:
        servico_top = por_servico[0]
        ins.append(
            f"O serviço {servico_top.get('servico', 'não informado')} "
            "está entre os mais contratados."
        )

    if correlacao:
        ins.append(
            "A análise de correlação compara a quantidade de serviços "
            "com o faturamento dos clientes."
        )

    if not ins:
        ins.append("Não há dados suficientes para gerar insights.")

    return ins
