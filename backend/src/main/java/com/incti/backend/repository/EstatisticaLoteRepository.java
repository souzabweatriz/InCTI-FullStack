package com.incti.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incti.backend.model.EstatisticaLote;

public interface EstatisticaLoteRepository extends JpaRepository<EstatisticaLote, Long> {
    Optional<EstatisticaLote> findFirstByLoteIdOrderByCriadoEmDesc(Long loteId);
}
