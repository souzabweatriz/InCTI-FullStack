package com.incti.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "estatistica_lote")
public class EstatisticaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loteId;
    @Column(columnDefinition = "jsonb")
    private String resultado;
    private LocalDateTime criadoEm;

    public EstatisticaLote() {
    }

    public Long getLoteId() {
        return loteId;
    }

    public String getResultado() {
        return resultado;
    }
}
