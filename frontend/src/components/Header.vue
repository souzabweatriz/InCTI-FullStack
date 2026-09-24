<script setup>
import { ref } from 'vue'

const menuAberto = ref(false)

function fecharMenu() {
    menuAberto.value = false
}
</script>

<template>
    <header class="fixed left-0 top-0 z-50 flex w-full justify-center px-3 py-3 sm:px-4 sm:py-4 lg:px-6">

        <nav
            class="relative flex min-h-14 w-full max-w-7xl items-center justify-between rounded-2xl border border-white/[0.08] bg-[#010300]/70 px-4 shadow-[0_0.75rem_2.5rem_rgba(0,0,0,0.25)] backdrop-blur-xl sm:min-h-16 sm:px-5 lg:min-h-[4.5rem] lg:px-7">

            <router-link to="/" class="flex shrink-0 items-center justify-center gap-2.5" @click="fecharMenu">

                <img src="../assets/icons/logo.png" alt="InCTI"
                    class="h-7 w-7 object-contain sm:h-8 sm:w-8 lg:h-9 lg:w-9">

                <span class="text-sm font-semibold tracking-[0.08em] text-white sm:text-base">
                    InCTI
                </span>

            </router-link>

            <div class="hidden h-full items-center justify-center gap-6 md:flex lg:gap-9 xl:gap-12">

                <a href="#inicio"
                    class="group relative flex h-full items-center justify-center px-1 text-[0.7rem] text-white/55 transition hover:text-white lg:text-xs">
                    <span>
                        Início
                    </span>

                    <span
                        class="absolute bottom-2.5 left-1/2 h-px w-0 -translate-x-1/2 bg-[#bbdec6] shadow-[0_0_0.6rem_rgba(187,222,198,0.6)] transition-all duration-300 group-hover:w-full lg:bottom-3"></span>
                </a>

                <a href="#plataforma"
                    class="group relative flex h-full items-center justify-center px-1 text-[0.7rem] text-white/55 transition hover:text-white lg:text-xs">
                    <span>
                        Plataforma
                    </span>

                    <span
                        class="absolute bottom-2.5 left-1/2 h-px w-0 -translate-x-1/2 bg-[#bbdec6] shadow-[0_0_0.6rem_rgba(187,222,198,0.6)] transition-all duration-300 group-hover:w-full lg:bottom-3"></span>
                </a>

                <a href="#grafico"
                    class="group relative flex h-full items-center justify-center px-1 text-[0.7rem] text-white/55 transition hover:text-white lg:text-xs">
                    <span>
                        Insights
                    </span>

                    <span
                        class="absolute bottom-2.5 left-1/2 h-px w-0 -translate-x-1/2 bg-[#bbdec6] shadow-[0_0_0.6rem_rgba(187,222,198,0.6)] transition-all duration-300 group-hover:w-full lg:bottom-3"></span>
                </a>

                <a href="#recursos"
                    class="group relative flex h-full items-center justify-center px-1 text-[0.7rem] text-white/55 transition hover:text-white lg:text-xs">
                    <span>
                        Recursos
                    </span>

                    <span
                        class="absolute bottom-2.5 left-1/2 h-px w-0 -translate-x-1/2 bg-[#bbdec6] shadow-[0_0_0.6rem_rgba(187,222,198,0.6)] transition-all duration-300 group-hover:w-full lg:bottom-3"></span>
                </a>

                <a href="#sobre"
                    class="group relative flex h-full items-center justify-center px-1 text-[0.7rem] text-white/55 transition hover:text-white lg:text-xs">
                    <span>
                        Sobre
                    </span>

                    <span
                        class="absolute bottom-2.5 left-1/2 h-px w-0 -translate-x-1/2 bg-[#bbdec6] shadow-[0_0_0.6rem_rgba(187,222,198,0.6)] transition-all duration-300 group-hover:w-full lg:bottom-3"></span>
                </a>

            </div>

            <div class="hidden shrink-0 items-center justify-center gap-2 sm:gap-3 md:flex">

                <router-link to="/login"
                    class="flex h-10 min-w-[5rem] items-center justify-center rounded-lg px-4 text-center text-[0.7rem] text-white/65 transition hover:bg-white/[0.04] hover:text-white lg:h-11 lg:min-w-[5.5rem] lg:text-xs">
                    Entrar
                </router-link>

                <router-link to="/login"
                    class="flex h-10 min-w-[8.5rem] items-center justify-center gap-2 rounded-lg bg-[#bbdec6] px-4 text-center text-[0.65rem] font-semibold text-[#010300] transition hover:bg-white hover:shadow-[0_0_1.8rem_rgba(187,222,198,0.15)] sm:min-w-[9rem] lg:h-11 lg:min-w-[9.5rem] lg:px-6 lg:text-xs">
                    Começar agora
                    <span>
                        →
                    </span>
                </router-link>

            </div>

            <button type="button" aria-label="Abrir menu"
                class="flex h-10 w-10 items-center justify-center rounded-lg border border-white/10 bg-white/[0.03] text-white/75 transition hover:bg-white/[0.07] md:hidden"
                @click="menuAberto = !menuAberto">

                <svg v-if="!menuAberto" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none"
                    stroke="currentColor" stroke-width="1.7" class="h-5 w-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M4 7h16M4 12h16M4 17h16" />
                </svg>

                <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="1.7" class="h-5 w-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 6l12 12M18 6L6 18" />
                </svg>

            </button>

            <div v-if="menuAberto"
                class="absolute left-0 right-0 top-[calc(100%+0.65rem)] flex flex-col overflow-hidden rounded-2xl border border-white/[0.08] bg-[#010300]/90 p-3 shadow-[0_1rem_3rem_rgba(0,0,0,0.35)] backdrop-blur-xl md:hidden">

                <a href="#inicio"
                    class="flex min-h-11 items-center justify-center rounded-xl px-4 text-xs text-white/65 transition hover:bg-white/[0.05] hover:text-white"
                    @click="fecharMenu">
                    Início
                </a>

                <a href="#plataforma"
                    class="flex min-h-11 items-center justify-center rounded-xl px-4 text-xs text-white/65 transition hover:bg-white/[0.05] hover:text-white"
                    @click="fecharMenu">
                    Plataforma
                </a>

                <a href="#grafico"
                    class="flex min-h-11 items-center justify-center rounded-xl px-4 text-xs text-white/65 transition hover:bg-white/[0.05] hover:text-white"
                    @click="fecharMenu">
                    Insights
                </a>

                <a href="#recursos"
                    class="flex min-h-11 items-center justify-center rounded-xl px-4 text-xs text-white/65 transition hover:bg-white/[0.05] hover:text-white"
                    @click="fecharMenu">
                    Recursos
                </a>

                <a href="#sobre"
                    class="flex min-h-11 items-center justify-center rounded-xl px-4 text-xs text-white/65 transition hover:bg-white/[0.05] hover:text-white"
                    @click="fecharMenu">
                    Sobre
                </a>

                <div class="my-2 h-px bg-white/[0.06]"></div>

                <router-link to="/login"
                    class="flex min-h-11 items-center justify-center rounded-xl px-4 text-xs text-white/70 transition hover:bg-white/[0.05] hover:text-white"
                    @click="fecharMenu">
                    Entrar
                </router-link>

                <router-link to="/login"
                    class="mt-1 flex min-h-11 items-center justify-center gap-2 rounded-xl bg-[#bbdec6] px-4 text-xs font-semibold text-[#010300] transition hover:bg-white"
                    @click="fecharMenu">
                    Começar agora
                    <span>→</span>
                </router-link>

            </div>

        </nav>

    </header>
</template>