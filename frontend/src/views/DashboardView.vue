
<script setup>
import Sidebar from '../components/Sidebar.vue'
</script>

<template>
    <div class="dashboard-layout">
        <div class="dashboard-decoration dashboard-decoration-pink"></div>
        <div class="dashboard-decoration dashboard-decoration-orange"></div>

        <Sidebar />

        <main class="dashboard-content">
            <div class="dashboard-top-line">
                <div class="dashboard-top-brand">
                    <span class="dashboard-brand-dot"></span>
                    <span>InCTI</span>
                    <span class="dashboard-brand-divider">/</span>
                    <span class="dashboard-brand-section">Insights & Intelligence</span>
                </div>

                <div class="dashboard-top-status">
                    <span class="dashboard-status-indicator"></span>
                    Ambiente de análise
                </div>
            </div>

            <div class="dashboard-page">
                <router-view v-slot="{ Component, route }">
                    <transition name="page-transition" mode="out-in">
                        <component :is="Component" :key="route.fullPath" />
                    </transition>
                </router-view>
            </div>

            <footer class="dashboard-footer">
                <div class="dashboard-footer-brand">
                    <span class="footer-brand-mark"></span>
                    <span>InCTI Insights</span>
                </div>

                <p>Transformando dados em descobertas.</p>

                <div class="dashboard-footer-colors">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
            </footer>
        </main>
    </div>
</template>

<style scoped>
.dashboard-layout {
    display: flex;
    position: relative;
    width: 100%;
    min-height: 100vh;
    overflow-x: clip;
    background: #faf9fc;
    color: #302536;
    isolation: isolate;
}

.dashboard-decoration {
    position: fixed;
    z-index: -1;
    pointer-events: none;
    border-radius: 50%;
    filter: blur(5rem);
}

.dashboard-decoration-pink {
    top: -7rem;
    right: -5rem;
    width: 20rem;
    height: 20rem;
    background: rgba(199, 58, 104, 0.055);
}

.dashboard-decoration-orange {
    bottom: 3rem;
    right: 2rem;
    width: 16rem;
    height: 16rem;
    background: rgba(255, 138, 76, 0.045);
}

.dashboard-content {
    display: flex;
    flex: 1;
    flex-direction: column;
    width: calc(100% - 16rem);
    min-width: 0;
    min-height: 100vh;
    margin-left: 16rem;
    padding: 1.5rem 2rem 1rem;
    box-sizing: border-box;
    transition: margin-left 0.3s ease, width 0.3s ease;
}

.dashboard-top-line {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
    width: 100%;
    min-height: 2.75rem;
    margin-bottom: 1.75rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid #eee7ef;
    box-sizing: border-box;
}

.dashboard-top-brand {
    display: flex;
    align-items: center;
    gap: 0.625rem;
    min-width: 0;
    color: #302536;
    font-size: 0.8125rem;
    font-weight: 800;
    letter-spacing: 0.015em;
}

.dashboard-brand-dot {
    width: 0.625rem;
    height: 0.625rem;
    flex-shrink: 0;
    background: linear-gradient(135deg, #c73a68, #ff8a4c);
    border-radius: 0.1875rem;
    transform: rotate(45deg);
}

.dashboard-brand-divider {
    color: #d5c8d4;
}

.dashboard-brand-section {
    overflow: hidden;
    color: #948594;
    font-size: 0.75rem;
    font-weight: 500;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.dashboard-top-status {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    flex-shrink: 0;
    padding: 0.5rem 0.75rem;
    color: #8b7888;
    background: rgba(255, 255, 255, 0.8);
    border: 1px solid #eee6ed;
    border-radius: 999px;
    font-size: 0.6875rem;
    font-weight: 600;
}

.dashboard-status-indicator {
    width: 0.4375rem;
    height: 0.4375rem;
    background: #43b889;
    border-radius: 50%;
    box-shadow: 0 0 0 0.1875rem rgba(67, 184, 137, 0.1);
}

.dashboard-page {
    display: flex;
    flex: 1;
    flex-direction: column;
    width: 100%;
    min-width: 0;
    min-height: 0;
    animation: content-appear 0.45s ease both;
}

.dashboard-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 0.75rem;
    width: 100%;
    margin-top: 2rem;
    padding: 1.25rem 0.25rem 0.5rem;
    border-top: 1px solid #eee7ef;
    box-sizing: border-box;
}

.dashboard-footer-brand {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: #796b7a;
    font-size: 0.6875rem;
    font-weight: 700;
}

.footer-brand-mark {
    width: 0.5rem;
    height: 0.5rem;
    background: #c73a68;
    border-radius: 0.125rem;
    box-shadow: 0.25rem 0.125rem 0 #ff8a4c;
}

.dashboard-footer p {
    margin: 0;
    color: #a397a3;
    font-size: 0.6875rem;
}

.dashboard-footer-colors {
    display: flex;
    align-items: center;
    gap: 0.3rem;
}

.dashboard-footer-colors span {
    width: 0.375rem;
    height: 0.375rem;
    border-radius: 50%;
}

.dashboard-footer-colors span:nth-child(1) {
    background: #c73a68;
}

.dashboard-footer-colors span:nth-child(2) {
    background: #ff8a4c;
}

.dashboard-footer-colors span:nth-child(3) {
    background: #ffbe55;
}

.page-transition-enter-active,
.page-transition-leave-active {
    transition: opacity 0.18s ease, transform 0.18s ease;
}

.page-transition-enter-from {
    opacity: 0;
    transform: translateY(0.375rem);
}

.page-transition-leave-to {
    opacity: 0;
    transform: translateY(-0.25rem);
}

@keyframes content-appear {
    from {
        opacity: 0;
        transform: translateY(0.375rem);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@media (min-width: 90rem) {
    .dashboard-content {
        padding-right: 3rem;
        padding-left: 3rem;
    }

    .dashboard-top-line {
        margin-bottom: 2rem;
    }
}

@media (max-width: 64rem) {
    .dashboard-content {
        padding: 1.5rem;
    }

    .dashboard-top-line {
        margin-bottom: 1.5rem;
    }

    .dashboard-footer {
        margin-top: 1.5rem;
    }
}

@media (max-width: 48rem) {
    .dashboard-layout {
        min-height: 100vh;
    }

    .dashboard-content {
        width: 100%;
        min-height: 100vh;
        margin-left: 0;
        padding: 1.25rem 1rem 6.5rem;
    }

    .dashboard-top-line {
        min-height: 2.5rem;
        margin-bottom: 1.25rem;
        padding-bottom: 0.875rem;
    }

    .dashboard-top-brand {
        gap: 0.5rem;
        font-size: 0.75rem;
    }

    .dashboard-brand-section {
        max-width: 10rem;
        font-size: 0.6875rem;
    }

    .dashboard-top-status {
        padding: 0.4375rem 0.5625rem;
        font-size: 0.625rem;
    }

    .dashboard-page {
        overflow-wrap: anywhere;
    }

    .dashboard-footer {
        justify-content: center;
        gap: 0.75rem;
        margin-top: 1.5rem;
        padding-top: 1rem;
        text-align: center;
    }

    .dashboard-footer p {
        width: 100%;
        order: 3;
    }
}

@media (max-width: 30rem) {
    .dashboard-content {
        padding: 1rem 0.875rem 6rem;
    }

    .dashboard-top-line {
        align-items: flex-start;
        gap: 0.625rem;
        margin-bottom: 1rem;
    }

    .dashboard-top-brand {
        flex-wrap: wrap;
        row-gap: 0.375rem;
    }

    .dashboard-brand-section {
        max-width: 8rem;
    }

    .dashboard-top-status {
        gap: 0.375rem;
        padding: 0.375rem 0.5rem;
        font-size: 0.5625rem;
    }

    .dashboard-status-indicator {
        width: 0.375rem;
        height: 0.375rem;
    }

    .dashboard-footer {
        margin-top: 1.25rem;
    }
}

@media (prefers-reduced-motion: reduce) {
    .dashboard-content,
    .dashboard-page,
    .page-transition-enter-active,
    .page-transition-leave-active {
        animation: none;
        transition: none;
    }
}
</style>
