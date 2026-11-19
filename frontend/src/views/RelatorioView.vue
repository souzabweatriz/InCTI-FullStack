<script setup>
import { computed, onMounted, ref } from 'vue'
import { dashboardService } from '../services/api'
import { useUploadStore } from '../stores/uploadStore'

const store = useUploadStore()

const dados = ref(null)
const loteAtual = ref(null)
const lotes = ref([])
const loteSelecionado = ref('')
const carregando = ref(true)
const erro = ref('')

const resumo = computed(() => dados.value?.resumo || {})
const insights = computed(() => dados.value?.insights || [])
const destaque = computed(() => insights.value[0])
const demaisInsights = computed(() => insights.value.slice(1))
const segmentos = computed(() => (dados.value?.porSegmento || []).slice(0, 6))
const topClientes = computed(() => dados.value?.topClientes || [])
const correlacao = computed(() => dados.value?.correlacao || {})
const qualidade = computed(() => dados.value?.qualidade || {})

const maiorFaturamentoTop = computed(() =>
    Math.max(...topClientes.value.map((c) => c.faturamento || 0), 1)
)

// Posição (0–100%) de cada medida na faixa mínimo → máximo
const distribuicao = computed(() => {
    const { minimo, q1, mediana, q3, maximo, media } = resumo.value
    if (minimo == null || maximo == null) return null
    const faixa = maximo - minimo || 1
    const pos = (v) => ((v - minimo) / faixa) * 100
    return {
        q1: pos(q1), mediana: pos(mediana), q3: pos(q3), media: pos(media)
    }
})

// Marcador da correlação na escala -1 → +1
const posicaoCorrelacao = computed(() =>
    correlacao.value.pearson == null ? null : ((correlacao.value.pearson + 1) / 2) * 100
)

const percentualValido = computed(() => {
    const { linhasRecebidas, linhasValidas } = qualidade.value
    return linhasRecebidas ? (linhasValidas / linhasRecebidas) * 100 : 0
})

const totalCorrecoes = computed(() =>
    (qualidade.value.correcoesSegmento || 0) +
    (qualidade.value.correcoesConsultor || 0) +
    (qualidade.value.correcoesNivel || 0)
)

const destaquesCarteira = computed(() => [
    { rotulo: 'Segmento líder', valor: resumo.value.segmentoMaisFrequente, icone: 'camadas' },
    { rotulo: 'Serviço mais contratado', valor: resumo.value.servicoMaisContratado, icone: 'maleta' },
    { rotulo: 'Nível predominante', valor: resumo.value.nivelMaisFrequente, icone: 'estrela' },
    {
        rotulo: 'Serviços por cliente',
        valor: resumo.value.mediaServicosPorCliente?.toLocaleString('pt-BR'),
        icone: 'grafico'
    }
])

const ICONES = {
    camadas: 'M12 3 3 8l9 5 9-5-9-5Zm-9 9 9 5 9-5M3 16l9 5 9-5',
    grafico: 'M3 3v18h18M8 16v-5m5 5V8m5 8v-3',
    estrela: 'm12 3 2.6 5.6 6.1.7-4.5 4.2 1.2 6L12 16.6 6.6 19.5l1.2-6-4.5-4.2 6.1-.7L12 3Z',
    pessoa: 'M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8Zm-7 9a7 7 0 0 1 14 0',
    maleta: 'M4 7h16v12H4V7Zm5 0V5h6v2',
    elo: 'M10 14a4 4 0 0 0 5.7 0l3-3a4 4 0 0 0-5.7-5.7l-1 1M14 10a4 4 0 0 0-5.7 0l-3 3a4 4 0 0 0 5.7 5.7l1-1',
    brilho: 'M12 3v4m0 10v4M3 12h4m10 0h4M6 6l2.5 2.5m7 7L18 18M6 18l2.5-2.5m7-7L18 6'
}

function iconeDoInsight(texto) {
    const t = texto.toLowerCase()
    if (t.includes('correlação')) return 'elo'
    if (t.includes('segmento')) return 'camadas'
    if (t.includes('média') || t.includes('mediana')) return 'grafico'
    if (t.includes('nível')) return 'estrela'
    if (t.includes('consultor')) return 'pessoa'
    if (t.includes('serviço')) return 'maleta'
    return 'brilho'
}

function moeda(valor, compacto = false) {
    if (valor === null || valor === undefined) return '—'
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL',
        notation: compacto ? 'compact' : 'standard',
        maximumFractionDigits: compacto ? 1 : 2
    }).format(valor)
}

function dataHora(valor) {
    if (!valor) return ''
    return new Date(valor).toLocaleString('pt-BR', {
        day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit'
    })
}

async function carregar(loteId) {
    carregando.value = true
    erro.value = ''
    try {
        const resposta = await dashboardService.obter(loteId || undefined)
        dados.value = resposta.estatisticas
        loteAtual.value = resposta.lote
        store.loteId = resposta.lote?.id || store.loteId
        loteSelecionado.value = String(resposta.lote?.id || '')
    } catch (e) {
        dados.value = null
        // 404 = ainda não existe lote processado: mostramos o estado vazio
        erro.value = /nenhum lote/i.test(e.message) ? '' : e.message
    } finally {
        carregando.value = false
    }
}

onMounted(async () => {
    try {
        const todos = await dashboardService.lotes()
        lotes.value = todos.filter((l) => l.status === 'PROCESSADO')
    } catch {
        lotes.value = []
    }
    await carregar(store.loteId)
})
</script>

<template>
    <div class="insights-page">
        <div class="glow glow-a"></div>
        <div class="glow glow-b"></div>

        <main class="insights-container">
            <!-- CABEÇALHO -->
            <header class="page-header">
                <div>
                    <span class="badge"><span class="pulse-dot"></span>InCTI Data</span>
                    <h1>Insights</h1>
                    <p>O que os seus dados estão dizendo, calculado pelo ETL a cada planilha enviada.</p>
                </div>

                <div v-if="loteAtual" class="lote-box">
                    <label v-if="lotes.length > 1" class="lote-select">
                        <span>Lote analisado</span>
                        <select v-model="loteSelecionado" @change="carregar(Number(loteSelecionado))">
                            <option v-for="lote in lotes" :key="lote.id" :value="String(lote.id)">
                                #{{ lote.id }} · {{ lote.arquivoNome }}
                            </option>
                        </select>
                    </label>
                    <div v-else class="lote-chip">
                        <span>Lote #{{ loteAtual.id }}</span>
                        <strong>{{ loteAtual.arquivoNome }}</strong>
                    </div>
                    <small v-if="loteAtual.criadoEm">Gerado em {{ dataHora(loteAtual.criadoEm) }}</small>
                </div>
            </header>

            <!-- CARREGANDO -->
            <div v-if="carregando" class="skeleton-grid" aria-label="Carregando insights">
                <div class="skeleton tall"></div>
                <div class="skeleton"></div>
                <div class="skeleton"></div>
                <div class="skeleton"></div>
            </div>

            <!-- ERRO -->
            <section v-else-if="erro" class="state-card error">
                <h2>Não foi possível carregar os insights</h2>
                <p>{{ erro }}</p>
                <button type="button" class="btn" @click="carregar(store.loteId)">Tentar de novo</button>
            </section>

            <!-- VAZIO -->
            <section v-else-if="!dados" class="state-card">
                <div class="state-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
                        <path :d="ICONES.brilho" />
                    </svg>
                </div>
                <h2>Nenhum insight ainda</h2>
                <p>Envie uma planilha para análise e os insights aparecem aqui automaticamente.</p>
                <router-link to="/dashboard/upload" class="btn">Enviar planilha</router-link>
            </section>

            <template v-else>
                <!-- INSIGHT PRINCIPAL -->
                <section v-if="destaque" class="hero-insight">
                    <div class="hero-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
                            <path :d="ICONES[iconeDoInsight(destaque)]" />
                        </svg>
                    </div>
                    <div>
                        <span class="eyebrow">Principal descoberta</span>
                        <p>{{ destaque }}</p>
                    </div>
                </section>

                <!-- DEMAIS INSIGHTS -->
                <section v-if="demaisInsights.length" class="insight-grid">
                    <article
                        v-for="(texto, i) in demaisInsights"
                        :key="texto"
                        class="insight-card"
                        :style="{ animationDelay: `${i * 60}ms` }"
                    >
                        <div class="insight-top">
                            <span class="insight-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
                                    <path :d="ICONES[iconeDoInsight(texto)]" />
                                </svg>
                            </span>
                            <span class="insight-number">{{ String(i + 2).padStart(2, '0') }}</span>
                        </div>
                        <p>{{ texto }}</p>
                    </article>
                </section>

                <!-- DESTAQUES DA CARTEIRA -->
                <section class="highlights">
                    <div v-for="item in destaquesCarteira" :key="item.rotulo" class="highlight">
                        <span class="highlight-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
                                <path :d="ICONES[item.icone]" />
                            </svg>
                        </span>
                        <div>
                            <small>{{ item.rotulo }}</small>
                            <strong>{{ item.valor ?? '—' }}</strong>
                        </div>
                    </div>
                </section>

                <div class="two-cols">
                    <!-- SEGMENTOS -->
                    <section class="panel">
                        <header class="panel-header">
                            <div>
                                <span class="eyebrow">Concentração</span>
                                <h2>Clientes × faturamento por segmento</h2>
                            </div>
                        </header>
                        <ul class="segment-list">
                            <li v-for="s in segmentos" :key="s.segmento">
                                <div class="segment-head">
                                    <strong>{{ s.segmento }}</strong>
                                    <span>{{ s.clientes }} clientes · {{ moeda(s.faturamentoTotal, true) }}</span>
                                </div>
                                <div class="bar-pair">
                                    <div class="bar"><span class="fill clients" :style="{ width: `${s.percentual}%` }"></span></div>
                                    <em>{{ s.percentual.toLocaleString('pt-BR') }}%</em>
                                    <div class="bar"><span class="fill revenue" :style="{ width: `${s.percentualFaturamento}%` }"></span></div>
                                    <em>{{ s.percentualFaturamento.toLocaleString('pt-BR') }}%</em>
                                </div>
                            </li>
                        </ul>
                        <div class="legend">
                            <span><i class="dot clients"></i>% dos clientes</span>
                            <span><i class="dot revenue"></i>% do faturamento</span>
                        </div>
                    </section>

                    <!-- TOP CLIENTES -->
                    <section class="panel">
                        <header class="panel-header">
                            <div>
                                <span class="eyebrow">Ranking</span>
                                <h2>Maiores clientes</h2>
                            </div>
                        </header>
                        <ol class="ranking">
                            <li v-for="(c, i) in topClientes" :key="c.codigo">
                                <span class="rank" :class="{ gold: i === 0 }">{{ i + 1 }}</span>
                                <div class="rank-body">
                                    <div class="rank-head">
                                        <strong>{{ c.nome }}</strong>
                                        <span>{{ moeda(c.faturamento, true) }}</span>
                                    </div>
                                    <div class="bar thin">
                                        <span class="fill revenue" :style="{ width: `${(c.faturamento / maiorFaturamentoTop) * 100}%` }"></span>
                                    </div>
                                    <small>{{ c.segmento || 'Sem segmento' }}<template v-if="c.nivel"> · Nível {{ c.nivel }}</template></small>
                                </div>
                            </li>
                        </ol>
                    </section>
                </div>

                <div class="two-cols">
                    <!-- DISTRIBUIÇÃO -->
                    <section class="panel">
                        <header class="panel-header">
                            <div>
                                <span class="eyebrow">Distribuição</span>
                                <h2>Faturamento anual</h2>
                            </div>
                        </header>
                        <div v-if="distribuicao" class="boxplot">
                            <div class="box-track">
                                <span class="box-range" :style="{ left: `${distribuicao.q1}%`, width: `${distribuicao.q3 - distribuicao.q1}%` }"></span>
                                <span class="box-median" :style="{ left: `${distribuicao.mediana}%` }" title="Mediana"></span>
                                <span class="box-mean" :style="{ left: `${distribuicao.media}%` }" title="Média"></span>
                            </div>
                            <div class="box-labels">
                                <span>{{ moeda(resumo.minimo, true) }}</span>
                                <span>{{ moeda(resumo.maximo, true) }}</span>
                            </div>
                        </div>
                        <dl class="stats">
                            <div><dt>Mediana</dt><dd>{{ moeda(resumo.mediana) }}</dd></div>
                            <div><dt>Média</dt><dd>{{ moeda(resumo.media) }}</dd></div>
                            <div><dt>Q1 – Q3</dt><dd>{{ moeda(resumo.q1, true) }} – {{ moeda(resumo.q3, true) }}</dd></div>
                            <div>
                                <dt>Variação</dt>
                                <dd>{{ resumo.coeficienteVariacao != null ? `${Math.round(resumo.coeficienteVariacao * 100)}%` : '—' }}</dd>
                            </div>
                        </dl>
                        <div class="legend">
                            <span><i class="dot range"></i>50% centrais</span>
                            <span><i class="dot median"></i>Mediana</span>
                            <span><i class="dot mean"></i>Média</span>
                        </div>
                    </section>

                    <!-- CORRELAÇÃO -->
                    <section class="panel">
                        <header class="panel-header">
                            <div>
                                <span class="eyebrow">Correlação</span>
                                <h2>Serviços × faturamento</h2>
                            </div>
                            <span
                                v-if="correlacao.p != null"
                                class="pill"
                                :class="correlacao.p < 0.05 ? 'ok' : 'muted'"
                            >
                                {{ correlacao.p < 0.05 ? 'Significativa' : 'Não significativa' }}
                            </span>
                        </header>
                        <template v-if="posicaoCorrelacao != null">
                            <strong class="big-number">{{ correlacao.pearson.toLocaleString('pt-BR') }}</strong>
                            <div class="corr-scale">
                                <span class="corr-marker" :style="{ left: `${posicaoCorrelacao}%` }"></span>
                            </div>
                            <div class="box-labels">
                                <span>−1 negativa</span><span>0</span><span>+1 positiva</span>
                            </div>
                            <p class="muted-text">{{ correlacao.interpretacao }}</p>
                        </template>
                        <p v-else class="muted-text">
                            São necessários pelo menos 3 clientes com quantidades de serviços diferentes para calcular a correlação.
                        </p>
                    </section>
                </div>

                <!-- QUALIDADE -->
                <section class="panel">
                    <header class="panel-header">
                        <div>
                            <span class="eyebrow">Qualidade dos dados</span>
                            <h2>{{ qualidade.linhasValidas }} de {{ qualidade.linhasRecebidas }} linhas aproveitadas</h2>
                        </div>
                        <span class="pill ok">{{ Math.round(percentualValido) }}%</span>
                    </header>
                    <div class="bar quality">
                        <span class="fill clients" :style="{ width: `${percentualValido}%` }"></span>
                    </div>
                    <div class="quality-chips">
                        <span><strong>{{ qualidade.linhasInvalidas || 0 }}</strong> linhas descartadas</span>
                        <span><strong>{{ totalCorrecoes }}</strong> correções automáticas</span>
                        <span><strong>{{ qualidade.correcoesSegmento || 0 }}</strong> segmentos padronizados</span>
                        <span><strong>{{ resumo.consultores || 0 }}</strong> consultores</span>
                    </div>
                </section>
            </template>

            <!-- PROBLEMAS DA PLANILHA LOCAL -->
            <details v-if="store.arquivo && store.erros.length" class="panel issues">
                <summary>
                    <div>
                        <span class="eyebrow">Validação da planilha</span>
                        <h2>{{ store.erros.length }} problemas em {{ store.arquivo.name }}</h2>
                    </div>
                    <span class="chevron" aria-hidden="true"></span>
                </summary>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr><th>Linha</th><th>Campo</th><th>Tipo</th><th>Descrição</th></tr>
                        </thead>
                        <tbody>
                            <tr v-for="(e, i) in store.erros" :key="i">
                                <td>{{ e.linha }}</td>
                                <td><code>{{ e.campo }}</code></td>
                                <td><span class="tag">{{ e.tipo }}</span></td>
                                <td>{{ e.descricao }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </details>
        </main>
    </div>
</template>

<style scoped>
/* Paleta da marca InCTI (a mesma da Home, Sidebar e Perfil) */
.insights-page {
    --pink: #c73a68;
    --pink-dark: #a73360;
    --pink-soft: #f9e5ed;
    --pink-faint: #fff5f9;
    --orange: #ff8a4c;
    --orange-soft: #ffeadf;
    --yellow: #ffbe55;
    --yellow-text: #b9781a;
    --yellow-soft: #fff3dc;
    --text: #302536;
    --text-soft: #403640;
    --muted: #857b85;
    --border: #eee5ea;
    --track: #f3edf1;
    --card: #ffffff;
    --shadow: 0 1px 2px rgba(48, 37, 54, 0.04), 0 0.75rem 2rem -1.25rem rgba(48, 37, 54, 0.12);

    position: relative;
    width: 100%;
    min-height: 100vh;
    padding: 1rem 0 4rem;
    overflow-x: hidden;
    box-sizing: border-box;
    color: var(--text);
}

.glow {
    position: absolute;
    border-radius: 50%;
    filter: blur(6rem);
    pointer-events: none;
}
.glow-a { width: 24rem; height: 24rem; top: -8rem; left: -6rem; background: rgba(199, 58, 104, 0.08); }
.glow-b { width: 20rem; height: 20rem; top: 30%; right: -8rem; background: rgba(255, 138, 76, 0.08); }

.insights-container {
    position: relative;
    z-index: 1;
    max-width: 75rem;
    margin: 0 auto;
    display: grid;
    gap: 1.25rem;
}

/* CABEÇALHO */
.page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    gap: 1.5rem;
    margin-bottom: 1rem;
}
.badge {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.45rem 0.9rem;
    border: 1px solid rgba(199, 58, 104, 0.18);
    border-radius: 999px;
    background: var(--pink-soft);
    color: var(--pink);
    font-size: 0.68rem;
    font-weight: 700;
    letter-spacing: 0.2em;
    text-transform: uppercase;
}
.pulse-dot {
    width: 0.45rem;
    height: 0.45rem;
    border-radius: 50%;
    background: var(--pink);
    animation: pulse 2s ease-in-out infinite;
}
.page-header h1 {
    margin: 1rem 0 0.5rem;
    font-size: clamp(2.25rem, 5vw, 3.5rem);
    line-height: 1.05;
    font-weight: 750;
    letter-spacing: -0.04em;
    background: linear-gradient(120deg, var(--text) 15%, var(--pink) 55%, var(--orange) 100%);
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
}
.page-header p { margin: 0; max-width: 34rem; color: var(--muted); line-height: 1.6; }

.lote-box { display: grid; gap: 0.4rem; justify-items: end; text-align: right; }
.lote-box small { color: var(--muted); font-size: 0.75rem; }
.lote-select { display: grid; gap: 0.35rem; color: var(--muted); font-size: 0.75rem; font-weight: 600; }
.lote-select select {
    min-width: 14rem;
    padding: 0.65rem 0.8rem;
    border: 1px solid var(--border);
    border-radius: 0.75rem;
    background: var(--card);
    color: var(--text);
    font: inherit;
    font-size: 0.85rem;
    box-shadow: var(--shadow);
}
.lote-select select:focus { outline: 2px solid rgba(199, 58, 104, 0.35); outline-offset: 1px; }
.lote-chip {
    display: grid;
    padding: 0.6rem 0.9rem;
    border: 1px solid var(--border);
    border-radius: 0.75rem;
    background: var(--card);
    box-shadow: var(--shadow);
}
.lote-chip span { color: var(--muted); font-size: 0.7rem; }
.lote-chip strong { font-size: 0.9rem; font-weight: 600; }

/* ESTADOS */
.skeleton-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 1rem; }
.skeleton {
    height: 9rem;
    border-radius: 1.25rem;
    background: linear-gradient(90deg, var(--track) 0%, var(--pink-faint) 50%, var(--track) 100%);
    background-size: 200% 100%;
    animation: shimmer 1.4s linear infinite;
}
.skeleton.tall { grid-column: 1 / -1; height: 8rem; }

.state-card {
    display: grid;
    justify-items: center;
    gap: 0.75rem;
    padding: 4rem 1.5rem;
    border: 1px dashed #e3d3dc;
    border-radius: 1.5rem;
    background: var(--card);
    text-align: center;
}
.state-card h2 { margin: 0; font-size: 1.35rem; }
.state-card p { margin: 0; max-width: 28rem; color: var(--muted); line-height: 1.6; }
.state-card.error { border-color: rgba(199, 58, 104, 0.4); background: var(--pink-faint); }
.state-card.error p { color: var(--pink-dark); }
.state-icon {
    display: grid;
    place-items: center;
    width: 3.5rem;
    height: 3.5rem;
    border-radius: 1rem;
    background: var(--pink-soft);
    color: var(--pink);
}
.state-icon svg { width: 1.6rem; }
.btn {
    margin-top: 0.5rem;
    padding: 0.75rem 1.4rem;
    border: 0;
    border-radius: 999px;
    background: linear-gradient(120deg, var(--pink), var(--orange));
    color: #fff;
    font: inherit;
    font-weight: 700;
    font-size: 0.875rem;
    text-decoration: none;
    cursor: pointer;
    transition: transform 0.15s ease, box-shadow 0.15s ease;
}
.btn:hover { transform: translateY(-1px); box-shadow: 0 0.6rem 1.5rem -0.4rem rgba(199, 58, 104, 0.45); }

/* INSIGHT PRINCIPAL */
.hero-insight {
    position: relative;
    display: flex;
    gap: 1.25rem;
    align-items: flex-start;
    padding: 1.75rem;
    border: 1px solid transparent;
    border-radius: 1.5rem;
    background:
        radial-gradient(circle at 0% 0%, var(--pink-faint), var(--card) 60%) padding-box,
        linear-gradient(120deg, var(--pink), var(--orange) 55%, var(--yellow)) border-box;
    box-shadow: 0 1.5rem 3.5rem -2rem rgba(199, 58, 104, 0.35);
    animation: rise 0.5s ease both;
}
.hero-icon {
    flex-shrink: 0;
    display: grid;
    place-items: center;
    width: 3.25rem;
    height: 3.25rem;
    border-radius: 1rem;
    background: linear-gradient(135deg, var(--pink), var(--orange));
    color: #fff;
    box-shadow: 0 0.5rem 1.25rem -0.5rem rgba(199, 58, 104, 0.6);
}
.hero-icon svg { width: 1.5rem; }
.hero-insight p {
    margin: 0.4rem 0 0;
    color: var(--text);
    font-size: clamp(1.1rem, 2.2vw, 1.45rem);
    line-height: 1.45;
    font-weight: 600;
    letter-spacing: -0.01em;
}

.eyebrow {
    color: var(--pink);
    font-size: 0.68rem;
    font-weight: 800;
    letter-spacing: 0.16em;
    text-transform: uppercase;
}

/* GRADE DE INSIGHTS */
.insight-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(16rem, 1fr));
    gap: 1rem;
}
.insight-card {
    position: relative;
    display: grid;
    align-content: start;
    gap: 1rem;
    padding: 1.35rem;
    overflow: hidden;
    border: 1px solid var(--border);
    border-radius: 1.25rem;
    background: var(--card);
    box-shadow: var(--shadow);
    transition: border-color 0.2s ease, transform 0.2s ease, box-shadow 0.2s ease;
    animation: rise 0.5s ease both;
}
.insight-card::after {
    content: "";
    position: absolute;
    inset: 0 0 auto;
    height: 3px;
    background: linear-gradient(90deg, var(--pink), var(--orange), var(--yellow));
    opacity: 0;
    transition: opacity 0.2s ease;
}
.insight-card:hover {
    transform: translateY(-2px);
    border-color: #ecd3df;
    box-shadow: 0 1rem 2rem -1.25rem rgba(199, 58, 104, 0.35);
}
.insight-card:hover::after { opacity: 1; }
.insight-top { display: flex; justify-content: space-between; align-items: center; }
.insight-icon {
    display: grid;
    place-items: center;
    width: 2.4rem;
    height: 2.4rem;
    border-radius: 0.75rem;
    background: var(--pink-soft);
    color: var(--pink);
}
.insight-icon svg { width: 1.15rem; }
.insight-number { color: #d6c6d0; font-size: 0.8rem; font-weight: 800; font-variant-numeric: tabular-nums; }
.insight-card p { margin: 0; color: var(--text-soft); line-height: 1.6; font-size: 0.925rem; }

/* DESTAQUES */
.highlights {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 1rem;
}
.highlight {
    display: flex;
    align-items: center;
    gap: 0.85rem;
    min-width: 0;
    padding: 1rem 1.1rem;
    border: 1px solid var(--border);
    border-radius: 1rem;
    background: var(--card);
    box-shadow: var(--shadow);
}
.highlight-icon {
    flex-shrink: 0;
    display: grid;
    place-items: center;
    width: 2.25rem;
    height: 2.25rem;
    border-radius: 0.7rem;
}
.highlight:nth-child(4n + 1) .highlight-icon { background: var(--pink-soft); color: var(--pink); }
.highlight:nth-child(4n + 2) .highlight-icon { background: var(--orange-soft); color: #e0692b; }
.highlight:nth-child(4n + 3) .highlight-icon { background: var(--yellow-soft); color: var(--yellow-text); }
.highlight:nth-child(4n + 4) .highlight-icon { background: #f1e9f0; color: #6e3d53; }
.highlight-icon svg { width: 1.1rem; }
.highlight > div { display: grid; min-width: 0; }
.highlight small { color: var(--muted); font-size: 0.72rem; }
.highlight strong { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 1rem; }

/* PAINÉIS */
.two-cols { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 1.25rem; }
.panel {
    min-width: 0;
    padding: 1.5rem;
    border: 1px solid var(--border);
    border-radius: 1.25rem;
    background: var(--card);
    box-shadow: var(--shadow);
}
.panel-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 1rem;
    margin-bottom: 1.25rem;
}
.panel-header h2 { margin: 0.35rem 0 0; font-size: 1.05rem; font-weight: 700; }

.pill {
    flex-shrink: 0;
    padding: 0.3rem 0.7rem;
    border-radius: 999px;
    font-size: 0.72rem;
    font-weight: 700;
}
.pill.ok { background: var(--pink-soft); color: var(--pink); }
.pill.muted { background: var(--track); color: var(--muted); }

/* BARRAS */
.bar {
    position: relative;
    height: 0.5rem;
    overflow: hidden;
    border-radius: 999px;
    background: var(--track);
}
.bar.thin { height: 0.3rem; }
.bar.quality { height: 0.75rem; }
.fill {
    position: absolute;
    inset: 0 auto 0 0;
    border-radius: inherit;
    transition: width 0.8s cubic-bezier(0.2, 0.8, 0.2, 1);
}
.fill.clients { background: linear-gradient(90deg, var(--pink), #e0587f); }
.fill.revenue { background: linear-gradient(90deg, var(--orange), var(--yellow)); }

.segment-list { display: grid; gap: 1rem; margin: 0; padding: 0; list-style: none; }
.segment-head { display: flex; justify-content: space-between; gap: 1rem; margin-bottom: 0.5rem; font-size: 0.875rem; }
.segment-head span { color: var(--muted); font-size: 0.78rem; white-space: nowrap; }
.bar-pair { display: grid; grid-template-columns: 1fr 3rem; align-items: center; gap: 0.35rem 0.6rem; }
.bar-pair em { color: var(--muted); font-size: 0.72rem; font-style: normal; text-align: right; font-variant-numeric: tabular-nums; }

.legend { display: flex; flex-wrap: wrap; gap: 1rem; margin-top: 1.25rem; color: var(--muted); font-size: 0.75rem; }
.legend span { display: inline-flex; align-items: center; gap: 0.4rem; }
.dot { width: 0.6rem; height: 0.6rem; border-radius: 50%; }
.dot.clients { background: var(--pink); }
.dot.revenue { background: var(--orange); }
.dot.range { background: var(--pink-soft); border: 1px solid rgba(199, 58, 104, 0.4); border-radius: 0.15rem; }
.dot.median { width: 0.2rem; border-radius: 0.1rem; background: var(--pink); }
.dot.mean { background: var(--orange); }

/* RANKING */
.ranking { display: grid; gap: 1rem; margin: 0; padding: 0; list-style: none; }
.ranking li { display: flex; gap: 0.9rem; align-items: flex-start; }
.rank {
    flex-shrink: 0;
    display: grid;
    place-items: center;
    width: 2rem;
    height: 2rem;
    border-radius: 0.6rem;
    background: var(--track);
    color: var(--muted);
    font-size: 0.8rem;
    font-weight: 800;
}
.rank.gold { background: linear-gradient(135deg, var(--yellow), var(--orange)); color: #fff; }
.rank-body { flex: 1; display: grid; gap: 0.4rem; min-width: 0; }
.rank-head { display: flex; justify-content: space-between; gap: 1rem; font-size: 0.9rem; }
.rank-head strong { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-weight: 600; }
.rank-head span { color: var(--pink); font-weight: 700; font-variant-numeric: tabular-nums; white-space: nowrap; }
.rank-body small { color: var(--muted); font-size: 0.75rem; }

/* DISTRIBUIÇÃO */
.boxplot { margin: 0.5rem 0 1.25rem; }
.box-track {
    position: relative;
    height: 2.25rem;
    background: linear-gradient(var(--border), var(--border)) center / 100% 2px no-repeat;
}
.box-range {
    position: absolute;
    top: 0.35rem;
    bottom: 0.35rem;
    min-width: 2px;
    border: 1px solid rgba(199, 58, 104, 0.45);
    border-radius: 0.4rem;
    background: var(--pink-soft);
}
.box-median, .box-mean { position: absolute; transform: translateX(-50%); }
.box-median { top: 0.15rem; bottom: 0.15rem; width: 3px; border-radius: 2px; background: var(--pink); }
.box-mean {
    top: 50%;
    width: 0.7rem;
    height: 0.7rem;
    margin-top: -0.35rem;
    border: 2px solid #fff;
    border-radius: 50%;
    background: var(--orange);
    box-shadow: 0 0 0 1px rgba(255, 138, 76, 0.4);
}
.box-labels {
    display: flex;
    justify-content: space-between;
    margin-top: 0.45rem;
    color: var(--muted);
    font-size: 0.72rem;
    font-variant-numeric: tabular-nums;
}
.stats { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 0.75rem; margin: 0; }
.stats div { padding: 0.75rem 0.9rem; border-radius: 0.8rem; background: var(--pink-faint); }
.stats dt { color: var(--muted); font-size: 0.72rem; }
.stats dd { margin: 0.2rem 0 0; font-weight: 700; font-size: 0.95rem; font-variant-numeric: tabular-nums; }

/* CORRELAÇÃO */
.big-number {
    display: block;
    margin-bottom: 1rem;
    font-size: 3rem;
    font-weight: 750;
    line-height: 1;
    letter-spacing: -0.03em;
    font-variant-numeric: tabular-nums;
    background: linear-gradient(120deg, var(--pink), var(--orange));
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
}
.corr-scale {
    position: relative;
    height: 0.6rem;
    border-radius: 999px;
    background: linear-gradient(90deg, #b9adb7, var(--track) 50%, var(--pink));
}
.corr-marker {
    position: absolute;
    top: 50%;
    width: 1.1rem;
    height: 1.1rem;
    border: 3px solid #fff;
    border-radius: 50%;
    background: var(--pink);
    box-shadow: 0 0.25rem 0.75rem rgba(199, 58, 104, 0.45);
    transform: translate(-50%, -50%);
    transition: left 0.8s cubic-bezier(0.2, 0.8, 0.2, 1);
}
.muted-text { margin: 1.25rem 0 0; color: var(--muted); line-height: 1.6; font-size: 0.9rem; }

/* QUALIDADE */
.quality-chips { display: flex; flex-wrap: wrap; gap: 0.6rem; margin-top: 1.1rem; }
.quality-chips span {
    padding: 0.5rem 0.8rem;
    border: 1px solid var(--border);
    border-radius: 999px;
    background: var(--pink-faint);
    color: var(--muted);
    font-size: 0.8rem;
}
.quality-chips strong { color: var(--text); }

/* PROBLEMAS */
.issues summary {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 1rem;
    list-style: none;
    cursor: pointer;
}
.issues summary::-webkit-details-marker { display: none; }
.issues summary h2 { margin: 0.35rem 0 0; font-size: 1.05rem; font-weight: 700; }
.chevron {
    width: 0.6rem;
    height: 0.6rem;
    border-right: 2px solid var(--muted);
    border-bottom: 2px solid var(--muted);
    transform: rotate(45deg);
    transition: transform 0.2s ease;
}
.issues[open] .chevron { transform: rotate(-135deg); }
.table-wrapper { margin-top: 1.25rem; overflow-x: auto; }
table { width: 100%; border-collapse: collapse; font-size: 0.85rem; }
th {
    padding: 0.7rem 0.8rem;
    border-bottom: 1px solid var(--border);
    color: var(--muted);
    font-size: 0.7rem;
    font-weight: 700;
    letter-spacing: 0.08em;
    text-align: left;
    text-transform: uppercase;
}
td { padding: 0.75rem 0.8rem; border-bottom: 1px solid #f5eff3; color: var(--text-soft); vertical-align: top; }
tbody tr:hover td { background: var(--pink-faint); }
td code { color: var(--pink); font-size: 0.8rem; }
.tag {
    padding: 0.2rem 0.55rem;
    border-radius: 999px;
    background: var(--pink-soft);
    color: var(--pink-dark);
    font-size: 0.72rem;
    font-weight: 600;
    white-space: nowrap;
}

/* ANIMAÇÕES */
@keyframes rise { from { opacity: 0; transform: translateY(0.5rem); } to { opacity: 1; transform: none; } }
@keyframes shimmer { to { background-position: -200% 0; } }
@keyframes pulse { 50% { opacity: 0.35; } }

/* RESPONSIVO */
@media (max-width: 64rem) {
    .highlights { grid-template-columns: repeat(2, minmax(0, 1fr)); }
    .two-cols { grid-template-columns: 1fr; }
}
@media (max-width: 40rem) {
    .insights-page { padding: 0.5rem 0 3rem; }
    .page-header { flex-direction: column; align-items: stretch; }
    .lote-box { justify-items: stretch; text-align: left; }
    .lote-select select { min-width: 0; width: 100%; }
    .hero-insight { flex-direction: column; padding: 1.35rem; }
    .highlights { grid-template-columns: 1fr; }
    .skeleton-grid { grid-template-columns: 1fr; }
    .stats { grid-template-columns: 1fr; }
    .segment-head { flex-direction: column; gap: 0.15rem; }
    .panel { padding: 1.15rem; }
}
@media (prefers-reduced-motion: reduce) {
    *, *::before, *::after { animation: none !important; transition: none !important; }
}
</style>
