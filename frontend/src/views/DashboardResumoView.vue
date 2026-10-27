<script setup>
import { computed, onMounted } from 'vue'
import { useUploadStore } from '../stores/uploadStore'

const store = useUploadStore()
const dashboard = computed(() => store.dashboard)
const resumo = computed(() => dashboard.value?.resumo || {})

onMounted(() => store.carregarDashboard())

function moeda(valor) {
    if (valor === null || valor === undefined) return '—'
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valor)
}
</script>

<template>
    <section class="dashboard-page">
        <header class="dashboard-header">
            <span class="eyebrow">InCTI Data</span>
            <h1>Dashboard de inteligência</h1>
            <p v-if="dashboard">
                Resultado do lote {{ store.loteId }} processado pelo ETL.
            </p>
            <p v-else>
                Envie uma planilha para visualizar os indicadores.
            </p>
        </header>

        <div v-if="dashboard" class="kpi-grid">
            <article><span>Clientes</span><strong>{{ resumo.totalClientes }}</strong></article>
            <article><span>Faturamento total</span><strong>{{ moeda(resumo.faturamentoTotal) }}</strong></article>
            <article><span>Faturamento médio</span><strong>{{ moeda(resumo.media) }}</strong></article>
            <article><span>Mediana</span><strong>{{ moeda(resumo.mediana) }}</strong></article>
        </div>

        <div v-if="dashboard" class="dashboard-panels">
            <article class="dashboard-panel">
                <h2>Insights</h2>
                <ul>
                    <li v-for="insight in dashboard.insights" :key="insight">{{ insight }}</li>
                </ul>
            </article>
            <article class="dashboard-panel">
                <h2>Segmentos</h2>
                <div v-for="item in dashboard.porSegmento" :key="item.segmento" class="bar-row">
                    <span>{{ item.segmento }}</span>
                    <strong>{{ item.clientes }} clientes</strong>
                </div>
            </article>
            <article class="dashboard-panel">
                <h2>Serviços mais contratados</h2>
                <div v-for="item in dashboard.porServico" :key="item.servico" class="bar-row">
                    <span>{{ item.servico }}</span>
                    <strong>{{ item.contratos }}</strong>
                </div>
            </article>
        </div>
    </section>
</template>

<style scoped>
.dashboard-page { width: 100%; color: #f7f8fc; }
.dashboard-header { margin-bottom: 2rem; }
.dashboard-header h1 { margin: .5rem 0; font-size: clamp(2rem, 4vw, 3.2rem); }
.dashboard-header p { color: #a4aaa8; }
.eyebrow { color: #ffbe55; font-size: .75rem; font-weight: 700; letter-spacing: .14em; text-transform: uppercase; }
.kpi-grid { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 1rem; }
.kpi-grid article, .dashboard-panel { border: 1px solid rgba(255,255,255,.1); border-radius: 1rem; background: rgba(255,255,255,.05); padding: 1.25rem; }
.kpi-grid span { display: block; color: #a4aaa8; font-size: .85rem; }
.kpi-grid strong { display: block; margin-top: .5rem; font-size: 1.35rem; }
.dashboard-panels { display: grid; grid-template-columns: 1.2fr 1fr 1fr; gap: 1rem; margin-top: 1rem; }
.dashboard-panel h2 { margin: 0 0 1rem; font-size: 1.1rem; }
.dashboard-panel ul { display: grid; gap: .75rem; margin: 0; padding-left: 1rem; color: #d7ddda; line-height: 1.5; }
.bar-row { display: flex; justify-content: space-between; gap: 1rem; padding: .7rem 0; border-bottom: 1px solid rgba(255,255,255,.08); color: #d7ddda; }
.bar-row:last-child { border-bottom: 0; }
.bar-row strong { color: #ffbe55; }
@media (max-width: 900px) { .kpi-grid, .dashboard-panels { grid-template-columns: 1fr 1fr; } }
@media (max-width: 560px) { .kpi-grid, .dashboard-panels { grid-template-columns: 1fr; } }
</style>
