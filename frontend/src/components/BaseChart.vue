<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { Chart, registerables } from 'chart.js'

Chart.register(...registerables)

// Paleta da marca InCTI
const CORES = ['#c73a68', '#ff8a4c', '#ffbe55', '#6e3d53', '#e0587f', '#ffa877', '#b9adb7']
const TEXTO_EIXO = '#857b85'
const GRADE = '#f3edf1'

const props = defineProps({
    tipo: { type: String, default: 'bar' },
    rotulos: { type: Array, default: () => [] },
    valores: { type: Array, default: () => [] },
    horizontal: { type: Boolean, default: false }
})

const canvas = ref(null)
let grafico = null

function desenhar() {
    if (!canvas.value) return
    grafico?.destroy()

    const linha = props.tipo === 'line'
    const rosca = props.tipo === 'doughnut'
    const eixo = {
        ticks: { color: TEXTO_EIXO, font: { size: 11 } },
        grid: { color: GRADE },
        border: { display: false }
    }

    grafico = new Chart(canvas.value, {
        type: props.tipo,
        data: {
            labels: props.rotulos,
            datasets: [{
                data: props.valores,
                backgroundColor: linha ? 'rgba(199, 58, 104, 0.12)' : CORES,
                borderColor: linha ? CORES[0] : rosca ? '#ffffff' : 'transparent',
                borderWidth: linha ? 2.5 : rosca ? 3 : 0,
                borderRadius: rosca ? 0 : 8,
                fill: linha,
                tension: 0.35,
                pointBackgroundColor: CORES[0],
                pointBorderColor: '#ffffff',
                pointBorderWidth: 2,
                pointRadius: linha ? 4 : 0,
                maxBarThickness: 36
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: props.horizontal ? 'y' : 'x',
            cutout: rosca ? '62%' : undefined,
            plugins: {
                legend: {
                    display: rosca,
                    position: 'bottom',
                    labels: { color: '#403640', usePointStyle: true, pointStyle: 'circle', padding: 16 }
                },
                tooltip: {
                    backgroundColor: '#302536',
                    padding: 10,
                    cornerRadius: 8,
                    displayColors: !linha
                }
            },
            scales: rosca ? {} : { x: eixo, y: eixo }
        }
    })
}

watch(
    () => [props.tipo, props.rotulos, props.valores, props.horizontal],
    async () => {
        await nextTick()
        desenhar()
    },
    { deep: true }
)

onMounted(desenhar)
onBeforeUnmount(() => grafico?.destroy())
</script>

<template>
    <div class="chart-container">
        <canvas ref="canvas"></canvas>
    </div>
</template>

<style scoped>
.chart-container {
    position: relative;
    min-height: 15rem;
}
</style>
