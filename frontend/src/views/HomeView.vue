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
    benefit: 'Acompanhe os principais indicadores da sua operação em tempo real.'
  },
  {
    id: 2,
    value: 42,
    number: '02',
    title: 'Análise',
    benefit: 'Encontre padrões e relações importantes entre os seus dados.'
  },
  {
    id: 3,
    value: 36,
    number: '03',
    title: 'Prevenção',
    benefit: 'Identifique sinais de risco antes que eles evoluam para problemas.'
  },
  {
    id: 4,
    value: 64,
    number: '04',
    title: 'Inteligência',
    benefit: 'Transforme dados dispersos em contexto para sua operação.'
  },
  {
    id: 5,
    value: 88,
    number: '05',
    title: 'Decisão',
    benefit: 'Use informações organizadas para apoiar decisões estratégicas.'
  }
]

function ativarPonto(point) {
  activePoint.value = point
}

function desativarPonto() {
  activePoint.value = null
}

let observer

onMounted(() => {
  observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        chartVisible.value = true
        observer.disconnect()
      }
    },
    { threshold: 0.35 }
  )

  if (chartSection.value) observer.observe(chartSection.value)
})

onUnmounted(() => {
  if (observer) observer.disconnect()
})
</script>

<template>
  <main class="min-h-screen w-full overflow-x-hidden bg-[#010300] text-white">
    <Header />

    <section
      id="inicio"
      class="relative flex min-h-[calc(100vh-1px)] w-full items-center justify-center overflow-hidden"
    >
      <img
        src="../assets/images/HomeBackground.gif"
        alt=""
        aria-hidden="true"
        class="absolute inset-0 h-full w-full object-cover object-center"
      />
      <div class="absolute inset-0 bg-[#010300]/80"></div>
      <div
        class="absolute inset-0 bg-[radial-gradient(circle_at_center,rgba(187,222,198,0.16),transparent_42%)]"
      ></div>
      <div
        class="absolute inset-0 bg-[radial-gradient(circle_at_50%_100%,rgba(91,139,117,0.14),transparent_40%)]"
      ></div>

      <div
        class="relative z-10 mx-auto flex w-full max-w-4xl flex-col items-center px-6 py-28 text-center sm:px-8 sm:py-32 lg:py-24"
      >
        <div
          class="inline-flex items-center gap-2 rounded-full border border-[#bbdec6]/20 bg-black/30 px-5 py-2.5 backdrop-blur-xl"
        >
          <span
            class="h-1.5 w-1.5 rounded-full bg-[#bbdec6] shadow-[0_0_1rem_rgba(187,222,198,0.9)]"
          ></span>
          <span class="text-[10px] font-medium uppercase tracking-[0.22em] text-[#bbdec6]/80">
            Nova geração de inteligência
          </span>
        </div>

        <h1
          class="mt-12 max-w-4xl text-5xl font-medium leading-[0.98] tracking-[-0.055em] sm:text-6xl md:text-7xl lg:text-[5.5rem]"
        >
          Enxergue os sinais
          <span class="mt-5 block text-[#bbdec6] sm:mt-6">
            por trás do ruído.
          </span>
        </h1>

        <p class="mt-12 max-w-2xl text-sm leading-7 tracking-wide text-white/45 sm:mt-11 sm:text-base sm:leading-8">
          Transforme dados em inteligência, acompanhe indicadores
          e identifique padrões para tomar decisões com mais contexto.
        </p>

        <div class="mt-16 flex w-full max-w-xl flex-col items-center justify-center gap-4 sm:flex-row sm:gap-5">
          <router-link
            to="/login"
            class="flex h-16 w-full items-center justify-center gap-4 rounded-xl bg-[#bbdec6] px-12 text-base font-semibold tracking-wide text-[#010300] transition duration-300 hover:-translate-y-0.5 hover:bg-white hover:shadow-[0_0_2.5rem_rgba(187,222,198,0.18)] sm:w-auto"
          >
            Explorar plataforma
            <span class="text-lg">→</span>
          </router-link>
          <a
            href="#grafico"
            class="flex h-16 w-full items-center justify-center gap-4 rounded-xl border border-white/10 bg-white/[0.035] px-12 text-base tracking-wide text-white/70 backdrop-blur-md transition duration-300 hover:-translate-y-0.5 hover:border-[#bbdec6]/20 hover:bg-white/[0.07] hover:text-white sm:w-auto"
          >
            Ver como funciona
            <span class="text-lg">↓</span>
          </a>
        </div>

        <div class="mt-24 flex w-full max-w-md items-center justify-center gap-4">
          <span class="h-px flex-1 bg-[#bbdec6]/20"></span>
          <span class="text-[10px] tracking-wide text-white/25">
            Inteligência que transforma informação em ação
          </span>
          <span class="h-px flex-1 bg-[#bbdec6]/20"></span>
        </div>
      </div>

      <div class="absolute bottom-8 left-1/2 -translate-x-1/2 text-center text-[10px] tracking-wide text-white/25">
        Role para explorar ↓
      </div>
    </section>

    <section class="flex w-full justify-center px-6 py-24 sm:px-8 sm:py-28 lg:py-32">
      <div class="grid w-full max-w-5xl grid-cols-2 overflow-hidden rounded-3xl border border-white/[0.07] bg-white/[0.025] backdrop-blur-xl md:grid-cols-4">
        <div class="flex min-h-[9rem] flex-col items-center justify-center gap-3 border-b border-white/[0.06] px-4 text-center md:border-b-0">
          <span class="block text-2xl font-medium text-[#bbdec6]">99.9%</span>
          <span class="mt-1 block text-xs tracking-wide text-white/30">Disponibilidade</span>
        </div>
        <div class="flex min-h-[9rem] flex-col items-center justify-center gap-3 border-b border-l border-white/[0.06] px-4 text-center md:border-b-0">
          <span class="block text-2xl font-medium text-[#bbdec6]">&lt; 5 min</span>
          <span class="mt-1 block text-xs tracking-wide text-white/30">Detecção média</span>
        </div>
        <div class="flex min-h-[9rem] flex-col items-center justify-center gap-3 border-l border-white/[0.06] px-4 text-center">
          <span class="block text-2xl font-medium text-[#bbdec6]">150+</span>
          <span class="mt-1 block text-xs tracking-wide text-white/30">Fontes integradas</span>
        </div>
        <div class="flex min-h-[9rem] flex-col items-center justify-center gap-3 border-l border-white/[0.06] px-4 text-center">
          <span class="block text-2xl font-medium text-[#bbdec6]">24/7</span>
          <span class="mt-1 block text-xs tracking-wide text-white/30">Monitoramento</span>
        </div>
      </div>
    </section>

    <section id="plataforma" class="flex w-full flex-col items-center px-6 py-40 text-center sm:px-8 md:py-48">
      <div class="mx-auto flex w-full max-w-6xl flex-col items-center">
        <span class="text-[10px] font-medium uppercase tracking-[0.28em] text-[#bbdec6]/60">Plataforma</span>
        <h2 class="mt-8 max-w-3xl text-4xl font-medium leading-[1.15] tracking-[-0.045em] sm:text-5xl md:text-6xl">
          Tudo o que você precisa
          <span class="mt-2 block text-[#bbdec6]">em um só lugar.</span>
        </h2>
        <p class="mt-9 max-w-2xl text-sm leading-8 tracking-wide text-white/35 sm:text-base sm:leading-9">
          Centralize informações, acompanhe tendências e transforme
          dados em inteligência para sua operação.
        </p>

        <div class="mt-24 grid w-full grid-cols-1 gap-8 md:grid-cols-3">
          <article class="group flex min-h-[18rem] flex-col items-center justify-center rounded-3xl border border-white/[0.07] bg-white/[0.025] p-12 text-center backdrop-blur-xl transition duration-300 hover:-translate-y-1.5 hover:border-[#bbdec6]/20 hover:bg-[#bbdec6]/[0.035]">
            <div class="flex h-14 w-14 items-center justify-center rounded-2xl border border-[#bbdec6]/10 bg-[#bbdec6]/[0.05] text-[#bbdec6] transition duration-300 group-hover:bg-[#bbdec6]/10">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" class="h-5 w-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4 19V5M4 19h16" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M7 16l4-5 3 2 5-7" />
              </svg>
            </div>
            <h3 class="mt-10 text-lg font-medium">Análise Preditiva</h3>
            <p class="mt-6 max-w-xs text-sm leading-7 text-white/30">
              Encontre padrões e acompanhe sinais relevantes
              para sua operação.
            </p>
          </article>

          <article class="group flex min-h-[18rem] flex-col items-center justify-center rounded-3xl border border-white/[0.07] bg-white/[0.025] p-12 text-center backdrop-blur-xl transition duration-300 hover:-translate-y-1.5 hover:border-[#bbdec6]/20 hover:bg-[#bbdec6]/[0.035]">
            <div class="flex h-14 w-14 items-center justify-center rounded-2xl border border-[#bbdec6]/10 bg-[#bbdec6]/[0.05] text-[#bbdec6] transition duration-300 group-hover:bg-[#bbdec6]/10">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" class="h-5 w-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 3l7 4v5c0 4.5-3 7.5-7 9-4-1.5-7-4.5-7-9V7l7-4Z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M9.5 12l1.7 1.7 3.5-3.5" />
              </svg>
            </div>
            <h3 class="mt-10 text-lg font-medium">Inteligência Defensiva</h3>
            <p class="mt-6 max-w-xs text-sm leading-7 text-white/30">
              Conecte informações e indicadores para compreender
              os principais riscos.
            </p>
          </article>

          <article class="group flex min-h-[18rem] flex-col items-center justify-center rounded-3xl border border-white/[0.07] bg-white/[0.025] p-12 text-center backdrop-blur-xl transition duration-300 hover:-translate-y-1.5 hover:border-[#bbdec6]/20 hover:bg-[#bbdec6]/[0.035]">
            <div class="flex h-14 w-14 items-center justify-center rounded-2xl border border-[#bbdec6]/10 bg-[#bbdec6]/[0.05] text-[#bbdec6] transition duration-300 group-hover:bg-[#bbdec6]/10">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" class="h-5 w-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M3 12h4l2-6 4 12 2-6h6" />
              </svg>
            </div>
            <h3 class="mt-10 text-lg font-medium">Tendências em Tempo Real</h3>
            <p class="mt-6 max-w-xs text-sm leading-7 text-white/30">
              Visualize mudanças importantes através de informações
              atualizadas.
            </p>
          </article>
        </div>
      </div>
    </section>

    <section id="grafico" ref="chartSection" class="flex w-full flex-col items-center px-6 py-40 text-center sm:px-8 md:py-48">
      <div class="mx-auto flex w-full max-w-6xl flex-col items-center">
        <span class="text-[10px] font-medium uppercase tracking-[0.28em] text-[#bbdec6]/60">Inteligência visual</span>
        <h2 class="mt-8 max-w-3xl text-4xl font-medium leading-[1.15] tracking-[-0.045em] sm:text-5xl md:text-6xl">
          Veja os dados
          <span class="mt-2 block text-[#bbdec6]">evoluírem.</span>
        </h2>
        <p class="mt-9 max-w-2xl text-sm leading-8 tracking-wide text-white/35 sm:text-base sm:leading-9">
          Passe o mouse sobre cada barra para descobrir
          como a informação evolui até chegar à decisão.
        </p>

        <div class="relative mt-12 w-full rounded-3xl border border-white/[0.07] bg-white/[0.02] p-4 backdrop-blur-xl sm:p-6">
          <div class="pointer-events-none absolute inset-4 sm:inset-6">
            <div class="absolute inset-x-0 top-0 h-px bg-white/[0.05]"></div>
            <div class="absolute inset-x-0 top-1/4 h-px bg-white/[0.04]"></div>
            <div class="absolute inset-x-0 top-1/2 h-px bg-white/[0.04]"></div>
            <div class="absolute inset-x-0 top-3/4 h-px bg-white/[0.04]"></div>
            <div class="absolute inset-x-0 bottom-0 h-px bg-white/[0.08]"></div>
          </div>

          <div class="relative flex h-[14rem] w-full items-end justify-between gap-2 sm:h-[18rem] sm:gap-4">
            <div
              v-for="(point, i) in graphPoints"
              :key="point.id"
              class="relative flex h-full flex-1 flex-col items-center justify-end"
              @mouseenter="ativarPonto(point)"
              @mouseleave="desativarPonto"
            >
              <transition
                enter-active-class="transition duration-200 ease-out"
                enter-from-class="opacity-0 translate-y-2"
                enter-to-class="opacity-100 translate-y-0"
                leave-active-class="transition duration-150 ease-in"
                leave-from-class="opacity-100 translate-y-0"
                leave-to-class="opacity-0 translate-y-2"
              >
                <div
                  v-if="activePoint?.id === point.id"
                  class="absolute bottom-full z-20 mb-3 w-[11rem] -translate-x-1/2 rounded-xl border border-[#bbdec6]/20 bg-[#07100d]/95 p-4 text-left shadow-[0_1rem_2rem_rgba(0,0,0,0.45)] backdrop-blur-xl sm:w-[13rem]"
                  :class="i === 0 ? 'left-0 translate-x-0' : i === graphPoints.length - 1 ? 'left-full -translate-x-full' : 'left-1/2'"
                >
                  <div class="flex items-center gap-3">
                    <span class="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-[#bbdec6]/10 text-[10px] font-semibold text-[#bbdec6]">
                      {{ point.number }}
                    </span>
                    <div>
                      <span class="block text-[9px] uppercase tracking-[0.15em] text-[#bbdec6]/60">
                        Benefício
                      </span>
                      <strong class="mt-1 block text-sm font-medium text-white">
                        {{ point.title }}
                      </strong>
                    </div>
                  </div>
                  <p class="mt-4 text-xs leading-7 text-white/50">
                    {{ point.benefit }}
                  </p>
                </div>
              </transition>

              <span
                class="mb-2 text-[10px] font-medium tracking-wide text-white/40 transition-all duration-300"
                :class="activePoint?.id === point.id ? 'text-[#bbdec6] opacity-100' : 'opacity-60'"
              >
                {{ point.number }}
              </span>

              <div
                class="w-full max-w-[2.25rem] cursor-pointer rounded-t-xl bg-gradient-to-t from-[#5b8b75] via-[#7fae91] to-[#bbdec6] transition-all duration-700 ease-out sm:max-w-[3rem]"
                :style="{
                  height: (chartVisible ? point.value : 0) + '%',
                  transitionDelay: chartVisible ? `${i * 90}ms` : '0ms'
                }"
                :class="activePoint?.id === point.id
                  ? '-translate-y-2 shadow-[0_0_1.5rem_rgba(187,222,198,0.45)] brightness-110'
                  : 'translate-y-0'"
              ></div>

              <span
                class="mt-3 text-[10px] font-medium tracking-wide transition-colors duration-300 sm:text-[11px]"
                :class="activePoint?.id === point.id ? 'text-[#bbdec6]' : 'text-white/35'"
              >
                {{ point.title }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section id="recursos" class="flex w-full flex-col items-center px-6 py-40 text-center sm:px-8 md:py-48">
      <div class="mx-auto flex w-full max-w-6xl flex-col items-center">
        <span class="text-[10px] font-medium uppercase tracking-[0.28em] text-[#bbdec6]/60">Recursos</span>
        <h2 class="mt-8 max-w-3xl text-4xl font-medium leading-[1.15] tracking-[-0.045em] sm:text-5xl md:text-6xl">
          Dados que fazem
          <span class="mt-2 block text-[#bbdec6]">sentido.</span>
        </h2>
        <p class="mt-9 max-w-2xl text-sm leading-8 tracking-wide text-white/35 sm:text-base sm:leading-9">
          Visualize informações complexas de forma simples,
          organizada e pronta para apoiar sua operação.
        </p>

        <div class="mt-24 grid w-full grid-cols-1 gap-8 md:grid-cols-2">
          <div class="group relative flex min-h-[23rem] flex-col items-center justify-center overflow-hidden rounded-3xl border border-white/[0.07] bg-gradient-to-br from-[#10231d]/70 via-[#07100d]/80 to-[#010300] p-14 text-center transition duration-300 hover:-translate-y-1 hover:border-[#bbdec6]/15">
            <div class="absolute -right-16 -top-16 h-48 w-48 rounded-full bg-[#bbdec6]/10 blur-3xl"></div>
            <span class="relative text-[10px] font-medium uppercase tracking-[0.25em] text-[#bbdec6]/60">Visualização</span>
            <h3 class="relative mt-8 max-w-md text-2xl font-medium leading-[1.15] sm:text-3xl">
              Encontre padrões
              <span class="mt-1 block text-[#bbdec6]">onde antes havia ruído.</span>
            </h3>
            <p class="relative mt-8 max-w-md text-sm leading-7 text-white/30">
              Dashboards organizados para facilitar
              a leitura dos principais indicadores.
            </p>
          </div>

          <div class="group relative flex min-h-[23rem] flex-col items-center justify-center overflow-hidden rounded-3xl border border-white/[0.07] bg-gradient-to-br from-[#091816]/70 via-[#07100d]/80 to-[#010300] p-14 text-center transition duration-300 hover:-translate-y-1 hover:border-[#bbdec6]/15">
            <div class="absolute bottom-8 right-10 flex items-end gap-2 opacity-50">
              <div class="h-12 w-3 rounded-t bg-[#bbdec6]/20"></div>
              <div class="h-20 w-3 rounded-t bg-[#bbdec6]/30"></div>
              <div class="h-16 w-3 rounded-t bg-[#bbdec6]/40"></div>
              <div class="h-28 w-3 rounded-t bg-[#bbdec6]/60"></div>
              <div class="h-36 w-3 rounded-t bg-[#bbdec6]/80"></div>
            </div>
            <span class="relative text-[10px] font-medium uppercase tracking-[0.25em] text-[#bbdec6]/60">Inteligência</span>
            <h3 class="relative mt-8 max-w-md text-2xl font-medium leading-[1.15] sm:text-3xl">
              Decisões baseadas
              <span class="mt-1 block text-[#bbdec6]">em contexto.</span>
            </h3>
            <p class="relative mt-8 max-w-md text-sm leading-7 text-white/30">
              Reúna informações relevantes e acompanhe
              os sinais que realmente importam.
            </p>
          </div>
        </div>
      </div>
    </section>

    <section id="sobre" class="flex w-full justify-center px-6 py-16 sm:px-8 lg:py-24">
      <div class="relative flex min-h-[27rem] w-full max-w-6xl items-center justify-center overflow-hidden rounded-[2rem] border border-[#bbdec6]/10 bg-[#071311]/70 px-6 py-16 text-center sm:px-10">
        <div class="absolute inset-0 bg-[radial-gradient(circle_at_50%_120%,rgba(187,222,198,0.14),transparent_48%)]"></div>
        <div class="relative z-10 flex w-full max-w-3xl flex-col items-center px-2 sm:px-4">
          <span class="text-[10px] font-medium uppercase tracking-[0.28em] text-[#bbdec6]/60">InCTI Insights</span>
          <h2 class="mt-8 text-4xl font-medium leading-[1.15] tracking-[-0.045em] sm:text-5xl md:text-6xl">
            Pronto para elevar
            <span class="mt-2 block text-[#bbdec6]">sua inteligência?</span>
          </h2>
          <p class="mt-9 max-w-xl text-sm leading-8 tracking-wide text-white/35 sm:text-base sm:leading-9">
            Transforme dados em contexto, contexto em inteligência
            e inteligência em decisões.
          </p>
          <router-link
            to="/login"
            class="mt-16 flex h-16 w-full max-w-xs items-center justify-center gap-4 rounded-xl bg-[#bbdec6] px-12 text-base font-semibold tracking-wide text-[#010300] transition duration-300 hover:-translate-y-0.5 hover:bg-white hover:shadow-[0_0_2.5rem_rgba(187,222,198,0.18)] sm:w-auto"
          >
            Solicitar acesso
            <span>→</span>
          </router-link>
        </div>
      </div>
    </section>

    <Footer />
  </main>
</template>