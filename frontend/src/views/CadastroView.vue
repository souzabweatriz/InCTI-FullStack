<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

const router = useRouter()

const nomeCompleto = ref('')
const email = ref('')
const senha = ref('')
const confirmarSenha = ref('')
const carregando = ref(false)
const erro = ref('')
const sucesso = ref('')
const mostrarSenha = ref(false)
const mostrarConfirmacao = ref(false)

const frases = [
    'novas possibilidades.',
    'respostas escondidas.',
    'padrões para revelar.',
    'um futuro inteligente.'
]

const textoAtual = ref('')
let timeoutTexto

function escreverTexto() {
    let indiceFrase = 0
    let indiceLetra = 0
    let apagando = false

    function digitar() {
        const frase = frases[indiceFrase]

        if (!apagando) {
            textoAtual.value = frase.substring(0, indiceLetra + 1)
            indiceLetra++

            if (indiceLetra === frase.length) {
                apagando = true
                timeoutTexto = setTimeout(digitar, 1800)
                return
            }

            timeoutTexto = setTimeout(digitar, 65)
        } else {
            textoAtual.value = frase.substring(0, indiceLetra - 1)
            indiceLetra--

            if (indiceLetra === 0) {
                apagando = false
                indiceFrase = (indiceFrase + 1) % frases.length
                timeoutTexto = setTimeout(digitar, 350)
                return
            }

            timeoutTexto = setTimeout(digitar, 30)
        }
    }

    digitar()
}

onMounted(() => {
    escreverTexto()
})

onUnmounted(() => {
    clearTimeout(timeoutTexto)
})

async function cadastrar() {
    erro.value = ''
    sucesso.value = ''

    if (!nomeCompleto.value.trim()) {
        erro.value = 'Informe seu nome completo.'
        return
    }

    if (!email.value.trim()) {
        erro.value = 'Informe seu e-mail.'
        return
    }

    const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

    if (!emailValido.test(email.value)) {
        erro.value = 'Informe um e-mail válido.'
        return
    }

    if (!senha.value) {
        erro.value = 'Informe uma senha.'
        return
    }

    if (senha.value !== confirmarSenha.value) {
        erro.value = 'As senhas não coincidem.'
        return
    }

    carregando.value = true

    try {
        await api('/auth/register', {
            method: 'POST',
            body: JSON.stringify({
                nomeCompleto: nomeCompleto.value.trim(),
                email: email.value.trim(),
                senha: senha.value
            })
        })

        sucesso.value = 'Cadastro realizado com sucesso!'

        setTimeout(() => {
            router.push({ name: 'login' })
        }, 1200)
    } catch (e) {
        erro.value =
            e?.message || 'Não foi possível realizar o cadastro. Tente novamente.'
    } finally {
        carregando.value = false
    }
}
</script>

<template>
    <main class="cadastro-page">
        <section class="cadastro-container">

            <aside class="intro-panel">
                <div class="decor decor-one"></div>
                <div class="decor decor-two"></div>
                <div class="decor decor-three"></div>

                <div class="intro-content">
                    <div class="brand">
                        <img src="../assets/icons/logo.png" alt="Logo InCTI Insights" class="brand-logo" />
                        <span>InCTI <strong>Insights</strong></span>
                    </div>

                    <div class="intro-heading">
                        <span class="eyebrow">
                            <span class="status-dot"></span>
                            INTELIGÊNCIA EM DADOS
                        </span>

                        <h1>
                            Seus dados revelam
                            <span class="typing-text">
                                {{ textoAtual }}<span class="cursor">|</span>
                            </span>
                        </h1>

                        <p class="intro-description">
                            Transforme informações em conhecimento e descubra novas
                            oportunidades com o poder da inteligência de dados.
                        </p>
                    </div>
                </div>

                <div class="intro-footer">
                    <span>InCTI Insights</span>
                    <span>Dados que revelam possibilidades.</span>
                </div>
            </aside>

            <section class="form-panel">
                <div class="form-content">
                    <div class="form-heading">
                        <span class="form-eyebrow">COMECE AGORA</span>
                        <h2>Crie sua conta</h2>
                        <p>Preencha seus dados para acessar a plataforma.</p>
                    </div>

                    <form class="cadastro-form" @submit.prevent="cadastrar">
                        <div class="form-field">
                            <label for="nomeCompleto">Nome completo</label>

                            <div class="input-wrapper">
                                <span class="input-icon">
                                    <svg viewBox="0 0 24 24" fill="none">
                                        <circle cx="12" cy="8" r="4" />
                                        <path d="M4 21v-2a8 8 0 0 1 16 0v2" />
                                    </svg>
                                </span>

                                <input id="nomeCompleto" v-model="nomeCompleto" type="text"
                                    placeholder="Digite seu nome completo" autocomplete="name" required />
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="email">E-mail</label>

                            <div class="input-wrapper">
                                <span class="input-icon">
                                    <svg viewBox="0 0 24 24" fill="none">
                                        <rect x="3" y="5" width="18" height="14" rx="3" />
                                        <path d="m4 7 8 6 8-6" />
                                    </svg>
                                </span>

                                <input id="email" v-model="email" type="email" placeholder="seu@email.com"
                                    autocomplete="email" required />
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="senha">Senha</label>

                            <div class="input-wrapper">
                                <span class="input-icon">
                                    <svg viewBox="0 0 24 24" fill="none">
                                        <rect x="4" y="10" width="16" height="11" rx="3" />
                                        <path d="M8 10V7a4 4 0 0 1 8 0v3" />
                                    </svg>
                                </span>

                                <input id="senha" v-model="senha" :type="mostrarSenha ? 'text' : 'password'"
                                    placeholder="Digite sua senha" autocomplete="new-password" required />

                                <button type="button" class="toggle-password"
                                    :aria-label="mostrarSenha ? 'Ocultar senha' : 'Mostrar senha'"
                                    @click="mostrarSenha = !mostrarSenha">
                                    <svg v-if="!mostrarSenha" viewBox="0 0 24 24" fill="none">
                                        <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7S2 12 2 12Z" />
                                        <circle cx="12" cy="12" r="3" />
                                    </svg>

                                    <svg v-else viewBox="0 0 24 24" fill="none">
                                        <path d="m3 3 18 18" />
                                        <path d="M10.6 5.2A10 10 0 0 1 12 5c6.4 0 10 7 10 7a15 15 0 0 1-4 4.7" />
                                        <path d="M6.2 6.2C3.5 8 2 12 2 12s3.6 7 10 7a9 9 0 0 0 4-.9" />
                                    </svg>
                                </button>
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="confirmarSenha">Confirmar senha</label>

                            <div class="input-wrapper">
                                <span class="input-icon">
                                    <svg viewBox="0 0 24 24" fill="none">
                                        <rect x="4" y="10" width="16" height="11" rx="3" />
                                        <path d="M8 10V7a4 4 0 0 1 8 0v3" />
                                    </svg>
                                </span>

                                <input id="confirmarSenha" v-model="confirmarSenha"
                                    :type="mostrarConfirmacao ? 'text' : 'password'"
                                    placeholder="Digite sua senha novamente" autocomplete="new-password" required />

                                <button type="button" class="toggle-password"
                                    :aria-label="mostrarConfirmacao ? 'Ocultar senha' : 'Mostrar senha'"
                                    @click="mostrarConfirmacao = !mostrarConfirmacao">
                                    <svg v-if="!mostrarConfirmacao" viewBox="0 0 24 24" fill="none">
                                        <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7S2 12 2 12Z" />
                                        <circle cx="12" cy="12" r="3" />
                                    </svg>

                                    <svg v-else viewBox="0 0 24 24" fill="none">
                                        <path d="m3 3 18 18" />
                                        <path d="M10.6 5.2A10 10 0 0 1 12 5c6.4 0 10 7 10 7a15 15 0 0 1-4 4.7" />
                                        <path d="M6.2 6.2C3.5 8 2 12 2 12s3.6 7 10 7a9 9 0 0 0 4-.9" />
                                    </svg>
                                </button>
                            </div>
                        </div>

                        <p v-if="erro" class="message message-error" role="alert">
                            {{ erro }}
                        </p>

                        <p v-if="sucesso" class="message message-success" role="status">
                            {{ sucesso }}
                        </p>

                        <button type="submit" class="submit-button" :disabled="carregando">
                            <span v-if="carregando" class="loading-spinner"></span>

                            {{ carregando ? 'Criando conta...' : 'Criar minha conta' }}

                            <svg v-if="!carregando" viewBox="0 0 24 24" fill="none">
                                <path d="M5 12h14M13 6l6 6-6 6" />
                            </svg>
                        </button>
                    </form>

                    <p class="login-link">
                        Já possui uma conta?
                        <RouterLink to="/login">Entrar</RouterLink>
                    </p>

                    <p class="form-legal">
                        Ao criar sua conta, você poderá explorar os recursos da
                        plataforma InCTI Insights.
                    </p>
                </div>
            </section>

        </section>
    </main>
</template>

<style scoped>
* {
    box-sizing: border-box;
}

.cadastro-page {
    min-height: 100vh;
    padding: 28px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f7f7fa;
    color: #252331;
    font-family: Inter, "Segoe UI", Arial, sans-serif;
}

.cadastro-container {
    width: 100%;
    max-width: 1120px;
    min-height: 690px;
    display: grid;
    grid-template-columns: 1fr 1fr;
    overflow: hidden;
    background: #fff;
    border: 1px solid #eceaf0;
    border-radius: 26px;
    box-shadow: 0 24px 80px rgba(40, 25, 45, 0.09);
}

/* Painel lateral */

.intro-panel {
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 35px;
    padding: 42px;
    overflow: hidden;
    color: #fff;
    background:
        radial-gradient(ellipse at 12% 88%, rgba(255, 190, 85, 0.2), transparent 42%),
        radial-gradient(ellipse at 90% 5%, rgba(255, 138, 76, 0.23), transparent 36%),
        linear-gradient(145deg, #38172c 0%, #702b4c 48%, #c73a68 100%);
}

.decor {
    position: absolute;
    z-index: 0;
    border: 1px solid rgba(255, 255, 255, 0.13);
    border-radius: 50%;
    pointer-events: none;
}

.decor-one {
    width: 360px;
    height: 360px;
    right: -205px;
    top: 80px;
}

.decor-two {
    width: 270px;
    height: 270px;
    right: -160px;
    top: 125px;
}

.decor-three {
    width: 180px;
    height: 180px;
    right: -115px;
    top: 170px;
    background: rgba(255, 190, 85, 0.035);
}

.intro-content,
.intro-footer {
    position: relative;
    z-index: 1;
}

.brand {
    display: flex;
    align-items: center;
    gap: 11px;
    font-size: 21px;
    font-weight: 500;
    letter-spacing: -0.5px;
}

.brand-logo {
    width: 40px;
    height: 40px;
    object-fit: contain;
}

.brand strong {
    font-weight: 800;
}

.intro-heading {
    margin-top: 76px;
}

.eyebrow,
.form-eyebrow {
    display: inline-flex;
    align-items: center;
    gap: 9px;
    font-size: 10px;
    font-weight: 800;
    letter-spacing: 2px;
}

.eyebrow {
    color: #ffd4bd;
}

.status-dot {
    width: 7px;
    height: 7px;
    flex-shrink: 0;
    border-radius: 50%;
    background: #ffbe55;
    box-shadow: 0 0 12px rgba(255, 190, 85, 0.75);
    animation: pulse 1.8s ease-in-out infinite;
}

.intro-heading h1 {
    max-width: 450px;
    margin: 22px 0 18px;
    font-size: clamp(32px, 3.3vw, 44px);
    font-weight: 750;
    line-height: 1.17;
    letter-spacing: -1.8px;
}

.typing-text {
    display: block;
    min-height: 1.25em;
    color: #ffbe55;
    overflow-wrap: anywhere;
}

.cursor {
    display: inline-block;
    margin-left: 2px;
    color: #ffbe55;
    animation: blink 0.8s steps(2, start) infinite;
}

.intro-description {
    max-width: 380px;
    margin: 0;
    color: rgba(255, 255, 255, 0.76);
    font-size: 14px;
    line-height: 1.85;
}

/* Benefícios */

.feature-list {
    display: grid;
    gap: 23px;
    margin-top: 35px;
}

.feature-item {
    display: flex;
    align-items: flex-start;
    gap: 14px;
    animation: slide-in 0.7s ease both;
}

.feature-item:nth-child(2) {
    animation-delay: 0.15s;
}

.feature-item:nth-child(3) {
    animation-delay: 0.3s;
}

.feature-icon {
    width: 34px;
    height: 34px;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(255, 255, 255, 0.23);
    border-radius: 10px;
    background: rgba(255, 255, 255, 0.09);
    color: #ffcf8a;
    font-size: 18px;
}

.feature-item strong {
    display: block;
    margin: 1px 0 5px;
    color: #fff;
    font-size: 12px;
    font-weight: 750;
}

.feature-item p {
    margin: 0;
    color: rgba(255, 255, 255, 0.66);
    font-size: 11px;
    line-height: 1.6;
}

.intro-footer {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 12px;
    color: rgba(255, 255, 255, 0.55);
    font-size: 10px;
}

/* Formulário */

.form-panel {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 46px 48px;
    background: #fff;
}

.form-content {
    width: 100%;
    max-width: 390px;
}

.form-heading {
    margin-bottom: 30px;
}

.form-eyebrow {
    margin-bottom: 12px;
    color: #c73a68;
}

.form-heading h2 {
    margin: 0 0 10px;
    color: #282431;
    font-size: 30px;
    font-weight: 800;
    letter-spacing: -1px;
}

.form-heading p {
    margin: 0;
    color: #888391;
    font-size: 13px;
    line-height: 1.7;
}

.cadastro-form {
    display: grid;
    gap: 18px;
}

.form-field {
    display: grid;
    gap: 8px;
}

.form-field label {
    color: #393441;
    font-size: 12px;
    font-weight: 700;
}

.input-wrapper {
    min-height: 49px;
    display: flex;
    align-items: center;
    gap: 11px;
    padding: 0 13px;
    border: 1px solid #e7e3ea;
    border-radius: 10px;
    background: #fff;
    transition: border-color 0.2s, box-shadow 0.2s;
}

.input-wrapper:focus-within {
    border-color: #c73a68;
    box-shadow: 0 0 0 3px rgba(199, 58, 104, 0.09);
}

.input-icon {
    width: 19px;
    height: 19px;
    flex-shrink: 0;
    color: #a29ba9;
}

.input-icon svg,
.toggle-password svg,
.submit-button svg {
    width: 100%;
    height: 100%;
    stroke: currentColor;
    stroke-width: 1.7;
    stroke-linecap: round;
    stroke-linejoin: round;
}

.input-wrapper input {
    width: 100%;
    min-width: 0;
    height: 46px;
    padding: 0;
    border: 0;
    outline: none;
    background: transparent;
    color: #302b37;
    font-family: inherit;
    font-size: 12px;
}

.input-wrapper input::placeholder {
    color: #b4aeb9;
}

.toggle-password {
    width: 19px;
    height: 19px;
    flex-shrink: 0;
    padding: 0;
    border: 0;
    background: transparent;
    color: #a29ba9;
    cursor: pointer;
}

.message {
    margin: 0;
    padding: 11px 12px;
    border-radius: 8px;
    font-size: 12px;
    line-height: 1.5;
}

.message-error {
    border: 1px solid #f5d0d8;
    background: #fff3f5;
    color: #b42346;
}

.message-success {
    border: 1px solid #c6ecd2;
    background: #f0fff4;
    color: #237541;
}

.submit-button {
    width: 100%;
    min-height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    margin-top: 3px;
    padding: 12px 18px;
    border: 0;
    border-radius: 10px;
    background: linear-gradient(110deg, #c73a68, #e45d68);
    box-shadow: 0 8px 20px rgba(199, 58, 104, 0.19);
    color: #fff;
    font-family: inherit;
    font-size: 13px;
    font-weight: 750;
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s, opacity 0.2s;
}

.submit-button:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 11px 24px rgba(199, 58, 104, 0.26);
}

.submit-button:disabled {
    cursor: not-allowed;
    opacity: 0.7;
}

.submit-button svg {
    width: 17px;
    height: 17px;
}

.loading-spinner {
    width: 15px;
    height: 15px;
    border: 2px solid rgba(255, 255, 255, 0.4);
    border-top-color: #fff;
    border-radius: 50%;
    animation: spin 0.7s linear infinite;
}

.login-link {
    margin: 24px 0 0;
    color: #888391;
    font-size: 12px;
    text-align: center;
}

.login-link a {
    margin-left: 4px;
    color: #c73a68;
    font-weight: 800;
    text-decoration: none;
}

.login-link a:hover {
    text-decoration: underline;
}

.form-legal {
    margin: 23px 0 0;
    color: #aaa4b0;
    font-size: 10px;
    line-height: 1.7;
    text-align: center;
}

/* Animações */

@keyframes blink {
    to {
        visibility: hidden;
    }
}

@keyframes pulse {

    0%,
    100% {
        opacity: 1;
        transform: scale(1);
    }

    50% {
        opacity: 0.5;
        transform: scale(0.8);
    }
}

@keyframes slide-in {
    from {
        opacity: 0;
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}

/* Responsividade */

@media (max-width: 900px) {
    .cadastro-page {
        padding: 18px;
    }

    .cadastro-container {
        max-width: 600px;
        grid-template-columns: 1fr;
    }

    .intro-panel {
        min-height: 550px;
        padding: 32px;
    }

    .intro-heading {
        margin-top: 45px;
    }

    .intro-heading h1 {
        font-size: 36px;
    }

    .feature-list {
        gap: 18px;
        margin-top: 28px;
    }

    .form-panel {
        padding: 42px 32px;
    }
}

@media (max-width: 480px) {
    .cadastro-page {
        padding: 0;
        align-items: flex-start;
        background: #fff;
    }

    .cadastro-container {
        min-height: 100vh;
        border: 0;
        border-radius: 0;
        box-shadow: none;
    }

    .intro-panel {
        min-height: auto;
        padding: 26px 23px;
    }

    .brand {
        font-size: 19px;
    }

    .brand-logo {
        width: 34px;
        height: 34px;
    }

    .intro-heading {
        margin-top: 38px;
    }

    .intro-heading h1 {
        margin-top: 18px;
        font-size: 32px;
    }

    .intro-description {
        font-size: 13px;
    }

    .feature-list {
        gap: 17px;
        margin-top: 27px;
    }

    .intro-footer {
        margin-top: 30px;
    }

    .form-panel {
        padding: 34px 23px 40px;
    }

    .form-heading h2 {
        font-size: 27px;
    }
}

@media (prefers-reduced-motion: reduce) {

    *,
    *::before,
    *::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        scroll-behavior: auto !important;
    }
}
</style>
