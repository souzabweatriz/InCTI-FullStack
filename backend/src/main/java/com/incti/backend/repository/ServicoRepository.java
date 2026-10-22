package com.incti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incti.backend.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}