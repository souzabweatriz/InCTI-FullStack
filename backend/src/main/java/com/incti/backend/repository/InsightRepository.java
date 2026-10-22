package com.incti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incti.backend.model.Insight;

public interface InsightRepository extends JpaRepository<Insight, Long> {
}