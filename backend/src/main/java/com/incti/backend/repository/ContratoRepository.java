package com.incti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incti.backend.model.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
}