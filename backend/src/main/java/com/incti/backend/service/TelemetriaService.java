package com.incti.backend.service;

import com.incti.backend.model.Telemetria;
import com.incti.backend.repository.TelemetriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelemetriaService {

    private final TelemetriaRepository telemetriaRepository;

    public TelemetriaService(TelemetriaRepository telemetriaRepository) {
        this.telemetriaRepository = telemetriaRepository;
    }

    public List<Telemetria> listarTodos() {
        return telemetriaRepository.findAll();
    }

    public Optional<Telemetria> buscarPorId(Long id) {
        return telemetriaRepository.findById(id);
    }

    public Telemetria criar(Telemetria telemetria) {
        return telemetriaRepository.save(telemetria);
    }

    public Optional<Telemetria> atualizar(Long id, Telemetria telemetriaAtualizada) {
        return telemetriaRepository.findById(id)
                .map(telemetria -> {
                    telemetria.setDispositivo(telemetriaAtualizada.getDispositivo());
                    telemetria.setValor(telemetriaAtualizada.getValor());
                    telemetria.setDataHora(telemetriaAtualizada.getDataHora());

                    return telemetriaRepository.save(telemetria);
                });
    }

    public boolean excluir(Long id) {
        if (!telemetriaRepository.existsById(id)) {
            return false;
        }

        telemetriaRepository.deleteById(id);
        return true;
    }
}