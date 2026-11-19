import { defineStore } from 'pinia'
import * as XLSX from 'xlsx'
import api, { dashboardService } from '../services/api'

const TIPOS_BLOQUEANTES = ['Campo vazio', 'E-mail inválido', 'Registro duplicado']

export const useUploadStore = defineStore('upload', {
    // ==========================================
    // STATE
    // ==========================================
    state: () => ({
        arquivo: null,

        dadosOriginais: [],

        dadosTratados: [],

        erros: [],

        carregando: false,

        validacaoRealizada: false,

        erro: '',
        enviando: false,
        loteId: null,
        dashboard: null,
        erroEnvio: ''
    }),

    // ==========================================
    // GETTERS
    // ==========================================
    getters: {

        // Quantidade total de registros
        totalLinhas: (state) => state.dadosTratados.length,

        // Quantidade de colunas
        totalColunas: (state) => {
            if (!state.dadosTratados.length) return 0

            return Object.keys(state.dadosTratados[0]).length
        },

        // Nome das colunas
        colunas: (state) => {
            if (!state.dadosTratados.length) return []

            return Object.keys(state.dadosTratados[0])
        },

        // Quantidade de erros encontrados
        quantidadeErros: (state) => state.erros.length,

        // Quantidade de registros que possuem erro
        quantidadeComErro: (state) => {
            const linhasComErro = new Set(
                state.erros.map((erro) => erro.linha)
            )

            return linhasComErro.size
        },

        // Quantidade de registros válidos
        quantidadeValidos() {
            return this.totalLinhas - this.quantidadeComErro
        },

        // Linhas que não podem ir para o ETL. Espaços extras e
        // "fora do padrão" são só avisos: o front e o Python já corrigem.
        linhasBloqueadas: (state) => new Set(
            state.erros
                .filter((erro) => TIPOS_BLOQUEANTES.includes(erro.tipo))
                .map((erro) => erro.linha)
        ),

        // Linhas que têm apenas avisos (são enviadas mesmo assim)
        quantidadeComAviso() {
            const comAviso = new Set(
                this.erros
                    .filter((erro) => !this.linhasBloqueadas.has(erro.linha))
                    .map((erro) => erro.linha)
            )
            return comAviso.size
        },

        // Retorna os registros que serão enviados para análise
        dadosValidos() {
            return this.dadosTratados.filter(
                (_, index) => !this.linhasBloqueadas.has(index + 2)
            )
        },

        // Quantidade de campos vazios
        quantidadeCamposVazios: (state) => {
            return state.erros.filter(
                (erro) => erro.tipo === 'Campo vazio'
            ).length
        },

        // Quantidade de e-mails inválidos
        quantidadeEmailsInvalidos: (state) => {
            return state.erros.filter(
                (erro) => erro.tipo === 'E-mail inválido'
            ).length
        },

        // Quantidade de registros duplicados
        quantidadeDuplicados: (state) => {
            return state.erros.filter(
                (erro) => erro.tipo === 'Registro duplicado'
            ).length
        },

        // Quantidade de dados fora do padrão
        quantidadeForaDoPadrao: (state) => {
            return state.erros.filter(
                (erro) => erro.tipo === 'Fora do padrão'
            ).length
        },

        // Quantidade de espaços desnecessários
        quantidadeEspacos: (state) => {
            return state.erros.filter(
                (erro) => erro.tipo === 'Espaços desnecessários'
            ).length
        }
    },

    // ==========================================
    // ACTIONS
    // ==========================================
    actions: {

        // ==========================================
        // LER ARQUIVO
        // ==========================================
        async lerArquivo(file) {

            this.erro = ''
            this.erros = []
            this.dadosOriginais = []
            this.dadosTratados = []
            this.validacaoRealizada = false

            if (!file) return

            const extensao = file.name
                .split('.')
                .pop()
                ?.toLowerCase()

            if (!['xlsx', 'xls', 'csv'].includes(extensao)) {

                this.arquivo = null

                this.erro =
                    'Formato inválido. Use XLSX, XLS ou CSV.'

                return
            }

            this.carregando = true

            try {

                const buffer = await file.arrayBuffer()

                const workbook = XLSX.read(buffer, {
                    type: 'array'
                })

                const nomePrimeiraAba =
                    workbook.SheetNames[0]

                const worksheet =
                    workbook.Sheets[nomePrimeiraAba]

                if (!worksheet) {
                    throw new Error(
                        'A planilha não possui uma aba válida.'
                    )
                }

                this.dadosOriginais =
                    XLSX.utils.sheet_to_json(
                        worksheet,
                        {
                            defval: ''
                        }
                    )

                if (!this.dadosOriginais.length) {

                    this.arquivo = null

                    this.erro =
                        'A planilha está vazia.'

                    return
                }

                this.arquivo = file

                this.tratarDados()

                this.validarDados()

            } catch (error) {

                console.error(error)

                this.arquivo = null

                this.erro =
                    'Não foi possível ler a planilha.'

            } finally {

                this.carregando = false
            }
        },

        async enviarParaAnalise() {
            if (!this.dadosValidos.length || this.enviando) return

            this.enviando = true
            this.erroEnvio = ''

            try {
                const resposta = await api('/analytics/lotes', {
                    method: 'POST',
                    body: JSON.stringify({
                        arquivoNome: this.arquivo?.name || 'planilha',
                        recebidas: this.dadosTratados.length,
                        // precisa bater: recebidas = linhas enviadas + invalidas
                        invalidas: this.linhasBloqueadas.size,
                        comAviso: this.quantidadeComAviso,
                        linhas: this.dadosValidos
                    })
                })
                this.loteId = resposta.loteId
                const dashboard = await dashboardService.obter(this.loteId)
                this.dashboard = dashboard.estatisticas
                return this.loteId
            } catch (error) {
                this.erroEnvio = error.message
                throw error
            } finally {
                this.enviando = false
            }
        },

        async carregarDashboard(loteId = this.loteId) {
            if (!loteId) return null
            const dashboard = await dashboardService.obter(loteId)
            this.dashboard = dashboard.estatisticas
            this.loteId = loteId
            return this.dashboard
        },

        // ==========================================
        // TRATAR DADOS
        // ==========================================
        tratarDados() {

            this.dadosTratados =
                this.dadosOriginais.map((linha) => {

                    const novaLinha = {}

                    for (
                        const [chave, valor]
                        of Object.entries(linha)
                    ) {

                        if (
                            chave === 'data_contratacao' &&
                            typeof valor === 'number' &&
                            valor > 1 &&
                            valor < 60000
                        ) {
                            const data = XLSX.SSF.parse_date_code(valor)
                            novaLinha[chave] = [
                                data.y,
                                String(data.m).padStart(2, '0'),
                                String(data.d).padStart(2, '0')
                            ].join('-')
                        } else if (typeof valor === 'string') {

                            novaLinha[chave] =
                                valor.trim()

                        } else {

                            novaLinha[chave] =
                                valor
                        }
                    }

                    // Padronização do segmento
                    if (
                        typeof novaLinha.segmento === 'string'
                    ) {

                        const segmento =
                            novaLinha.segmento
                                .trim()
                                .toUpperCase()

                        const mapaSegmentos = {

                            'IND.': 'Indústria',

                            'INDUSTRIA': 'Indústria',

                            'INDÚSTRIA': 'Indústria',

                            'COMERCIO': 'Comércio',

                            'COMÉRCIO': 'Comércio',

                            'SERVICOS': 'Serviços',

                            'SERVIÇOS': 'Serviços'
                        }

                        novaLinha.segmento =
                            mapaSegmentos[segmento] ||
                            novaLinha.segmento
                    }

                    // Padronização do nível do cliente
                    if (
                        typeof novaLinha.nivel_cliente === 'string'
                    ) {

                        novaLinha.nivel_cliente =
                            novaLinha.nivel_cliente
                                .toUpperCase()
                    }

                    return novaLinha
                })
        },

        // ==========================================
        // VALIDAR DADOS
        // ==========================================
        validarDados() {

            this.erros = []

            const colunas = this.colunas

            this.dadosTratados.forEach(
                (linha, index) => {

                    // Excel começa na linha 2
                    // porque a linha 1 é o cabeçalho
                    const numeroLinha = index + 2

                    // ----------------------------------
                    // CAMPOS VAZIOS
                    // ----------------------------------
                    const camposObrigatorios = [
                        'codigo_cliente',
                        'nome_cliente',
                        'faturamento_anual'
                    ]

                    camposObrigatorios.forEach((campo) => {
                        const valor = linha[campo]

                        if (
                            valor === '' ||
                            valor === null ||
                            valor === undefined
                        ) {
                            this.erros.push({
                                linha: numeroLinha,
                                campo,
                                tipo: 'Campo vazio',
                                descricao:
                                    `O campo obrigatório "${campo}" está vazio.`
                            })
                        }
                    })

                    // ----------------------------------
                    // E-MAIL
                    // ----------------------------------
                    const campoEmail =
                        colunas.find(
                            (coluna) =>
                                coluna.toLowerCase()
                                    .includes('email') ||
                                coluna.toLowerCase()
                                    .includes('e-mail')
                        )

                    if (campoEmail) {

                        const email =
                            String(
                                linha[campoEmail] || ''
                            ).trim()

                        if (email !== '') {

                            const emailValido =
                                /^[^\s@]+@[^\s@]+\.[^\s@]+$/
                                    .test(email)

                            if (!emailValido) {

                                this.erros.push({

                                    linha: numeroLinha,

                                    campo: campoEmail,

                                    tipo: 'E-mail inválido',

                                    descricao:
                                        `O e-mail "${email}" não possui um formato válido.`
                                })
                            }
                        }
                    }

                    // ----------------------------------
                    // ESPAÇOS DESNECESSÁRIOS
                    // ----------------------------------
                    Object.entries(
                        this.dadosOriginais[index] || {}
                    ).forEach(
                        ([campo, valor]) => {

                            if (
                                typeof valor === 'string' &&
                                (
                                    valor !== valor.trim() ||
                                    /\s{2,}/.test(valor)
                                )
                            ) {

                                this.erros.push({

                                    linha: numeroLinha,

                                    campo: campo,

                                    tipo: 'Espaços desnecessários',

                                    descricao:
                                        `O campo "${campo}" possui espaços desnecessários.`
                                })
                            }
                        }
                    )

                    // ----------------------------------
                    // DADOS FORA DO PADRÃO
                    // ----------------------------------

                    // Segmento
                    if (
                        linha.segmento &&
                        ![
                            'Indústria',
                            'Comércio',
                            'Serviços'
                        ].includes(linha.segmento)
                    ) {

                        this.erros.push({

                            linha: numeroLinha,

                            campo: 'segmento',

                            tipo: 'Fora do padrão',

                            descricao:
                                'O segmento informado não está dentro dos padrões esperados.'
                        })
                    }

                    // Nível do cliente
                    if (
                        linha.nivel_cliente &&
                        ![
                            'A',
                            'B',
                            'C'
                        ].includes(
                            String(
                                linha.nivel_cliente
                            ).toUpperCase()
                        )
                    ) {

                        this.erros.push({

                            linha: numeroLinha,

                            campo: 'nivel_cliente',

                            tipo: 'Fora do padrão',

                            descricao:
                                'O nível do cliente deve ser A, B ou C.'
                        })
                    }
                }
            )

            // ----------------------------------
            // REGISTROS DUPLICADOS
            // ----------------------------------

            const registros = new Map()

            this.dadosTratados.forEach(
                (linha, index) => {

                    const numeroLinha = index + 2

                    // Transformamos a linha inteira
                    // em uma string para comparação
                    const identificador =
                        JSON.stringify(linha)

                    if (
                        registros.has(identificador)
                    ) {

                        this.erros.push({

                            linha: numeroLinha,

                            campo: 'Registro',

                            tipo: 'Registro duplicado',

                            descricao:
                                `O registro é duplicado da linha ${registros.get(identificador)}.`
                        })

                    } else {

                        registros.set(
                            identificador,
                            numeroLinha
                        )
                    }
                }
            )

            this.validacaoRealizada = true
        },

        // ==========================================
        // LIMPAR
        // ==========================================
        limpar() {

            this.arquivo = null

            this.dadosOriginais = []

            this.dadosTratados = []

            this.erros = []

            this.erro = ''

            this.carregando = false

            this.validacaoRealizada = false
            this.loteId = null
            this.dashboard = null
            this.erroEnvio = ''
        }
    }
})