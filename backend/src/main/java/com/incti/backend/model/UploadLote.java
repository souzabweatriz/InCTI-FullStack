package com.incti.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "upload_lote")
public class UploadLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private Integer totalLinhas;
    private Integer linhasValidas;
    private Integer linhasInvalidas;
    private String mensagem;
    private LocalDateTime criadoEm;

    public UploadLote() {
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalLinhas() {
        return totalLinhas;
    }

    public void setTotalLinhas(Integer totalLinhas) {
        this.totalLinhas = totalLinhas;
    }

    public Integer getLinhasValidas() {
        return linhasValidas;
    }

    public void setLinhasValidas(Integer linhasValidas) {
        this.linhasValidas = linhasValidas;
    }

    public Integer getLinhasInvalidas() {
        return linhasInvalidas;
    }

    public void setLinhasInvalidas(Integer linhasInvalidas) {
        this.linhasInvalidas = linhasInvalidas;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
