<script setup>
import { useUploadStore } from '../stores/uploadStore'

const store = useUploadStore()
</script>

<template>
    <div class="relatorio-page">

        <!-- FUNDO -->
        <div class="background-effects">
            <div class="orb orb-one"></div>
            <div class="orb orb-two"></div>
            <div class="orb orb-three"></div>
        </div>

        <main class="relatorio-container">

            <!-- CABEÇALHO -->
            <header class="page-header">

                <div class="badge">
                    <span class="pulse-dot"></span>
                    InCTI Data
                </div>

                <h1>
                    Relatório de validação
                </h1>

                <p>
                    Confira os resultados da análise da sua
                    planilha e os problemas encontrados.
                </p>

            </header>


            <!-- SEM ARQUIVO -->
            <section
                v-if="!store.arquivo"
                class="empty-card"
            >

                <div class="empty-icon">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.6"
                    >
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M7 3h7l5 5v13a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Z"
                        />

                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M14 3v5h5"
                        />
                    </svg>
                </div>

                <h2>
                    Nenhuma planilha analisada
                </h2>

                <p>
                    Envie uma planilha na tela de upload
                    para visualizar o relatório de validação.
                </p>

            </section>


            <!-- RELATÓRIO -->
            <template v-else>

                <!-- ARQUIVO -->
                <section class="file-card">

                    <div class="file-icon">

                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="1.6"
                        >
                            <path
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                d="M7 3h7l5 5v13a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Z"
                            />

                            <path
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                d="M14 3v5h5"
                            />
                        </svg>

                    </div>

                    <div class="file-info">

                        <span>
                            ARQUIVO ANALISADO
                        </span>

                        <strong>
                            {{ store.arquivo.name }}
                        </strong>

                    </div>

                </section>


                <!-- CARDS DE RESUMO -->
                <section class="summary-grid">

                    <!-- TOTAL -->
                    <div class="summary-card">

                        <span class="card-label">
                            Total de registros
                        </span>

                        <strong>
                            {{ store.totalLinhas }}
                        </strong>

                        <small>
                            registros analisados
                        </small>

                    </div>


                    <!-- VÁLIDOS -->
                    <div class="summary-card valid">

                        <span class="card-label">
                            Registros válidos
                        </span>

                        <strong>
                            {{ store.quantidadeValidos }}
                        </strong>

                        <small>
                            sem erros encontrados
                        </small>

                    </div>


                    <!-- COM ERRO -->
                    <div class="summary-card error">

                        <span class="card-label">
                            Registros com erro
                        </span>

                        <strong>
                            {{ store.quantidadeComErro }}
                        </strong>

                        <small>
                            registros precisam de atenção
                        </small>

                    </div>


                    <!-- ERROS -->
                    <div class="summary-card warning">

                        <span class="card-label">
                            Total de erros
                        </span>

                        <strong>
                            {{ store.quantidadeErros }}
                        </strong>

                        <small>
                            problemas encontrados
                        </small>

                    </div>

                </section>


                <!-- TIPOS DE ERRO -->
                <section class="panel">

                    <div class="panel-header">

                        <div>

                            <span class="eyebrow">
                                Validação
                            </span>

                            <h2>
                                Tipos de erro encontrados
                            </h2>

                        </div>

                        <span class="total-badge">
                            {{ store.quantidadeErros }} erros
                        </span>

                    </div>


                    <div class="error-types">

                        <!-- CAMPOS VAZIOS -->
                        <div class="error-type">

                            <div class="error-type-icon">
                                !
                            </div>

                            <div>
                                <strong>
                                    Campos vazios
                                </strong>

                                <span>
                                    {{ store.quantidadeCamposVazios }}
                                    ocorrência(s)
                                </span>
                            </div>

                        </div>


                        <!-- EMAIL -->
                        <div class="error-type">

                            <div class="error-type-icon">
                                @
                            </div>

                            <div>
                                <strong>
                                    E-mails inválidos
                                </strong>

                                <span>
                                    {{ store.quantidadeEmailsInvalidos }}
                                    ocorrência(s)
                                </span>
                            </div>

                        </div>


                        <!-- DUPLICADOS -->
                        <div class="error-type">

                            <div class="error-type-icon">
                                ↔
                            </div>

                            <div>
                                <strong>
                                    Registros duplicados
                                </strong>

                                <span>
                                    {{ store.quantidadeDuplicados }}
                                    ocorrência(s)
                                </span>
                            </div>

                        </div>


                        <!-- PADRÃO -->
                        <div class="error-type">

                            <div class="error-type-icon">
                                #
                            </div>

                            <div>
                                <strong>
                                    Dados fora do padrão
                                </strong>

                                <span>
                                    {{ store.quantidadeForaDoPadrao }}
                                    ocorrência(s)
                                </span>
                            </div>

                        </div>


                        <!-- ESPAÇOS -->
                        <div class="error-type">

                            <div class="error-type-icon">
                                ␠
                            </div>

                            <div>
                                <strong>
                                    Espaços desnecessários
                                </strong>

                                <span>
                                    {{ store.quantidadeEspacos }}
                                    ocorrência(s)
                                </span>
                            </div>

                        </div>

                    </div>

                </section>


                <!-- DETALHAMENTO -->
                <section class="panel">

                    <div class="panel-header">

                        <div>

                            <span class="eyebrow">
                                Detalhamento
                            </span>

                            <h2>
                                Problemas encontrados
                            </h2>

                        </div>

                        <span class="total-badge">
                            {{ store.erros.length }}
                            registros
                        </span>

                    </div>


                    <!-- NÃO HÁ ERROS -->
                    <div
                        v-if="!store.erros.length"
                        class="success-message"
                    >

                        <div class="success-icon">
                            ✓
                        </div>

                        <div>

                            <strong>
                                Nenhum problema encontrado
                            </strong>

                            <p>
                                Todos os registros da planilha
                                passaram pelas validações.
                            </p>

                        </div>

                    </div>


                    <!-- TABELA -->
                    <div
                        v-else
                        class="table-wrapper"
                    >

                        <table>

                            <thead>

                                <tr>

                                    <th>
                                        Linha
                                    </th>

                                    <th>
                                        Campo
                                    </th>

                                    <th>
                                        Tipo
                                    </th>

                                    <th>
                                        Descrição
                                    </th>

                                </tr>

                            </thead>

                            <tbody>

                                <tr
                                    v-for="(erro, index) in store.erros"
                                    :key="index"
                                >

                                    <td>
                                        <span class="line-number">
                                            {{ erro.linha }}
                                        </span>
                                    </td>

                                    <td>
                                        <strong class="field-name">
                                            {{ erro.campo }}
                                        </strong>
                                    </td>

                                    <td>

                                        <span class="error-tag">
                                            {{ erro.tipo }}
                                        </span>

                                    </td>

                                    <td class="description">
                                        {{ erro.descricao }}
                                    </td>

                                </tr>

                            </tbody>

                        </table>

                    </div>

                </section>

            </template>

        </main>

    </div>
</template>


<style scoped>

.relatorio-page {
    width: 100%;
    min-height: 100vh;
    padding: 5rem 1.5rem;
    overflow-x: hidden;

    background:
        radial-gradient(
            circle at 50% -10%,
            rgba(187, 222, 198, 0.1),
            transparent 34rem
        ),
        #010300;

    color: #f7f8fc;
}


/* ==========================================
   FUNDO
========================================== */

.background-effects {
    position: fixed;
    inset: 0;
    z-index: 0;
    overflow: hidden;
    pointer-events: none;
}

.orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(5rem);
    opacity: 0.18;
}

.orb-one {
    width: 25rem;
    height: 25rem;
    top: -12rem;
    left: -10rem;
    background: rgba(187, 222, 198, 0.12);
}

.orb-two {
    width: 22rem;
    height: 22rem;
    top: 35%;
    right: -12rem;
    background: rgba(90, 127, 120, 0.13);
}

.orb-three {
    width: 20rem;
    height: 20rem;
    bottom: -10rem;
    left: 35%;
    background: rgba(49, 76, 83, 0.18);
}


/* ==========================================
   CONTAINER
========================================== */

.relatorio-container {
    position: relative;
    z-index: 2;

    width: 100%;
    max-width: 75rem;
    margin: 0 auto;
}


/* ==========================================
   CABEÇALHO
========================================== */

.page-header {
    max-width: 48rem;
    margin: 0 auto 3rem;

    display: flex;
    flex-direction: column;
    align-items: center;

    text-align: center;
}

.badge {
    display: flex;
    align-items: center;
    gap: 0.5rem;

    margin-bottom: 1.25rem;
    padding: 0.55rem 1rem;

    border: 1px solid rgba(90, 127, 120, 0.4);
    border-radius: 999rem;

    background: rgba(49, 76, 83, 0.2);

    color: #bbdec6;

    font-size: 0.68rem;
    font-weight: 700;
    letter-spacing: 0.2em;
    text-transform: uppercase;
}

.pulse-dot {
    width: 0.5rem;
    height: 0.5rem;

    border-radius: 50%;

    background: #bbdec6;
}

.page-header h1 {
    margin: 0;

    font-size: clamp(2rem, 5vw, 3.25rem);
    line-height: 1.1;
    font-weight: 650;
    letter-spacing: -0.04em;

    background:
        linear-gradient(
            120deg,
            #f7f8fc 10%,
            #bbdec6 52%,
            #5a7f78 100%
        );

    -webkit-background-clip: text;
    background-clip: text;

    color: transparent;
}

.page-header p {
    max-width: 40rem;
    margin: 1.25rem 0 0;

    color: rgba(187, 222, 198, 0.6);

    font-size: 1rem;
    line-height: 1.8;
}


/* ==========================================
   ARQUIVO
========================================== */

.file-card {
    width: 100%;

    display: flex;
    align-items: center;
    gap: 1rem;

    margin-bottom: 1.5rem;
    padding: 1.25rem 1.5rem;

    border: 1px solid rgba(90, 127, 120, 0.25);
    border-radius: 1.25rem;

    background: rgba(10, 17, 13, 0.82);

    box-shadow:
        0 2rem 5rem rgba(0, 0, 0, 0.25);

    backdrop-filter: blur(1.25rem);
}

.file-icon {
    width: 3rem;
    height: 3rem;

    flex-shrink: 0;

    display: flex;
    align-items: center;
    justify-content: center;

    border-radius: 0.75rem;

    background: rgba(187, 222, 198, 0.08);

    color: #bbdec6;
}

.file-icon svg {
    width: 1.3rem;
    height: 1.3rem;
}

.file-info {
    min-width: 0;

    display: flex;
    flex-direction: column;
    gap: 0.3rem;
}

.file-info span {
    color: #5a7f78;

    font-size: 0.65rem;
    font-weight: 700;
    letter-spacing: 0.15em;
}

.file-info strong {
    overflow: hidden;

    text-overflow: ellipsis;
    white-space: nowrap;

    color: #f7f8fc;

    font-size: 0.95rem;
}


/* ==========================================
   RESUMO
========================================== */

.summary-grid {
    width: 100%;

    display: grid;
    grid-template-columns: repeat(4, 1fr);

    gap: 1rem;

    margin-bottom: 1.5rem;
}

.summary-card {
    padding: 1.5rem;

    border: 1px solid rgba(90, 127, 120, 0.22);
    border-radius: 1.25rem;

    background: rgba(10, 17, 13, 0.82);

    backdrop-filter: blur(1.25rem);
}

.card-label {
    display: block;

    color: rgba(187, 222, 198, 0.5);

    font-size: 0.72rem;
}

.summary-card strong {
    display: block;

    margin-top: 0.6rem;

    color: #bbdec6;

    font-size: 2.3rem;
    line-height: 1;
}

.summary-card small {
    display: block;

    margin-top: 0.6rem;

    color: rgba(187, 222, 198, 0.35);

    font-size: 0.7rem;
}

.summary-card.valid strong {
    color: #bbdec6;
}

.summary-card.error strong {
    color: #fca5a5;
}

.summary-card.warning strong {
    color: #fde68a;
}


/* ==========================================
   PAINEL
========================================== */

.panel {
    width: 100%;

    margin-bottom: 1.5rem;

    overflow: hidden;

    border: 1px solid rgba(90, 127, 120, 0.25);
    border-radius: 1.5rem;

    background: rgba(10, 17, 13, 0.82);

    box-shadow:
        0 2rem 5rem rgba(0, 0, 0, 0.25);

    backdrop-filter: blur(1.25rem);
}

.panel-header {
    padding: 1.5rem 1.75rem;

    display: flex;
    align-items: center;
    justify-content: space-between;

    gap: 1rem;

    border-bottom: 1px solid rgba(90, 127, 120, 0.18);
}

.eyebrow {
    display: block;

    color: #5a7f78;

    font-size: 0.65rem;
    font-weight: 700;
    letter-spacing: 0.2em;
    text-transform: uppercase;
}

.panel-header h2 {
    margin: 0.4rem 0 0;

    color: #f7f8fc;

    font-size: 1.15rem;
    font-weight: 650;
}

.total-badge {
    flex-shrink: 0;

    padding: 0.55rem 0.85rem;

    border: 1px solid rgba(90, 127, 120, 0.3);
    border-radius: 999rem;

    background: rgba(49, 76, 83, 0.2);

    color: #bbdec6;

    font-size: 0.7rem;
    font-weight: 650;
}


/* ==========================================
   TIPOS DE ERRO
========================================== */

.error-types {
    display: grid;
    grid-template-columns: repeat(5, 1fr);

    gap: 1rem;

    padding: 1.5rem;
}

.error-type {
    display: flex;
    align-items: center;

    gap: 0.75rem;

    padding: 1rem;

    border: 1px solid rgba(90, 127, 120, 0.16);
    border-radius: 1rem;

    background: rgba(49, 76, 83, 0.08);
}

.error-type-icon {
    width: 2.2rem;
    height: 2.2rem;

    flex-shrink: 0;

    display: flex;
    align-items: center;
    justify-content: center;

    border-radius: 0.6rem;

    background: rgba(187, 222, 198, 0.08);

    color: #bbdec6;

    font-size: 0.85rem;
    font-weight: 700;
}

.error-type strong {
    display: block;

    color: #f7f8fc;

    font-size: 0.75rem;
}

.error-type span {
    display: block;

    margin-top: 0.25rem;

    color: rgba(187, 222, 198, 0.4);

    font-size: 0.65rem;
}


/* ==========================================
   TABELA
========================================== */

.table-wrapper {
    width: 100%;
    overflow-x: auto;
}

table {
    width: 100%;
    min-width: 50rem;

    border-collapse: separate;
    border-spacing: 0;
}

thead {
    background: rgba(49, 76, 83, 0.15);
}

th {
    padding: 1rem 1.25rem;

    border-bottom: 1px solid rgba(90, 127, 120, 0.2);

    color: #5a7f78;

    font-size: 0.65rem;
    font-weight: 700;
    letter-spacing: 0.12em;

    text-align: left;
    text-transform: uppercase;
}

td {
    padding: 1rem 1.25rem;

    border-bottom: 1px solid rgba(90, 127, 120, 0.1);

    color: rgba(187, 222, 198, 0.65);

    font-size: 0.8rem;
}

tbody tr:hover {
    background: rgba(187, 222, 198, 0.035);
}

.line-number {
    display: inline-flex;

    min-width: 2rem;
    height: 2rem;

    align-items: center;
    justify-content: center;

    border-radius: 0.5rem;

    background: rgba(49, 76, 83, 0.25);

    color: #bbdec6;

    font-size: 0.7rem;
    font-weight: 700;
}

.field-name {
    color: #f7f8fc;
}

.error-tag {
    display: inline-flex;

    padding: 0.4rem 0.6rem;

    border: 1px solid rgba(248, 113, 113, 0.2);
    border-radius: 999rem;

    background: rgba(248, 113, 113, 0.06);

    color: #fecaca;

    font-size: 0.65rem;
    font-weight: 650;

    white-space: nowrap;
}

.description {
    max-width: 30rem;

    line-height: 1.6;
}


/* ==========================================
   SUCESSO
========================================== */

.success-message {
    display: flex;
    align-items: center;

    gap: 1rem;

    margin: 1.5rem;
    padding: 1.25rem;

    border: 1px solid rgba(187, 222, 198, 0.2);
    border-radius: 1rem;

    background: rgba(187, 222, 198, 0.05);
}

.success-icon {
    width: 2.75rem;
    height: 2.75rem;

    flex-shrink: 0;

    display: flex;
    align-items: center;
    justify-content: center;

    border-radius: 50%;

    background: rgba(187, 222, 198, 0.1);

    color: #bbdec6;

    font-size: 1.2rem;
    font-weight: 700;
}

.success-message strong {
    color: #f7f8fc;
}

.success-message p {
    margin: 0.35rem 0 0;

    color: rgba(187, 222, 198, 0.5);

    font-size: 0.8rem;
}


/* ==========================================
   SEM ARQUIVO
========================================== */

.empty-card {
    max-width: 40rem;
    margin: 0 auto;

    padding: 4rem 2rem;

    display: flex;
    flex-direction: column;
    align-items: center;

    text-align: center;

    border: 1px solid rgba(90, 127, 120, 0.25);
    border-radius: 1.5rem;

    background: rgba(10, 17, 13, 0.82);

    backdrop-filter: blur(1.25rem);
}

.empty-icon {
    width: 4rem;
    height: 4rem;

    display: flex;
    align-items: center;
    justify-content: center;

    border-radius: 1rem;

    background: rgba(187, 222, 198, 0.08);

    color: #bbdec6;
}

.empty-icon svg {
    width: 1.75rem;
    height: 1.75rem;
}

.empty-card h2 {
    margin: 1.5rem 0 0;

    color: #f7f8fc;

    font-size: 1.2rem;
}

.empty-card p {
    max-width: 28rem;

    margin: 0.75rem 0 0;

    color: rgba(187, 222, 198, 0.5);

    font-size: 0.85rem;
    line-height: 1.7;
}


/* ==========================================
   RESPONSIVO
========================================== */

@media (max-width: 70rem) {

    .summary-grid {
        grid-template-columns: repeat(2, 1fr);
    }

    .error-types {
        grid-template-columns: repeat(2, 1fr);
    }
}


@media (max-width: 45rem) {

    .relatorio-page {
        padding: 3rem 1rem;
    }

    .summary-grid {
        grid-template-columns: 1fr;
    }

    .error-types {
        grid-template-columns: 1fr;
    }

    .panel-header {
        align-items: flex-start;
        flex-direction: column;
    }

    .file-card {
        align-items: flex-start;
    }

    .page-header h1 {
        font-size: 2rem;
    }
}

</style>