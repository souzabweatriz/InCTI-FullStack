import { defineStore } from "pinia";
import api from "../services/api";

export const useAuthStore = defineStore("auth", {
    // STATE = dados que ficam guardados enquanto a aplicação está em uso.
    state: () => ({
        logado: localStorage.getItem("cti_logado") === "true",
        usuario: localStorage.getItem("cti_usuario") || "",
        token: localStorage.getItem("cti_token") || "",
    }),

    // GETTERS = valores calculados a partir do state.
    getters: {
        nomeExibicao: (state) => state.usuario || "Usuário",
    },

    // ACTIONS = ações que alteram o state.
    actions: {
        async login(usuario, senha) {
            if (!usuario || !senha) {
                return false;
            }

            const resposta = await api('/auth/login', {
                method: 'POST',
                body: JSON.stringify({ email: usuario, senha }),
            })

            if (!resposta?.token || !resposta?.nome) {
                throw new Error('Resposta de autenticação inválida.')
            }

            this.logado = true;
            this.usuario = resposta.nome;
            this.token = resposta.token;

            localStorage.setItem("cti_logado", "true");
            localStorage.setItem("cti_usuario", resposta.nome);
            localStorage.setItem("cti_token", resposta.token);

            return true;
        },

        logout() {
            this.logado = false;
            this.usuario = "";
            this.token = "";

            localStorage.removeItem("cti_logado");
            localStorage.removeItem("cti_usuario");
            localStorage.removeItem("cti_token");
        },
    },
});
