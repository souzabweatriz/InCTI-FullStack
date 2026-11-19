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
.dashboard-page { width: 100%; color: #f7f8fc; }
.dashboard-header { display: flex; justify-content: space-between; gap: 1rem; align-items: end; margin-bottom: 2rem; }
.dashboard-header h1 { margin: .5rem 0; font-size: clamp(2rem, 4vw, 3.2rem); }
.dashboard-header p, .lote-select { color: #a4aaa8; }
.eyebrow { color: #ffbe55; font-size: .75rem; font-weight: 700; letter-spacing: .14em; text-transform: uppercase; }
.lote-select { display: grid; gap: .4rem; font-size: .8rem; }
.lote-select select { min-width: 13rem; border: 1px solid rgba(255,255,255,.15); border-radius: .5rem; background: #25232d; color: #fff; padding: .65rem; }
.kpi-grid { display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 1rem; }
.kpi-grid article, .dashboard-panel { border: 1px solid rgba(255,255,255,.1); border-radius: 1rem; background: rgba(255,255,255,.05); padding: 1.25rem; }
.kpi-grid span { display: block; color: #a4aaa8; font-size: .85rem; }
.kpi-grid strong { display: block; margin-top: .5rem; font-size: 1.15rem; }
.chart-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 1rem; margin-top: 1rem; }
.dashboard-panel h2 { margin: 0 0 1rem; font-size: 1.1rem; }
.wide { grid-column: span 2; }
.insights ul { display: grid; gap: .75rem; margin: 0; padding-left: 1rem; color: #d7ddda; line-height: 1.5; }
.message { color: #a4aaa8; }
.message.error { color: #ff8f9d; }
@media (max-width: 1000px) { .kpi-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 700px) { .dashboard-header { display: block; } .chart-grid, .kpi-grid { grid-template-columns: 1fr; } .wide { grid-column: auto; } }
</style>
