<script setup>
import Sidebar from '../components/Sidebar.vue'
</script>

<template>
    <div class="dashboard-layout">
        <Sidebar />

        <main class="dashboard-content">
            <router-view />
        </main>
    </div>
</template>

<style scoped>
.dashboard-layout {
    display: flex;
    width: 100%;
    min-height: 100vh;
    background: #010300;
}

.dashboard-content {
    display: flex;
    flex-direction: column;
    width: 100%;
    min-width: 0;
    min-height: 100vh;
    margin-left: 16rem;
    padding: 2rem;
    box-sizing: border-box;
}

@media (max-width: 48rem) {
    .dashboard-layout {
        display: flex;
        flex-direction: column;
        width: 100%;
        min-height: 100vh;
    }

    .dashboard-content {
        display: flex;
        flex-direction: column;
        width: 100%;
        min-width: 0;
        min-height: 100vh;
        margin-left: 0;
        padding-bottom: 6rem;
        padding: 1rem;
        box-sizing: border-box;
    }
}
</style>