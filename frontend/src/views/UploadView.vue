<script setup>
import { ref } from 'vue'
import { useUploadStore } from '../stores/uploadStore'

const store = useUploadStore()

const fileInput = ref(null)
const isDragging = ref(false)

function escolherArquivo(event) {
    const file = event.target.files?.[0]

    event.target.value = ''

    if (file) {
        store.lerArquivo(file)
    }
}

function abrirSeletor() {
    if (store.carregando) return

    fileInput.value?.click()
}

function onDragOver() {
    if (store.carregando) return

    isDragging.value = true
}

function onDragLeave() {
    isDragging.value = false
}

function onDrop(event) {
    isDragging.value = false

    if (store.carregando) return

    const file = event.dataTransfer.files?.[0]

    if (file) {
        store.lerArquivo(file)
    }
}

function formatarTamanho(bytes) {
    if (!bytes && bytes !== 0) return ''

    const kb = bytes / 1024

    if (kb < 1024) {
        return `${kb.toFixed(1)} KB`
    }

    return `${(kb / 1024).toFixed(1)} MB`
}
</script>

<template>
    <div class="upload-page">
        <section class="upload-section">
            <div class="background-effects">
                <div class="orb orb-one"></div>
                <div class="orb orb-two"></div>
                <div class="orb orb-three"></div>
                <div class="grain"></div>
            </div>

            <div class="upload-container">
                <div
                    class="page-header reveal"
                    style="--delay: 0ms"
                >
                    <div class="badge">
                        <span class="pulse-dot"></span>
                        InCTI Data
                    </div>

                    <h1>
                        Envie sua planilha
                    </h1>

                    <p>
                        Importe seus dados para começar a análise e transformar
                        informações brutas em insights inteligentes.
                    </p>
                </div>

                <div class="main-content">
                    <div
                        class="upload-card glass-panel reveal"
                        style="--delay: 80ms"
                    >
                        <input
                            ref="fileInput"
                            type="file"
                            accept=".xlsx,.xls,.csv"
                            class="file-input"
                            :disabled="store.carregando"
                            @change="escolherArquivo"
                        />

                        <div
                            class="dropzone"
                            :class="{
                                'is-dragging': isDragging,
                                'is-loading': store.carregando
                            }"
                            role="button"
                            :tabindex="store.carregando ? -1 : 0"
                            :aria-disabled="store.carregando"
                            @click="abrirSeletor"
                            @keydown.enter="abrirSeletor"
                            @dragover.prevent="onDragOver"
                            @dragleave.prevent="onDragLeave"
                            @drop.prevent="onDrop"
                        >
                            <div class="upload-icon">
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
                                        d="M12 16V4m0 0 4 4m-4-4-4 4"
                                    />

                                    <path
                                        stroke-linecap="round"
                                        stroke-linejoin="round"
                                        d="M4 16v2a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-2"
                                    />
                                </svg>
                            </div>

                            <h2>
                                Arraste seu arquivo aqui
                            </h2>

                            <p>
                                Ou clique para selecionar um arquivo do seu
                                computador.
                            </p>

                            <div class="file-types">
                                <span>XLSX</span>
                                <span>XLS</span>
                                <span>CSV</span>
                            </div>

                            <span
                                class="select-button"
                                :class="{ disabled: store.carregando }"
                            >
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                >
                                    <path
                                        stroke-linecap="round"
                                        d="M12 5v14M5 12h14"
                                    />
                                </svg>

                                {{
                                    store.carregando
                                        ? 'Lendo arquivo...'
                                        : 'Selecionar arquivo'
                                }}
                            </span>
                        </div>

                        <div
                            v-if="store.arquivo"
                            class="file-info reveal"
                            style="--delay: 0ms"
                        >
                            <div class="file-details">
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

                                <div class="file-text">
                                    <p>
                                        {{ store.arquivo.name }}
                                    </p>

                                    <span>
                                        {{ formatarTamanho(store.arquivo.size) }}
                                    </span>
                                </div>
                            </div>

                            <button
                                type="button"
                                class="change-button"
                                :disabled="store.carregando"
                                @click="abrirSeletor"
                            >
                                Trocar arquivo
                            </button>
                        </div>

                        <div
                            v-if="store.erro"
                            class="error-message reveal"
                            style="--delay: 0ms"
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="1.6"
                            >
                                <circle
                                    cx="12"
                                    cy="12"
                                    r="9"
                                />

                                <path
                                    stroke-linecap="round"
                                    d="M12 8v5M12 16h.01"
                                />
                            </svg>

                            <p>
                                {{ store.erro }}
                            </p>
                        </div>

                        <div
                            v-if="store.carregando"
                            class="progress-track"
                        >
                            <div class="progress-bar"></div>
                        </div>
                    </div>

                    <aside
                        class="summary-card glass-panel reveal"
                        style="--delay: 160ms"
                    >
                        <div class="summary-header">
                            <div>
                                <span class="eyebrow">
                                    Resumo
                                </span>

                                <h3>
                                    Dados carregados
                                </h3>
                            </div>

                            <div class="summary-icon">
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
                                        d="M4 19V5m0 14h16M8 16v-5m4 5V7m4 9V9"
                                    />
                                </svg>
                            </div>
                        </div>

                        <div class="stats">
                            <div class="stat-card">
                                <p>
                                    Linhas
                                </p>

                                <strong>
                                    {{ store.totalLinhas }}
                                </strong>

                                <span>
                                    registros carregados
                                </span>
                            </div>

                            <div class="stat-card">
                                <p>
                                    Colunas
                                </p>

                                <strong>
                                    {{ store.totalColunas }}
                                </strong>

                                <span>
                                    campos identificados
                                </span>
                            </div>
                        </div>

                        <button
                            v-if="store.dadosTratados.length"
                            type="button"
                            class="clear-button"
                            @click="store.limpar()"
                        >
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
                                    d="M6 7h12M9 7V5a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2m-8 0 1 12a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1l1-12"
                                />
                            </svg>

                            Limpar dados
                        </button>
                    </aside>
                </div>

                <div
                    v-if="store.dadosTratados.length"
                    class="preview-card glass-panel reveal"
                    style="--delay: 220ms"
                >
                    <div class="preview-header">
                        <div>
                            <div class="preview-title">
                                <span class="pulse-dot"></span>

                                <h2>
                                    Prévia dos dados tratados
                                </h2>
                            </div>

                            <p>
                                Mostrando até 10 de {{ store.totalLinhas }} linhas.
                            </p>
                        </div>

                        <span class="column-badge">
                            {{ store.totalColunas }} colunas
                        </span>
                    </div>

                    <div class="table-wrapper">
                        <table>
                            <thead>
                                <tr>
                                    <th
                                        v-for="coluna in store.colunas"
                                        :key="coluna"
                                    >
                                        {{ coluna }}
                                    </th>
                                </tr>
                            </thead>

                            <tbody>
                                <tr
                                    v-for="(linha, indice) in store.dadosTratados.slice(0, 10)"
                                    :key="indice"
                                >
                                    <td
                                        v-for="coluna in store.colunas"
                                        :key="coluna"
                                    >
                                        {{ linha[coluna] }}
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<style scoped>
.upload-page {
    width: 100%;
    min-height: 100vh;
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

.upload-section {
    position: relative;
    width: 100%;
    min-height: 100vh;
    display: flex;
    align-items: center;
    padding: 5rem 1.5rem;
    overflow: hidden;
}

.upload-container {
    position: relative;
    z-index: 2;
    width: 100%;
    max-width: 72rem;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
}

.background-effects {
    position: absolute;
    inset: 0;
    z-index: 0;
    overflow: hidden;
    pointer-events: none;
}

.background-effects::before {
    content: '';
    position: absolute;
    inset: 0;
    background:
        linear-gradient(
            rgba(255, 255, 255, 0.018) 1px,
            transparent 1px
        ),
        linear-gradient(
            90deg,
            rgba(255, 255, 255, 0.018) 1px,
            transparent 1px
        );
    background-size: 3rem 3rem;
    mask-image: linear-gradient(
        to bottom,
        black,
        transparent 90%
    );
}

.background-effects::after {
    content: '';
    position: absolute;
    inset: 0;
    background:
        radial-gradient(
            circle at center,
            transparent 30%,
            rgba(0, 0, 0, 0.4)
        );
}

.orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(5rem);
    opacity: 0.2;
}

.orb-one {
    width: 26rem;
    height: 26rem;
    top: -13rem;
    left: -10rem;
    background: rgba(187, 222, 198, 0.12);
    animation: float-one 12s ease-in-out infinite;
}

.orb-two {
    width: 23rem;
    height: 23rem;
    top: 25%;
    right: -12rem;
    background: rgba(90, 127, 120, 0.13);
    animation: float-two 15s ease-in-out infinite;
}

.orb-three {
    width: 20rem;
    height: 20rem;
    bottom: -12rem;
    left: 35%;
    background: rgba(49, 76, 83, 0.18);
    animation: float-three 18s ease-in-out infinite;
}

.grain {
    position: absolute;
    inset: 0;
    opacity: 0.025;
    background-image:
        url("data:image/svg+xml,%3Csvg viewBox='0 0 180 180' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='.55'/%3E%3C/svg%3E");
}

.page-header {
    width: 100%;
    max-width: 48rem;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

.badge {
    width: fit-content;
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
    backdrop-filter: blur(1rem);
}

.page-header h1 {
    width: 100%;
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
    background-size: 200% 200%;
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
    animation: gradient-shift 8s ease-in-out infinite;
}

.page-header p {
    width: 100%;
    max-width: 40rem;
    margin: 1.25rem 0 0;
    color: rgba(187, 222, 198, 0.6);
    font-size: 1rem;
    line-height: 1.8;
}

.main-content {
    width: 100%;
    margin-top: 3rem;
    display: flex;
    align-items: stretch;
    gap: 1.5rem;
}

.glass-panel {
    position: relative;
    overflow: hidden;
    border: 1px solid rgba(90, 127, 120, 0.25);
    border-radius: 1.75rem;
    background: rgba(10, 17, 13, 0.82);
    box-shadow:
        0 2rem 5rem rgba(0, 0, 0, 0.3),
        inset 0 1px 0 rgba(255, 255, 255, 0.025);
    backdrop-filter: blur(1.25rem);
}

.upload-card {
    flex: 1;
    width: 100%;
    padding: 1.75rem;
}

.file-input {
    display: none;
}

.dropzone {
    position: relative;
    width: 100%;
    min-height: 26rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 3rem 1.5rem;
    overflow: hidden;
    border: 2px dashed rgba(90, 127, 120, 0.3);
    border-radius: 1.4rem;
    background:
        radial-gradient(
            circle at 50% 10%,
            rgba(187, 222, 198, 0.07),
            transparent 42%
        ),
        rgba(49, 76, 83, 0.08);
    text-align: center;
    cursor: pointer;
    transition:
        border-color 0.3s ease,
        background 0.3s ease,
        box-shadow 0.3s ease,
        transform 0.3s ease;
}

.dropzone::before {
    content: '';
    position: absolute;
    inset: 0;
    background:
        linear-gradient(
            110deg,
            transparent 25%,
            rgba(187, 222, 198, 0.045) 50%,
            transparent 75%
        );
    transform: translateX(-100%);
    transition: transform 0.8s ease;
    pointer-events: none;
}

.dropzone:hover::before {
    transform: translateX(100%);
}

.dropzone:hover {
    border-color: rgba(187, 222, 198, 0.5);
    background:
        radial-gradient(
            circle at 50% 10%,
            rgba(187, 222, 198, 0.09),
            transparent 45%
        ),
        rgba(49, 76, 83, 0.12);
    box-shadow:
        0 0 0 1px rgba(187, 222, 198, 0.04),
        0 1.5rem 4rem rgba(0, 0, 0, 0.2);
    transform: translateY(-0.1rem);
}

.dropzone.is-dragging {
    border-color: #bbdec6;
    background:
        radial-gradient(
            circle at center,
            rgba(187, 222, 198, 0.13),
            transparent 55%
        ),
        rgba(49, 76, 83, 0.2);
    box-shadow:
        0 0 0 1px rgba(187, 222, 198, 0.1),
        0 0 3rem rgba(187, 222, 198, 0.06);
    transform: scale(1.01);
}

.dropzone.is-loading {
    cursor: not-allowed;
    opacity: 0.65;
}

.upload-icon {
    width: 5rem;
    height: 5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(187, 222, 198, 0.2);
    border-radius: 1.5rem;
    background: rgba(187, 222, 198, 0.08);
    color: #bbdec6;
    box-shadow:
        0 1rem 2.5rem rgba(0, 0, 0, 0.2),
        inset 0 1px 0 rgba(255, 255, 255, 0.03);
    animation: icon-float 4s ease-in-out infinite;
}

.upload-icon svg {
    width: 2rem;
    height: 2rem;
}

.dropzone h2 {
    margin: 1.75rem 0 0;
    color: #f7f8fc;
    font-size: 1.25rem;
    line-height: 1.4;
    font-weight: 650;
}

.dropzone > p {
    width: 100%;
    max-width: 28rem;
    margin: 0.75rem 0 0;
    color: rgba(187, 222, 198, 0.55);
    font-size: 0.9rem;
    line-height: 1.75;
}

.file-types {
    margin-top: 1rem;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-wrap: wrap;
    gap: 0.5rem;
}

.file-types span {
    padding: 0.4rem 0.8rem;
    border: 1px solid rgba(90, 127, 120, 0.3);
    border-radius: 999rem;
    background: rgba(49, 76, 83, 0.2);
    color: rgba(187, 222, 198, 0.7);
    font-size: 0.68rem;
    font-weight: 600;
}

.select-button {
    position: relative;
    overflow: hidden;
    min-height: 3rem;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    margin-top: 2rem;
    padding: 0.75rem 1.5rem;
    border-radius: 0.75rem;
    background: #bbdec6;
    color: #010300;
    font-size: 0.9rem;
    font-weight: 700;
    box-shadow:
        0 0.75rem 2rem rgba(187, 222, 198, 0.08),
        inset 0 1px 0 rgba(255, 255, 255, 0.3);
    transition:
        background 0.25s ease,
        transform 0.25s ease,
        box-shadow 0.25s ease;
}

.select-button:hover {
    background: #f7f8fc;
}

.select-button.disabled {
    opacity: 0.6;
}

.select-button svg {
    width: 1rem;
    height: 1rem;
}

.file-info {
    width: 100%;
    margin-top: 1.25rem;
    padding: 1rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
    border: 1px solid rgba(90, 127, 120, 0.25);
    border-radius: 1rem;
    background: rgba(49, 76, 83, 0.1);
}

.file-details {
    min-width: 0;
    display: flex;
    align-items: center;
    gap: 1rem;
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
    width: 1.25rem;
    height: 1.25rem;
}

.file-text {
    min-width: 0;
}

.file-text p {
    max-width: 25rem;
    margin: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: #f7f8fc;
    font-size: 0.875rem;
    font-weight: 650;
}

.file-text span {
    display: block;
    margin-top: 0.25rem;
    color: rgba(187, 222, 198, 0.45);
    font-size: 0.75rem;
}

.change-button {
    flex-shrink: 0;
    padding: 0.6rem 1rem;
    border: 1px solid rgba(90, 127, 120, 0.3);
    border-radius: 0.6rem;
    background: transparent;
    color: #bbdec6;
    font-size: 0.75rem;
    font-weight: 650;
    cursor: pointer;
}

.change-button:hover {
    border-color: rgba(187, 222, 198, 0.5);
    background: rgba(187, 222, 198, 0.07);
}

.error-message {
    width: 100%;
    margin-top: 1.25rem;
    padding: 1rem;
    display: flex;
    align-items: flex-start;
    gap: 0.75rem;
    border: 1px solid rgba(248, 113, 113, 0.2);
    border-radius: 1rem;
    background: rgba(248, 113, 113, 0.05);
}

.error-message svg {
    width: 1.25rem;
    height: 1.25rem;
    flex-shrink: 0;
    color: #fca5a5;
}

.error-message p {
    margin: 0;
    color: #fecaca;
    font-size: 0.875rem;
    line-height: 1.6;
}

.progress-track {
    position: relative;
    width: 100%;
    height: 0.35rem;
    margin-top: 1.25rem;
    overflow: hidden;
    border-radius: 999rem;
    background: rgba(49, 76, 83, 0.4);
}

.progress-bar {
    width: 40%;
    height: 100%;
    border-radius: inherit;
    background: linear-gradient(
        90deg,
        transparent,
        #bbdec6,
        transparent
    );
    box-shadow: 0 0 1rem rgba(187, 222, 198, 0.35);
    animation: progress-sweep 1.5s ease-in-out infinite;
}

.summary-card {
    width: 17rem;
    flex-shrink: 0;
    padding: 1.75rem;
    display: flex;
    flex-direction: column;
}

.summary-header {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
}

.eyebrow {
    display: block;
    color: #5a7f78;
    font-size: 0.65rem;
    font-weight: 700;
    letter-spacing: 0.2em;
    text-transform: uppercase;
}

.summary-header h3 {
    margin: 0.5rem 0 0;
    color: #f7f8fc;
    font-size: 1.15rem;
    font-weight: 650;
}

.summary-icon {
    width: 2.5rem;
    height: 2.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 0.75rem;
    background: rgba(187, 222, 198, 0.08);
    color: #bbdec6;
}

.summary-icon svg {
    width: 1.25rem;
    height: 1.25rem;
}

.stats {
    width: 100%;
    margin-top: 1.75rem;
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.stat-card {
    position: relative;
    overflow: hidden;
    width: 100%;
    padding: 1.25rem;
    border: 1px solid rgba(90, 127, 120, 0.2);
    border-radius: 1rem;
    background: rgba(49, 76, 83, 0.1);
    transition:
        transform 0.3s ease,
        border-color 0.3s ease,
        background 0.3s ease;
}

.stat-card:hover {
    transform: translateY(-0.25rem);
    border-color: rgba(187, 222, 198, 0.25);
    background: rgba(49, 76, 83, 0.16);
}

.stat-card p {
    margin: 0;
    color: rgba(187, 222, 198, 0.45);
    font-size: 0.75rem;
}

.stat-card strong {
    display: block;
    margin-top: 0.4rem;
    color: #bbdec6;
    font-size: 2.25rem;
    line-height: 1;
    font-weight: 650;
}

.stat-card:nth-child(2) strong {
    color: #f7f8fc;
}

.stat-card span {
    display: block;
    margin-top: 0.45rem;
    color: rgba(187, 222, 198, 0.35);
    font-size: 0.7rem;
}

.clear-button {
    width: 100%;
    margin-top: 1.5rem;
    padding: 0.8rem 1rem;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    border: 1px solid rgba(252, 165, 165, 0.2);
    border-radius: 0.75rem;
    background: rgba(248, 113, 113, 0.05);
    color: #fecaca;
    font-size: 0.8rem;
    font-weight: 650;
    cursor: pointer;
}

.clear-button:hover {
    border-color: rgba(252, 165, 165, 0.35);
    background: rgba(248, 113, 113, 0.1);
}

.clear-button svg {
    width: 1rem;
    height: 1rem;
}

.preview-card {
    width: 100%;
    margin-top: 1.5rem;
}

.preview-header {
    width: 100%;
    padding: 1.5rem 2rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
    border-bottom: 1px solid rgba(90, 127, 120, 0.2);
}

.preview-title {
    display: flex;
    align-items: center;
    gap: 0.75rem;
}

.preview-title h2 {
    margin: 0;
    color: #f7f8fc;
    font-size: 1.1rem;
    font-weight: 650;
}

.preview-header p {
    margin: 0.5rem 0 0;
    color: rgba(187, 222, 198, 0.45);
    font-size: 0.8rem;
}

.column-badge {
    flex-shrink: 0;
    padding: 0.6rem 1rem;
    border: 1px solid rgba(90, 127, 120, 0.3);
    border-radius: 999rem;
    background: rgba(49, 76, 83, 0.2);
    color: #bbdec6;
    font-size: 0.75rem;
    font-weight: 650;
}

.table-wrapper {
    width: 100%;
    overflow-x: auto;
    scrollbar-width: thin;
    scrollbar-color: rgba(187, 222, 198, 0.35) transparent;
}

.table-wrapper::-webkit-scrollbar {
    width: 0.45rem;
    height: 0.45rem;
}

.table-wrapper::-webkit-scrollbar-track {
    background: transparent;
}

.table-wrapper::-webkit-scrollbar-thumb {
    background: rgba(187, 222, 198, 0.28);
    border-radius: 999rem;
}

table {
    width: 100%;
    min-width: 40rem;
    border-collapse: separate;
    border-spacing: 0;
}

thead {
    background: rgba(49, 76, 83, 0.15);
}

th {
    padding: 1rem 1.5rem;
    border-bottom: 1px solid rgba(90, 127, 120, 0.2);
    color: #5a7f78;
    font-size: 0.65rem;
    font-weight: 700;
    letter-spacing: 0.15em;
    text-align: left;
    text-transform: uppercase;
    white-space: nowrap;
}

td {
    padding: 1rem 1.5rem;
    border-bottom: 1px solid rgba(90, 127, 120, 0.1);
    color: rgba(187, 222, 198, 0.65);
    white-space: nowrap;
}

tbody tr {
    transition: background 0.2s ease;
}

tbody tr:hover {
    background: rgba(187, 222, 198, 0.035);
}

tbody tr:hover td {
    color: rgba(187, 222, 198, 0.85);
}

.reveal {
    animation:
        fade-in-up 0.7s cubic-bezier(0.16, 1, 0.3, 1) both;
    animation-delay: var(--delay, 0ms);
}

.pulse-dot {
    width: 0.5rem;
    height: 0.5rem;
    flex-shrink: 0;
    display: inline-block;
    border-radius: 50%;
    background: #bbdec6;
    animation: pulse-dot 2.2s ease-out infinite;
}

@keyframes gradient-shift {
    0%,
    100% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }
}

@keyframes fade-in-up {
    from {
        opacity: 0;
        transform: translateY(1.1rem);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes icon-float {
    0%,
    100% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(-0.3rem);
    }
}

@keyframes progress-sweep {
    0% {
        transform: translateX(-150%);
    }

    100% {
        transform: translateX(350%);
    }
}

@keyframes pulse-dot {
    0% {
        box-shadow: 0 0 0 0 rgba(187, 222, 198, 0.35);
    }

    70% {
        box-shadow: 0 0 0 0.45rem rgba(187, 222, 198, 0);
    }

    100% {
        box-shadow: 0 0 0 0 rgba(187, 222, 198, 0);
    }
}

@keyframes float-one {
    0%,
    100% {
        transform: translate(0, 0);
    }

    50% {
        transform: translate(2rem, 1.5rem);
    }
}

@keyframes float-two {
    0%,
    100% {
        transform: translate(0, 0);
    }

    50% {
        transform: translate(-1.5rem, 2rem);
    }
}

@keyframes float-three {
    0%,
    100% {
        transform: translate(0, 0);
    }

    50% {
        transform: translate(1.25rem, -1.8rem);
    }
}

@media (max-width: 60rem) {
    .upload-section {
        padding: 4rem 1.25rem;
    }

    .main-content {
        flex-direction: column;
    }

    .summary-card {
        width: 100%;
    }

    .stats {
        flex-direction: row;
    }

    .stat-card {
        flex: 1;
    }
}

@media (max-width: 40rem) {
    .upload-section {
        padding: 3rem 1rem;
    }

    .upload-card {
        padding: 0.75rem;
        border-radius: 1.5rem;
    }

    .dropzone {
        min-height: 23rem;
        padding: 2.5rem 1rem;
    }

    .page-header h1 {
        font-size: 2rem;
    }

    .page-header p {
        font-size: 0.875rem;
    }

    .stats {
        flex-direction: column;
    }

    .file-info {
        flex-direction: column;
        align-items: stretch;
    }

    .change-button {
        width: 100%;
    }

    .preview-header {
        padding: 1.25rem;
        align-items: flex-start;
        flex-direction: column;
    }

    .column-badge {
        width: fit-content;
    }

    .orb {
        filter: blur(4rem);
        opacity: 0.14;
    }
}

@media (prefers-reduced-motion: reduce) {
    *,
    *::before,
    *::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        scroll-behavior: auto !important;
        transition-duration: 0.01ms !important;
    }
}
</style>

