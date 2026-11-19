<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

function carregarUsuario() {
    try {
        return JSON.parse(localStorage.getItem('cti_usuario') || '{}')
    } catch {
        return {}
    }
}

const usuario = ref(carregarUsuario())
const editando = ref(false)
const mensagem = ref('')
const erro = ref('')
const inputFoto = ref(null)

const formulario = ref({
    nomeCompleto: usuario.value.nomeCompleto || usuario.value.nome || '',
    email: usuario.value.email || '',
    cargo: usuario.value.cargo || '',
    setor: usuario.value.setor || '',
    telefone: usuario.value.telefone || '',
    sobre: usuario.value.sobre || '',
    foto: usuario.value.foto || ''
})

const iniciais = computed(() => {
    const nome = formulario.value.nomeCompleto.trim()

    if (!nome) return 'U'

    return nome
        .split(/\s+/)
        .slice(0, 2)
        .map(parte => parte[0])
        .join('')
        .toUpperCase()
})

const perfil = computed(() => {
    const valor = usuario.value.perfil || usuario.value.tipoPerfil || 'USUARIO'

    return String(valor)
        .replaceAll('_', ' ')
        .toLowerCase()
        .replace(/\b\w/g, letra => letra.toUpperCase())
})

function selecionarFoto() {
    inputFoto.value?.click()
}

function alterarFoto(event) {
    const arquivo = event.target.files?.[0]

    event.target.value = ''

    if (!arquivo) return

    if (!arquivo.type.startsWith('image/')) {
        erro.value = 'Selecione um arquivo de imagem válido.'
        return
    }

    if (arquivo.size > 2 * 1024 * 1024) {
        erro.value = 'A imagem deve ter no máximo 2 MB.'
        return
    }

    const leitor = new FileReader()

    leitor.onload = () => {
        formulario.value.foto = leitor.result
        mensagem.value = ''
        erro.value = ''
    }

    leitor.onerror = () => {
        erro.value = 'Não foi possível carregar a imagem.'
    }

    leitor.readAsDataURL(arquivo)
}

function removerFoto() {
    formulario.value.foto = ''
    mensagem.value = ''
    erro.value = ''
}

function salvarPerfil() {
    erro.value = ''
    mensagem.value = ''

    if (!formulario.value.nomeCompleto.trim()) {
        erro.value = 'Informe seu nome completo.'
        return
    }

    if (!formulario.value.email.trim()) {
        erro.value = 'Informe seu e-mail.'
        return
    }

    const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

    if (!emailValido.test(formulario.value.email)) {
        erro.value = 'Informe um e-mail válido.'
        return
    }

    const usuarioAtualizado = {
        ...usuario.value,
        nomeCompleto: formulario.value.nomeCompleto.trim(),
        email: formulario.value.email.trim(),
        cargo: formulario.value.cargo.trim(),
        setor: formulario.value.setor.trim(),
        telefone: formulario.value.telefone.trim(),
        sobre: formulario.value.sobre.trim(),
        foto: formulario.value.foto
    }

    try {
        localStorage.setItem('cti_usuario', JSON.stringify(usuarioAtualizado))

        usuario.value = usuarioAtualizado
        formulario.value.nomeCompleto = usuarioAtualizado.nomeCompleto
        formulario.value.email = usuarioAtualizado.email

        editando.value = false
        mensagem.value = 'Perfil atualizado com sucesso!'
    } catch {
        erro.value =
            'Não foi possível salvar. Tente usar uma imagem menor.'
    }
}

function cancelarEdicao() {
    formulario.value = {
        nomeCompleto: usuario.value.nomeCompleto || usuario.value.nome || '',
        email: usuario.value.email || '',
        cargo: usuario.value.cargo || '',
        setor: usuario.value.setor || '',
        telefone: usuario.value.telefone || '',
        sobre: usuario.value.sobre || '',
        foto: usuario.value.foto || ''
    }

    editando.value = false
    mensagem.value = ''
    erro.value = ''
}

function iniciarEdicao() {
    mensagem.value = ''
    erro.value = ''
    editando.value = true
}

function sair() {
    localStorage.removeItem('cti_logado')
    localStorage.removeItem('cti_token')
    localStorage.removeItem('cti_usuario')

    router.push({ name: 'login' })
}
</script>

<template>
    <main class="perfil-page">

        <section class="page-content">
            <div class="page-heading">
                <div>
                    <span class="eyebrow">MINHA CONTA</span>
                    <h1>Meu perfil</h1>
                    <p>Personalize suas informações e deixe seu perfil com a sua cara.</p>
                </div>

                <div class="heading-actions">
                    <button v-if="!editando" class="edit-button" type="button" @click="iniciarEdicao">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="m15 5 4 4M4 20l4.5-1 11-11a2.1 2.1 0 0 0-3-3l-11 11L4 20Z" />
                        </svg>
                        Editar perfil
                    </button>

                    <button class="logout-button" type="button" @click="sair">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M9 4H6a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h3M14 17l5-5-5-5M19 12H8" />
                        </svg>
                        Sair
                    </button>
                </div>
            </div>

            <section class="profile-layout">
                <aside class="profile-sidebar">
                    <div class="sidebar-cover">
                        <span>INCTI INSIGHTS</span>
                        <div class="cover-decoration"></div>
                    </div>

                    <div class="sidebar-content">
                        <div class="avatar-container">
                            <img v-if="formulario.foto" :src="formulario.foto" alt="Foto de perfil"
                                class="avatar-image" />

                            <div v-else class="avatar-placeholder">
                                {{ iniciais }}
                            </div>

                            <button v-if="editando" class="photo-button" type="button" aria-label="Alterar foto"
                                title="Alterar foto" @click="selecionarFoto">
                                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                                    <path d="M4 8h3l2-3h6l2 3h3v11H4V8Z" />
                                    <circle cx="12" cy="13" r="3.5" />
                                </svg>
                            </button>

                            <input ref="inputFoto" class="hidden-input" type="file" accept="image/*"
                                @change="alterarFoto" />
                        </div>

                        <button v-if="editando && formulario.foto" class="remove-photo" type="button"
                            @click="removerFoto">
                            Remover foto
                        </button>

                        <h2>{{ formulario.nomeCompleto || 'Seu nome' }}</h2>

                        <p class="sidebar-email">
                            {{ formulario.email || 'Seu e-mail' }}
                        </p>

                        <span class="profile-badge">
                            <span class="badge-dot"></span>
                            {{ perfil }}
                        </span>

                        <div class="sidebar-divider"></div>

                        <div class="quick-info">
                            <div class="quick-icon">✦</div>

                            <div>
                                <span class="quick-label">CARGO</span>
                                <p>{{ formulario.cargo || 'Não informado' }}</p>
                            </div>
                        </div>

                        <div class="quick-info">
                            <div class="quick-icon">⌂</div>

                            <div>
                                <span class="quick-label">SETOR</span>
                                <p>{{ formulario.setor || 'Não informado' }}</p>
                            </div>
                        </div>

                        <div class="sidebar-tip" v-if="editando">
                            <strong>Dica</strong>
                            <p>
                                Escolha uma foto quadrada para ter um resultado melhor.
                                O limite é de 2 MB.
                            </p>
                        </div>
                    </div>
                </aside>

                <section class="profile-main">
                    <form class="profile-form" @submit.prevent="salvarPerfil">
                        <div class="section-heading">
                            <div>
                                <h3>Informações pessoais</h3>
                                <p>Seus dados de identificação.</p>
                            </div>

                            <span class="section-number">01</span>
                        </div>

                        <div class="fields-grid">
                            <div class="field">
                                <label for="nomeCompleto">Nome completo</label>

                                <input id="nomeCompleto" v-model="formulario.nomeCompleto" type="text"
                                    placeholder="Digite seu nome" autocomplete="name" :readonly="!editando" />
                            </div>

                            <div class="field">
                                <label for="email">E-mail</label>

                                <input id="email" v-model="formulario.email" type="email" placeholder="seu@email.com"
                                    autocomplete="email" :readonly="!editando" />
                            </div>

                            <div class="field">
                                <label for="telefone">Telefone</label>

                                <input id="telefone" v-model="formulario.telefone" type="tel"
                                    placeholder="(00) 00000-0000" autocomplete="tel" :readonly="!editando" />
                            </div>

                            <div class="field">
                                <label for="cargo">Cargo</label>

                                <input id="cargo" v-model="formulario.cargo" type="text"
                                    placeholder="Ex.: Analista de dados" :readonly="!editando" />
                            </div>

                            <div class="field">
                                <label for="setor">Setor / Departamento</label>

                                <input id="setor" v-model="formulario.setor" type="text"
                                    placeholder="Ex.: Tecnologia da Informação" :readonly="!editando" />
                            </div>

                            <div class="field">
                                <label for="perfil">Tipo de acesso</label>

                                <input id="perfil" :value="perfil" type="text" readonly />
                            </div>

                            <div class="field field-full">
                                <label for="sobre">Sobre mim</label>

                                <textarea id="sobre" v-model="formulario.sobre"
                                    placeholder="Conte um pouco sobre você, sua função ou suas atividades..."
                                    maxlength="300" :readonly="!editando"></textarea>

                                <small>{{ formulario.sobre.length }}/300 caracteres</small>
                            </div>
                        </div>

                        <div class="account-status">
                            <span class="status-icon">✓</span>

                            <div>
                                <strong>Conta conectada</strong>
                                <p>Sua sessão está ativa neste navegador.</p>
                            </div>
                        </div>

                        <p v-if="erro" class="feedback feedback-error" role="alert">
                            {{ erro }}
                        </p>

                        <p v-if="mensagem" class="feedback feedback-success" role="status">
                            {{ mensagem }}
                        </p>

                        <div v-if="editando" class="form-actions">
                            <button class="cancel-button" type="button" @click="cancelarEdicao">
                                Cancelar
                            </button>

                            <button class="save-button" type="submit">
                                Salvar alterações
                            </button>
                        </div>
                    </form>
                </section>
            </section>

        </section>
    </main>
</template>

<style scoped>
* {
    box-sizing: border-box;
}

.perfil-page {
    min-height: 100vh;
    color: #352633;
    font-family: Inter, "Segoe UI", Arial, sans-serif;
}










.heading-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

.logout-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 7px;
    padding: 9px 15px;
    border: 1px solid #eadce5;
    border-radius: 8px;
    background: white;
    color: #9b315b;
    font: inherit;
    font-size: 12px;
    font-weight: 700;
    cursor: pointer;
}

.logout-button svg {
    width: 15px;
    height: 15px;
    stroke: currentColor;
    stroke-width: 2;
    stroke-linecap: round;
    stroke-linejoin: round;
}

.logout-button:hover {
    background: #fff5f9;
    border-color: #c96b8e;
}

.page-content {
    width: 100%;
    max-width: 1180px;
    margin: 0 auto;
    padding: 1rem 0 22px;
}

.page-heading {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;
    margin-bottom: 22px;
}

.eyebrow {
    color: #b43b68;
    font-size: 10px;
    font-weight: 800;
    letter-spacing: 1.8px;
}

.page-heading h1 {
    margin: 5px 0;
    color: #352330;
    font-size: 29px;
    letter-spacing: -1px;
}

.page-heading p {
    margin: 0;
    color: #8d808c;
    font-size: 12px;
    line-height: 1.6;
}

.edit-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    min-height: 39px;
    padding: 0 14px;
    flex-shrink: 0;
    border: 1px solid #eadce5;
    border-radius: 9px;
    background: white;
    color: #9b315b;
    font: inherit;
    font-size: 12px;
    font-weight: 700;
    cursor: pointer;
}

.edit-button:hover {
    background: #fff5f9;
    border-color: #c96b8e;
}

.edit-button svg {
    width: 15px;
    height: 15px;
    stroke: currentColor;
    stroke-width: 1.8;
    stroke-linecap: round;
    stroke-linejoin: round;
}

.profile-layout {
    display: grid;
    grid-template-columns: 290px minmax(0, 1fr);
    align-items: start;
    gap: 20px;
}

.profile-sidebar,
.profile-main {
    min-width: 0;
    overflow: hidden;
    border: 1px solid #eee5ed;
    border-radius: 13px;
    background: #fff;
    box-shadow: 0 5px 18px rgba(53, 30, 47, 0.035);
}

.sidebar-cover {
    position: relative;
    height: 90px;
    display: flex;
    align-items: flex-start;
    padding: 19px;
    overflow: hidden;
    background:
        radial-gradient(circle at 90% 10%, rgba(255, 190, 85, 0.25), transparent 35%),
        linear-gradient(115deg, #38172c, #702b4c 55%, #c73a68);
}

.sidebar-cover span {
    position: relative;
    z-index: 1;
    color: rgba(255, 255, 255, 0.85);
    font-size: 9px;
    font-weight: 800;
    letter-spacing: 1.7px;
}

.cover-decoration {
    position: absolute;
    width: 170px;
    height: 170px;
    top: -115px;
    right: -20px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    border-radius: 50%;
}

.sidebar-content {
    padding: 0 20px 21px;
}

.avatar-container {
    position: relative;
    width: 90px;
    height: 90px;
    margin-top: -40px;
    margin-bottom: 12px;
}

.avatar-image,
.avatar-placeholder {
    width: 90px;
    height: 90px;
    border: 4px solid #fff;
    border-radius: 50%;
    box-shadow: 0 3px 12px rgba(70, 30, 55, 0.12);
}

.avatar-image {
    display: block;
    object-fit: cover;
}

.avatar-placeholder {
    display: grid;
    place-items: center;
    background: linear-gradient(145deg, #f5dce7, #e8b5cb);
    color: #762a4b;
    font-size: 27px;
    font-weight: 800;
}

.photo-button {
    position: absolute;
    right: -1px;
    bottom: 2px;
    width: 30px;
    height: 30px;
    display: grid;
    place-items: center;
    border: 3px solid white;
    border-radius: 50%;
    background: #a73360;
    color: white;
    cursor: pointer;
}

.photo-button:hover {
    background: #702b4c;
}

.photo-button svg {
    width: 15px;
    height: 15px;
    stroke: currentColor;
    stroke-width: 1.7;
    stroke-linejoin: round;
}

.hidden-input {
    display: none;
}

.remove-photo {
    display: block;
    margin: -5px 0 10px;
    padding: 0;
    border: 0;
    background: none;
    color: #a73360;
    font-size: 11px;
    cursor: pointer;
}

.sidebar-content h2 {
    margin: 0 0 5px;
    overflow-wrap: anywhere;
    color: #352330;
    font-size: 18px;
    font-weight: 800;
    letter-spacing: -0.4px;
}

.sidebar-email {
    margin: 0 0 12px;
    overflow-wrap: anywhere;
    color: #928391;
    font-size: 11px;
    line-height: 1.6;
}

.profile-badge {
    display: inline-flex;
    align-items: center;
    gap: 7px;
    padding: 6px 10px;
    border: 1px solid #f1dce7;
    border-radius: 20px;
    background: #fff5f9;
    color: #a73360;
    font-size: 10px;
    font-weight: 800;
}

.badge-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #c73a68;
}

.sidebar-divider {
    height: 1px;
    margin: 19px 0;
    background: #f0e9ef;
}

.quick-info {
    display: flex;
    align-items: center;
    gap: 11px;
    margin-top: 15px;
}

.quick-icon {
    width: 34px;
    height: 34px;
    flex-shrink: 0;
    display: grid;
    place-items: center;
    border-radius: 9px;
    background: #fff3f7;
    color: #ad3c67;
    font-size: 16px;
}

.quick-info>div:last-child {
    min-width: 0;
}

.quick-label {
    color: #a093a0;
    font-size: 9px;
    font-weight: 800;
    letter-spacing: 1px;
}

.quick-info p {
    margin: 4px 0 0;
    overflow-wrap: anywhere;
    color: #554451;
    font-size: 12px;
    font-weight: 600;
    line-height: 1.5;
}

.sidebar-tip {
    margin-top: 20px;
    padding: 12px;
    border: 1px solid #f0e0b9;
    border-radius: 9px;
    background: #fffcf5;
}

.sidebar-tip strong {
    color: #8e672f;
    font-size: 11px;
}

.sidebar-tip p {
    margin: 5px 0 0;
    color: #88785f;
    font-size: 10px;
    line-height: 1.6;
}

.profile-form {
    padding: 25px;
}

.section-heading {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 15px;
    margin-bottom: 22px;
}

.section-heading h3 {
    margin: 0 0 5px;
    color: #3b2b39;
    font-size: 15px;
    font-weight: 800;
}

.section-heading p {
    margin: 0;
    color: #968895;
    font-size: 11px;
    line-height: 1.6;
}

.section-number {
    color: #c08a40;
    font-size: 12px;
    font-weight: 800;
}

.fields-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 19px 16px;
}

.field {
    display: flex;
    flex-direction: column;
    gap: 7px;
    min-width: 0;
}

.field-full {
    grid-column: 1 / -1;
}

.field label {
    color: #5b4a58;
    font-size: 11px;
    font-weight: 750;
}

.field input,
.field textarea {
    width: 100%;
    min-width: 0;
    border: 1px solid #eae2e9;
    border-radius: 8px;
    outline: none;
    background: #fcfafc;
    color: #443541;
    font-family: inherit;
    font-size: 12px;
    transition: border-color 0.2s, box-shadow 0.2s;
}

.field input {
    min-height: 42px;
    padding: 0 12px;
}

.field textarea {
    min-height: 95px;
    padding: 12px;
    resize: vertical;
    line-height: 1.6;
}

.field input:focus:not(:read-only),
.field textarea:focus:not(:read-only) {
    border-color: #bd4b76;
    background: #fff;
    box-shadow: 0 0 0 3px rgba(189, 75, 118, 0.09);
}

.field input:read-only,
.field textarea:read-only {
    color: #817481;
    cursor: default;
}

.field small {
    align-self: flex-end;
    color: #a196a1;
    font-size: 10px;
}

.account-status {
    display: flex;
    align-items: center;
    gap: 11px;
    margin-top: 24px;
    padding: 13px;
    border: 1px solid #dceee4;
    border-radius: 9px;
    background: #f5fbf7;
}

.status-icon {
    width: 29px;
    height: 29px;
    display: grid;
    place-items: center;
    flex-shrink: 0;
    border-radius: 50%;
    background: #dff3e7;
    color: #288653;
    font-size: 14px;
    font-weight: 800;
}

.account-status strong {
    color: #397b58;
    font-size: 11px;
}

.account-status p {
    margin: 4px 0 0;
    color: #69917a;
    font-size: 10px;
}

.feedback {
    margin: 17px 0 0;
    padding: 11px 13px;
    border-radius: 8px;
    font-size: 12px;
    line-height: 1.6;
}

.feedback-error {
    border: 1px solid #f0cbd2;
    background: #fff2f4;
    color: #b4233d;
}

.feedback-success {
    border: 1px solid #c4e7d2;
    background: #f0fbf4;
    color: #24744b;
}

.form-actions {
    display: flex;
    justify-content: flex-end;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 22px;
    padding-top: 18px;
    border-top: 1px solid #f0e9ef;
}

.cancel-button,
.save-button {
    min-height: 39px;
    padding: 0 16px;
    border-radius: 8px;
    font-family: inherit;
    font-size: 11px;
    font-weight: 750;
    cursor: pointer;
    transition: background 0.2s, transform 0.2s;
}

.cancel-button {
    border: 1px solid #e7dce5;
    background: white;
    color: #756572;
}

.cancel-button:hover {
    background: #faf6f9;
}

.save-button {
    border: 1px solid transparent;
    background: linear-gradient(110deg, #8d2e55, #c73a68);
    color: white;
    box-shadow: 0 4px 10px rgba(166, 49, 95, 0.12);
}

.save-button:hover {
    transform: translateY(-1px);
}



@media (max-width: 850px) {
    .profile-layout {
        grid-template-columns: 240px minmax(0, 1fr);
        gap: 14px;
    }

    .sidebar-content {
        padding-right: 15px;
        padding-left: 15px;
    }

    .profile-form {
        padding: 20px;
    }

    .fields-grid {
        grid-template-columns: 1fr;
        gap: 15px;
    }

    .field-full {
        grid-column: auto;
    }
}

@media (max-width: 620px) {





    .logout-button {
        padding: 8px 11px;
    }

    .page-content {
        padding-top: 0.5rem;
    }

    .page-heading {
        flex-direction: column;
    }

    .heading-actions {
        width: 100%;
    }

    .heading-actions > * {
        flex: 1;
    }

    .page-heading {
        align-items: flex-start;
    }

    .page-heading h1 {
        font-size: 25px;
    }

    .page-heading p {
        max-width: 260px;
    }

    .edit-button {
        min-height: 35px;
        padding: 0 10px;
        font-size: 10px;
    }

    .edit-button svg {
        width: 13px;
        height: 13px;
    }

    .profile-layout {
        grid-template-columns: minmax(0, 1fr);
        gap: 15px;
    }

    .profile-sidebar {
        display: grid;
        grid-template-columns: 1fr;
    }

    .sidebar-cover {
        height: 75px;
    }

    .sidebar-content {
        padding: 0 18px 18px;
    }

    .avatar-container {
        width: 78px;
        height: 78px;
        margin-top: -35px;
    }

    .avatar-image,
    .avatar-placeholder {
        width: 78px;
        height: 78px;
    }

    .avatar-placeholder {
        font-size: 23px;
    }

    .profile-form {
        padding: 19px 16px;
    }

}

@media (prefers-reduced-motion: reduce) {

    *,
    *::before,
    *::after {
        transition-duration: 0.01ms !important;
    }
}
</style>