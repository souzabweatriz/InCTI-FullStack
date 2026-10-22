<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

const usuario = ref('')
const senha = ref('')
const mensagem = ref('')
const showPassword = ref(false)
const carregando = ref(false)
const lembrar = ref(false)

const router = useRouter()
const auth = useAuthStore()

async function entrar() {
    mensagem.value = ''

    if (carregando.value) return

    try {
        carregando.value = true

        if (!await auth.login(usuario.value, senha.value)) {
            mensagem.value = 'Preencha usuário e senha.'
            return
        }

        router.push({ name: 'upload' })
    } catch (error) {
        mensagem.value = error.message || 'Não foi possível entrar.'
    } finally {
        carregando.value = false
    }
}
</script>

<template>
    <main class="login-page">
        <section class="form-side">
            <div class="login-container">
                <router-link to="/" class="mobile-brand">
                    <img src="../assets/icons/logo.png" alt="Logo da InCTI">
                </router-link>

                <div class="form-heading">
                    <span class="eyebrow">ACESSE SUA CONTA</span>
                    <h1>Bem-vindo de volta<span>.</span></h1>
                    <p>Entre na sua conta para continuar de onde parou.</p>
                </div>

                <form class="login-form" @submit.prevent="entrar">
                    <div class="field">
                        <label for="usuario">Usuário ou e-mail</label>
                        <input id="usuario" v-model.trim="usuario" type="text"
                            placeholder="Digite seu usuário ou e-mail" autocomplete="username" required />
                    </div>

                    <div class="field">
                        <label for="senha">Senha</label>

                        <div class="password-wrapper">
                            <input id="senha" v-model="senha" :type="showPassword ? 'text' : 'password'"
                                placeholder="Digite sua senha" autocomplete="current-password" required />

                            <button type="button" class="toggle-password" @click="showPassword = !showPassword"
                                :aria-label="showPassword ? 'Ocultar senha' : 'Mostrar senha'">
                                <svg v-if="!showPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"
                                    aria-hidden="true">
                                    <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z" />
                                    <circle cx="12" cy="12" r="3" />
                                </svg>

                                <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7"
                                    stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                    <path d="m3 3 18 18" />
                                    <path d="M10.6 10.6a2 2 0 0 0 2.8 2.8" />
                                    <path d="M9.9 5.2A10.7 10.7 0 0 1 12 5c6.4 0 10 7 10 7a15.8 15.8 0 0 1-3.1 3.8" />
                                    <path d="M6.2 6.2C3.5 8 2 12 2 12s3.6 7 10 7c1.1 0 2.1-.2 3-.5" />
                                </svg>
                            </button>
                        </div>
                    </div>

                    <div class="form-options">
                        <label class="remember-option">
                            <input v-model="lembrar" type="checkbox" />
                            <span>Lembrar de mim</span>
                        </label>

                        <button type="button" class="forgot-link"
                            @click="mensagem = 'Entre em contato com o administrador para recuperar sua senha.'">
                            Esqueceu a senha?
                        </button>
                    </div>

                    <p v-if="mensagem" class="message" role="alert">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                            <circle cx="12" cy="12" r="9" />
                            <path d="M12 8v4m0 4h.01" />
                        </svg>
                        {{ mensagem }}
                    </p>

                    <button type="submit" class="submit-button" :disabled="carregando">
                        <span>{{ carregando ? 'Entrando...' : 'Entrar na plataforma' }}</span>

                        <svg v-if="!carregando" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
                            stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                            <path d="M5 12h14" />
                            <path d="m12 5 7 7-7 7" />
                        </svg>
                    </button>
                </form>

                <div class="signup-link">
                    Ainda não tem uma conta?
                    <router-link to="/cadastro">Criar conta</router-link>
                </div>

                <p class="form-footer">
                    © {{ new Date().getFullYear() }} InCTI Insights
                </p>
            </div>
        </section>

        <aside class="brand-side">
            <div class="brand-content">
                <router-link to="/" class="brand-logo">
                    <span class="brand-symbol">i</span>
                    <span>InCTI</span>
                </router-link>

                <div class="brand-message">
                    <span class="brand-eyebrow">
                        <span class="status-dot"></span>
                        INTELIGÊNCIA EM DADOS
                    </span>

                    <h2>
                        Dados que fazem sentido.
                        <span>Decisões que transformam.</span>
                    </h2>

                    <p>
                        Transforme informações em oportunidades e descubra novas
                        possibilidades com o InCTI Insights.
                    </p>
                </div>

                <div class="insight-card">
                    <div class="insight-top">
                        <div>
                            <span class="insight-label">INSIGHTS</span>
                            <h3>Encontre novas perspectivas</h3>
                        </div>

                        <div class="insight-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
                                stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <path d="M3 3v18h18" />
                                <path d="m7 14 4-4 4 3 6-7" />
                            </svg>
                        </div>
                    </div>

                    <div class="chart">
                        <div class="chart-grid">
                            <span></span>
                            <span></span>
                            <span></span>
                        </div>

                        <svg viewBox="0 0 420 112" preserveAspectRatio="none" class="chart-line" role="img"
                            aria-label="Ilustração de uma linha de crescimento">
                            <defs>
                                <linearGradient id="chartFill" x1="0" y1="0" x2="0" y2="1">
                                    <stop offset="0%" stop-color="#FFBE55" stop-opacity=".25" />
                                    <stop offset="100%" stop-color="#FFBE55" stop-opacity="0" />
                                </linearGradient>
                            </defs>

                            <path
                                d="M0 91 C35 85 43 63 78 72 S126 87 157 55 S208 67 240 42 S288 53 320 26 S376 38 420 8 L420 112 L0 112 Z"
                                fill="url(#chartFill)" />

                            <path
                                d="M0 91 C35 85 43 63 78 72 S126 87 157 55 S208 67 240 42 S288 53 320 26 S376 38 420 8"
                                fill="none" stroke="#FFBE55" stroke-width="3" stroke-linecap="round" />

                            <circle cx="320" cy="26" r="5" fill="#FFBE55" />
                            <circle cx="320" cy="26" r="9" fill="none" stroke="#FFBE55" stroke-opacity=".4" />
                        </svg>
                    </div>

                    <div class="insight-bottom">
                        <span><span class="legend-dot"></span> Análise de dados</span>
                        <span class="live-label">Novas possibilidades</span>
                    </div>
                </div>

                <div class="brand-footer">
                    <span>InCTI Insights</span>
                    <span class="footer-separator"></span>
                    <span>Inteligência para transformar</span>
                </div>
            </div>
        </aside>
    </main>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Manrope:wght@400;500;600;700;800&display=swap');

:global(*) {
    box-sizing: border-box;
}

:global(html) {
    min-height: 100%;
    width: 100%;
    overflow-x: clip;
}

:global(body) {
    min-width: 320px;
    min-height: 100vh;
    margin: 0;
    font-family: 'DM Sans', sans-serif;
    color: #010300;
    background: #fff;
}

:global(#app) {
    min-height: 100vh;
}

:global(button),
:global(input) {
    font: inherit;
}

.login-page {
    --magenta: #c73a68;
    --orange: #ff8a4c;
    --amber: #ffbe55;
    --ink: #010300;
    --muted: #747474;

    display: grid;
    grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
    width: 100%;
    min-height: 100vh;
    min-height: 100dvh;
    background: #fff;
}

/* FORMULÁRIO */

.form-side {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 0;
    padding: clamp(24px, 4vw, 64px);
    background: #fff;
}

.login-container {
    width: 100%;
    max-width: 410px;
    min-width: 0;
    margin: auto;
}

.mobile-brand {
    display: none;
    align-items: center;
    gap: 9px;
    margin-bottom: 38px;
    color: var(--ink);
    text-decoration: none;
    font-family: 'Manrope', sans-serif;
    font-size: 23px;
    font-weight: 800;
}

.brand-symbol {
    display: inline-flex;
    flex: 0 0 34px;
    align-items: center;
    justify-content: center;
    width: 34px;
    height: 34px;
    border-radius: 10px;
    color: #fff;
    background: var(--magenta);
    font-family: 'Manrope', sans-serif;
    font-size: 25px;
    font-weight: 800;
}

.form-heading {
    margin-bottom: 32px;
}

.eyebrow {
    display: block;
    margin-bottom: 12px;
    color: var(--magenta);
    font-size: 11px;
    font-weight: 700;
    letter-spacing: 1.8px;
}

.form-heading h1 {
    margin: 0;
    font-family: 'Manrope', sans-serif;
    font-size: clamp(27px, 2.5vw, 35px);
    font-weight: 800;
    letter-spacing: -1.5px;
    line-height: 1.25;
    overflow-wrap: anywhere;
}

.form-heading h1 span {
    color: var(--magenta);
}

.form-heading p {
    margin: 12px 0 0;
    color: var(--muted);
    font-size: 14px;
    line-height: 1.7;
}

.login-form {
    display: flex;
    flex-direction: column;
    gap: 21px;
    width: 100%;
}

.field {
    display: flex;
    flex-direction: column;
    gap: 9px;
    min-width: 0;
}

.field label {
    color: #292929;
    font-size: 13px;
    font-weight: 600;
}

.field input {
    display: block;
    width: 100%;
    min-width: 0;
    height: 49px;
    padding: 0 15px;
    border: 1px solid #dedede;
    border-radius: 9px;
    outline: none;
    color: var(--ink);
    background: #fff;
    font-size: 13px;
    transition: border-color .2s, box-shadow .2s;
}

.field input::placeholder {
    color: #a0a0a0;
    opacity: 1;
}

.field input:focus {
    border-color: var(--magenta);
    box-shadow: 0 0 0 3px rgb(199 58 104 / 10%);
}

.password-wrapper {
    position: relative;
    width: 100%;
    min-width: 0;
}

.password-wrapper input {
    padding-right: 48px;
}

.toggle-password {
    position: absolute;
    top: 50%;
    right: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    padding: 5px;
    border: 0;
    color: #888;
    background: transparent;
    cursor: pointer;
    transform: translateY(-50%);
}

.toggle-password svg {
    width: 19px;
    height: 19px;
}

.form-options {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 12px;
    margin-top: -3px;
}

.remember-option {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    color: #626262;
    font-size: 12px;
    cursor: pointer;
}

.remember-option input {
    width: 15px;
    height: 15px;
    margin: 0;
    accent-color: var(--magenta);
    cursor: pointer;
}

.forgot-link {
    padding: 0;
    border: 0;
    color: var(--magenta);
    background: transparent;
    font-size: 12px;
    font-weight: 600;
    text-align: right;
    cursor: pointer;
}

.forgot-link:hover,
.signup-link a:hover {
    text-decoration: underline;
}

.message {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    min-width: 0;
    margin: -3px 0 0;
    padding: 11px 12px;
    border: 1px solid #f0c5d3;
    border-radius: 8px;
    color: #a3234c;
    background: #fff5f8;
    font-size: 12px;
    line-height: 1.6;
    overflow-wrap: anywhere;
}

.message svg {
    flex: 0 0 17px;
    width: 17px;
    height: 17px;
    margin-top: 1px;
}

.submit-button {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    width: 100%;
    min-height: 49px;
    margin-top: 2px;
    padding: 13px 18px;
    border: 0;
    border-radius: 9px;
    color: #fff;
    background: var(--magenta);
    font-size: 13px;
    font-weight: 700;
    cursor: pointer;
    transition: background .2s, transform .2s, box-shadow .2s;
}

.submit-button:hover:not(:disabled) {
    background: #ad2d58;
    box-shadow: 0 7px 18px rgb(199 58 104 / 20%);
    transform: translateY(-1px);
}

.submit-button:disabled {
    cursor: wait;
    opacity: .7;
}

.submit-button svg {
    flex-shrink: 0;
    width: 18px;
    height: 18px;
}

.signup-link {
    margin-top: 26px;
    color: #777;
    text-align: center;
    font-size: 12px;
    line-height: 1.8;
}

.signup-link a {
    margin-left: 4px;
    color: var(--magenta);
    font-weight: 700;
    text-decoration: none;
}

.form-footer {
    margin: 42px 0 0;
    color: #a0a0a0;
    text-align: center;
    font-size: 11px;
}

/* PAINEL DE APRESENTAÇÃO */

.brand-side {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 0;
    min-height: 100vh;
    min-height: 100dvh;
    padding: clamp(28px, 4vw, 64px);
    border-left: 1px solid #f5e9e4;
    background: #fff8f4;
    overflow: hidden;
}

.brand-side::before {
    position: absolute;
    top: -150px;
    right: -100px;
    width: 330px;
    height: 330px;
    border-radius: 50%;
    background: rgb(255 190 85 / 15%);
    content: '';
    pointer-events: none;
}

.brand-side::after {
    position: absolute;
    bottom: -180px;
    left: -100px;
    width: 370px;
    height: 370px;
    border-radius: 50%;
    background: rgb(199 58 104 / 8%);
    content: '';
    pointer-events: none;
}

.brand-content {
    position: relative;
    z-index: 1;
    width: 100%;
    max-width: 480px;
    min-width: 0;
    margin: auto;
}

.brand-logo {
    display: inline-flex;
    align-items: center;
    gap: 10px;
    color: var(--ink);
    font-family: 'Manrope', sans-serif;
    font-size: 24px;
    font-weight: 800;
    text-decoration: none;
}

.brand-message {
    margin-top: clamp(30px, 6vh, 78px);
}

.brand-eyebrow {
    display: inline-flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 9px;
    color: #77504f;
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 1.7px;
}

.status-dot {
    flex-shrink: 0;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: var(--orange);
}

.brand-message h2 {
    margin: 20px 0 16px;
    font-family: 'Manrope', sans-serif;
    font-size: clamp(29px, 3vw, 45px);
    font-weight: 800;
    letter-spacing: -2px;
    line-height: 1.2;
    overflow-wrap: anywhere;
}

.brand-message h2 span {
    display: block;
    color: var(--magenta);
}

.brand-message p {
    max-width: 390px;
    margin: 0;
    color: #77706d;
    font-size: 14px;
    line-height: 1.9;
}

.insight-card {
    width: 100%;
    min-width: 0;
    margin-top: clamp(24px, 4vh, 42px);
    padding: clamp(16px, 2vw, 23px);
    border: 1px solid #f0e2dc;
    border-radius: 15px;
    background: rgb(255 255 255 / 88%);
    box-shadow: 0 12px 35px rgb(65 34 35 / 5%);
}

.insight-top {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
}

.insight-label {
    color: #a0a0a0;
    font-size: 9px;
    font-weight: 700;
    letter-spacing: 1.5px;
}

.insight-top h3 {
    margin: 7px 0 0;
    color: #292929;
    font-family: 'Manrope', sans-serif;
    font-size: 15px;
    font-weight: 700;
    overflow-wrap: anywhere;
}

.insight-icon {
    display: flex;
    flex: 0 0 38px;
    align-items: center;
    justify-content: center;
    width: 38px;
    height: 38px;
    border-radius: 10px;
    color: var(--magenta);
    background: #fff0f4;
}

.insight-icon svg {
    width: 21px;
    height: 21px;
}

.chart {
    position: relative;
    width: 100%;
    height: clamp(75px, 10vh, 105px);
    margin-top: 19px;
    overflow: hidden;
}

.chart-grid {
    position: absolute;
    inset: 0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.chart-grid span {
    display: block;
    width: 100%;
    border-top: 1px dashed #eee4df;
}

.chart-line {
    position: absolute;
    inset: 0;
    width: 100%;
    height: 100%;
    overflow: visible;
}

.insight-bottom {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 16px;
    padding-top: 15px;
    border-top: 1px solid #f1e9e5;
    color: #777;
    font-size: 10px;
}

.insight-bottom>span:first-child {
    display: flex;
    align-items: center;
    gap: 7px;
}

.legend-dot {
    flex-shrink: 0;
    width: 7px;
    height: 7px;
    border-radius: 50%;
    background: var(--amber);
}

.live-label {
    color: #a54a65;
    font-weight: 600;
}

.brand-footer {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 11px;
    margin-top: clamp(22px, 4vh, 38px);
    color: #918580;
    font-size: 10px;
}

.footer-separator {
    flex-shrink: 0;
    width: 4px;
    height: 4px;
    border-radius: 50%;
    background: var(--orange);
}

/* NOTEBOOKS E TELAS MÉDIAS */

@media (min-width: 1500px) {
    .form-side {
        padding-right: 9%;
        padding-left: 9%;
    }

    .brand-side {
        padding-right: 9%;
        padding-left: 9%;
    }

    .login-container {
        max-width: 430px;
    }

    .brand-content {
        max-width: 500px;
    }
}

@media (max-width: 1100px) {
    .login-page {
        grid-template-columns: minmax(0, .95fr) minmax(0, 1.05fr);
    }

    .form-side {
        padding: 36px 5%;
    }

    .brand-side {
        padding: 36px 5%;
    }

    .brand-message h2 {
        font-size: clamp(28px, 3.2vw, 36px);
        letter-spacing: -1.4px;
    }

    .brand-message p {
        font-size: 13px;
    }

    .insight-card {
        margin-top: 28px;
    }
}

/* TABLETS E CELULARES */

@media (max-width: 760px) {
    .login-page {
        display: flex;
        flex-direction: column;
        min-height: 100vh;
        min-height: 100dvh;
    }

    .form-side {
        flex: 1;
        align-items: center;
        justify-content: center;
        min-height: 100vh;
        min-height: 100dvh;
        padding: 32px 24px;
    }

    .login-container {
        max-width: 440px;
        margin: auto;
    }

    .mobile-brand {
        display: inline-flex;
        margin-bottom: 38px;
    }

    .brand-side {
        display: none;
    }

    .form-heading {
        margin-bottom: 28px;
    }

    .form-heading h1 {
        font-size: clamp(28px, 5vw, 35px);
    }

    .login-form {
        gap: 20px;
    }

    .field input {
        height: 50px;
        font-size: 14px;
    }

    .submit-button {
        min-height: 50px;
    }

    .form-footer {
        margin-top: 32px;
    }
}

/* TABLETS EM RETRATO E CELULARES GRANDES */

@media (min-width: 480px) and (max-width: 760px) {
    .form-side {
        padding: 40px 32px;
    }

    .login-container {
        max-width: 430px;
    }

    .mobile-brand {
        margin-bottom: 44px;
    }
}

/* CELULARES PEQUENOS */

@media (max-width: 380px) {
    .form-side {
        align-items: flex-start;
        padding: 25px 18px;
    }

    .login-container {
        margin: auto;
    }

    .mobile-brand {
        margin-bottom: 30px;
    }

    .form-heading h1 {
        font-size: 27px;
        letter-spacing: -1px;
    }

    .form-heading p {
        font-size: 13px;
    }

    .form-options {
        align-items: flex-start;
        gap: 12px 8px;
    }

    .remember-option,
    .forgot-link {
        font-size: 11px;
    }

    .login-form {
        gap: 18px;
    }

    .signup-link {
        font-size: 11px;
    }
}

/* TELAS BAIXAS: NOTEBOOKS COM POUCA ALTURA */

@media (min-width: 761px) and (max-height: 750px) {

    .form-side,
    .brand-side {
        padding-top: 24px;
        padding-bottom: 24px;
    }

    .form-heading {
        margin-bottom: 22px;
    }

    .login-form {
        gap: 15px;
    }

    .form-footer {
        margin-top: 24px;
    }

    .brand-message {
        margin-top: 28px;
    }

    .brand-message h2 {
        margin-top: 14px;
        margin-bottom: 12px;
    }

    .brand-message p {
        line-height: 1.65;
    }

    .insight-card {
        margin-top: 22px;
        padding: 16px;
    }

    .chart {
        height: 72px;
        margin-top: 12px;
    }

    .brand-footer {
        margin-top: 20px;
    }
}


@media (prefers-reduced-motion: reduce) {

    *,
    *::before,
    *::after {
        scroll-behavior: auto !important;
        transition-duration: .01ms !important;
        animation-duration: .01ms !important;
        animation-iteration-count: 1 !important;
    }
}
</style>
