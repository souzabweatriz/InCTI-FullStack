
<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'

const activePoint = ref(null)
const chartVisible = ref(false)
const chartSection = ref(null)

const graphPoints = [
  {
    id: 1,
    value: 28,
    number: '01',
    title: 'Monitoramento',
    benefit: 'Acompanhe os principais indicadores da sua operação em tempo real.',
    color: '#C73A68'
  },
  {
    id: 2,
    value: 42,
    number: '02',
    title: 'Análise',
    benefit: 'Encontre padrões e relações importantes entre os seus dados.',
    color: '#E45A69'
  },
  {
    id: 3,
    value: 36,
    number: '03',
    title: 'Prevenção',
    benefit: 'Identifique sinais de risco antes que eles evoluam para problemas.',
    color: '#FF8A4C'
  },
  {
    id: 4,
    value: 64,
    number: '04',
    title: 'Inteligência',
    benefit: 'Transforme dados dispersos em contexto para sua operação.',
    color: '#FFA34F'
  },
  {
    id: 5,
    value: 88,
    number: '05',
    title: 'Decisão',
    benefit: 'Use informações organizadas para apoiar decisões estratégicas.',
    color: '#FFBE55'
  }
]

const platformFeatures = [
  {
    number: '01',
    title: 'Análise preditiva',
    description: 'Antecipe cenários com uma visão mais clara dos padrões presentes nos seus dados.',
    tag: 'Antecipação',
    icon: 'chart'
  },
  {
    number: '02',
    title: 'Inteligência defensiva',
    description: 'Identifique comportamentos incomuns e reconheça possíveis riscos com mais contexto.',
    tag: 'Proteção',
    icon: 'shield'
  },
  {
    number: '03',
    title: 'Tendências em tempo real',
    description: 'Acompanhe mudanças relevantes e tenha informações atualizadas para agir.',
    tag: 'Atualização contínua',
    icon: 'activity'
  }
]

const indicators = [
  {
    value: '99,9%',
    label: 'Disponibilidade',
    detail: 'Continuidade operacional',
    icon: 'check'
  },
  {
    value: '< 5 min',
    label: 'Detecção média',
    detail: 'Agilidade na identificação',
    icon: 'clock'
  },
  {
    value: '150+',
    label: 'Fontes integradas',
    detail: 'Informações conectadas',
    icon: 'layers'
  },
  {
    value: '24/7',
    label: 'Monitoramento',
    detail: 'Acompanhamento contínuo',
    icon: 'activity'
  }
]

let observer

function ativarPonto(point) {
  activePoint.value = point
}

function desativarPonto() {
  activePoint.value = null
}

onMounted(() => {
  if (!chartSection.value) return

  observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        chartVisible.value = true
        observer.disconnect()
      }
    },
    { threshold: 0.2 }
  )

  observer.observe(chartSection.value)
})

onUnmounted(() => {
  observer?.disconnect()
})
</script>

<template>
  <main class="min-h-screen w-full overflow-x-hidden bg-white font-sans text-[#252126]">
    <Header />

    <section id="inicio" class="relative isolate overflow-hidden px-6 pb-20 pt-20 sm:px-10 sm:pb-28 sm:pt-28 lg:px-16 lg:pb-32 lg:pt-32">
      <div class="pointer-events-none absolute -right-40 -top-32 -z-10 h-[30rem] w-[30rem] rounded-full bg-[#FFBE55]/10 blur-3xl"></div>
      <div class="pointer-events-none absolute -left-48 top-40 -z-10 h-[28rem] w-[28rem] rounded-full bg-[#C73A68]/10 blur-3xl"></div>
      <div class="pointer-events-none absolute right-16 top-32 -z-10 hidden h-40 w-40 rounded-full border border-[#C73A68]/10 lg:block"></div>
      <div class="pointer-events-none absolute right-24 top-40 -z-10 hidden h-24 w-24 rounded-full border border-[#FF8A4C]/20 lg:block"></div>

      <div class="mx-auto flex max-w-7xl flex-col items-center gap-14 lg:flex-row lg:items-center lg:gap-16">
        <div class="flex w-full flex-col items-start lg:w-3/5">
          <div class="mb-8 inline-flex items-center gap-3 rounded-full border border-[#C73A68]/15 bg-white/90 px-4 py-3 shadow-sm shadow-[#C73A68]/5">
            <span class="flex h-7 w-7 items-center justify-center rounded-full bg-[#C73A68]/10 text-[#C73A68]">
              <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                <path d="m12 3 1.9 5.8L20 11l-6.1 2.2L12 19l-1.9-5.8L4 11l6.1-2.2L12 3Z" stroke-linejoin="round" />
                <path d="m19 15 .9 2.1L22 18l-2.1.9L19 21l-.9-2.1L16 18l2.1-.9L19 15Z" stroke-linejoin="round" />
              </svg>
            </span>
            <span class="text-xs font-semibold uppercase tracking-[0.18em] text-[#7E475C] sm:text-sm">Nova geração de inteligência</span>
            <span class="h-2 w-2 rounded-full bg-[#FF8A4C]"></span>
          </div>

          <h1 class="max-w-3xl text-5xl font-semibold leading-[1.08] tracking-[-0.055em] sm:text-6xl lg:text-7xl">
            Enxergue os sinais
            <span class="block">por trás do</span>
            <span class="relative inline-block text-[#C73A68]">
              ruído.
              <svg class="absolute -bottom-2 left-0 w-full" viewBox="0 0 220 13" fill="none">
                <path d="M3 8.5C50 2 143 1 216 7" stroke="#FFBE55" stroke-width="5" stroke-linecap="round" />
              </svg>
            </span>
          </h1>

          <p class="mt-8 max-w-2xl text-base leading-8 text-[#77717A] sm:text-lg sm:leading-9">
            Transforme informações complexas em inteligência estratégica. A Makiia conecta dados, revela padrões e ajuda você a tomar decisões com mais clareza.
          </p>

          <div class="mt-10 flex w-full flex-col gap-4 sm:w-auto sm:flex-row sm:items-center">
            <router-link
              to="/cadastro"
              class="group inline-flex min-h-14 items-center justify-center gap-3 rounded-2xl bg-[#C73A68] px-7 py-4 text-sm font-semibold text-white shadow-lg shadow-[#C73A68]/20 transition duration-300 hover:-translate-y-1 hover:bg-[#AD2D59] hover:shadow-xl hover:shadow-[#C73A68]/25"
            >
              Começar agora
              <svg class="h-4 w-4 transition-transform duration-300 group-hover:translate-x-1" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M13 6l6 6-6 6" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </router-link>

            <a
              href="#grafico"
              class="inline-flex min-h-14 items-center justify-center gap-3 rounded-2xl border border-[#E9E3E7] bg-white px-7 py-4 text-sm font-semibold text-[#514852] transition duration-300 hover:border-[#C73A68]/30 hover:bg-[#FFF8FA] hover:text-[#C73A68]"
            >
              Conhecer a plataforma
              <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                <path d="m7 10 5 5 5-5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </a>
          </div>

          <div class="mt-12 flex flex-wrap items-center gap-x-6 gap-y-4 border-t border-[#EEE8EC] pt-7">
            <div class="flex items-center -space-x-2">
              <span class="flex h-9 w-9 items-center justify-center rounded-full border-2 border-white bg-[#F8DCE6] text-[#9D3156]">
                <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="M12 3 5 6v5c0 4.5 3 7.5 7 10 4-2.5 7-5.5 7-10V6l-7-3Z" stroke-linejoin="round" />
                  <path d="m9 12 2 2 4-4" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </span>
              <span class="flex h-9 w-9 items-center justify-center rounded-full border-2 border-white bg-[#FFE9D8] text-[#C16B36]">
                <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="M4 19V5M4 19h17" stroke-linecap="round" />
                  <path d="m7 15 4-4 3 2 5-6" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </span>
              <span class="flex h-9 w-9 items-center justify-center rounded-full border-2 border-white bg-[#FFF1C9] text-[#A87920]">
                <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <circle cx="12" cy="12" r="8" />
                  <path d="M12 8v4l3 2" stroke-linecap="round" />
                </svg>
              </span>
            </div>
            <div>
              <p class="text-sm font-semibold text-[#403840]">Informação que faz a diferença</p>
              <p class="mt-1 text-xs leading-5 text-[#938A92]">Dados organizados. Decisões mais conscientes.</p>
            </div>
          </div>
        </div>

        <div class="relative w-full lg:w-2/5">
          <div class="absolute -right-5 -top-5 h-24 w-24 rounded-full bg-[#FFBE55]/20 blur-2xl"></div>
          <div class="absolute -bottom-8 -left-7 h-32 w-32 rounded-full bg-[#C73A68]/15 blur-2xl"></div>

          <div class="relative rounded-[2rem] border border-[#F0E8ED] bg-white p-4 shadow-2xl shadow-[#6E3D53]/10 sm:p-6">
            <div class="flex items-center justify-between gap-4 border-b border-[#F1EBEF] pb-5">
              <div class="flex items-center gap-3">
                <div class="flex h-11 w-11 items-center justify-center rounded-2xl bg-[#C73A68]/10 text-[#C73A68]">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                    <path d="M4 19V5M4 19h16" stroke-linecap="round" />
                    <path d="m7 15 3-4 3 2 5-7" stroke-linecap="round" stroke-linejoin="round" />
                  </svg>
                </div>
                <div>
                  <p class="text-sm font-semibold text-[#352C34]">Visão estratégica</p>
                  <p class="mt-1 text-xs text-[#968C95]">Painel de inteligência</p>
                </div>
              </div>
              <span class="flex items-center gap-2 rounded-full bg-[#EFF8F0] px-3 py-2 text-xs font-medium text-[#45835A]">
                <span class="h-2 w-2 rounded-full bg-[#5DAE72]"></span>
                Ativo
              </span>
            </div>

            <div class="mt-6 grid grid-cols-2 gap-4">
              <div class="rounded-2xl border border-[#F0E7EC] bg-[#FFFAFC] p-4">
                <div class="flex items-center justify-between gap-2">
                  <span class="text-xs font-medium text-[#8D818B]">Índice de análise</span>
                  <span class="flex h-7 w-7 items-center justify-center rounded-lg bg-[#C73A68]/10 text-[#C73A68]">
                    <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                      <path d="m4 16 5-5 4 3 7-8" stroke-linecap="round" stroke-linejoin="round" />
                      <path d="M15 6h5v5" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                  </span>
                </div>
                <div class="mt-4 flex items-end gap-2">
                  <span class="text-3xl font-semibold tracking-tight text-[#332932]">87,4</span>
                  <span class="mb-1 text-xs font-semibold text-[#559B67]">+12,8%</span>
                </div>
                <div class="mt-4 flex h-10 items-end gap-1.5">
                  <span class="h-4 flex-1 rounded-t-md bg-[#EAC0D0]"></span>
                  <span class="h-6 flex-1 rounded-t-md bg-[#E6A1B9]"></span>
                  <span class="h-5 flex-1 rounded-t-md bg-[#D97D9E]"></span>
                  <span class="h-8 flex-1 rounded-t-md bg-[#D45D87]"></span>
                  <span class="h-6 flex-1 rounded-t-md bg-[#C73A68]"></span>
                  <span class="h-10 flex-1 rounded-t-md bg-[#C73A68]"></span>
                  <span class="h-8 flex-1 rounded-t-md bg-[#D45D87]"></span>
                  <span class="h-10 flex-1 rounded-t-md bg-[#C73A68]"></span>
                </div>
              </div>

              <div class="rounded-2xl border border-[#F0E7EC] bg-white p-4">
                <div class="flex items-center justify-between gap-2">
                  <span class="text-xs font-medium text-[#8D818B]">Sinais detectados</span>
                  <span class="flex h-7 w-7 items-center justify-center rounded-lg bg-[#FF8A4C]/10 text-[#D86D36]">
                    <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                      <path d="M12 3v3M12 18v3M3 12h3M18 12h3M5.6 5.6l2.1 2.1m8.6 8.6 2.1 2.1m0-12.8-2.1 2.1m-8.6 8.6-2.1 2.1" stroke-linecap="round" />
                      <circle cx="12" cy="12" r="4" />
                    </svg>
                  </span>
                </div>
                <p class="mt-4 text-3xl font-semibold tracking-tight text-[#332932]">1.248</p>
                <p class="mt-2 text-xs leading-5 text-[#968C95]">Informações organizadas para análise</p>
                <div class="mt-4 h-2 overflow-hidden rounded-full bg-[#F5EDF1]">
                  <div class="h-full w-4/5 rounded-full bg-gradient-to-r from-[#C73A68] to-[#FF8A4C]"></div>
                </div>
              </div>
            </div>

            <div class="mt-4 rounded-2xl border border-[#F0E7EC] p-4 sm:p-5">
              <div class="flex items-center justify-between gap-3">
                <div>
                  <p class="text-sm font-semibold text-[#403640]">Evolução dos indicadores</p>
                  <p class="mt-1 text-xs text-[#968C95]">Comparativo ilustrativo</p>
                </div>
                <span class="rounded-lg bg-[#FFF2E9] px-3 py-2 text-xs font-semibold text-[#C16B36]">+28,6%</span>
              </div>

              <div class="relative mt-6 h-36">
                <div class="absolute inset-0 flex flex-col justify-between">
                  <div class="border-t border-dashed border-[#ECE5E9]"></div>
                  <div class="border-t border-dashed border-[#ECE5E9]"></div>
                  <div class="border-t border-dashed border-[#ECE5E9]"></div>
                  <div class="border-t border-dashed border-[#ECE5E9]"></div>
                </div>
                <svg class="relative h-full w-full overflow-visible" viewBox="0 0 320 120" preserveAspectRatio="none" fill="none">
                  <defs>
                    <linearGradient id="heroArea" x1="160" y1="10" x2="160" y2="120" gradientUnits="userSpaceOnUse">
                      <stop stop-color="#C73A68" stop-opacity="0.2" />
                      <stop offset="1" stop-color="#C73A68" stop-opacity="0" />
                    </linearGradient>
                  </defs>
                  <path d="M0 100 C24 95 30 78 54 82 S90 92 112 63 S145 75 166 50 S203 62 226 37 S269 44 290 18 L320 12 V120 H0 Z" fill="url(#heroArea)" />
                  <path d="M0 100 C24 95 30 78 54 82 S90 92 112 63 S145 75 166 50 S203 62 226 37 S269 44 290 18 L320 12" stroke="#C73A68" stroke-width="3" stroke-linecap="round" />
                  <circle cx="112" cy="63" r="4" fill="#FF8A4C" stroke="white" stroke-width="2" />
                  <circle cx="226" cy="37" r="4" fill="#FF8A4C" stroke="white" stroke-width="2" />
                  <circle cx="290" cy="18" r="5" fill="#C73A68" stroke="white" stroke-width="2" />
                </svg>
              </div>

              <div class="mt-4 flex items-center justify-between gap-3 text-[10px] font-medium uppercase tracking-[0.12em] text-[#A198A0] sm:text-xs">
                <span>Início</span>
                <span>Análise</span>
                <span>Identificação</span>
                <span>Resultado</span>
              </div>
            </div>

            <div class="mt-4 flex items-center gap-3 rounded-2xl bg-[#FCF8FA] p-4">
              <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-[#FFF0D2] text-[#B27B24]">
                <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="m12 3 2.5 5.5L20 11l-5.5 2.5L12 19l-2.5-5.5L4 11l5.5-2.5L12 3Z" stroke-linejoin="round" />
                </svg>
              </span>
              <div>
                <p class="text-xs font-semibold text-[#453943]">Inteligência em contexto</p>
                <p class="mt-1 text-xs leading-5 text-[#968C95]">Informações conectadas para uma visão mais ampla.</p>
              </div>
              <svg class="ml-auto h-4 w-4 shrink-0 text-[#C73A68]" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                <path d="M5 12h14M13 6l6 6-6 6" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </div>
          </div>

          <div class="absolute -left-6 top-20 hidden items-center gap-3 rounded-2xl border border-[#F0E8ED] bg-white p-4 shadow-xl shadow-[#6E3D53]/10 sm:flex">
            <span class="flex h-10 w-10 items-center justify-center rounded-xl bg-[#EAF6ED] text-[#478C5A]">
              <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                <path d="m5 12 4 4L19 6" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </span>
            <div>
              <p class="text-xs font-semibold text-[#403640]">Padrão identificado</p>
              <p class="mt-1 text-[11px] text-[#968C95]">Análise concluída</p>
            </div>
          </div>

          <div class="absolute -bottom-5 right-5 hidden items-center gap-3 rounded-2xl border border-[#F0E8ED] bg-white p-4 shadow-xl shadow-[#6E3D53]/10 sm:flex">
            <span class="flex h-10 w-10 items-center justify-center rounded-xl bg-[#FFF0D2] text-[#B27B24]">
              <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                <path d="M12 3v18M3 12h18" stroke-linecap="round" />
                <circle cx="12" cy="12" r="8" />
              </svg>
            </span>
            <div>
              <p class="text-xs font-semibold text-[#403640]">Novo contexto</p>
              <p class="mt-1 text-[11px] text-[#968C95]">Informações conectadas</p>
            </div>
          </div>
        </div>
      </div>

      <div class="mx-auto mt-24 flex max-w-7xl flex-col gap-5 border-t border-[#EEE8EC] pt-8 sm:flex-row sm:items-center sm:justify-between">
        <p class="text-xs font-semibold uppercase tracking-[0.2em] text-[#948993]">Menos ruído. Mais clareza. Melhores decisões.</p>
        <div class="flex items-center gap-2">
          <span class="h-2.5 w-8 rounded-full bg-[#C73A68]"></span>
          <span class="h-2.5 w-5 rounded-full bg-[#FF8A4C]"></span>
          <span class="h-2.5 w-3 rounded-full bg-[#FFBE55]"></span>
          <span class="ml-2 text-xs text-[#948993]">Inteligência que conecta possibilidades</span>
        </div>
      </div>
    </section>

    <section class="border-y border-[#F0E9ED] bg-[#FCFAFB] px-6 py-14 sm:px-10 sm:py-16 lg:px-16">
      <div class="mx-auto max-w-7xl">
        <div class="mb-10 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <span class="text-xs font-semibold uppercase tracking-[0.2em] text-[#C73A68]">Visão em números</span>
            <h2 class="mt-3 text-2xl font-semibold tracking-tight text-[#302730] sm:text-3xl">Informação que trabalha com você.</h2>
          </div>
          <p class="max-w-md text-sm leading-7 text-[#857B85]">Uma visão organizada para acompanhar indicadores importantes e entender melhor o cenário da sua operação.</p>
        </div>

        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
          <article
            v-for="(item, index) in indicators"
            :key="item.label"
            class="group rounded-2xl border border-[#EEE5EA] bg-white p-6 transition duration-300 hover:-translate-y-1 hover:border-[#C73A68]/25 hover:shadow-xl hover:shadow-[#6E3D53]/5 sm:p-7"
          >
            <div class="flex items-center justify-between gap-3">
              <span class="flex h-11 w-11 items-center justify-center rounded-2xl transition duration-300 group-hover:scale-105" :class="index === 0 ? 'bg-[#F9E5ED] text-[#C73A68]' : index === 1 ? 'bg-[#FFE9DC] text-[#D86D36]' : index === 2 ? 'bg-[#FFF2D6] text-[#B27B24]' : 'bg-[#F9E5ED] text-[#C73A68]'">
                <svg v-if="item.icon === 'check'" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="m5 12 4 4L19 6" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
                <svg v-else-if="item.icon === 'clock'" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <circle cx="12" cy="12" r="9" />
                  <path d="M12 7v5l3 2" stroke-linecap="round" />
                </svg>
                <svg v-else-if="item.icon === 'layers'" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="m12 3 9 5-9 5-9-5 9-5Z" stroke-linejoin="round" />
                  <path d="m3 12 9 5 9-5M3 16l9 5 9-5" stroke-linejoin="round" />
                </svg>
                <svg v-else class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="M3 12h4l3-8 4 16 3-8h4" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </span>
              <span class="text-xs font-medium text-[#A79AA4]">{{ item.label }}</span>
            </div>
            <p class="mt-7 text-3xl font-semibold tracking-tight text-[#302730] sm:text-4xl">{{ item.value }}</p>
            <p class="mt-3 text-xs leading-5 text-[#918691]">{{ item.detail }}</p>
            <div class="mt-6 h-1 overflow-hidden rounded-full bg-[#F3EDF1]">
              <div class="h-full rounded-full transition-all duration-700 group-hover:w-full" :class="index === 0 ? 'w-[88%] bg-[#C73A68]' : index === 1 ? 'w-[68%] bg-[#FF8A4C]' : index === 2 ? 'w-[80%] bg-[#FFBE55]' : 'w-[94%] bg-[#C73A68]'"></div>
            </div>
          </article>
        </div>
        <p class="mt-5 text-xs leading-5 text-[#A198A0]">Indicadores ilustrativos para demonstrar a apresentação de informações na plataforma.</p>
      </div>
    </section>

    <section id="plataforma" class="relative overflow-hidden px-6 py-20 sm:px-10 sm:py-28 lg:px-16 lg:py-32">
      <div class="pointer-events-none absolute -right-32 top-20 h-72 w-72 rounded-full bg-[#FFBE55]/10 blur-3xl"></div>
      <div class="mx-auto max-w-7xl">
        <div class="mx-auto flex max-w-3xl flex-col items-center text-center">
          <span class="inline-flex items-center gap-2 rounded-full border border-[#C73A68]/15 bg-[#FFF8FA] px-4 py-2.5 text-xs font-semibold uppercase tracking-[0.18em] text-[#C73A68]">
            <span class="h-2 w-2 rounded-full bg-[#FF8A4C]"></span>
            Uma plataforma, novas possibilidades
          </span>
          <h2 class="mt-6 text-3xl font-semibold leading-tight tracking-[-0.04em] text-[#302730] sm:text-4xl lg:text-5xl">
            Tudo o que você precisa
            <span class="block text-[#C73A68]">em um só lugar.</span>
          </h2>
          <p class="mt-6 max-w-2xl text-base leading-8 text-[#857B85]">
            Conecte informações, acompanhe mudanças e transforme dados em conhecimento útil para o dia a dia da sua operação.
          </p>
        </div>

        <div class="mt-16 grid grid-cols-1 gap-6 md:grid-cols-3 lg:mt-20">
          <article
            v-for="(feature, index) in platformFeatures"
            :key="feature.number"
            class="group relative flex flex-col overflow-hidden rounded-[1.75rem] border border-[#EEE5EA] bg-white p-7 transition duration-300 hover:-translate-y-2 hover:border-[#C73A68]/25 hover:shadow-2xl hover:shadow-[#6E3D53]/10 sm:p-8 lg:p-9"
          >
            <div class="absolute right-0 top-0 h-32 w-32 rounded-bl-full transition duration-500 group-hover:scale-125" :class="index === 0 ? 'bg-[#F9E5ED]/60' : index === 1 ? 'bg-[#FFE9DC]/60' : 'bg-[#FFF2D6]/70'"></div>

            <div class="relative flex items-start justify-between gap-4">
              <span class="flex h-14 w-14 items-center justify-center rounded-2xl transition duration-300 group-hover:rotate-3 group-hover:scale-105" :class="index === 0 ? 'bg-[#F9E5ED] text-[#C73A68]' : index === 1 ? 'bg-[#FFE9DC] text-[#D86D36]' : 'bg-[#FFF2D6] text-[#B27B24]'">
                <svg v-if="feature.icon === 'chart'" class="h-7 w-7" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7">
                  <path d="M4 19V5M4 19h16" stroke-linecap="round" />
                  <rect x="7" y="12" width="3" height="4" rx="1" />
                  <rect x="12" y="8" width="3" height="8" rx="1" />
                  <rect x="17" y="4" width="3" height="12" rx="1" />
                </svg>
                <svg v-else-if="feature.icon === 'shield'" class="h-7 w-7" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7">
                  <path d="M12 3 5 6v5c0 4.5 3 7.5 7 10 4-2.5 7-5.5 7-10V6l-7-3Z" stroke-linejoin="round" />
                  <path d="m9 12 2 2 4-4" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
                <svg v-else class="h-7 w-7" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7">
                  <path d="M3 12h4l3-8 4 16 3-8h4" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </span>
              <span class="relative pt-1 text-sm font-semibold tracking-[0.12em] text-[#C9BFC7]">{{ feature.number }}</span>
            </div>

            <span class="relative mt-8 w-fit rounded-full px-3 py-2 text-xs font-semibold" :class="index === 0 ? 'bg-[#FFF2F6] text-[#B43C64]' : index === 1 ? 'bg-[#FFF2E9] text-[#C16B36]' : 'bg-[#FFF7E3] text-[#A87920]'">{{ feature.tag }}</span>
            <h3 class="relative mt-5 text-xl font-semibold tracking-tight text-[#352C34] sm:text-2xl">{{ feature.title }}</h3>
            <p class="relative mt-4 flex-1 text-sm leading-7 text-[#857B85]">{{ feature.description }}</p>

            <div class="relative mt-8 flex items-center justify-between border-t border-[#F0E9ED] pt-5">
              <span class="text-xs font-medium text-[#9B9099]">Explore possibilidades</span>
              <span class="flex h-9 w-9 items-center justify-center rounded-full border border-[#EEE5EA] text-[#C73A68] transition duration-300 group-hover:border-[#C73A68] group-hover:bg-[#C73A68] group-hover:text-white">
                <svg class="h-4 w-4 transition-transform duration-300 group-hover:translate-x-0.5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="M5 12h14M13 6l6 6-6 6" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </span>
            </div>
          </article>
        </div>

        <div class="mt-10 flex flex-col gap-4 rounded-3xl border border-[#F0E5EB] bg-gradient-to-r from-[#FFF7FA] via-white to-[#FFF9F0] p-6 sm:flex-row sm:items-center sm:justify-between sm:p-8">
          <div class="flex items-start gap-4">
            <span class="flex h-12 w-12 shrink-0 items-center justify-center rounded-2xl bg-white text-[#C73A68] shadow-sm shadow-[#6E3D53]/5">
              <svg class="h-6 w-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7">
                <path d="m12 3 1.9 5.8L20 11l-6.1 2.2L12 19l-1.9-5.8L4 11l6.1-2.2L12 3Z" stroke-linejoin="round" />
              </svg>
            </span>
            <div>
              <h3 class="text-base font-semibold text-[#403640]">Inteligência conectada ao seu contexto</h3>
              <p class="mt-2 max-w-2xl text-sm leading-6 text-[#857B85]">Mais do que reunir informações, o objetivo é ajudar você a compreender relações e enxergar novas possibilidades.</p>
            </div>
          </div>
          <a href="#recursos" class="inline-flex shrink-0 items-center gap-2 text-sm font-semibold text-[#C73A68] transition hover:gap-3">
            Conhecer recursos
            <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
              <path d="M5 12h14M13 6l6 6-6 6" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </a>
        </div>
      </div>
    </section>

    <section id="grafico" ref="chartSection" class="relative overflow-hidden border-y border-[#F0E9ED] bg-[#FCFAFB] px-6 py-20 sm:px-10 sm:py-28 lg:px-16 lg:py-32">
      <div class="pointer-events-none absolute -left-32 top-10 h-80 w-80 rounded-full bg-[#C73A68]/5 blur-3xl"></div>
      <div class="pointer-events-none absolute -right-24 bottom-0 h-72 w-72 rounded-full bg-[#FFBE55]/10 blur-3xl"></div>

      <div class="relative mx-auto max-w-7xl">
        <div class="grid grid-cols-1 gap-12 lg:grid-cols-2 lg:items-end lg:gap-16">
          <div>
            <span class="inline-flex items-center gap-2 rounded-full border border-[#C73A68]/15 bg-white px-4 py-2.5 text-xs font-semibold uppercase tracking-[0.18em] text-[#C73A68]">
              <span class="h-2 w-2 rounded-full bg-[#FF8A4C]"></span>
              Dados em movimento
            </span>
            <h2 class="mt-6 text-3xl font-semibold leading-tight tracking-[-0.04em] text-[#302730] sm:text-4xl lg:text-5xl">
              Veja os dados
              <span class="block text-[#C73A68]">evoluírem.</span>
            </h2>
            <p class="mt-6 max-w-xl text-base leading-8 text-[#857B85]">
              Uma boa análise não termina em números. Ela conecta etapas, revela relações e transforma observações em caminhos possíveis.
            </p>

            <div class="mt-8 flex flex-col gap-4">
              <div class="flex items-start gap-4 rounded-2xl border border-[#EEE5EA] bg-white p-5">
                <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-[#F9E5ED] text-[#C73A68]">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                    <circle cx="11" cy="11" r="7" />
                    <path d="m16 16 4 4M8 11h6M11 8v6" stroke-linecap="round" />
                  </svg>
                </span>
                <div>
                  <h3 class="text-sm font-semibold text-[#403640]">Identifique padrões</h3>
                  <p class="mt-2 text-sm leading-6 text-[#918691]">Encontre relações importantes entre diferentes pontos da análise.</p>
                </div>
              </div>

              <div class="flex items-start gap-4 rounded-2xl border border-[#EEE5EA] bg-white p-5">
                <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-[#FFF0E6] text-[#D86D36]">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                    <path d="M12 3v18M3 12h18" stroke-linecap="round" />
                    <circle cx="12" cy="12" r="8" />
                  </svg>
                </span>
                <div>
                  <h3 class="text-sm font-semibold text-[#403640]">Entenda o contexto</h3>
                  <p class="mt-2 text-sm leading-6 text-[#918691]">Observe como as informações se relacionam ao longo do processo.</p>
                </div>
              </div>

              <div class="flex items-start gap-4 rounded-2xl border border-[#EEE5EA] bg-white p-5">
                <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-[#FFF4D9] text-[#B27B24]">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                    <path d="m5 12 4 4L19 6" stroke-linecap="round" stroke-linejoin="round" />
                  </svg>
                </span>
                <div>
                  <h3 class="text-sm font-semibold text-[#403640]">Apoie decisões</h3>
                  <p class="mt-2 text-sm leading-6 text-[#918691]">Use uma visão organizada para avaliar alternativas com mais clareza.</p>
                </div>
              </div>
            </div>
          </div>

          <div class="relative">
            <div class="absolute -inset-4 rounded-[2rem] bg-gradient-to-br from-[#C73A68]/10 via-transparent to-[#FFBE55]/15 blur-xl"></div>
            <div class="relative overflow-hidden rounded-[2rem] border border-[#EEE5EA] bg-white p-5 shadow-xl shadow-[#6E3D53]/5 sm:p-8 lg:p-9">
              <div class="flex flex-col gap-5 border-b border-[#F0E9ED] pb-6 sm:flex-row sm:items-start sm:justify-between">
                <div>
                  <span class="text-xs font-semibold uppercase tracking-[0.16em] text-[#C73A68]">Painel de evolução</span>
                  <h3 class="mt-3 text-xl font-semibold tracking-tight text-[#352C34]">Jornada da inteligência</h3>
                  <p class="mt-2 text-sm leading-6 text-[#968C95]">Selecione um ponto para explorar a etapa.</p>
                </div>
                <span class="flex w-fit items-center gap-2 rounded-full bg-[#EFF8F0] px-3 py-2 text-xs font-semibold text-[#45835A]">
                  <span class="h-2 w-2 rounded-full bg-[#5DAE72]"></span>
                  Demonstração
                </span>
              </div>

              <div class="mt-8">
                <div class="mb-4 flex items-center justify-between text-xs text-[#A198A0]">
                  <span>Nível ilustrativo</span>
                  <span>0 — 100</span>
                </div>

                <div class="relative">
                  <div class="pointer-events-none absolute inset-0 flex flex-col justify-between pb-10">
                    <div class="border-t border-dashed border-[#EEE5EA]"></div>
                    <div class="border-t border-dashed border-[#EEE5EA]"></div>
                    <div class="border-t border-dashed border-[#EEE5EA]"></div>
                    <div class="border-t border-dashed border-[#EEE5EA]"></div>
                  </div>

                  <div class="relative flex h-64 items-end justify-around gap-2 px-1 sm:gap-4 sm:px-3">
                    <button
                      v-for="(point, index) in graphPoints"
                      :key="point.id"
                      type="button"
                      class="group relative flex h-full min-w-0 flex-1 flex-col items-center justify-end gap-3 rounded-xl px-1 pt-2 outline-none focus-visible:ring-2 focus-visible:ring-[#C73A68] focus-visible:ring-offset-2"
                      :aria-label="`${point.title}: ${point.value}`"
                      @mouseenter="ativarPonto(point)"
                      @mouseleave="desativarPonto"
                      @focus="ativarPonto(point)"
                      @blur="desativarPonto"
                      @click="activePoint = activePoint?.id === point.id ? null : point"
                    >
                      <span
                        class="z-10 rounded-lg border border-[#F0E5EB] bg-white px-2 py-1.5 text-[10px] font-semibold text-[#716571] shadow-sm transition duration-300 group-hover:-translate-y-1 group-hover:border-[#C73A68]/20 group-hover:text-[#C73A68] sm:text-xs"
                        :class="activePoint?.id === point.id ? 'border-[#C73A68]/30 text-[#C73A68]' : ''"
                      >
                        {{ point.value }}
                      </span>

                      <div class="relative flex h-44 w-full items-end justify-center">
                        <div
                          class="relative w-full max-w-12 overflow-hidden rounded-t-xl transition-all duration-1000 ease-out sm:max-w-16"
                          :class="activePoint?.id === point.id ? 'ring-2 ring-[#C73A68]/20 ring-offset-2' : ''"
                          :style="{
                            height: chartVisible ? `${point.value}%` : '0%',
                            background: `linear-gradient(to top, ${point.color}, ${point.color}99)`
                          }"
                        >
                          <div class="absolute inset-x-0 top-0 h-1 bg-white/40"></div>
                          <div class="absolute inset-x-0 bottom-0 h-1/3 bg-white/10"></div>
                        </div>
                      </div>

                      <span class="pb-1 text-[10px] font-semibold uppercase tracking-[0.1em] text-[#A198A0] transition group-hover:text-[#C73A68] sm:text-xs">{{ point.number }}</span>
                    </button>
                  </div>
                </div>
              </div>

              <div class="mt-6 min-h-28 rounded-2xl border border-[#F0E5EB] bg-[#FCF8FA] p-5 transition duration-300 sm:p-6">
                <div v-if="activePoint" class="flex items-start gap-4">
                  <span class="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl text-sm font-semibold text-white" :style="{ backgroundColor: activePoint.color }">{{ activePoint.number }}</span>
                  <div>
                    <h4 class="text-sm font-semibold text-[#403640]">{{ activePoint.title }}</h4>
                    <p class="mt-2 text-sm leading-6 text-[#857B85]">{{ activePoint.benefit }}</p>
                  </div>
                </div>
                <div v-else class="flex items-start gap-4">
                  <span class="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-[#F9E5ED] text-[#C73A68]">
                    <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                      <path d="m12 3 1.9 5.8L20 11l-6.1 2.2L12 19l-1.9-5.8L4 11l6.1-2.2L12 3Z" stroke-linejoin="round" />
                    </svg>
                  </span>
                  <div>
                    <h4 class="text-sm font-semibold text-[#403640]">Cada etapa revela uma possibilidade</h4>
                    <p class="mt-2 text-sm leading-6 text-[#857B85]">Passe o cursor, toque ou selecione uma barra para entender cada parte da jornada.</p>
                  </div>
                </div>
              </div>

              <p class="mt-5 text-xs leading-5 text-[#A198A0]">Valores e etapas demonstrativos, utilizados para ilustrar a experiência de visualização.</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section id="recursos" class="px-6 py-20 sm:px-10 sm:py-28 lg:px-16 lg:py-32">
      <div class="mx-auto max-w-7xl">
        <div class="flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between">
          <div class="max-w-2xl">
            <span class="inline-flex items-center gap-2 rounded-full border border-[#C73A68]/15 bg-[#FFF8FA] px-4 py-2.5 text-xs font-semibold uppercase tracking-[0.18em] text-[#C73A68]">
              Recursos da plataforma
            </span>
            <h2 class="mt-6 text-3xl font-semibold leading-tight tracking-[-0.04em] text-[#302730] sm:text-4xl lg:text-5xl">
              Clareza para compreender.
              <span class="block text-[#C73A68]">Contexto para decidir.</span>
            </h2>
          </div>
          <p class="max-w-md text-sm leading-7 text-[#857B85] sm:text-base sm:leading-8">
            Visualizações intuitivas e informações conectadas ajudam a transformar grandes volumes de dados em uma experiência mais clara.
          </p>
        </div>

        <div class="mt-14 grid grid-cols-1 gap-6 lg:mt-16 lg:grid-cols-2 lg:gap-8">
          <article class="group relative overflow-hidden rounded-[2rem] border border-[#EEE5EA] bg-[#FCFAFB] p-7 transition duration-300 hover:border-[#C73A68]/20 hover:shadow-xl hover:shadow-[#6E3D53]/5 sm:p-9 lg:p-10">
            <div class="absolute -right-12 -top-12 h-48 w-48 rounded-full bg-[#C73A68]/5 transition duration-500 group-hover:scale-125"></div>

            <div class="relative flex items-center justify-between gap-4">
              <span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-[#F9E5ED] text-[#C73A68]">
                <svg class="h-6 w-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="M4 19V5M4 19h16" stroke-linecap="round" />
                  <path d="m7 15 3-4 3 2 5-7" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </span>
              <span class="rounded-full border border-[#EAD9E1] bg-white px-3 py-2 text-xs font-medium text-[#9A6A7D]">Visualização</span>
            </div>

            <h3 class="relative mt-8 text-2xl font-semibold tracking-tight text-[#352C34] sm:text-3xl">Encontre padrões onde antes havia ruído.</h3>
            <p class="relative mt-4 max-w-lg text-sm leading-7 text-[#857B85]">
              Organize informações em representações visuais que facilitam a leitura dos indicadores e a identificação de mudanças relevantes.
            </p>

            <div class="relative mt-9 rounded-2xl border border-[#EEE5EA] bg-white p-5 sm:p-6">
              <div class="flex items-center justify-between gap-3">
                <div>
                  <p class="text-sm font-semibold text-[#403640]">Distribuição de indicadores</p>
                  <p class="mt-1 text-xs text-[#A198A0]">Exemplo de visualização</p>
                </div>
                <span class="flex h-8 w-8 items-center justify-center rounded-xl bg-[#FFF2F6] text-[#C73A68]">
                  <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                    <circle cx="12" cy="12" r="9" />
                    <path d="M12 3v9h9" />
                  </svg>
                </span>
              </div>

              <div class="mt-6 flex flex-col gap-5">
                <div>
                  <div class="mb-2 flex items-center justify-between gap-3 text-xs">
                    <span class="font-medium text-[#716571]">Análise de dados</span>
                    <span class="font-semibold text-[#C73A68]">78%</span>
                  </div>
                  <div class="h-2.5 overflow-hidden rounded-full bg-[#F4EDF1]">
                    <div class="h-full w-[78%] rounded-full bg-[#C73A68] transition-all duration-700 group-hover:w-[84%]"></div>
                  </div>
                </div>
                <div>
                  <div class="mb-2 flex items-center justify-between gap-3 text-xs">
                    <span class="font-medium text-[#716571]">Identificação de padrões</span>
                    <span class="font-semibold text-[#D86D36]">62%</span>
                  </div>
                  <div class="h-2.5 overflow-hidden rounded-full bg-[#F4EDF1]">
                    <div class="h-full w-[62%] rounded-full bg-[#FF8A4C] transition-all duration-700 group-hover:w-[72%]"></div>
                  </div>
                </div>
                <div>
                  <div class="mb-2 flex items-center justify-between gap-3 text-xs">
                    <span class="font-medium text-[#716571]">Contextualização</span>
                    <span class="font-semibold text-[#B27B24]">91%</span>
                  </div>
                  <div class="h-2.5 overflow-hidden rounded-full bg-[#F4EDF1]">
                    <div class="h-full w-[91%] rounded-full bg-[#FFBE55] transition-all duration-700 group-hover:w-[96%]"></div>
                  </div>
                </div>
              </div>
            </div>
          </article>

          <article class="group relative overflow-hidden rounded-[2rem] border border-[#EEE5EA] bg-[#FCFAFB] p-7 transition duration-300 hover:border-[#FF8A4C]/30 hover:shadow-xl hover:shadow-[#6E3D53]/5 sm:p-9 lg:p-10">
            <div class="absolute -right-12 -top-12 h-48 w-48 rounded-full bg-[#FFBE55]/10 transition duration-500 group-hover:scale-125"></div>

            <div class="relative flex items-center justify-between gap-4">
              <span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-[#FFF0E6] text-[#D86D36]">
                <svg class="h-6 w-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="M12 3v18M3 12h18" stroke-linecap="round" />
                  <circle cx="12" cy="12" r="8" />
                  <circle cx="12" cy="12" r="3" />
                </svg>
              </span>
              <span class="rounded-full border border-[#F3DFD2] bg-white px-3 py-2 text-xs font-medium text-[#B97750]">Inteligência</span>
            </div>

            <h3 class="relative mt-8 text-2xl font-semibold tracking-tight text-[#352C34] sm:text-3xl">Decisões baseadas em contexto.</h3>
            <p class="relative mt-4 max-w-lg text-sm leading-7 text-[#857B85]">
              Vá além de informações isoladas. Relacione sinais, acompanhe tendências e construa uma visão mais completa do cenário analisado.
            </p>

            <div class="relative mt-9 overflow-hidden rounded-2xl border border-[#EEE5EA] bg-white p-5 sm:p-6">
              <div class="flex items-center justify-between gap-3">
                <div>
                  <p class="text-sm font-semibold text-[#403640]">Conexão de informações</p>
                  <p class="mt-1 text-xs text-[#A198A0]">Representação conceitual</p>
                </div>
                <span class="flex h-8 w-8 items-center justify-center rounded-xl bg-[#FFF4D9] text-[#B27B24]">
                  <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                    <circle cx="12" cy="12" r="3" />
                    <circle cx="5" cy="5" r="2" />
                    <circle cx="19" cy="5" r="2" />
                    <circle cx="5" cy="19" r="2" />
                    <circle cx="19" cy="19" r="2" />
                    <path d="m7 6 3 4m7-4-3 4m-7 7 3-4m7 4-3-4" stroke-linecap="round" />
                  </svg>
                </span>
              </div>

              <div class="relative mt-6 flex min-h-48 items-center justify-center">
                <div class="absolute h-36 w-36 rounded-full border border-dashed border-[#EAD8E1]"></div>
                <div class="absolute h-28 w-28 rounded-full border border-[#F0E5EA]"></div>
                <div class="absolute left-[18%] top-[15%] h-3 w-3 rounded-full bg-[#C73A68] shadow-md shadow-[#C73A68]/20"></div>
                <div class="absolute right-[19%] top-[24%] h-3 w-3 rounded-full bg-[#FF8A4C] shadow-md shadow-[#FF8A4C]/20"></div>
                <div class="absolute bottom-[17%] left-[25%] h-3 w-3 rounded-full bg-[#FFBE55] shadow-md shadow-[#FFBE55]/20"></div>
                <div class="absolute bottom-[12%] right-[24%] h-3 w-3 rounded-full bg-[#C73A68] shadow-md shadow-[#C73A68]/20"></div>
                <div class="absolute left-[25%] top-[30%] h-px w-[22%] rotate-[25deg] bg-[#E8C8D5]"></div>
                <div class="absolute right-[25%] top-[38%] h-px w-[22%] -rotate-[25deg] bg-[#F2D2C1]"></div>
                <div class="absolute bottom-[27%] left-[30%] h-px w-[20%] -rotate-[25deg] bg-[#EADBB8]"></div>
                <div class="absolute bottom-[23%] right-[29%] h-px w-[20%] rotate-[25deg] bg-[#E8C8D5]"></div>
                <div class="relative flex h-20 w-20 items-center justify-center rounded-3xl border border-[#F0D8E2] bg-gradient-to-br from-[#FFF5F8] to-white text-[#C73A68] shadow-lg shadow-[#C73A68]/10 transition duration-500 group-hover:rotate-6 group-hover:scale-105">
                  <svg class="h-9 w-9" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                    <path d="m12 3 1.9 5.8L20 11l-6.1 2.2L12 19l-1.9-5.8L4 11l6.1-2.2L12 3Z" stroke-linejoin="round" />
                    <path d="m19 15 .9 2.1L22 18l-2.1.9L19 21l-.9-2.1L16 18l2.1-.9L19 15Z" stroke-linejoin="round" />
                  </svg>
                </div>
              </div>

              <div class="flex flex-wrap justify-center gap-2">
                <span class="rounded-full bg-[#FFF2F6] px-3 py-2 text-xs font-medium text-[#B43C64]">Dados</span>
                <span class="rounded-full bg-[#FFF2E9] px-3 py-2 text-xs font-medium text-[#C16B36]">Contexto</span>
                <span class="rounded-full bg-[#FFF7E3] px-3 py-2 text-xs font-medium text-[#A87920]">Decisão</span>
              </div>
            </div>
          </article>
        </div>
      </div>
    </section>

    <section id="sobre" class="relative overflow-hidden px-6 pb-20 sm:px-10 sm:pb-28 lg:px-16 lg:pb-32">
      <div class="mx-auto max-w-7xl">
        <div class="relative overflow-hidden rounded-[2rem] bg-[#2D202B] px-7 py-12 sm:px-12 sm:py-16 lg:px-16 lg:py-20">
          <div class="pointer-events-none absolute -right-24 -top-32 h-96 w-96 rounded-full border border-white/10"></div>
          <div class="pointer-events-none absolute -right-12 -top-20 h-72 w-72 rounded-full border border-white/10"></div>
          <div class="pointer-events-none absolute -bottom-32 left-1/3 h-72 w-72 rounded-full bg-[#C73A68]/20 blur-3xl"></div>
          <div class="pointer-events-none absolute right-1/4 top-0 h-48 w-48 rounded-full bg-[#FF8A4C]/10 blur-3xl"></div>

          <div class="relative grid grid-cols-1 gap-12 lg:grid-cols-[1fr_auto] lg:items-center lg:gap-16">
            <div class="max-w-3xl">
              <span class="inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/5 px-4 py-2.5 text-xs font-semibold uppercase tracking-[0.18em] text-[#F4B6CB]">
                <span class="h-2 w-2 rounded-full bg-[#FFBE55]"></span>
                O próximo passo começa aqui
              </span>

              <h2 class="mt-7 text-3xl font-semibold leading-tight tracking-[-0.04em] text-white sm:text-4xl lg:text-5xl">
                Pronto para elevar
                <span class="block text-[#F2A1BC]">sua inteligência?</span>
              </h2>

              <p class="mt-6 max-w-2xl text-sm leading-8 text-[#D0C2CD] sm:text-base">
                Descubra uma nova forma de compreender informações, conectar possibilidades e explorar decisões com mais clareza.
              </p>

              <div class="mt-8 flex flex-wrap gap-3">
                <span class="inline-flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-4 py-2.5 text-xs text-[#E8DDE5]">
                  <span class="h-2 w-2 rounded-full bg-[#C73A68]"></span>
                  Análise inteligente
                </span>
                <span class="inline-flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-4 py-2.5 text-xs text-[#E8DDE5]">
                  <span class="h-2 w-2 rounded-full bg-[#FF8A4C]"></span>
                  Visão estratégica
                </span>
                <span class="inline-flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-4 py-2.5 text-xs text-[#E8DDE5]">
                  <span class="h-2 w-2 rounded-full bg-[#FFBE55]"></span>
                  Novas possibilidades
                </span>
              </div>
            </div>

            <div class="flex flex-col gap-4 lg:min-w-56">
              <router-link
                to="/cadastro"
                class="group inline-flex min-h-14 items-center justify-center gap-3 rounded-2xl bg-[#C73A68] px-7 py-4 text-sm font-semibold text-white shadow-lg shadow-black/10 transition duration-300 hover:-translate-y-1 hover:bg-[#DA4C7B]"
              >
                Criar minha conta
                <svg class="h-4 w-4 transition-transform duration-300 group-hover:translate-x-1" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M5 12h14M13 6l6 6-6 6" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </router-link>
              <a href="#inicio" class="inline-flex min-h-12 items-center justify-center gap-2 rounded-2xl border border-white/15 px-6 py-3 text-sm font-medium text-[#E8DDE5] transition hover:border-white/30 hover:bg-white/5">
                Voltar ao início
                <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <path d="m7 14 5-5 5 5" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </a>
            </div>
          </div>

          <div class="relative mt-12 border-t border-white/10 pt-6 sm:mt-16 sm:pt-8">
            <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
              <p class="text-xs leading-6 text-[#BFB0BD]">Transformando complexidade em clareza, um dado de cada vez.</p>
              <div class="flex items-center gap-2">
                <span class="h-2.5 w-8 rounded-full bg-[#C73A68]"></span>
                <span class="h-2.5 w-5 rounded-full bg-[#FF8A4C]"></span>
                <span class="h-2.5 w-3 rounded-full bg-[#FFBE55]"></span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <Footer />
  </main>
</template>
