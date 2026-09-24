<template>
    <footer class="footer">

        <div class="footer-glow"></div>

        <div class="footer-container">

            <div class="footer-main">

                <div class="footer-brand">

                    <router-link
                        to="/"
                        class="footer-logo-link"
                    >

                        <img
                            src="../assets/icons/logo.png"
                            alt="InCTI"
                            class="footer-logo"
                        >

                        <span class="footer-brand-name">
                            InCTI
                        </span>

                    </router-link>

                    <p class="footer-description">
                        Uma plataforma analítica criada para transformar
                        dados em contexto, contexto em inteligência e
                        inteligência em decisões.
                    </p>

                    <div class="footer-tagline">

                        <span class="footer-status"></span>

                        <span>
                            Enxergue os sinais por trás do ruído.
                        </span>

                    </div>

                    <div class="footer-socials">

                        <a
                            href="#"
                            aria-label="Instagram"
                            class="social-link"
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="1.6"
                            >
                                <rect
                                    width="18"
                                    height="18"
                                    x="3"
                                    y="3"
                                    rx="5"
                                />

                                <circle
                                    cx="12"
                                    cy="12"
                                    r="4"
                                />

                                <path d="M17.5 6.5h.01" />
                            </svg>
                        </a>

                        <a
                            href="#"
                            aria-label="LinkedIn"
                            class="social-link"
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="currentColor"
                            >
                                <path
                                    d="M6.5 8.5H3.2V21h3.3V8.5ZM4.85 3A2 2 0 1 0 4.84 7 2 2 0 0 0 4.85 3ZM21 13.9c0-3.76-2-5.5-4.68-5.5-2.15 0-3.11 1.18-3.65 2.02V8.5H9.37V21h3.3v-6.19c0-1.63.3-3.2 2.32-3.2 1.99 0 2.02 1.86 2.02 3.3V21H20.3l.7-7.1Z"
                                />
                            </svg>
                        </a>

                        <a
                            href="#"
                            aria-label="GitHub"
                            class="social-link"
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="currentColor"
                            >
                                <path
                                    fill-rule="evenodd"
                                    d="M12 2C6.48 2 2 6.58 2 12.24c0 4.52 2.87 8.36 6.84 9.71.5.1.68-.22.68-.49v-1.72c-2.78.62-3.37-1.23-3.37-1.23-.46-1.2-1.12-1.52-1.12-1.52-.91-.64.07-.63.07-.63 1.01.08 1.54 1.06 1.54 1.06.9 1.58 2.36 1.12 2.94.86.09-.67.35-1.12.64-1.38-2.22-.26-4.55-1.15-4.55-5.06 0-1.12.39-2.03 1.03-2.75-.1-.26-.45-1.3.1-2.71 0 0 .84-.28 2.76 1.05A9.2 9.2 0 0 1 12 8.83c.85 0 1.71.12 2.51.37 1.92-1.33 2.76-1.05 2.76-1.05.55 1.41.2 2.45.1 2.71.64.72 1.03 1.63 1.03 2.75 0 3.92-2.34 4.8-4.57 5.05.36.32.68.94.68 1.9v2.81c0 .27.18.59.69.49A10.25 10.25 0 0 0 22 12.24C22 6.58 17.52 2 12 2Z"
                                    clip-rule="evenodd"
                                />
                            </svg>
                        </a>

                    </div>

                </div>

                <div class="footer-links">

                    <div class="footer-column">

                        <h3 class="footer-title">
                            Navegação
                        </h3>

                        <div class="footer-list">

                            <a href="#inicio">
                                Início
                            </a>

                            <a href="#plataforma">
                                Plataforma
                            </a>

                            <a href="#grafico">
                                Insights
                            </a>

                            <a href="#recursos">
                                Recursos
                            </a>

                            <a href="#sobre">
                                Sobre
                            </a>

                        </div>

                    </div>

                    <div class="footer-column">

                        <h3 class="footer-title">
                            Plataforma
                        </h3>

                        <div class="footer-list">

                            <a href="#plataforma">
                                Análise
                            </a>

                            <a href="#grafico">
                                Monitoramento
                            </a>

                            <a href="#grafico">
                                Indicadores
                            </a>

                            <a href="#recursos">
                                Visualizações
                            </a>

                            <router-link to="/login">
                                Acessar plataforma
                            </router-link>

                        </div>

                    </div>

                    <div class="footer-column footer-contact">

                        <h3 class="footer-title">
                            Vamos conversar
                        </h3>

                        <p>
                            Quer conhecer melhor a plataforma?
                            Entre em contato com nossa equipe.
                        </p>

                        <a
                            href="mailto:contato@incti.com"
                            class="footer-email"
                        >
                            contato@incti.com
                        </a>

                        <router-link
                            to="/login"
                            class="footer-button"
                        >
                            Conhecer a plataforma
                            <span>→</span>
                        </router-link>

                    </div>

                </div>

            </div>

            <div class="footer-divider"></div>

            <div class="footer-bottom">

                <div class="footer-legal">

                    <span>
                        © 2026 InCTI. Todos os direitos reservados.
                    </span>

                    <a href="#">
                        Política de Privacidade
                    </a>

                    <a href="#">
                        Termos de Uso
                    </a>

                </div>

                <div class="footer-platform">

                    <span class="footer-status"></span>

                    <span>
                        Plataforma InCTI
                    </span>

                </div>

            </div>

        </div>

    </footer>
</template>

<style scoped>
.footer {
    position: relative;
    width: 100%;
    overflow: hidden;
    border-top: 1px solid rgba(255, 255, 255, 0.06);
    background: #010300;
}

.footer-glow {
    position: absolute;
    top: -10rem;
    left: 50%;
    width: 30rem;
    height: 20rem;
    transform: translateX(-50%);
    border-radius: 50%;
    background: rgba(187, 222, 198, 0.04);
    filter: blur(7rem);
    pointer-events: none;
}

.footer-container {
    position: relative;
    width: 100%;
    max-width: 90rem;
    margin: 0 auto;
    padding: 4rem 3rem 2rem;
}

.footer-main {
    display: flex;
    width: 100%;
    align-items: flex-start;
    justify-content: space-between;
    gap: 4rem;
}

.footer-brand {
    display: flex;
    width: 24rem;
    max-width: 100%;
    flex-direction: column;
    align-items: flex-start;
    flex-shrink: 0;
}

.footer-logo-link {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.75rem;
    text-decoration: none;
}

.footer-logo {
    width: 2.5rem;
    height: 2.5rem;
    object-fit: contain;
}

.footer-brand-name {
    color: white;
    font-size: 1.15rem;
    font-weight: 600;
    letter-spacing: 0.08em;
}

.footer-description {
    width: 100%;
    margin-top: 1.5rem;
    color: rgba(255, 255, 255, 0.4);
    font-size: 0.85rem;
    line-height: 1.8;
}

.footer-tagline {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    margin-top: 1.25rem;
    color: rgba(187, 222, 198, 0.65);
    font-size: 0.6rem;
    letter-spacing: 0.03em;
}

.footer-status {
    width: 0.4rem;
    height: 0.4rem;
    flex-shrink: 0;
    border-radius: 50%;
    background: #bbdec6;
    box-shadow: 0 0 0.8rem rgba(187, 222, 198, 0.8);
}

.footer-socials {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    gap: 0.65rem;
    margin-top: 1.75rem;
}

.social-link {
    display: flex;
    width: 2.5rem;
    height: 2.5rem;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(255, 255, 255, 0.08);
    border-radius: 0.7rem;
    background: rgba(255, 255, 255, 0.025);
    color: rgba(255, 255, 255, 0.35);
    transition: 0.25s ease;
}

.social-link svg {
    width: 1rem;
    height: 1rem;
}

.social-link:hover {
    border-color: rgba(187, 222, 198, 0.2);
    background: rgba(187, 222, 198, 0.05);
    color: #bbdec6;
}

.footer-links {
    display: flex;
    flex: 1;
    align-items: flex-start;
    justify-content: center;
    gap: 5rem;
}

.footer-column {
    display: flex;
    min-width: 8rem;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

.footer-title {
    color: rgba(255, 255, 255, 0.75);
    font-size: 0.7rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.18em;
}

.footer-list {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    margin-top: 1.5rem;
}

.footer-list a {
    color: rgba(255, 255, 255, 0.38);
    font-size: 0.75rem;
    text-decoration: none;
    transition: 0.2s ease;
}

.footer-list a:hover {
    color: #bbdec6;
}

.footer-contact {
    width: 14rem;
}

.footer-contact p {
    margin-top: 1.5rem;
    color: rgba(255, 255, 255, 0.3);
    font-size: 0.75rem;
    line-height: 1.7;
}

.footer-email {
    margin-top: 1rem;
    color: #bbdec6;
    font-size: 0.75rem;
    text-decoration: none;
    transition: 0.2s ease;
}

.footer-email:hover {
    color: white;
}

.footer-button {
    display: flex;
    width: 100%;
    height: 2.9rem;
    margin-top: 1.5rem;
    align-items: center;
    justify-content: center;
    gap: 0.6rem;
    border: 1px solid rgba(187, 222, 198, 0.15);
    border-radius: 0.65rem;
    background: rgba(187, 222, 198, 0.05);
    color: #bbdec6;
    font-size: 0.7rem;
    text-decoration: none;
    transition: 0.25s ease;
}

.footer-button:hover {
    border-color: rgba(187, 222, 198, 0.3);
    background: rgba(187, 222, 198, 0.1);
    color: white;
}

.footer-divider {
    width: 100%;
    height: 1px;
    margin: 3rem 0 1.75rem;
    background: rgba(255, 255, 255, 0.06);
}

.footer-bottom {
    display: flex;
    width: 100%;
    align-items: center;
    justify-content: space-between;
    gap: 2rem;
}

.footer-legal {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: wrap;
    gap: 1.5rem;
}

.footer-legal span,
.footer-legal a {
    color: rgba(255, 255, 255, 0.22);
    font-size: 0.65rem;
    text-decoration: none;
}

.footer-legal a:hover {
    color: rgba(255, 255, 255, 0.5);
}

.footer-platform {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    color: rgba(255, 255, 255, 0.2);
    font-size: 0.6rem;
    text-transform: uppercase;
    letter-spacing: 0.15em;
    white-space: nowrap;
}

@media (max-width: 1100px) {
    .footer-container {
        padding: 3.5rem 2rem 2rem;
    }

    .footer-main {
        gap: 2.5rem;
    }

    .footer-links {
        gap: 2.5rem;
    }

    .footer-brand {
        width: 21rem;
    }

    .footer-contact {
        width: 12rem;
    }
}

@media (max-width: 900px) {
    .footer-main {
        flex-direction: column;
        align-items: center;
        gap: 3rem;
    }

    .footer-brand {
        width: 100%;
        align-items: center;
        text-align: center;
    }

    .footer-description {
        max-width: 32rem;
    }

    .footer-socials {
        justify-content: center;
    }

    .footer-links {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        align-items: flex-start;
        justify-content: center;
        gap: 3rem;
    }

    .footer-contact {
        width: 16rem;
    }

    .footer-bottom {
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .footer-legal {
        justify-content: center;
        text-align: center;
    }
}

@media (max-width: 640px) {
    .footer-container {
        padding: 3rem 1.25rem 1.5rem;
    }

    .footer-main {
        gap: 2.5rem;
    }

    .footer-brand-name {
        font-size: 1rem;
    }

    .footer-description {
        margin-top: 1.25rem;
        font-size: 0.8rem;
        line-height: 1.7;
    }

    .footer-tagline {
        justify-content: center;
        text-align: center;
        line-height: 1.5;
    }

    .footer-links {
        flex-direction: column;
        align-items: center;
        gap: 2.5rem;
    }

    .footer-column {
        width: 100%;
    }

    .footer-contact {
        width: 100%;
        max-width: 18rem;
    }

    .footer-divider {
        margin: 2.5rem 0 1.5rem;
    }

    .footer-legal {
        flex-direction: column;
        gap: 0.75rem;
    }

    .footer-platform {
        font-size: 0.5rem;
    }
}

@media (max-width: 400px) {
    .footer-container {
        padding-left: 1rem;
        padding-right: 1rem;
    }

    .footer-description {
        max-width: 18rem;
        font-size: 0.75rem;
    }

    .footer-title {
        font-size: 0.65rem;
    }

    .footer-list a,
    .footer-contact p,
    .footer-email {
        font-size: 0.7rem;
    }

    .social-link {
        width: 2.35rem;
        height: 2.35rem;
    }

    .footer-button {
        height: 2.75rem;
        font-size: 0.65rem;
    }
}
</style>