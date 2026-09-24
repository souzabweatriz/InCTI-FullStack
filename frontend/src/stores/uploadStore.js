import { defineStore } from 'pinia'
import * as XLSX from 'xlsx'

export const useUploadStore = defineStore('upload', {

    state: () => ({
        arquivo: null,
        dadosOriginais: [],
        dadosTratados: [],
        erro: '',
        carregando: false
    }),

    getters: {

        totalLinhas: (state) => state.dadosTratados.length,

        totalColunas: (state) => {
            if (!state.dadosTratados.length) return 0

            return Object.keys(state.dadosTratados[0]).length
        },

        colunas: (state) => {
            if (!state.dadosTratados.length) return []

            return Object.keys(state.dadosTratados[0])
        },

        dadosValidos: (state) => {
            return state.dadosTratados.filter((linha) => {
                return Object.values(linha).some(
                    (valor) => valor !== ''
                )
            })
        },

        quantidadeValidos: (state) => {
            return state.dadosTratados.filter((linha) => {
                return Object.values(linha).some(
                    (valor) => valor !== ''
                )
            }).length
        }

    },

    actions: {

        async lerArquivo(file) {

            this.erro = ''
            this.dadosOriginais = []
            this.dadosTratados = []

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

            } catch (error) {

                console.error(error)

                this.arquivo = null

                this.erro =
                    'Não foi possível ler a planilha.'

            } finally {

                this.carregando = false

            }
        },

        tratarDados() {

            this.dadosTratados =
                this.dadosOriginais.map((linha) => {

                    const novaLinha = {}

                    for (
                        const [chave, valor]
                        of Object.entries(linha)
                    ) {

                        if (typeof valor === 'string') {

                            novaLinha[chave] =
                                valor.trim()

                        } else {

                            novaLinha[chave] =
                                valor

                        }
                    }

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

        limpar() {

            this.arquivo = null
            this.dadosOriginais = []
            this.dadosTratados = []
            this.erro = ''
            this.carregando = false

        }

    }

})