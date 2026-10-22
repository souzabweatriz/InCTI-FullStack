<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const menuAberto = ref(false)
const rolaramPagina = ref(false)

function fecharMenu() {
    menuAberto.value = false
}

function atualizarScroll() {
    rolaramPagina.value = window.scrollY > 20
}

onMounted(() => {
    atualizarScroll()
    window.addEventListener('scroll', atualizarScroll, { passive: true })
})

onUnmounted(() => {
    window.removeEventListener('scroll', atualizarScroll)
})
</script>

<template>
    <header class="fixed inset-x-0 top-0 z-50 px-4 pt-4 transition-all duration-500 sm:px-6 lg:px-10"
        :class="rolaramPagina ? 'pt-2 sm:pt-3' : 'pt-4 sm:pt-5'">
        <nav class="relative mx-auto flex min-h-[68px] max-w-7xl items-center justify-between gap-4 rounded-[22px] border px-4 transition-all duration-500 sm:px-6 lg:px-7"
            :class="rolaramPagina
                ? 'border-white/80 bg-white/90 shadow-[0_12px_45px_rgba(119,45,76,0.12)] backdrop-blur-2xl'
                : 'border-white/80 bg-white/75 shadow-[0_8px_35px_rgba(119,45,76,0.06)] backdrop-blur-xl'">
            <div
                class="absolute inset-x-8 top-0 h-px bg-gradient-to-r from-transparent via-[#E8A2B8]/80 to-transparent">
            </div>

            <router-link to="/" class="group relative flex shrink-0 items-center gap-3" @click="fecharMenu">
                <span
                    class="relative flex h-11 w-11 items-center justify-center overflow-hidden rounded-[15px]  transition duration-500 group-hover:-rotate-6 group-hover:scale-110">
                    <img src="../assets/icons/logo.png" alt="InCTI" class="relative z-50 h-15 w-90 object-contain" />
                    <span class="absolute -right-3 -top-3 h-8 w-8 rounded-full"></span>
                </span>

                <span class="flex flex-col">
                    <span class="text-lg font-extrabold tracking-tight text-[#302730]">
                        InCTI<span class="text-[#C73A68]">.</span>
                    </span>
                    <span class="mt-0.5 text-[9px] font-semibold uppercase tracking-[0.19em] text-[#9B8B96]">
                        Inteligência conectada
                    </span>
                </span>
            </router-link>

            <!-- Navegação desktop -->
            <div class="hidden items-center gap-1 rounded-full border border-[#F4E9EE] bg-[#FFF9FB]/80 p-1.5 md:flex">
                <a v-for="item in [
                    { nome: 'Início', href: '#inicio' },
                    { nome: 'Plataforma', href: '#plataforma' },
                    { nome: 'Insights', href: '#grafico' },
                    { nome: 'Recursos', href: '#recursos' },
                    { nome: 'Sobre', href: '#sobre' }
                ]" :key="item.href" :href="item.href"
                    class="rounded-full px-3 py-2 text-xs font-semibold text-[#77717A] transition-all duration-300 hover:bg-white hover:text-[#C73A68] hover:shadow-sm lg:px-3.5 lg:text-sm">
                    {{ item.nome }}
                </a>
            </div>

            <!-- Ações desktop -->
            <div class="hidden shrink-0 items-center gap-2 md:flex">
                <router-link to="/login"
                    class="rounded-full px-4 py-2.5 text-sm font-semibold text-[#655560] transition hover:text-[#C73A68]">
                    Entrar
                </router-link>

                <router-link to="/cadastro"
                    class="group relative inline-flex min-h-11 items-center gap-3 overflow-hidden rounded-full bg-gradient-to-r from-[#C73A68] to-[#E45F78] px-5 text-sm font-bold text-white shadow-lg shadow-[#C73A68]/20 transition-all duration-300 hover:-translate-y-0.5 hover:shadow-xl hover:shadow-[#C73A68]/30 lg:px-6">
                    <span
                        class="absolute inset-0 translate-x-[-120%] bg-gradient-to-r from-transparent via-white/20 to-transparent transition-transform duration-700 group-hover:translate-x-[120%]"></span>
                    <span class="relative">Começar agora</span>
                    <span
                        class="relative flex h-6 w-6 items-center justify-center rounded-full bg-white/20 transition-transform duration-300 group-hover:translate-x-1">
                        →
                    </span>
                </router-link>
            </div>

            <!-- Botão mobile -->
            <button type="button" :aria-label="menuAberto ? 'Fechar menu' : 'Abrir menu'" :aria-expanded="menuAberto"
                class="flex h-11 w-11 shrink-0 items-center justify-center rounded-full border border-[#F0E0E8] bg-white text-[#C73A68] transition hover:bg-[#FFF1F6] md:hidden"
                @click="menuAberto = !menuAberto">
                <svg v-if="!menuAberto" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="1.8">
                    <path stroke-linecap="round" d="M4 7h16M7 12h10M4 17h16" />
                </svg>

                <svg v-else class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                    <path stroke-linecap="round" d="m6 6 12 12M18 6 6 18" />
                </svg>
            </button>

            <!-- Menu mobile -->
            <Transition enter-active-class="transition duration-300 ease-out" enter-from-class="translate-y-2 opacity-0"
                enter-to-class="translate-y-0 opacity-100" leave-active-class="transition duration-200 ease-in"
                leave-from-class="translate-y-0 opacity-100" leave-to-class="translate-y-2 opacity-0">
                <div v-if="menuAberto"
                    class="absolute left-0 right-0 top-[calc(100%+12px)] overflow-hidden rounded-[24px] border border-[#F1E3E9] bg-white/95 p-3 shadow-[0_20px_60px_rgba(90,42,65,0.16)] backdrop-blur-2xl md:hidden">
                    <div
                        class="mb-2 flex items-center gap-3 rounded-[18px] bg-gradient-to-r from-[#FFF2F6] to-[#FFF8F2] p-4">
                        <span
                            class="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-[#C73A68] to-[#FF8A4C] text-lg text-white shadow-md">
                            ✳
                        </span>
                        <div>
                            <p class="text-sm font-bold text-[#352C34]">
                                Explore a InCTI
                            </p>
                            <p class="mt-1 text-xs text-[#968C95]">
                                Dados que se transformam em possibilidades.
                            </p>
                        </div>
                    </div>

                    <a v-for="item in [
                        { nome: 'Início', href: '#inicio', numero: '01' },
                        { nome: 'Plataforma', href: '#plataforma', numero: '02' },
                        { nome: 'Insights', href: '#grafico', numero: '03' },
                        { nome: 'Recursos', href: '#recursos', numero: '04' },
                        { nome: 'Sobre', href: '#sobre', numero: '05' }
                    ]" :key="item.href" :href="item.href"
                        class="group flex min-h-12 items-center justify-between rounded-xl px-4 text-sm font-semibold text-[#655560] transition hover:bg-[#FFF3F7] hover:text-[#C73A68]"
                        @click="fecharMenu">
                        <span>{{ item.nome }}</span>
                        <span class="flex items-center gap-3">
                            <span class="text-[10px] font-medium tracking-widest text-[#C8B7C1]">
                                {{ item.numero }}
                            </span>
                            <span class="transition-transform group-hover:translate-x-1">↗</span>
                        </span>
                    </a>

                    <div class="my-3 h-px bg-gradient-to-r from-transparent via-[#F0DCE5] to-transparent"></div>

                    <router-link to="/login"
                        class="flex min-h-11 items-center justify-center rounded-xl text-sm font-semibold text-[#655560] transition hover:bg-[#FFF3F7]"
                        @click="fecharMenu">
                        Entrar na plataforma
                    </router-link>

                    <router-link to="/cadastro"
                        class="mt-2 flex min-h-12 items-center justify-center gap-3 rounded-full bg-gradient-to-r from-[#C73A68] to-[#E45F78] px-4 text-sm font-bold text-white shadow-lg shadow-[#C73A68]/20 transition hover:shadow-xl"
                        @click="fecharMenu">
                        Começar agora
                        <span>→</span>
                    </router-link>

                    <div class="flex items-center justify-center gap-1.5 pb-1 pt-4">
                        <span class="h-1 w-8 rounded-full bg-[#C73A68]"></span>
                        <span class="h-1 w-4 rounded-full bg-[#FF8A4C]"></span>
                        <span class="h-1 w-1 rounded-full bg-[#FFBE55]"></span>
                    </div>
                </div>
            </Transition>
        </nav>
    </header>
</template>
