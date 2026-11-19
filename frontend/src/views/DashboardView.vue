
<script setup>
import Sidebar from '../components/Sidebar.vue'
</script>

<template>
    <div class="dashboard-layout">
        <div class="dashboard-decoration dashboard-decoration-pink"></div>
        <div class="dashboard-decoration dashboard-decoration-orange"></div>

        <Sidebar />

        <main class="dashboard-content">
            <div class="dashboard-page">
                <router-view v-slot="{ Component, route }">
                    <transition name="page-transition" mode="out-in">
                        <component :is="Component" :key="route.fullPath" />
                    </transition>
                </router-view>
            </div>

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








.dashboard-page {
    display: flex;
    flex: 1;
    flex-direction: column;
    width: 100%;
    min-width: 0;
    min-height: 0;
    animation: content-appear 0.45s ease both;
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

}

@media (max-width: 64rem) {
    .dashboard-content {
        padding: 1.5rem;
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





    .dashboard-page {
        overflow-wrap: anywhere;
    }


}

@media (max-width: 30rem) {
    .dashboard-content {
        padding: 1rem 0.875rem 6rem;
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
