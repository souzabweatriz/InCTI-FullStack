
<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const aberta = ref(true)
const route = useRoute()
const router = useRouter()

const usuario = ref({
    nome: 'Ana Beatriz',
    email: '',
    cargo: 'Administrador'
})

const itens = [
    {
        nome: 'Dashboard',
        rota: '/dashboard',
        icone: 'dashboard'
    },
    {
        nome: 'Insights',
        rota: '/dashboard/relatorios',
        icone: 'insights'
    },
    {
        nome: 'Upload de dados',
        rota: '/dashboard/upload',
        icone: 'upload'
    },
    {
        nome: 'Meu perfil',
        rota: '/dashboard/perfil',
        icone: 'perfil'
    }
]

const iniciais = computed(() => {
    const partes = usuario.value.nome.trim().split(/\s+/).filter(Boolean)

    return partes.length
        ? partes.slice(0, 2).map(parte => parte.charAt(0).toUpperCase()).join('')
        : 'IN'
})

function carregarUsuario() {
    try {
        const perfilSalvo = localStorage.getItem('cti_perfil')
        const usuarioSalvo = localStorage.getItem('cti_usuario')

        if (perfilSalvo) {
            const perfil = JSON.parse(perfilSalvo)

            usuario.value = {
                ...usuario.value,
                ...perfil,
                nome: perfil.nome || usuario.value.nome,
                cargo: perfil.cargo || usuario.value.cargo
            }
        } else if (usuarioSalvo) {
            const dados = JSON.parse(usuarioSalvo)

            usuario.value = {
                ...usuario.value,
                nome: dados.nome || dados.name || usuario.value.nome,
                email: dados.email || '',
                cargo: dados.cargo || usuario.value.cargo
            }
        }
    } catch {
        usuario.value = {
            nome: 'Ana Beatriz',
            email: '',
            cargo: 'Administrador'
        }
    }
}

function navegar(rota) {
    if (route.path !== rota) {
        router.push(rota)
    }
}

function abrirPerfil() {
    navegar('/dashboard/perfil')
}

function tratarAtualizacao(evento) {
    if (evento.key === 'cti_perfil' || evento.key === 'cti_usuario') {
        carregarUsuario()
    }
}

watch(() => route.fullPath, () => {
    carregarUsuario()
})

onMounted(() => {
    carregarUsuario()
    window.addEventListener('storage', tratarAtualizacao)
    window.addEventListener('cti-perfil-atualizado', carregarUsuario)
})

onUnmounted(() => {
    window.removeEventListener('storage', tratarAtualizacao)
    window.removeEventListener('cti-perfil-atualizado', carregarUsuario)
})
</script>

<template>
    <aside :class="['sidebar-shell', { fechada: !aberta }]">
        <header :class="['sidebar-header', { fechado: !aberta }]">
            <button
                v-if="aberta"
                class="brand"
                type="button"
                @click="navegar('/dashboard')"
            >
                <div class="brand-logo">
                    <img src="../assets/icons/logo.png" alt="InCTI">
                </div>

                <div class="brand-info">
                    <p>InCTI</p>
                    <span>Insights</span>
                </div>
            </button>

            <button
                v-else
                class="brand-logo"
                type="button"
                aria-label="Ir para o dashboard"
                @click="navegar('/dashboard')"
            >
                <img src="../assets/icons/logo.png" alt="InCTI">
            </button>

            <button
                class="toggle-button"
                :class="{ fechado: !aberta }"
                type="button"
                :aria-label="aberta ? 'Recolher menu' : 'Expandir menu'"
                @click="aberta = !aberta"
            >
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M15 18l-6-6 6-6" />
                </svg>
            </button>
        </header>

        <div v-if="aberta" class="nav-heading">
            <span>NAVEGAÇÃO</span>
            <div class="nav-heading-line"></div>
        </div>

        <nav class="sidebar-nav">
            <button
                v-for="item in itens"
                :key="item.rota"
                :class="[
                    'nav-item',
                    {
                        ativo: item.icone === 'dashboard'
                            ? route.path === '/dashboard'
                            : route.path === item.rota,
                        fechado: !aberta
                    }
                ]"
                type="button"
                :aria-label="item.nome"
                :aria-current="route.path === item.rota ? 'page' : undefined"
                @click="navegar(item.rota)"
            >
                <span class="nav-icon">
                    <svg
                        v-if="item.icone === 'dashboard'"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.8"
                    >
                        <rect x="3" y="3" width="7" height="7" rx="1.5" />
                        <rect x="14" y="3" width="7" height="7" rx="1.5" />
                        <rect x="3" y="14" width="7" height="7" rx="1.5" />
                        <rect x="14" y="14" width="7" height="7" rx="1.5" />
                    </svg>

                    <svg
                        v-else-if="item.icone === 'insights'"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.8"
                    >
                        <path d="M4 19V5" />
                        <path d="M4 19h16" />
                        <path d="M7 15l4-4 3 2 5-6" />
                        <path d="M16 7h3v3" />
                    </svg>

                    <svg
                        v-else-if="item.icone === 'upload'"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.8"
                    >
                        <path d="M12 16V4" />
                        <path d="M7 9l5-5 5 5" />
                        <path d="M5 20h14" />
                    </svg>

                    <svg
                        v-else-if="item.icone === 'perfil'"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.8"
                    >
                        <circle cx="12" cy="8" r="4" />
                        <path d="M4 21v-2a8 8 0 0 1 16 0v2" />
                    </svg>
                </span>

                <span v-if="aberta" class="nav-label">
                    {{ item.nome }}
                </span>

                <span v-if="item.icone === 'perfil' && route.path === item.rota && aberta" class="nav-status">
                    ATUAL
                </span>

                <span v-if="!aberta" class="tooltip">
                    {{ item.nome }}
                </span>
            </button>
        </nav>

        <div v-if="aberta" class="sidebar-bottom-label">
            <div class="bottom-decoration">
                <span></span>
                <span></span>
                <span></span>
            </div>
            <p>Transformando dados em descobertas.</p>
        </div>

        <footer class="sidebar-footer">
            <button
                :class="['profile', { 'profile-active': route.path === '/dashboard/perfil' }]"
                type="button"
                aria-label="Abrir meu perfil"
                @click="abrirPerfil"
            >
                <div class="profile-icon">
                    {{ iniciais }}
                    <span class="profile-online"></span>
                </div>

                <div v-if="aberta" class="profile-info">
                    <p>{{ usuario.nome }}</p>
                    <span>{{ usuario.cargo }}</span>
                </div>

                <svg
                    v-if="aberta"
                    class="profile-arrow"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="1.8"
                >
                    <path d="m9 18 6-6-6-6" />
                </svg>

                <span v-if="!aberta" class="tooltip profile-tooltip">
                    Meu perfil
                </span>
            </button>
        </footer>
    </aside>
</template>

<style scoped>
.sidebar-shell {
    display: flex;
    flex-direction: column;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 50;
    width: 16rem;
    height: 100vh;
    overflow: hidden;
    background: #100d12;
    border-right: 1px solid rgba(199, 58, 104, 0.18);
    box-shadow: 1rem 0 3rem rgba(26, 12, 22, 0.08);
    box-sizing: border-box;
    transition: width 0.3s ease;
}

.sidebar-shell.fechada {
    width: 4.75rem;
}

.sidebar-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 5rem;
    padding: 0 1rem;
    flex-shrink: 0;
    border-bottom: 1px solid rgba(199, 58, 104, 0.14);
    box-sizing: border-box;
}

.sidebar-header.fechado {
    justify-content: center;
    padding: 0;
}

.brand {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    min-width: 0;
    padding: 0;
    border: 0;
    background: transparent;
    cursor: pointer;
    text-align: left;
}

.brand-logo {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 2.5rem;
    height: 2.5rem;
    flex-shrink: 0;
    background: rgba(199, 58, 104, 0.1);
    border: 1px solid rgba(255, 138, 76, 0.25);
    border-radius: 0.875rem;
}

.brand-logo img {
    width: auto;
    height: 1.75rem;
    object-fit: contain;
}

.brand-info {
    display: flex;
    flex-direction: column;
}

.brand-info p {
    margin: 0;
    color: #fff8fb;
    font-size: 0.9375rem;
    font-weight: 700;
    letter-spacing: 0.02em;
}

.brand-info span {
    margin-top: 0.2rem;
    color: #ff8a4c;
    font-size: 0.625rem;
    text-transform: uppercase;
    letter-spacing: 0.2em;
}

.toggle-button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 2rem;
    height: 2rem;
    padding: 0;
    flex-shrink: 0;
    color: #ffbe55;
    background: rgba(255, 190, 85, 0.06);
    border: 1px solid rgba(255, 190, 85, 0.15);
    border-radius: 0.625rem;
    cursor: pointer;
    transition: background 0.2s ease, border-color 0.2s ease;
}

.toggle-button:hover {
    background: rgba(199, 58, 104, 0.18);
    border-color: rgba(199, 58, 104, 0.4);
}

.toggle-button svg {
    width: 1.125rem;
    height: 1.125rem;
    transition: transform 0.3s ease;
}

.toggle-button.fechado svg {
    transform: rotate(180deg);
}

.nav-heading {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 1.75rem 1.25rem 0.75rem;
}

.nav-heading span {
    color: #81727f;
    font-size: 0.625rem;
    font-weight: 700;
    letter-spacing: 0.16em;
    white-space: nowrap;
}

.nav-heading-line {
    height: 1px;
    flex: 1;
    background: rgba(199, 58, 104, 0.18);
}

.sidebar-nav {
    display: flex;
    flex: 1;
    flex-direction: column;
    gap: 0.5rem;
    width: 100%;
    height: auto;
    padding: 0.75rem;
    box-sizing: border-box;
    overflow-y: auto;
}

.nav-item {
    display: flex;
    align-items: center;
    position: relative;
    width: 100%;
    height: 3.25rem;
    flex-shrink: 0;
    gap: 0.875rem;
    padding: 0 1rem;
    color: #a89aa7;
    background: transparent;
    border: 1px solid transparent;
    border-radius: 0.875rem;
    text-align: left;
    cursor: pointer;
    box-sizing: border-box;
    transition: color 0.2s ease, background 0.2s ease, border-color 0.2s ease;
}

.nav-item:hover {
    color: #fff8fb;
    background: rgba(199, 58, 104, 0.08);
    border-color: rgba(199, 58, 104, 0.12);
}

.nav-item.ativo {
    color: #fff8fb;
    background: linear-gradient(100deg, rgba(199, 58, 104, 0.2), rgba(255, 138, 76, 0.07));
    border-color: rgba(199, 58, 104, 0.25);
    box-shadow: inset 0.1875rem 0 #c73a68;
}

.nav-item.ativo .nav-icon {
    color: #ff8a4c;
}

.nav-item.fechado {
    justify-content: center;
    gap: 0;
    padding: 0;
}

.nav-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 1.25rem;
    height: 1.25rem;
    flex-shrink: 0;
    color: #b4a1b0;
    transition: color 0.2s ease;
}

.nav-icon svg {
    width: 1.25rem;
    height: 1.25rem;
}

.nav-label {
    display: flex;
    align-items: center;
    min-width: 0;
    white-space: nowrap;
    font-size: 0.8125rem;
    font-weight: 500;
}

.nav-status {
    margin-left: auto;
    padding: 0.25rem 0.375rem;
    color: #ffbe55;
    background: rgba(255, 190, 85, 0.1);
    border-radius: 0.375rem;
    font-size: 0.5rem;
    font-weight: 800;
    letter-spacing: 0.05em;
}

.tooltip {
    display: none;
}

.sidebar-shell.fechada .nav-item:hover .tooltip,
.sidebar-shell.fechada .profile:hover .profile-tooltip {
    display: flex;
    align-items: center;
    position: absolute;
    left: calc(100% + 0.75rem);
    z-index: 70;
    width: max-content;
    height: 2.25rem;
    padding: 0 0.875rem;
    color: #fff8fb;
    background: #21151e;
    border: 1px solid rgba(199, 58, 104, 0.35);
    border-radius: 0.625rem;
    white-space: nowrap;
    font-size: 0.75rem;
    font-weight: 600;
    box-shadow: 0 0.5rem 1.5rem rgba(0, 0, 0, 0.15);
}

.sidebar-bottom-label {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
    margin: 0.75rem;
    padding: 1rem;
    background: linear-gradient(135deg, rgba(199, 58, 104, 0.1), rgba(255, 138, 76, 0.04));
    border: 1px solid rgba(199, 58, 104, 0.14);
    border-radius: 1rem;
}

.bottom-decoration {
    display: flex;
    align-items: center;
    gap: 0.3rem;
}

.bottom-decoration span {
    width: 0.375rem;
    height: 0.375rem;
    background: #c73a68;
    border-radius: 50%;
}

.bottom-decoration span:nth-child(2) {
    background: #ff8a4c;
}

.bottom-decoration span:nth-child(3) {
    background: #ffbe55;
}

.sidebar-bottom-label p {
    margin: 0;
    color: #b5a3b1;
    font-size: 0.6875rem;
    line-height: 1.5;
}

.sidebar-footer {
    display: flex;
    width: 100%;
    padding: 0.75rem;
    flex-shrink: 0;
    box-sizing: border-box;
    border-top: 1px solid rgba(199, 58, 104, 0.15);
}

.profile {
    display: flex;
    align-items: center;
    position: relative;
    width: 100%;
    height: 3.75rem;
    gap: 0.75rem;
    padding: 0 0.625rem;
    background: rgba(255, 255, 255, 0.025);
    border: 1px solid rgba(199, 58, 104, 0.12);
    border-radius: 0.875rem;
    box-sizing: border-box;
    text-align: left;
    cursor: pointer;
    transition: background 0.2s ease, border-color 0.2s ease;
}

.profile:hover,
.profile.profile-active {
    background: rgba(199, 58, 104, 0.1);
    border-color: rgba(199, 58, 104, 0.3);
}

.sidebar-shell.fechada .profile {
    justify-content: center;
    padding: 0;
}

.profile-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    width: 2.375rem;
    height: 2.375rem;
    flex-shrink: 0;
    color: #fff8fb;
    background: linear-gradient(135deg, #c73a68, #ff8a4c);
    border: 1px solid rgba(255, 190, 85, 0.45);
    border-radius: 0.875rem;
    font-size: 0.75rem;
    font-weight: 800;
    letter-spacing: 0.02em;
}

.profile-online {
    position: absolute;
    right: -0.125rem;
    bottom: -0.125rem;
    width: 0.625rem;
    height: 0.625rem;
    background: #ffbe55;
    border: 2px solid #100d12;
    border-radius: 50%;
}

.profile-info {
    display: flex;
    flex: 1;
    flex-direction: column;
    min-width: 0;
    gap: 0.25rem;
}

.profile-info p {
    margin: 0;
    overflow: hidden;
    color: #fff8fb;
    font-size: 0.75rem;
    font-weight: 600;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.profile-info span {
    overflow: hidden;
    color: #ff8a4c;
    font-size: 0.625rem;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.profile-arrow {
    width: 1rem;
    height: 1rem;
    flex-shrink: 0;
    color: #8f7989;
}

@media (max-width: 48rem) {
    .sidebar-shell,
    .sidebar-shell.fechada {
        top: auto;
        bottom: 0.75rem;
        left: 0.75rem;
        width: calc(100% - 1.5rem);
        height: 4.5rem;
        overflow: visible;
        border: 1px solid rgba(199, 58, 104, 0.25);
        border-radius: 1.25rem;
        box-shadow: 0 0.5rem 2rem rgba(26, 12, 22, 0.18);
    }

    .sidebar-header,
    .nav-heading,
    .sidebar-bottom-label,
    .sidebar-footer {
        display: none;
    }

    .sidebar-nav {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        width: 100%;
        height: 100%;
        padding: 0.5rem;
        gap: 0.375rem;
        overflow: visible;
        box-sizing: border-box;
    }

    .nav-item,
    .nav-item.fechado {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 3.25rem;
        flex: 1;
        padding: 0;
    }

    .nav-label,
    .tooltip,
    .nav-status {
        display: none;
    }

    .nav-icon,
    .nav-icon svg {
        width: 1.25rem;
        height: 1.25rem;
    }
}

@media (max-width: 30rem) {
    .sidebar-shell,
    .sidebar-shell.fechada {
        bottom: 0.5rem;
        left: 0.5rem;
        width: calc(100% - 1rem);
        height: 4rem;
        border-radius: 1rem;
    }

    .sidebar-nav {
        gap: 0.25rem;
        padding: 0.375rem;
    }

    .nav-item,
    .nav-item.fechado {
        height: 3rem;
    }
}
</style>
