<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

function carregarUsuario() {
  try {
    const dados = localStorage.getItem('cti_usuario')
    return dados ? JSON.parse(dados) : {}
  } catch {
    return {}
  }
}

const usuario = ref(carregarUsuario())
const editando = ref(false)
const mensagem = ref('')
const erro = ref('')

const formulario = ref({
  nomeCompleto: usuario.value.nomeCompleto || usuario.value.nome || '',
  email: usuario.value.email || ''
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
    email: formulario.value.email.trim()
  }

  try {
    localStorage.setItem('cti_usuario', JSON.stringify(usuarioAtualizado))

    usuario.value = usuarioAtualizado
    formulario.value.nomeCompleto = usuarioAtualizado.nomeCompleto
    formulario.value.email = usuarioAtualizado.email
    editando.value = false
    mensagem.value = 'Alterações salvas neste navegador.'
  } catch {
    erro.value = 'Não foi possível salvar as alterações.'
  }
}

function cancelarEdicao() {
  formulario.value.nomeCompleto =
    usuario.value.nomeCompleto || usuario.value.nome || ''

  formulario.value.email = usuario.value.email || ''
  editando.value = false
  mensagem.value = ''
  erro.value = ''
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
    <header class="topbar">
      <RouterLink to="/dashboard" class="brand">
        <span class="brand-symbol">C</span>

        <span class="brand-name">
          InCTI <strong>Insights</strong>
        </span>
      </RouterLink>

      <nav class="topbar-actions">
        <RouterLink to="/dashboard" class="back-link">
          <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
            <path d="M15 18l-6-6 6-6" />
          </svg>

          Voltar ao dashboard
        </RouterLink>

        <button class="logout-button" type="button" @click="sair">
          Sair
        </button>
      </nav>
    </header>

    <section class="page-content">
      <div class="page-heading">
        <div>
          <span class="eyebrow">SUA CONTA</span>
          <h1>Meu perfil</h1>
          <p>Consulte e atualize suas informações pessoais.</p>
        </div>

        <div class="heading-decoration">✦</div>
      </div>

      <section class="profile-card">
        <div class="profile-cover">
          <div class="cover-circle circle-one"></div>
          <div class="cover-circle circle-two"></div>

          <span class="cover-label">INCTI INSIGHTS</span>
          <span class="cover-star">✦</span>
        </div>

        <div class="profile-summary">
          <div class="avatar">
            {{ iniciais }}
          </div>

          <div class="summary-info">
            <h2>
              {{ formulario.nomeCompleto || 'Seu nome' }}
            </h2>

            <p>{{ formulario.email || 'Seu e-mail' }}</p>

            <span class="profile-badge">
              <span class="badge-dot"></span>
              {{ perfil }}
            </span>
          </div>

          <button
            v-if="!editando"
            class="edit-button"
            type="button"
            @click="editando = true; mensagem = ''; erro = ''"
          >
            <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
              <path d="m15 5 4 4M4 20l4.5-1 11-11a2.1 2.1 0 0 0-3-3l-11 11L4 20Z" />
            </svg>

            Editar perfil
          </button>
        </div>

        <div class="profile-divider"></div>

        <form class="profile-form" @submit.prevent="salvarPerfil">
          <div class="section-heading">
            <div>
              <h3>Informações pessoais</h3>
              <p>Seus dados de identificação na plataforma.</p>
            </div>

            <span class="section-number">01</span>
          </div>

          <div class="fields-grid">
            <div class="field">
              <label for="nomeCompleto">Nome completo</label>

              <input
                id="nomeCompleto"
                v-model="formulario.nomeCompleto"
                type="text"
                placeholder="Digite seu nome"
                autocomplete="name"
                :readonly="!editando"
              />
            </div>

            <div class="field">
              <label for="email">E-mail</label>

              <input
                id="email"
                v-model="formulario.email"
                type="email"
                placeholder="Digite seu e-mail"
                autocomplete="email"
                :readonly="!editando"
              />
            </div>

            <div class="field">
              <label for="perfil">Tipo de perfil</label>

              <input
                id="perfil"
                :value="perfil"
                type="text"
                readonly
              />

              <small>O tipo de perfil não pode ser alterado aqui.</small>
            </div>

            <div class="field">
              <label for="status">Status da sessão</label>

              <div id="status" class="status-field">
                <span class="status-dot"></span>
                Sessão ativa neste navegador
              </div>
            </div>
          </div>

          <p v-if="erro" class="feedback feedback-error" role="alert">
            {{ erro }}
          </p>

          <p v-if="mensagem" class="feedback feedback-success" role="status">
            {{ mensagem }}
          </p>

          <div v-if="editando" class="form-actions">
            <button
              class="cancel-button"
              type="button"
              @click="cancelarEdicao"
            >
              Cancelar
            </button>

            <button class="save-button" type="submit">
              Salvar alterações
            </button>
          </div>
        </form>
      </section>

      <section class="info-card">
        <div class="info-icon">✦</div>

        <div>
          <h3>Seus dados, suas informações</h3>
          <p>
            Esta página permite editar o nome e o e-mail armazenados no navegador.
            Para alterar os dados permanentemente no banco de dados, é necessário
            integrar a página a um endpoint de atualização do backend.
          </p>
        </div>
      </section>

      <footer class="page-footer">
        <span>InCTI Insights</span>
        <span>Transformando dados em conhecimento.</span>
      </footer>
    </section>
  </main>
</template>

<style scoped>
* {
  box-sizing: border-box;
}

.perfil-page {
  min-height: 100vh;
  background: #f7f5f8;
  color: #302330;
  font-family: Inter, "Segoe UI", Arial, sans-serif;
}

.topbar {
  min-height: 76px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 0 6%;
  background: #fff;
  border-bottom: 1px solid #eee7ed;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 11px;
  color: #382331;
  text-decoration: none;
}

.brand-symbol {
  width: 39px;
  height: 39px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  background: linear-gradient(145deg, #702b4c, #c73a68);
  color: #fff;
  font-size: 20px;
  font-weight: 800;
}

.brand-name {
  font-size: 19px;
  letter-spacing: -0.5px;
}

.brand-name strong {
  color: #b43b68;
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: #756572;
  font-size: 12px;
  font-weight: 600;
  text-decoration: none;
}

.back-link:hover {
  color: #b43b68;
}

.back-link svg {
  width: 17px;
  height: 17px;
  stroke: currentColor;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.logout-button {
  padding: 10px 17px;
  border: 1px solid #eadce5;
  border-radius: 9px;
  background: #fff;
  color: #9b315b;
  font: inherit;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
}

.logout-button:hover {
  border-color: #b43b68;
  background: #fff7fa;
}

.page-content {
  width: min(100% - 40px, 900px);
  margin: 0 auto;
  padding: 42px 0 28px;
}

.page-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 25px;
}

.eyebrow {
  color: #b43b68;
  font-size: 10px;
  font-weight: 800;
  letter-spacing: 2px;
}

.page-heading h1 {
  margin: 8px 0;
  color: #352330;
  font-size: clamp(28px, 4vw, 36px);
  letter-spacing: -1.2px;
}

.page-heading p {
  margin: 0;
  color: #8d808c;
  font-size: 13px;
  line-height: 1.7;
}

.heading-decoration {
  width: 49px;
  height: 49px;
  display: grid;
  place-items: center;
  border: 1px solid #f0dce6;
  border-radius: 15px;
  background: #fff7fa;
  color: #c18a40;
  font-size: 23px;
}

.profile-card {
  overflow: hidden;
  border: 1px solid #eee5ed;
  border-radius: 18px;
  background: #fff;
  box-shadow: 0 10px 35px rgba(53, 30, 47, 0.045);
}

.profile-cover {
  position: relative;
  height: 135px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 24px 29px;
  overflow: hidden;
  background:
    radial-gradient(ellipse at 90% 10%, rgba(255, 190, 85, 0.24), transparent 35%),
    linear-gradient(115deg, #38172c, #702b4c 55%, #c73a68);
}

.cover-label {
  position: relative;
  z-index: 1;
  color: rgba(255, 255, 255, 0.8);
  font-size: 10px;
  font-weight: 800;
  letter-spacing: 2px;
}

.cover-star {
  position: relative;
  z-index: 1;
  color: #ffbe55;
  font-size: 25px;
}

.cover-circle {
  position: absolute;
  border: 1px solid rgba(255, 255, 255, 0.13);
  border-radius: 50%;
}

.circle-one {
  width: 260px;
  height: 260px;
  top: -150px;
  right: 60px;
}

.circle-two {
  width: 200px;
  height: 200px;
  top: -115px;
  right: 90px;
}

.profile-summary {
  display: flex;
  align-items: center;
  gap: 18px;
  padding: 0 30px 25px;
}

.avatar {
  width: 86px;
  height: 86px;
  display: grid;
  place-items: center;
  flex-shrink: 0;
  margin-top: -38px;
  border: 5px solid #fff;
  border-radius: 25px;
  background: linear-gradient(145deg, #f5dce7, #e8b5cb);
  box-shadow: 0 4px 14px rgba(70, 30, 55, 0.1);
  color: #762a4b;
  font-size: 26px;
  font-weight: 800;
  letter-spacing: -1px;
}

.summary-info {
  min-width: 0;
  padding-top: 17px;
}

.summary-info h2 {
  margin: 0 0 5px;
  overflow-wrap: anywhere;
  color: #352330;
  font-size: 19px;
  font-weight: 800;
  letter-spacing: -0.4px;
}

.summary-info p {
  margin: 0 0 10px;
  overflow-wrap: anywhere;
  color: #928391;
  font-size: 12px;
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

.edit-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-height: 39px;
  margin: 14px 0 0 auto;
  padding: 0 13px;
  border: 1px solid #eadce5;
  border-radius: 9px;
  background: #fff;
  color: #9b315b;
  font: inherit;
  font-size: 11px;
  font-weight: 750;
  cursor: pointer;
  transition: background 0.2s, border-color 0.2s;
}

.edit-button:hover {
  border-color: #c96b8e;
  background: #fff7fa;
}

.edit-button svg {
  width: 15px;
  height: 15px;
  stroke: currentColor;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.profile-divider {
  height: 1px;
  margin: 0 30px;
  background: #f0e9ef;
}

.profile-form {
  padding: 26px 30px 30px;
}

.section-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 23px;
}

.section-heading h3 {
  margin: 0 0 6px;
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
  grid-template-columns: 1fr 1fr;
  gap: 22px 18px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 0;
}

.field label {
  color: #5b4a58;
  font-size: 11px;
  font-weight: 750;
}

.field input {
  width: 100%;
  min-height: 45px;
  padding: 0 13px;
  border: 1px solid #eae2e9;
  border-radius: 9px;
  outline: none;
  background: #fcfafc;
  color: #443541;
  font: inherit;
  font-size: 12px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.field input:focus:not(:read-only) {
  border-color: #bd4b76;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(189, 75, 118, 0.09);
}

.field input:read-only {
  color: #817481;
  cursor: default;
}

.field small {
  color: #a196a1;
  font-size: 10px;
  line-height: 1.5;
}

.status-field {
  min-height: 45px;
  display: flex;
  align-items: center;
  gap: 9px;
  padding: 0 13px;
  border: 1px solid #dceee4;
  border-radius: 9px;
  background: #f5fbf7;
  color: #397b58;
  font-size: 11px;
}

.status-dot {
  width: 7px;
  height: 7px;
  flex-shrink: 0;
  border-radius: 50%;
  background: #39a66b;
  box-shadow: 0 0 0 3px rgba(57, 166, 107, 0.12);
}

.feedback {
  margin: 20px 0 0;
  padding: 12px 14px;
  border-radius: 9px;
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
  gap: 10px;
  margin-top: 26px;
}

.cancel-button,
.save-button {
  min-height: 42px;
  padding: 0 17px;
  border-radius: 9px;
  font: inherit;
  font-size: 11px;
  font-weight: 750;
  cursor: pointer;
  transition: transform 0.2s, background 0.2s;
}

.cancel-button {
  border: 1px solid #e7dce5;
  background: #fff;
  color: #756572;
}

.cancel-button:hover {
  background: #faf6f9;
}

.save-button {
  border: 1px solid transparent;
  background: linear-gradient(110deg, #8d2e55, #c73a68);
  color: #fff;
  box-shadow: 0 5px 12px rgba(166, 49, 95, 0.15);
}

.save-button:hover {
  transform: translateY(-1px);
}

.info-card {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  margin-top: 19px;
  padding: 19px 21px;
  border: 1px solid #f0e0b9;
  border-radius: 13px;
  background: #fffcf5;
}

.info-icon {
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  flex-shrink: 0;
  border-radius: 10px;
  background: #fff0cf;
  color: #b17b2d;
  font-size: 17px;
}

.info-card h3 {
  margin: 1px 0 6px;
  color: #624b2d;
  font-size: 12px;
  font-weight: 800;
}

.info-card p {
  margin: 0;
  color: #88785f;
  font-size: 11px;
  line-height: 1.8;
}

.page-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 26px 2px 0;
  color: #a095a1;
  font-size: 10px;
}

.page-footer span:first-child {
  color: #806b7d;
  font-weight: 800;
}

@media (max-width: 600px) {
  .topbar {
    min-height: 68px;
    padding: 0 18px;
  }

  .brand-name {
    font-size: 16px;
  }

  .topbar-actions {
    gap: 10px;
  }

  .back-link {
    font-size: 0;
  }

  .back-link svg {
    width: 21px;
    height: 21px;
  }

  .logout-button {
    padding: 9px 12px;
  }

  .page-content {
    width: calc(100% - 28px);
    padding-top: 28px;
  }

  .page-heading {
    margin-bottom: 20px;
  }

  .heading-decoration {
    width: 40px;
    height: 40px;
    flex-shrink: 0;
  }

  .profile-cover {
    height: 110px;
    padding: 20px;
  }

  .profile-summary {
    flex-wrap: wrap;
    gap: 13px;
    padding: 0 19px 20px;
  }

  .avatar {
    width: 70px;
    height: 70px;
    margin-top: -30px;
    border-radius: 21px;
    font-size: 22px;
  }

  .summary-info {
    flex: 1;
    padding-top: 12px;
  }

  .summary-info h2 {
    font-size: 16px;
  }

  .summary-info p {
    font-size: 11px;
  }

  .edit-button {
    width: 100%;
    margin: 0;
  }

  .profile-divider {
    margin: 0 19px;
  }

  .profile-form {
    padding: 23px 19px;
  }

  .fields-grid {
    grid-template-columns: 1fr;
    gap: 17px;
  }

  .info-card {
    padding: 16px;
  }

  .page-footer {
    flex-direction: column;
    align-items: flex-start;
    padding-top: 22px;
  }
}

@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    scroll-behavior: auto !important;
    transition-duration: 0.01ms !important;
  }
}
</style>