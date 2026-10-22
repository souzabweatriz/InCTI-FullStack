const api = async (endpoint, options = {}) => {
	const token = localStorage.getItem('cti_token')
	const headers = new Headers(options.headers || {})

	headers.set('Content-Type', 'application/json')

	if (token) {
		headers.set('Authorization', `Bearer ${token}`)
	}

	const response = await fetch(`${import.meta.env.VITE_API_URL || 'http://localhost:8080'}${endpoint}`, {
		...options,
		headers,
	})
	const data = await response.json().catch(() => null)

	if (!response.ok) {
		throw new Error(data?.message || 'Não foi possível concluir a solicitação.')
	}

	return data
}

export default api
