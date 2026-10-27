const api = async (endpoint, options = {}) => {
    const token = localStorage.getItem('cti_token')
    const headers = new Headers(options.headers || {})

    if (options.body && !(options.body instanceof FormData)) {
        headers.set('Content-Type', 'application/json')
    }

    if (token) {
        headers.set('Authorization', `Bearer ${token}`)
    }

    const response = await fetch(
        `${import.meta.env.VITE_API_URL || 'http://localhost:8080'}${endpoint}`,
        { ...options, headers }
    )
    const data = await response.json().catch(() => null)

    if (!response.ok) {
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
