// Aceita VITE_API_URL com ou sem "/api" no fim (as rotas já começam em /auth, /api...)
const BASE_URL = (import.meta.env.VITE_API_URL || 'http://localhost:8080')
    .trim()
    .replace(/\/+$/, '')
    .replace(/\/api$/, '')

const api = async (endpoint, options = {}) => {
    const token = localStorage.getItem('cti_token')
    const headers = new Headers(options.headers || {})
    const endpointPublico = ['/auth/login', '/auth/register'].includes(endpoint)

    if (options.body && !(options.body instanceof FormData)) {
        headers.set('Content-Type', 'application/json')
    }

    if (token && !endpointPublico) {
        headers.set('Authorization', `Bearer ${token}`)
    }

    const response = await fetch(
        `${BASE_URL}${endpoint}`,
        { ...options, headers }
    )
    const data = await response.json().catch(() => null)

    if (!response.ok) {
        if (response.status === 401 && !endpointPublico) {
            localStorage.removeItem('cti_token')
            localStorage.removeItem('cti_logado')
            localStorage.removeItem('cti_usuario')

            if (window.location.pathname !== '/login') {
                window.location.assign('/login?redirect=' + encodeURIComponent(window.location.pathname))
            }

            throw new Error('Sua sessão expirou. Faça login novamente.')
        }

        const validationMessage = data?.errors
            ?.map((item) => item.defaultMessage || item.message)
            .filter(Boolean)
            .join(' ')

        throw new Error(
            validationMessage ||
            data?.message ||
            data?.detail ||
            'Não foi possível concluir a solicitação.'
        )
    }

    return data
}

export default api

export const dashboardService = {
    obter: (loteId) => api(
        loteId ? `/api/dashboard?loteId=${loteId}` : '/api/dashboard'
    ),
    lotes: () => api('/api/dashboard/lotes')
}
