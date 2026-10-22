
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
}

function sair() {
  localStorage.removeItem('cti_logado')
  localStorage.removeItem('cti_token')

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
              @click="cancelarEdicao"
            >
              Cancelar
            </button>

            <button
              type="submit"
              :disabled="carregando"
              class="rounded-2xl bg-gradient-to-r from-[#c73a68] to-[#ed795e] px-6 py-3 text-sm font-bold text-white shadow-lg shadow-[#c73a68]/15 transition hover:-translate-y-0.5 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {{ carregando ? 'Salvando...' : 'Salvar alterações' }}
            </button>
          </div>
        </form>
      </section>

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
