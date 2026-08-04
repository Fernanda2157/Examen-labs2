package com.example.laboratorios2.domain.repository;

import java.util.Optional;

import com.example.laboratorios2.domain.model.Laboratorio;

public interface LaboratorioRepository {
    Optional<Laboratorio> findByCode(String code);
}
