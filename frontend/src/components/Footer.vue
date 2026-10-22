<template>
    <footer class="footer" id="sobre">
        <div class="footer-container">
            <section class="footer-cta">
                <div class="cta-content">
                    <p class="eyebrow">INCTI INSIGHTS</p>
                    <h2>
                        Transforme seus dados em
                        <span class="gradient-text">decisões inteligentes.</span>
                    </h2>
                    <p class="cta-description">
                        Analise informações, identifique padrões e descubra novas
                        oportunidades para o seu negócio.
                    </p>
                </div>

                <RouterLink to="/cadastro" class="cta-button">
                    Começar agora
                    <span aria-hidden="true">↗</span>
                </RouterLink>
            </section>

            <div class="footer-main">
                <div class="footer-brand">
                    <RouterLink to="/" class="logo-link" aria-label="InCTI - Início">
                        <img src="../assets/icons/logo.png" alt="InCTI Insights" />
                    </RouterLink>

                    <p>
                        Inteligência e tecnologia para transformar dados em
                        conhecimento estratégico.
                    </p>

                    <div class="social-links">
                        <a href="#" aria-label="Instagram" class="social-link">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <rect x="3" y="3" width="18" height="18" rx="5" />
                                <circle cx="12" cy="12" r="4" />
                                <circle cx="17.5" cy="6.5" r="0.8" class="filled" />
                            </svg>
                        </a>

                        <a href="#" aria-label="LinkedIn" class="social-link">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path d="M5 9v10M5 5v.1M10 19v-6a4 4 0 0 1 8 0v6M10 9v10" />
                            </svg>
                        </a>

                        <a href="#" aria-label="GitHub" class="social-link">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path
                                    d="M9 19c-4.3 1.4-4.3-2.5-6-3m12 6v-3.9a3.4 3.4 0 0 0-.9-2.7c3-.3 6.2-1.5 6.2-6.7a5.2 5.2 0 0 0-1.4-3.6 4.8 4.8 0 0 0-.1-3.6s-1.2-.4-3.8 1.4a13.2 13.2 0 0 0-6.9 0C5.5 1.1 4.3 1.5 4.3 1.5a4.8 4.8 0 0 0-.1 3.6 5.2 5.2 0 0 0-1.4 3.6c0 5.2 3.2 6.4 6.2 6.7a3.4 3.4 0 0 0-.9 2.7V22" />
                            </svg>
                        </a>
                    </div>
                </div>

                <nav class="footer-column" aria-label="Navegação">
                    <h3>Navegação</h3>
                    <a href="#inicio">Início</a>
                    <a href="#plataforma">Plataforma</a>
                    <a href="#grafico">Insights</a>
                    <a href="#recursos">Recursos</a>
                    <a href="#sobre">Sobre nós</a>
                </nav>

                <nav class="footer-column" aria-label="Plataforma">
                    <h3>Plataforma</h3>
                    <RouterLink to="/login">Entrar</RouterLink>
                    <RouterLink to="/cadastro">Criar conta</RouterLink>
                </nav>

                <div class="footer-column footer-contact">
                    <h3>Vamos conversar?</h3>
                    <p>Tem alguma dúvida ou sugestão? Entre em contato.</p>
                    <a href="mailto:contato@incti.com" class="email-link">
                        contato@incti.com
                    </a>
                </div>
            </div>

            <div class="footer-bottom">
                <p>© {{ new Date().getFullYear() }} InCTI Insights.</p>
                <p>Feito para transformar dados em possibilidades.</p>
            </div>
        </div>
    </footer>
</template>

<style scoped>
.footer {
    position: relative;
    overflow: hidden;
    background: #ffffff;
    color: #29252a;
    border-top: 1px solid #f0e8eb;
}

.footer-container {
    width: min(1160px, calc(100% - 48px));
    margin: 0 auto;
}

.footer-cta {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 32px;
    margin: 64px 0;
    padding: 40px;
    border: 1px solid #f2dce4;
    border-radius: 24px;
    background:
        radial-gradient(circle at 90% 10%,
            rgba(255, 190, 85, 0.18),
            transparent 35%),
        linear-gradient(120deg, #fff5f8, #fffaf5);
}

.cta-content {
    max-width: 650px;
}

.eyebrow {
    margin: 0 0 12px;
    color: #c73a68;
    font-size: 12px;
    font-weight: 800;
    letter-spacing: 2px;
}

.footer-cta h2 {
    margin: 0;
    color: #29252a;
    font-size: clamp(26px, 3.5vw, 40px);
    font-weight: 750;
    line-height: 1.2;
    letter-spacing: -1.2px;
}

.gradient-text {
    display: block;
    color: #c73a68;
}

.cta-description {
    max-width: 540px;
    margin: 16px 0 0;
    color: #777078;
    font-size: 15px;
    line-height: 1.8;
}

.cta-button {
    display: inline-flex;
    flex-shrink: 0;
    align-items: center;
    justify-content: center;
    gap: 14px;
    padding: 15px 22px;
    border-radius: 12px;
    background: linear-gradient(110deg, #c73a68, #ff8a4c);
    color: #ffffff;
    font-size: 14px;
    font-weight: 700;
    text-decoration: none;
    box-shadow: 0 6px 18px rgba(199, 58, 104, 0.18);
    transition:
        transform 0.2s ease,
        box-shadow 0.2s ease;
}

.cta-button:hover {
    transform: translateY(-3px);
    box-shadow: 0 10px 24px rgba(199, 58, 104, 0.25);
}

.cta-button span {
    font-size: 18px;
}

.footer-main {
    display: grid;
    grid-template-columns: 1.5fr 0.8fr 0.8fr 1.2fr;
    gap: 48px;
    padding: 8px 0 52px;
}

.footer-brand {
    max-width: 290px;
}

.logo-link {
    display: inline-flex;
    align-items: center;
}

.logo-link img {
    display: block;
    width: 140px;
    max-width: 100%;
    height: auto;
    object-fit: contain;
}

.footer-brand>p,
.footer-contact>p {
    color: #777078;
    font-size: 14px;
    line-height: 1.8;
}

.footer-brand>p {
    margin: 18px 0;
}

.social-links {
    display: flex;
    gap: 10px;
    margin-top: 22px;
}

.social-link {
    display: grid;
    width: 38px;
    height: 38px;
    place-items: center;
    border: 1px solid #eee5e9;
    border-radius: 11px;
    background: #ffffff;
    color: #c73a68;
    transition:
        background 0.2s ease,
        color 0.2s ease,
        border-color 0.2s ease;
}

.social-link:hover {
    border-color: #c73a68;
    background: #c73a68;
    color: #ffffff;
}

.social-link svg {
    width: 18px;
    height: 18px;
    fill: none;
    stroke: currentColor;
    stroke-width: 1.7;
    stroke-linecap: round;
    stroke-linejoin: round;
}

.social-link svg .filled {
    fill: currentColor;
    stroke: none;
}

.footer-column {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
}

.footer-column h3 {
    margin: 5px 0 7px;
    color: #29252a;
    font-size: 14px;
    font-weight: 750;
}

.footer-column a {
    color: #777078;
    font-size: 13px;
    text-decoration: none;
    transition: color 0.2s ease;
}

.footer-column a:hover,
.email-link {
    color: #c73a68;
}

.footer-contact>p {
    margin: 0;
}

.footer-contact .email-link {
    font-weight: 650;
    overflow-wrap: anywhere;
}

.footer-bottom {
    display: flex;
    justify-content: space-between;
    gap: 16px;
    padding: 22px 0;
    border-top: 1px solid #eee7ea;
}

.footer-bottom p {
    margin: 0;
    color: #898189;
    font-size: 12px;
    line-height: 1.7;
}

@media (max-width: 1000px) {
    .footer-main {
        grid-template-columns: 1.3fr 1fr 1fr;
        gap: 36px;
    }

    .footer-contact {
        grid-column: 2 / 4;
    }

    .footer-cta {
        align-items: flex-start;
        flex-direction: column;
    }
}

@media (max-width: 640px) {
    .footer-container {
        width: min(100% - 32px, 480px);
    }

    .footer-cta {
        margin: 36px 0;
        padding: 26px 22px;
        border-radius: 18px;
    }

    .footer-cta h2 {
        font-size: 29px;
    }

    .cta-description {
        font-size: 14px;
    }

    .footer-main {
        grid-template-columns: 1fr 1fr;
        gap: 34px 22px;
        padding-bottom: 34px;
    }

    .footer-brand {
        grid-column: 1 / -1;
        max-width: 100%;
    }

    .footer-contact {
        grid-column: 1 / -1;
    }

    .footer-bottom {
        flex-direction: column;
        gap: 6px;
    }
}

@media (prefers-reduced-motion: reduce) {

    .cta-button,
    .social-link,
    .footer-column a {
        transition: none;
    }
}
</style>