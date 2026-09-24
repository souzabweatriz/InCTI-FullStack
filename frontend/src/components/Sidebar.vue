<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const aberta = ref(true)

const route = useRoute()
const router = useRouter()

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
    }
]

function navegar(rota) {
    if (route.path !== rota) {
        router.push(rota)
    }
}
</script>

<template>
    <aside
        :class="[
            'sidebar-shell',
            { fechada: !aberta }
        ]"
    >
        <header
            :class="[
                'sidebar-header',
                { fechado: !aberta }
            ]"
        >
            <div
                v-if="aberta"
                class="brand"
            >
                <div class="brand-logo">
                    <img
                        src="../assets/logo.png"
                        alt="InCTI"
                    >
                </div>

                <div class="brand-info">
                    <p>InCTI</p>
                    <span>Insights</span>
                </div>
            </div>

            <div
                v-else
                class="brand-logo"
            >
                <img
                    src="../assets/logo.png"
                    alt="InCTI"
                >
            </div>

            <button
                class="toggle-button"
                :class="{ fechado: !aberta }"
                type="button"
                @click="aberta = !aberta"
            >
                <svg
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M15 18l-6-6 6-6" />
                </svg>
            </button>
        </header>

        <nav class="sidebar-nav">
            <button
                v-for="item in itens"
                :key="item.rota"
                :class="[
                    'nav-item',
                    {
                        ativo: route.path === item.rota,
                        fechado: !aberta
                    }
                ]"
                type="button"
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
                        <rect
                            x="3"
                            y="3"
                            width="7"
                            height="7"
                            rx="1"
                        />
                        <rect
                            x="14"
                            y="3"
                            width="7"
                            height="7"
                            rx="1"
                        />
                        <rect
                            x="3"
                            y="14"
                            width="7"
                            height="7"
                            rx="1"
                        />
                        <rect
                            x="14"
                            y="14"
                            width="7"
                            height="7"
                            rx="1"
                        />
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
                </span>

                <span
                    v-if="aberta"
                    class="nav-label"
                >
                    {{ item.nome }}
                </span>

                <span
                    v-if="!aberta"
                    class="tooltip"
                >
                    {{ item.nome }}
                </span>
            </button>
        </nav>

        <footer class="sidebar-footer">
            <div class="profile">
                <div class="profile-icon">
                    AB
                </div>

                <div
                    v-if="aberta"
                    class="profile-info"
                >
                    <p>Ana Beatriz</p>
                    <span>Administrador</span>
                </div>
            </div>
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
    background: rgba(5, 8, 7, 0.95);
    border-right: 1px solid rgba(90, 127, 120, 0.2);
    box-shadow: 1rem 0 3rem rgba(0, 0, 0, 0.3);
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
    border-bottom: 1px solid rgba(90, 127, 120, 0.2);
    box-sizing: border-box;
}

.sidebar-header.fechado {
    justify-content: center;
}

.brand {
    display: flex;
    align-items: center;
    gap: 0.75rem;
}

.brand-logo {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 2.5rem;
    height: 2.5rem;
    flex-shrink: 0;
    background: rgba(187, 222, 198, 0.1);
    border: 1px solid rgba(187, 222, 198, 0.2);
    border-radius: 0.75rem;
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
    color: #f7f8fc;
    font-size: 0.875rem;
    font-weight: 600;
}

.brand-info span {
    margin-top: 0.2rem;
    color: #5a7f78;
    font-size: 0.625rem;
    text-transform: uppercase;
    letter-spacing: 0.2em;
}

.toggle-button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 2.5rem;
    height: 2.5rem;
    padding: 0;
    flex-shrink: 0;
    color: #bbdec6;
    background: rgba(49, 76, 83, 0.15);
    border: 1px solid rgba(90, 127, 120, 0.2);
    border-radius: 0.75rem;
    cursor: pointer;
    transition: 0.2s ease;
}

.toggle-button:hover {
    background: rgba(187, 222, 198, 0.1);
    border-color: rgba(187, 222, 198, 0.3);
}

.toggle-button svg {
    width: 1.25rem;
    height: 1.25rem;
    transition: transform 0.3s ease;
}

.toggle-button.fechado svg {
    transform: rotate(180deg);
}

.sidebar-nav {
    display: flex;
    flex: 1;
    flex-direction: column;
    gap: 0.5rem;
    width: 100%;
    height: auto;
    padding: 1.5rem 0.75rem;
    box-sizing: border-box;
    overflow-y: auto;
}

.nav-item {
    display: flex;
    align-items: center;
    width: 100%;
    height: 3.25rem;
    flex-shrink: 0;
    gap: 1rem;
    padding: 0 1rem;
    color: rgba(187, 222, 198, 0.5);
    background: transparent;
    border: 0;
    border-radius: 0.75rem;
    text-align: left;
    cursor: pointer;
    box-sizing: border-box;
    transition: 0.2s ease;
}

.nav-item:hover {
    color: #f7f8fc;
    background: rgba(49, 76, 83, 0.2);
}

.nav-item.ativo {
    color: #bbdec6;
    background: rgba(187, 222, 198, 0.1);
    box-shadow: inset 0.1875rem 0 0 #bbdec6;
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
}

.nav-icon svg {
    width: 1.25rem;
    height: 1.25rem;
}

.nav-label {
    display: flex;
    align-items: center;
    white-space: nowrap;
    font-size: 0.875rem;
    font-weight: 500;
}

.tooltip {
    display: none;
}

.sidebar-shell.fechada .nav-item:hover .tooltip {
    display: flex;
    align-items: center;
    width: max-content;
    height: 2rem;
    margin-left: 0.5rem;
    padding: 0 0.75rem;
    color: #f7f8fc;
    background: #0a110d;
    border: 1px solid rgba(90, 127, 120, 0.3);
    border-radius: 0.5rem;
    white-space: nowrap;
    font-size: 0.75rem;
    font-weight: 500;
}

.sidebar-footer {
    display: flex;
    width: 100%;
    height: auto;
    padding: 0.75rem;
    flex-shrink: 0;
    box-sizing: border-box;
    border-top: 1px solid rgba(90, 127, 120, 0.2);
}

.profile {
    display: flex;
    align-items: center;
    width: 100%;
    height: 3.75rem;
    gap: 0.75rem;
    padding: 0 0.75rem;
    background: rgba(49, 76, 83, 0.15);
    border-radius: 0.75rem;
    box-sizing: border-box;
}

.sidebar-shell.fechada .profile {
    justify-content: center;
    padding: 0;
}

.profile-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 2.25rem;
    height: 2.25rem;
    flex-shrink: 0;
    color: #bbdec6;
    background: rgba(187, 222, 198, 0.1);
    border-radius: 50%;
    font-size: 0.875rem;
    font-weight: 600;
}

.profile-info {
    display: flex;
    flex-direction: column;
    min-width: 0;
}

.profile-info p {
    margin: 0;
    color: #f7f8fc;
    font-size: 0.875rem;
    font-weight: 500;
}

.profile-info span {
    margin-top: 0.15rem;
    color: #5a7f78;
    font-size: 0.6875rem;
}

@media (max-width: 48rem) {
    .sidebar-shell {
        display: flex;
        flex-direction: row;
        top: auto;
        bottom: 0.75rem;
        left: 0.75rem;
        width: calc(100% - 1.5rem);
        height: 4.25rem;
        margin: 0;
        overflow: visible;
        border: 1px solid rgba(90, 127, 120, 0.25);
        border-radius: 1.25rem;
        box-shadow: 0 0.5rem 2rem rgba(0, 0, 0, 0.3);
    }

    .sidebar-shell.fechada {
        width: calc(100% - 1.5rem);
    }

    .sidebar-header {
        display: none;
    }

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
        gap: 0.5rem;
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
    .tooltip {
        display: none;
    }
}

@media (max-width: 30rem) {
    .sidebar-shell {
        width: calc(100% - 1rem);
        height: 4rem;
        margin: 0.5rem;
        border-radius: 1rem;
    }

    .sidebar-shell.fechada {
        width: calc(100% - 1rem);
    }

    .sidebar-nav {
        gap: 0.25rem;
        padding: 0.4rem;
    }

    .nav-item,
    .nav-item.fechado {
        height: 3.1rem;
    }

    .nav-icon,
    .nav-icon svg {
        width: 1.2rem;
        height: 1.2rem;
    }
}
</style>