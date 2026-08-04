package com.example.laboratorios2.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.laboratorios2.domain.model.Novedad;

public interface NovedadRepository {
    void save(Novedad novedad);
    Optional<Novedad> findById(int id);
    List<Novedad> findAll();
    boolean hasOpenIncident(String labCode, int machineNumber);
    int nextSequence();
}
