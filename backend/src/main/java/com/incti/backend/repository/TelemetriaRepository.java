package com.incti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incti.backend.model.Telemetria;

public interface TelemetriaRepository extends JpaRepository<Telemetria, Long> {
}