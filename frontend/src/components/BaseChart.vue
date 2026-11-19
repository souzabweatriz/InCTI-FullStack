<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { Chart, registerables } from 'chart.js'

Chart.register(...registerables)

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

    grafico = new Chart(canvas.value, {
        type: props.tipo,
        data: {
            labels: props.rotulos,
            datasets: [{
                data: props.valores,
                backgroundColor: [
                    '#ffbe55', '#ef6a8a', '#7bdff2', '#b8e986',
                    '#b39ddb', '#f7a072', '#8bd3dd'
                ],
                borderColor: '#ffbe55',
                borderWidth: 1,
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: props.horizontal ? 'y' : 'x',
            plugins: { legend: { display: props.tipo === 'doughnut' } },
            scales: props.tipo === 'doughnut' ? {} : {
                x: { ticks: { color: '#a4aaa8' }, grid: { color: 'rgba(255,255,255,.08)' } },
                y: { ticks: { color: '#a4aaa8' }, grid: { color: 'rgba(255,255,255,.08)' } }
            }
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
