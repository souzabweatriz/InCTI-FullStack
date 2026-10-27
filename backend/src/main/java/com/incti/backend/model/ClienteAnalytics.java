package com.incti.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class ClienteAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loteId;
    private String codigoCliente;
    private String nomeCliente;
    private String segmento;
    private String nivelCliente;
    private BigDecimal faturamentoAnual;
    private String cidade;
    private String uf;
    private Long consultorId;

    public ClienteAnalytics() {
    }
}
