<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

const usuario = ref('')
const senha = ref('')
const mensagem = ref('')
const showPassword = ref(false)

const router = useRouter()
const auth = useAuthStore()

function entrar() {
    mensagem.value = ''

    if (auth.login(usuario.value, senha.value)) {
        router.push({ name: 'upload' })
    } else {
        mensagem.value = 'Preencha usuário e senha.'
    }
}
</script>

<template>
    <main class="login-page flex min-h-screen w-full relative overflow-hidden">

        <div class="absolute inset-0 w-full h-full bg-[#010300]/[0.25]"></div>

        <div
            class="relative z-[1] flex min-h-screen w-full items-center justify-center px-4 py-8 sm:px-6 md:px-8 lg:px-12">

            <div class="flex w-full max-w-[72rem] items-center justify-center gap-8 md:gap-12 lg:gap-20 xl:gap-28">

                <section class="hidden md:flex w-[18rem] lg:w-[24rem] xl:w-[27rem] shrink-0 flex-col justify-center">

                    <p class="text-[#bbdec6] text-[0.55rem] sm:text-[0.6rem] uppercase tracking-[0.3em] font-medium">
                        Plataforma analítica
                    </p>

                    <h1
                        class="text-white text-[1.7rem] lg:text-[2rem] xl:text-[2.2rem] font-medium leading-[1.15] mt-3">
                        Enxergue os sinais
                        <br>
                        por trás do ruído.
                    </h1>

                    <p class="text-white/35 text-[0.7rem] lg:text-[0.75rem] leading-relaxed mt-5 max-w-[22rem]">
                        Transforme dados em informações para apoiar decisões mais inteligentes.
                    </p>

                </section>

                <section class="login-card">

                    <div class="login-card-content">

                        <div class="flex items-center justify-center gap-2 mb-8">

                            <img src="../assets/icons/logo.png" alt="InCTI" class="flex w-8 h-8 object-contain">

                            <span class="text-white text-[0.95rem] font-semibold tracking-[0.08em]">
                                InCTI
                            </span>

                        </div>

                        <div class="flex flex-col items-center text-center mb-8">

                            <h2 class="text-white text-[1.45rem] font-semibold tracking-[0.01em]">
                                Bem-vindo de volta
                            </h2>

                            <p class="text-white/45 text-[0.7rem] mt-3 leading-relaxed max-w-[19rem]">
                                Entre com seus dados para acessar sua conta.
                            </p>

                        </div>

                        <form class="flex flex-col gap-5" @submit.prevent="entrar">

                            <div class="flex flex-col gap-2">

                                <label for="usuario" class="text-white/60 text-[0.6rem] uppercase tracking-[0.16em]">
                                    Usuário
                                </label>

                                <input id="usuario" v-model="usuario" type="text" autocomplete="username"
                                    placeholder="voce@empresa.com" class="login-input">

                            </div>

                            <div class="flex flex-col gap-2">

                                <label for="senha" class="text-white/60 text-[0.6rem] uppercase tracking-[0.16em]">
                                    Senha
                                </label>

                                <div class="password-input">

                                    <input id="senha" v-model="senha" :type="showPassword ? 'text' : 'password'"
                                        autocomplete="current-password" placeholder="••••••••" class="password-field">

                                    <button type="button" aria-label="Mostrar ou ocultar senha" class="password-toggle"
                                        @click="showPassword = !showPassword">

                                        <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                                            fill="none" stroke="currentColor" stroke-width="1.7" class="w-4 h-4">
                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                d="M2.25 12s3.5-6 9.75-6 9.75 6 9.75 6-3.5 6-9.75 6-9.75-6-9.75-6Z" />

                                            <circle cx="12" cy="12" r="2.5" />
                                        </svg>

                                        <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none"
                                            stroke="currentColor" stroke-width="1.7" class="w-4 h-4">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M3 3l18 18" />

                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                d="M10.58 10.58a2 2 0 0 0 2.83 2.83" />

                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                d="M9.88 5.09A10.72 10.72 0 0 1 12 4.88c6.25 0 9.75 7.12 9.75 7.12a17.74 17.74 0 0 1-3.13 3.9" />

                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                d="M6.61 6.61C3.83 8.43 2.25 12 2.25 12s3.5 7.12 9.75 7.12a10.76 10.76 0 0 0 4.04-.78" />
                                        </svg>

                                    </button>

                                </div>

                            </div>

                            <div
                                class="flex flex-col gap-3 min-[400px]:flex-row min-[400px]:items-center min-[400px]:justify-between pt-1">

                                <label class="flex items-center gap-2 text-white/45 text-[0.6rem] cursor-pointer">

                                    <input type="checkbox" class="flex w-3 h-3 accent-[#bbdec6]">

                                    Lembrar de mim

                                </label>

                                <a href="#" class="text-[#bbdec6] text-[0.6rem] hover:underline">
                                    Esqueceu a senha?
                                </a>

                            </div>

                            <button type="submit"
                                class="flex w-full h-11 items-center justify-center rounded-md bg-[#bbdec6] text-[#010300] text-[0.7rem] font-semibold hover:bg-white active:scale-[0.99] transition mt-1">

                                Entrar

                                <span class="ml-2 text-sm">
                                    →
                                </span>

                            </button>

                            <p v-if="mensagem" class="flex justify-center text-[#ff8f8f] text-[0.65rem] text-center">
                                {{ mensagem }}
                            </p>

                        </form>

                        <div class="flex items-center gap-4 my-7">

                            <div class="flex h-px flex-1 bg-white/[0.08]"></div>

                            <span class="text-white/25 text-[0.55rem] whitespace-nowrap tracking-[0.04em]">
                                ou continue com
                            </span>

                            <div class="flex h-px flex-1 bg-white/[0.08]"></div>

                        </div>

                        <div class="flex flex-col min-[400px]:flex-row items-center gap-3">

                            <button type="button"
                                class="flex w-full min-[400px]:flex-1 h-10 items-center justify-center rounded-md border border-white/10 bg-white/[0.02] text-white/60 text-[0.6rem] hover:bg-white/[0.05] hover:text-white transition">
                                Google
                            </button>

                            <button type="button"
                                class="flex w-full min-[400px]:flex-1 h-10 items-center justify-center rounded-md border border-white/10 bg-white/[0.02] text-white/60 text-[0.6rem] hover:bg-white/[0.05] hover:text-white transition">
                                Apple
                            </button>

                        </div>

                        <p
                            class="flex flex-wrap items-center justify-center text-white/25 text-[0.55rem] mt-7 text-center">
                            Ainda não tem uma conta?

                            <router-link to="/" class="text-[#bbdec6] ml-1 hover:underline">
                                Saiba mais
                            </router-link>

                        </p>

                    </div>

                </section>

            </div>

        </div>

    </main>
</template>

<style scoped>
.login-page {
    position: relative;
    background-image: url('../assets/images/BackgroundLogin.png');
    background-size: 125% 125%;
    background-position: center;
    background-repeat: no-repeat;
    animation: backgroundMove 10s ease-in-out infinite alternate;
}

.login-page::before {
    content: '';
    position: absolute;
    width: 35rem;
    height: 35rem;
    top: 50%;
    left: 62%;
    transform: translate(-50%, -50%);
    background: radial-gradient(circle,
            rgba(187, 222, 198, 0.18) 0%,
            rgba(91, 139, 117, 0.1) 35%,
            rgba(1, 3, 0, 0) 70%);
    filter: blur(1rem);
    pointer-events: none;
    animation: lightMove 7s ease-in-out infinite alternate;
}

.login-page::after {
    content: '';
    position: absolute;
    inset: 0;
    background: radial-gradient(circle at 65% 50%,
            rgba(187, 222, 198, 0.08),
            transparent 35%);
    pointer-events: none;
}

.login-card {
    position: relative;
    width: min(34rem, 100%);
    padding: 3rem;
    background: rgba(1, 3, 0, 0.64);
    border: 1px solid rgba(187, 222, 198, 0.14);
    border-radius: 1rem;
    backdrop-filter: blur(0.7rem);
    -webkit-backdrop-filter: blur(0.7rem);
    box-shadow:
        0 1.5rem 4rem rgba(0, 0, 0, 0.45),
        0 0 3rem rgba(187, 222, 198, 0.06),
        inset 0 0 2rem rgba(187, 222, 198, 0.025);
}

.login-card::before {
    content: '';
    position: absolute;
    inset: -1px;
    border-radius: inherit;
    background: linear-gradient(135deg,
            rgba(187, 222, 198, 0.2),
            transparent 30%,
            transparent 70%,
            rgba(187, 222, 198, 0.08));
    pointer-events: none;
    z-index: -1;
}

.login-card-content {
    width: 24rem;
    max-width: 100%;
    margin: 0 auto;
}

.login-input {
    width: 100%;
    height: 2.9rem;
    padding: 0 1.25rem;
    border-radius: 0.45rem;
    background: rgba(255, 255, 255, 0.04);
    border: 1px solid rgba(255, 255, 255, 0.1);
    color: white;
    font-size: 0.7rem;
    outline: none;
    transition: 0.2s ease;
}

.login-input::placeholder {
    color: rgba(255, 255, 255, 0.2);
}

.login-input:focus {
    border-color: #bbdec6;
    background: rgba(255, 255, 255, 0.07);
    box-shadow: 0 0 1rem rgba(187, 222, 198, 0.05);
}

.password-input {
    display: flex;
    width: 100%;
    height: 2.9rem;
    align-items: center;
    border-radius: 0.45rem;
    background: rgba(255, 255, 255, 0.04);
    border: 1px solid rgba(255, 255, 255, 0.1);
    transition: 0.2s ease;
}

.password-input:focus-within {
    border-color: #bbdec6;
    background: rgba(255, 255, 255, 0.07);
    box-shadow: 0 0 1rem rgba(187, 222, 198, 0.05);
}

.password-field {
    width: 100%;
    height: 100%;
    min-width: 0;
    padding: 0 1.25rem;
    flex: 1;
    background: transparent;
    border: 0;
    color: white;
    font-size: 0.7rem;
    outline: none;
}

.password-field::placeholder {
    color: rgba(255, 255, 255, 0.2);
}

.password-toggle {
    display: flex;
    width: 3.25rem;
    height: 100%;
    flex-shrink: 0;
    align-items: center;
    justify-content: center;
    padding: 0;
    border: 0;
    background: transparent;
    color: rgba(255, 255, 255, 0.4);
    cursor: pointer;
    transition: 0.2s ease;
}

.password-toggle:hover {
    color: #bbdec6;
}

@keyframes backgroundMove {
    0% {
        background-position: 35% 40%;
        background-size: 120% 120%;
    }

    25% {
        background-position: 60% 35%;
        background-size: 130% 130%;
    }

    50% {
        background-position: 65% 60%;
        background-size: 125% 125%;
    }

    75% {
        background-position: 40% 65%;
        background-size: 135% 135%;
    }

    100% {
        background-position: 55% 45%;
        background-size: 120% 120%;
    }
}

@keyframes lightMove {
    0% {
        transform: translate(-55%, -50%) scale(0.9);
        opacity: 0.65;
    }

    50% {
        transform: translate(-45%, -55%) scale(1.15);
        opacity: 1;
    }

    100% {
        transform: translate(-50%, -45%) scale(0.95);
        opacity: 0.7;
    }
}

@media (max-width: 1024px) {
    .login-card {
        padding: 2.5rem;
    }

    .login-card-content {
        width: 22rem;
    }

    .login-page {
        background-size: 135% 135%;
    }
}

@media (max-width: 768px) {
    .login-card {
        width: min(32rem, 100%);
        padding: 2.25rem;
    }

    .login-card-content {
        width: 100%;
    }

    .login-page::before {
        width: 28rem;
        height: 28rem;
        left: 50%;
    }

    .login-page {
        background-size: 150% 150%;
    }
}

@media (max-width: 480px) {
    .login-card {
        padding: 1.75rem;
        border-radius: 0.9rem;
        background: rgba(1, 3, 0, 0.68);
    }

    .login-card-content {
        width: 100%;
    }

    .login-page::before {
        width: 24rem;
        height: 24rem;
    }

    .login-input,
    .password-input {
        height: 3rem;
    }

    .login-input,
    .password-field {
        padding-left: 1rem;
        padding-right: 1rem;
    }
}

@media (max-width: 360px) {
    .login-card {
        padding: 1.35rem;
    }

    .login-card-content {
        width: 100%;
    }
}

@media (prefers-reduced-motion: reduce) {

    .login-page,
    .login-page::before {
        animation: none;
    }
}
</style>
