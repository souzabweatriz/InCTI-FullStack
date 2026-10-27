"""Testa a conexão com o PostgreSQL do Azure: python testar_conexao.py"""

from sqlalchemy import text

from banco import criar_engine

try:
    with criar_engine().connect() as conexao:
        versao = conexao.execute(text("SELECT version()")).scalar()
        print("Conectou!", versao)

        try:
            lotes = conexao.execute(
                text("SELECT COUNT(*) FROM upload_lote")
            ).scalar()
            print("Lotes no banco:", lotes)

        except Exception:
            print(
                "As tabelas ainda não foram criadas. "
                "Rode o backend uma vez (mvn spring-boot:run) "
                "e teste de novo."
            )

except Exception as erro:
    print(
        "Não conectou. Confira o .env e o firewall do Azure "
        "(seu IP precisa estar liberado)."
    )
    print("Detalhe:", str(erro).splitlines()[0])
