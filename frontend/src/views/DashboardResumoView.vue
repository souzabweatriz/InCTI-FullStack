<script setup>
import { computed, onMounted, ref } from 'vue'
import BaseChart from '../components/BaseChart.vue'
import { dashboardService } from '../services/api'
import { useUploadStore } from '../stores/uploadStore'

const store = useUploadStore()
const dashboard = ref(null)
const lotes = ref([])
const loteSelecionado = ref('')
const erro = ref('')
const carregando = ref(false)
const resumo = computed(() => dashboard.value?.resumo || {})

async function carregar(loteId) {
    carregando.value = true
    erro.value = ''
    try {
        const resposta = await dashboardService.obter(loteId || undefined)
        dashboard.value = resposta.estatisticas
        store.loteId = resposta.lote?.id || loteId || store.loteId
        store.dashboard = dashboard.value
        loteSelecionado.value = String(store.loteId || '')
    } catch (error) {
        erro.value = error.message
    } finally {
        carregando.value = false
    }
}

async function carregarLotes() {
    try {
        lotes.value = await dashboardService.lotes()
    } catch (error) {
        erro.value = error.message
    }
}

function moeda(valor) {
    if (valor === null || valor === undefined) return '—'
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valor)
}

onMounted(async () => {
    await carregarLotes()
    await carregar(store.loteId)
})
</script>

<template>
    <section class="dashboard-page">
        <header class="dashboard-header">
            <div>
                <span class="eyebrow">InCTI Data</span>
                <h1>Dashboard de inteligência</h1>
                <p>Indicadores e gráficos calculados pelo ETL Python.</p>
            </div>
            <label v-if="lotes.length" class="lote-select">
                Lote
                <select v-model="loteSelecionado" @change="carregar(Number(loteSelecionado))">
                    <option v-for="lote in lotes" :key="lote.id" :value="String(lote.id)">
                        #{{ lote.id }} — {{ lote.arquivoNome }}
                    </option>
                </select>
            </label>
        </header>

        <p v-if="carregando" class="message">Carregando dashboard...</p>
        <p v-else-if="erro" class="message error">{{ erro }}</p>
        <p v-else-if="!dashboard" class="message">
            Envie uma planilha para visualizar os indicadores.
        </p>

        <template v-else>
            <div class="kpi-grid">
                <article><span>Clientes</span><strong>{{ resumo.totalClientes }}</strong></article>
                <article><span>Faturamento total</span><strong>{{ moeda(resumo.faturamentoTotal) }}</strong></article>
                <article><span>Faturamento médio</span><strong>{{ moeda(resumo.media) }}</strong></article>
                <article><span>Mediana</span><strong>{{ moeda(resumo.mediana) }}</strong></article>
                <article><span>Desvio padrão</span><strong>{{ moeda(resumo.desvioPadrao) }}</strong></article>
            </div>

            <div class="chart-grid">
                <article class="dashboard-panel wide">
                    <h2>Clientes por segmento</h2>
                    <BaseChart tipo="bar" :rotulos="dashboard.porSegmento?.map((item) => item.segmento)" :valores="dashboard.porSegmento?.map((item) => item.clientes)" />
                </article>
                <article class="dashboard-panel">
                    <h2>Clientes por nível</h2>
                    <BaseChart tipo="doughnut" :rotulos="dashboard.porNivel?.map((item) => item.nivel)" :valores="dashboard.porNivel?.map((item) => item.clientes)" />
                </article>
                <article class="dashboard-panel">
                    <h2>Clientes por consultor</h2>
                    <BaseChart horizontal :rotulos="dashboard.porConsultor?.map((item) => item.consultor)" :valores="dashboard.porConsultor?.map((item) => item.clientes)" />
                </article>
                <article class="dashboard-panel">
                    <h2>Serviços contratados</h2>
                    <BaseChart horizontal :rotulos="dashboard.porServico?.map((item) => item.servico)" :valores="dashboard.porServico?.map((item) => item.contratos)" />
                </article>
                <article class="dashboard-panel wide">
                    <h2>Contratações por mês</h2>
                    <BaseChart tipo="line" :rotulos="dashboard.porMes?.map((item) => item.mes)" :valores="dashboard.porMes?.map((item) => item.clientes)" />
                </article>
                <article class="dashboard-panel insights">
                    <h2>Insights</h2>
                    <ul>
                        <li v-for="insight in dashboard.insights" :key="insight">{{ insight }}</li>
                    </ul>
                </article>
            </div>
        </template>
    </section>
</template>

<style scoped>
/* Paleta da marca InCTI (a mesma da Home, Sidebar, Insights e Upload) */
.dashboard-page {
    --pink: #c73a68;
    --orange: #ff8a4c;
    --yellow: #ffbe55;
    --text: #302536;
    --text-soft: #403640;
    --muted: #857b85;
    --border: #eee5ea;
    --pink-soft: #f9e5ed;
    --pink-faint: #fff5f9;
    --shadow: 0 1px 2px rgba(48, 37, 54, 0.04), 0 0.75rem 2rem -1.25rem rgba(48, 37, 54, 0.12);
    width: 100%;
    padding-top: 1rem;
    color: var(--text);
}
.dashboard-header { display: flex; justify-content: space-between; gap: 1rem; align-items: end; margin-bottom: 2rem; }
.dashboard-header h1 {
    margin: .5rem 0;
    font-size: clamp(2rem, 4vw, 3.2rem);
    font-weight: 750;
    letter-spacing: -.04em;
    line-height: 1.05;
    background: linear-gradient(120deg, var(--text) 15%, var(--pink) 55%, var(--orange) 100%);
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
}
.dashboard-header p { margin: 0; color: var(--muted); }
.eyebrow { color: var(--pink); font-size: .72rem; font-weight: 800; letter-spacing: .16em; text-transform: uppercase; }
.lote-select { display: grid; gap: .4rem; color: var(--muted); font-size: .75rem; font-weight: 600; }
.lote-select select {
    min-width: 13rem;
    padding: .65rem .8rem;
    border: 1px solid var(--border);
    border-radius: .75rem;
    background: #fff;
    color: var(--text);
    font: inherit;
    font-size: .85rem;
    box-shadow: var(--shadow);
}
.lote-select select:focus { outline: 2px solid rgba(199, 58, 104, .35); outline-offset: 1px; }

.kpi-grid { display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 1rem; }
.kpi-grid article, .dashboard-panel {
    border: 1px solid var(--border);
    border-radius: 1.1rem;
    background: #fff;
    box-shadow: var(--shadow);
    padding: 1.25rem;
}
.kpi-grid article { position: relative; overflow: hidden; transition: transform .2s ease, box-shadow .2s ease; }
.kpi-grid article::before { content: ""; position: absolute; inset: 0 auto 0 0; width: 4px; background: var(--accent, var(--pink)); }
.kpi-grid article:nth-child(5n + 2) { --accent: var(--orange); }
.kpi-grid article:nth-child(5n + 3) { --accent: var(--yellow); }
.kpi-grid article:nth-child(5n + 4) { --accent: #e0587f; }
.kpi-grid article:nth-child(5n + 5) { --accent: #6e3d53; }
.kpi-grid article:hover { transform: translateY(-2px); box-shadow: 0 1rem 2rem -1.25rem rgba(199, 58, 104, .35); }
.kpi-grid span { display: block; color: var(--muted); font-size: .8rem; }
.kpi-grid strong { display: block; margin-top: .5rem; font-size: 1.2rem; font-weight: 750; font-variant-numeric: tabular-nums; }

.chart-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 1rem; margin-top: 1rem; }
.dashboard-panel { min-width: 0; }
.dashboard-panel h2 { margin: 0 0 1rem; font-size: 1.05rem; font-weight: 700; }
.wide { grid-column: span 2; }

.insights {
    border: 1px solid transparent;
    background:
        radial-gradient(circle at 0% 0%, var(--pink-faint), #fff 60%) padding-box,
        linear-gradient(120deg, var(--pink), var(--orange) 55%, var(--yellow)) border-box;
}
.insights ul { display: grid; gap: .75rem; margin: 0; padding: 0; list-style: none; color: var(--text-soft); line-height: 1.55; font-size: .92rem; }
.insights li { position: relative; padding-left: 1.25rem; }
.insights li::before {
    content: "";
    position: absolute;
    left: 0;
    top: .5em;
    width: .5rem;
    height: .5rem;
    border-radius: .15rem;
    background: linear-gradient(135deg, var(--pink), var(--orange));
    transform: rotate(45deg);
}

.message { padding: 1rem 1.25rem; border: 1px dashed #e3d3dc; border-radius: 1rem; background: #fff; color: var(--muted); }
.message.error { border-color: rgba(199, 58, 104, .4); background: var(--pink-faint); color: #a73360; }

@media (max-width: 1000px) { .kpi-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 700px) {
    .dashboard-header { display: block; }
    .lote-select { margin-top: 1rem; }
    .lote-select select { width: 100%; min-width: 0; }
    .chart-grid, .kpi-grid { grid-template-columns: 1fr; }
    .wide { grid-column: auto; }
}
@media (prefers-reduced-motion: reduce) { .kpi-grid article { transition: none; } }
</style>
