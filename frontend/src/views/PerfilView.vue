<<<<<<< HEAD
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
=======

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'

const router = useRouter()
const editando = ref(false)
const mensagem = ref('')
const tipoMensagem = ref('sucesso')
const carregando = ref(false)

const perfil = reactive({
  nome: '',
  email: '',
  cargo: '',
  instituicao: '',
  telefone: '',
  bio: ''
})

const perfilOriginal = ref(null)

const iniciais = computed(() => {
  const partes = perfil.nome.trim().split(/\s+/).filter(Boolean)

  if (!partes.length) return 'IN'

  return partes
    .slice(0, 2)
    .map((parte) => parte.charAt(0).toUpperCase())
    .join('')
})

const camposPreenchidos = computed(() => {
  return [
    perfil.nome,
    perfil.email,
    perfil.cargo,
    perfil.instituicao,
    perfil.telefone,
    perfil.bio
  ].filter((valor) => String(valor || '').trim()).length
})

const progressoPerfil = computed(() => {
  return Math.round((camposPreenchidos.value / 6) * 100)
})

function carregarPerfil() {
  try {
    const perfilSalvo = localStorage.getItem('cti_perfil')
    const usuarioSalvo = localStorage.getItem('cti_usuario')

    let dados = {}

    if (usuarioSalvo) {
      const usuario = JSON.parse(usuarioSalvo)

      dados = {
        nome: usuario.nome || usuario.name || '',
        email: usuario.email || '',
        cargo: usuario.cargo || '',
        instituicao: usuario.instituicao || '',
        telefone: usuario.telefone || '',
        bio: usuario.bio || ''
      }
    }

    if (perfilSalvo) {
      const perfilLocal = JSON.parse(perfilSalvo)

      dados = {
        ...dados,
        ...perfilLocal
      }
    }

    Object.assign(perfil, {
      nome: dados.nome || '',
      email: dados.email || '',
      cargo: dados.cargo || '',
      instituicao: dados.instituicao || '',
      telefone: dados.telefone || '',
      bio: dados.bio || ''
    })

    perfilOriginal.value = { ...perfil }
  } catch {
    mensagem.value = 'Não foi possível carregar os dados salvos do perfil.'
    tipoMensagem.value = 'erro'
  }
}

function iniciarEdicao() {
  perfilOriginal.value = { ...perfil }
  mensagem.value = ''
  editando.value = true
}

function cancelarEdicao() {
  if (perfilOriginal.value) {
    Object.assign(perfil, perfilOriginal.value)
  }

  mensagem.value = ''
  editando.value = false
}

function salvarPerfil() {
  if (!perfil.nome.trim() || !perfil.email.trim()) {
    mensagem.value = 'Preencha seu nome e e-mail antes de salvar.'
    tipoMensagem.value = 'erro'
    return
  }

  const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(perfil.email.trim())

  if (!emailValido) {
    mensagem.value = 'Informe um endereço de e-mail válido.'
    tipoMensagem.value = 'erro'
    return
  }

  carregando.value = true

  try {
    const dadosAtualizados = {
      nome: perfil.nome.trim(),
      email: perfil.email.trim(),
      cargo: perfil.cargo.trim(),
      instituicao: perfil.instituicao.trim(),
      telefone: perfil.telefone.trim(),
      bio: perfil.bio.trim()
    }

    localStorage.setItem('cti_perfil', JSON.stringify(dadosAtualizados))

    Object.assign(perfil, dadosAtualizados)
    perfilOriginal.value = { ...dadosAtualizados }

    window.dispatchEvent(new Event('cti-perfil-atualizado'))

    mensagem.value = 'Seu perfil foi atualizado com sucesso!'
    tipoMensagem.value = 'sucesso'
    editando.value = false
  } catch {
    mensagem.value = 'Não foi possível salvar as alterações neste navegador.'
    tipoMensagem.value = 'erro'
  } finally {
    carregando.value = false
  }
>>>>>>> 03906982a0ce7747eb7d3cf1ea4d25905ffcfa8b
}

function sair() {
  localStorage.removeItem('cti_logado')
  localStorage.removeItem('cti_token')
<<<<<<< HEAD
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
=======

  router.push({
    name: 'login'
  })
}

onMounted(() => {
  carregarPerfil()
})
</script>

<template>
  <main class="min-h-screen bg-[#faf9fc] px-4 py-6 font-sans text-[#302536] sm:px-6 sm:py-8 lg:px-10">
    <div class="mx-auto flex w-full max-w-6xl flex-col gap-7">

      <section class="profile-hero relative overflow-hidden rounded-[2rem] border border-[#f0e7ed] bg-white p-6 shadow-[0_12px_50px_rgba(68,38,65,0.04)] sm:p-9">
        <div class="pointer-events-none absolute -right-16 -top-24 h-64 w-64 rounded-full bg-[#f9dce7] opacity-70 blur-3xl"></div>
        <div class="pointer-events-none absolute right-32 top-20 h-36 w-36 rounded-full bg-[#ffe4d5] opacity-70 blur-3xl"></div>

        <div class="relative flex flex-col gap-7">
          <div class="flex flex-wrap items-center justify-between gap-4">
            <div class="flex items-center gap-2 text-sm text-[#928894]">
              <RouterLink to="/dashboard" class="transition hover:text-[#c73a68]">
                Dashboard
              </RouterLink>
              <span class="text-[#d0c2ce]">/</span>
              <span class="font-semibold text-[#c73a68]">Meu perfil</span>
            </div>

            <div class="flex items-center gap-2 rounded-full border border-[#f0e4eb] bg-white/80 px-3 py-2 text-xs font-semibold text-[#7d6575]">
              <span class="h-2 w-2 rounded-full bg-[#43b889]"></span>
              Minha conta
            </div>
          </div>

          <div class="flex flex-col gap-6 sm:flex-row sm:items-center">
            <div class="relative flex h-28 w-28 shrink-0 items-center justify-center rounded-[2rem] bg-gradient-to-br from-[#c73a68] via-[#e65e77] to-[#ff9b57] text-3xl font-bold text-white shadow-[0_12px_30px_rgba(199,58,104,0.22)]">
              {{ iniciais }}

              <span class="absolute -bottom-2 -right-2 flex h-9 w-9 items-center justify-center rounded-2xl border-4 border-white bg-[#ffbe55] text-[#75451d]">
                <svg viewBox="0 0 24 24" fill="none" class="h-4 w-4" stroke="currentColor" stroke-width="2">
                  <path d="m12 3 2.7 5.5 6.1.9-4.4 4.3 1 6.1-5.4-2.9-5.4 2.9 1-6.1-4.4-4.3 6.1-.9L12 3Z" stroke-linejoin="round"/>
                </svg>
              </span>
            </div>

            <div class="flex min-w-0 flex-1 flex-col gap-3">
              <div>
                <p class="mb-2 text-xs font-bold uppercase tracking-[0.22em] text-[#c73a68]">
                  Seu espaço na InCTI
                </p>

                <h1 class="break-words text-3xl font-bold tracking-tight text-[#302536] sm:text-4xl">
                  {{ perfil.nome || 'Seu perfil' }}
                </h1>

                <p class="mt-2 break-all text-sm text-[#8e8390]">
                  {{ perfil.email || 'Adicione seu e-mail para completar seu perfil.' }}
                </p>
              </div>

              <div class="flex flex-wrap gap-2">
                <span class="rounded-full bg-[#fff0f5] px-3 py-1.5 text-xs font-semibold text-[#c73a68]">
                  {{ perfil.cargo || 'Cargo não informado' }}
                </span>

                <span v-if="perfil.instituicao" class="rounded-full bg-[#fff4e9] px-3 py-1.5 text-xs font-semibold text-[#b66a35]">
                  {{ perfil.instituicao }}
                </span>
              </div>
            </div>

            <button
              v-if="!editando"
              type="button"
              class="inline-flex items-center justify-center gap-2 self-start rounded-2xl bg-[#302536] px-5 py-3 text-sm font-semibold text-white transition hover:bg-[#493449] sm:self-center"
              @click="iniciarEdicao"
            >
              <svg viewBox="0 0 24 24" fill="none" class="h-4 w-4" stroke="currentColor" stroke-width="1.8">
                <path d="m14 5 5 5M4 20l4.5-1 11-11a2.12 2.12 0 0 0-3-3l-11 11L4 20Z" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              Editar perfil
            </button>
          </div>

          <div class="grid grid-cols-1 gap-4 border-t border-[#f2edf1] pt-5 sm:grid-cols-2">
            <div class="flex items-center gap-3">
              <div class="flex h-10 w-10 items-center justify-center rounded-xl bg-[#f9edf3] text-[#c73a68]">
                <svg viewBox="0 0 24 24" fill="none" class="h-5 w-5" stroke="currentColor" stroke-width="1.8">
                  <circle cx="12" cy="8" r="4"/>
                  <path d="M4 21v-2a8 8 0 0 1 16 0v2"/>
                </svg>
              </div>

              <div>
                <p class="text-xs text-[#928894]">Identificação</p>
                <p class="mt-1 text-sm font-semibold text-[#514451]">{{ iniciais }} · Perfil pessoal</p>
              </div>
            </div>

            <div class="flex items-center gap-3">
              <div class="flex h-10 w-10 items-center justify-center rounded-xl bg-[#fff3e8] text-[#d98242]">
                <svg viewBox="0 0 24 24" fill="none" class="h-5 w-5" stroke="currentColor" stroke-width="1.8">
                  <path d="M12 22s8-4 8-11V5l-8-3-8 3v6c0 7 8 11 8 11Z"/>
                  <path d="m9 12 2 2 4-4" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>

              <div>
                <p class="text-xs text-[#928894]">Acesso</p>
                <p class="mt-1 text-sm font-semibold text-[#514451]">Área autenticada</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <div
        v-if="mensagem"
        class="flex items-start gap-3 rounded-2xl border px-4 py-4 text-sm"
        :class="tipoMensagem === 'sucesso'
          ? 'border-[#ccebdd] bg-[#f0fbf5] text-[#287c58]'
          : 'border-[#f1d4df] bg-[#fff5f8] text-[#ad345e]'"
      >
        <span class="font-bold">{{ tipoMensagem === 'sucesso' ? '✓' : '!' }}</span>
        <p>{{ mensagem }}</p>
      </div>

      <section v-if="editando" class="rounded-[2rem] border border-[#f0e7ed] bg-white p-6 shadow-[0_12px_50px_rgba(68,38,65,0.04)] sm:p-8">
        <div class="mb-7">
          <p class="mb-2 text-xs font-bold uppercase tracking-[0.2em] text-[#c73a68]">
            Personalização
          </p>

          <h2 class="text-2xl font-bold text-[#302536]">
            Vamos atualizar seus dados?
          </h2>

          <p class="mt-2 text-sm leading-6 text-[#8e8390]">
            Mantenha suas informações organizadas para personalizar sua experiência na plataforma.
          </p>
        </div>

        <form class="flex flex-col gap-5" @submit.prevent="salvarPerfil">
          <div class="grid grid-cols-1 gap-5 md:grid-cols-2">
            <label class="flex flex-col gap-2 text-sm font-semibold text-[#514451]">
              Nome completo
              <input
                v-model="perfil.nome"
                type="text"
                autocomplete="name"
                required
                maxlength="100"
                placeholder="Digite seu nome"
                class="w-full rounded-2xl border border-[#e9e0e7] bg-[#fdfbfd] px-4 py-3.5 font-normal outline-none transition placeholder:text-[#b8adb8] focus:border-[#d85a80] focus:ring-4 focus:ring-[#c73a68]/10"
              >
            </label>

            <label class="flex flex-col gap-2 text-sm font-semibold text-[#514451]">
              E-mail
              <input
                v-model="perfil.email"
                type="email"
                autocomplete="email"
                required
                maxlength="150"
                placeholder="voce@email.com"
                class="w-full rounded-2xl border border-[#e9e0e7] bg-[#fdfbfd] px-4 py-3.5 font-normal outline-none transition placeholder:text-[#b8adb8] focus:border-[#d85a80] focus:ring-4 focus:ring-[#c73a68]/10"
              >
            </label>

            <label class="flex flex-col gap-2 text-sm font-semibold text-[#514451]">
              Cargo ou área de atuação
              <input
                v-model="perfil.cargo"
                type="text"
                maxlength="100"
                placeholder="Ex.: Analista de dados"
                class="w-full rounded-2xl border border-[#e9e0e7] bg-[#fdfbfd] px-4 py-3.5 font-normal outline-none transition placeholder:text-[#b8adb8] focus:border-[#d85a80] focus:ring-4 focus:ring-[#c73a68]/10"
              >
            </label>

            <label class="flex flex-col gap-2 text-sm font-semibold text-[#514451]">
              Instituição ou empresa
              <input
                v-model="perfil.instituicao"
                type="text"
                maxlength="120"
                placeholder="Onde você estuda ou trabalha?"
                class="w-full rounded-2xl border border-[#e9e0e7] bg-[#fdfbfd] px-4 py-3.5 font-normal outline-none transition placeholder:text-[#b8adb8] focus:border-[#d85a80] focus:ring-4 focus:ring-[#c73a68]/10"
              >
            </label>

            <label class="flex flex-col gap-2 text-sm font-semibold text-[#514451] md:col-span-2">
              Telefone
              <input
                v-model="perfil.telefone"
                type="tel"
                autocomplete="tel"
                maxlength="30"
                placeholder="Seu telefone (opcional)"
                class="w-full rounded-2xl border border-[#e9e0e7] bg-[#fdfbfd] px-4 py-3.5 font-normal outline-none transition placeholder:text-[#b8adb8] focus:border-[#d85a80] focus:ring-4 focus:ring-[#c73a68]/10"
              >
            </label>

            <label class="flex flex-col gap-2 text-sm font-semibold text-[#514451] md:col-span-2">
              Sobre você
              <textarea
                v-model="perfil.bio"
                rows="4"
                maxlength="500"
                placeholder="Conte um pouco sobre você e seus interesses..."
                class="w-full resize-y rounded-2xl border border-[#e9e0e7] bg-[#fdfbfd] px-4 py-3.5 font-normal outline-none transition placeholder:text-[#b8adb8] focus:border-[#d85a80] focus:ring-4 focus:ring-[#c73a68]/10"
              ></textarea>

              <span class="text-right text-xs font-normal text-[#a397a3]">
                {{ perfil.bio.length }}/500 caracteres
              </span>
            </label>
          </div>

          <div class="flex flex-wrap justify-end gap-3 border-t border-[#f2edf1] pt-5">
            <button
              type="button"
              class="rounded-2xl border border-[#e9e0e7] px-5 py-3 text-sm font-semibold text-[#766876] transition hover:bg-[#faf7fa]"
>>>>>>> 03906982a0ce7747eb7d3cf1ea4d25905ffcfa8b
              @click="cancelarEdicao"
            >
              Cancelar
            </button>

<<<<<<< HEAD
            <button class="save-button" type="submit">
              Salvar alterações
=======
            <button
              type="submit"
              :disabled="carregando"
              class="rounded-2xl bg-gradient-to-r from-[#c73a68] to-[#ed795e] px-6 py-3 text-sm font-bold text-white shadow-lg shadow-[#c73a68]/15 transition hover:-translate-y-0.5 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {{ carregando ? 'Salvando...' : 'Salvar alterações' }}
>>>>>>> 03906982a0ce7747eb7d3cf1ea4d25905ffcfa8b
            </button>
          </div>
        </form>
      </section>

<<<<<<< HEAD
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
=======
      <section class="grid grid-cols-1 gap-5 lg:grid-cols-3">
        <article class="relative flex flex-col gap-4 overflow-hidden rounded-[1.75rem] border border-[#f0e7ed] bg-white p-6">
          <div class="flex h-12 w-12 items-center justify-center rounded-2xl bg-[#fff0f5] text-[#c73a68]">
            <svg viewBox="0 0 24 24" fill="none" class="h-6 w-6" stroke="currentColor" stroke-width="1.7">
              <circle cx="12" cy="8" r="4"/>
              <path d="M4 21v-2a8 8 0 0 1 16 0v2"/>
            </svg>
          </div>

          <div>
            <h2 class="text-lg font-bold text-[#302536]">Seus dados</h2>
            <p class="mt-2 text-sm leading-6 text-[#928894]">
              Mantenha suas informações pessoais e profissionais atualizadas.
            </p>
          </div>

          <button
            type="button"
            class="mt-auto flex items-center gap-2 self-start pt-2 text-sm font-bold text-[#c73a68] transition hover:gap-3"
            @click="iniciarEdicao"
          >
            Atualizar informações
            <span>→</span>
          </button>

          <div class="absolute -bottom-10 -right-8 h-24 w-24 rounded-full bg-[#f9dce7]/50 blur-2xl"></div>
        </article>

        <article class="relative flex flex-col gap-4 overflow-hidden rounded-[1.75rem] border border-[#f0e7ed] bg-white p-6">
          <div class="flex h-12 w-12 items-center justify-center rounded-2xl bg-[#fff3e8] text-[#d98242]">
            <svg viewBox="0 0 24 24" fill="none" class="h-6 w-6" stroke="currentColor" stroke-width="1.7">
              <rect x="3" y="11" width="18" height="10" rx="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              <circle cx="12" cy="16" r="1"/>
            </svg>
          </div>

          <div>
            <h2 class="text-lg font-bold text-[#302536]">Segurança da conta</h2>
            <p class="mt-2 text-sm leading-6 text-[#928894]">
              Sua conta utiliza os dados de autenticação do sistema. A troca de senha depende da integração com o serviço de autenticação.
            </p>
          </div>

          <div class="mt-auto flex items-center gap-2 pt-2 text-xs font-semibold text-[#478d6b]">
            <span class="h-2 w-2 rounded-full bg-[#43b889]"></span>
            Sessão autenticada
          </div>

          <div class="absolute -bottom-10 -right-8 h-24 w-24 rounded-full bg-[#ffe4d5]/60 blur-2xl"></div>
        </article>

        <article class="relative flex flex-col gap-4 overflow-hidden rounded-[1.75rem] border border-[#f0e7ed] bg-white p-6">
          <div class="flex h-12 w-12 items-center justify-center rounded-2xl bg-[#fff7df] text-[#c18a20]">
            <svg viewBox="0 0 24 24" fill="none" class="h-6 w-6" stroke="currentColor" stroke-width="1.7">
              <circle cx="12" cy="12" r="9"/>
              <path d="M12 7v10M7 12h10"/>
            </svg>
          </div>

          <div>
            <h2 class="text-lg font-bold text-[#302536]">Seu espaço de trabalho</h2>
            <p class="mt-2 text-sm leading-6 text-[#928894]">
              Envie arquivos, explore seus dados e acompanhe os relatórios gerados pela plataforma.
            </p>
          </div>

          <RouterLink
            :to="{ name: 'upload' }"
            class="mt-auto flex items-center gap-2 self-start pt-2 text-sm font-bold text-[#b17c1b] transition hover:gap-3"
          >
            Ir para a plataforma
            <span>→</span>
          </RouterLink>

          <div class="absolute -bottom-10 -right-8 h-24 w-24 rounded-full bg-[#fff0b8]/60 blur-2xl"></div>
        </article>
      </section>

      <section class="rounded-[1.75rem] border border-[#f0e7ed] bg-white p-6 sm:p-7">
        <div class="flex flex-col gap-5 sm:flex-row sm:items-center sm:justify-between">
          <div class="flex items-start gap-4">
            <div class="flex h-11 w-11 shrink-0 items-center justify-center rounded-2xl bg-[#f7f3f6] text-[#827582]">
              <svg viewBox="0 0 24 24" fill="none" class="h-5 w-5" stroke="currentColor" stroke-width="1.8">
                <path d="M10 17l5-5-5-5M15 12H3"/>
                <path d="M12 3h6a3 3 0 0 1 3 3v12a3 3 0 0 1-3 3h-6"/>
              </svg>
            </div>

            <div>
              <h2 class="font-bold text-[#302536]">Encerrar sessão</h2>
              <p class="mt-1 text-sm leading-6 text-[#928894]">
                Saia da sua conta neste dispositivo. Você poderá entrar novamente com suas credenciais.
              </p>
            </div>
          </div>

          <button
            type="button"
            class="shrink-0 rounded-2xl border border-[#f0cbd9] px-5 py-3 text-sm font-bold text-[#c73a68] transition hover:bg-[#fff0f5]"
            @click="sair"
          >
            Sair da conta
          </button>
        </div>
      </section>

      <footer class="flex flex-wrap items-center justify-between gap-3 px-1 pb-4 text-xs text-[#a397a3]">
        <p>InCTI Insights · Seu espaço, seus dados, suas descobertas.</p>
        <p>Perfil da conta</p>
      </footer>
    </div>
  </main>
</template>
>>>>>>> 03906982a0ce7747eb7d3cf1ea4d25905ffcfa8b
