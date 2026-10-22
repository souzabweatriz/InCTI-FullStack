package com.incti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incti.backend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}