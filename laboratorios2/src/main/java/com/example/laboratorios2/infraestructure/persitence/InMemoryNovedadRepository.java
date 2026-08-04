package com.example.laboratorios2.infraestructure.persitence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

import org.springframework.stereotype.Repository;

import com.example.laboratorios2.domain.model.Novedad;
import com.example.laboratorios2.domain.repository.NovedadRepository;
import com.example.laboratorios2.domain.valueobject.NovedadEstado;

@Repository
public class InMemoryNovedadRepository implements NovedadRepository {

    private final List<Novedad> incidents = new ArrayList<>();
    private int sequence = 0;

    @Override
    public void save(Novedad incident) {
        incidents.add(incident);
    }

    @Override
    public Optional<Novedad> findById(int id) {
        return incidents.stream()
                .filter(i -> i.getId() == id)
                .findFirst();
    }

    @Override
    public List<Novedad> findAll() {
        return incidents;
    }

    @Override
    public boolean hasOpenIncident(String labCode, int machineNumber) {
        return incidents.stream().anyMatch(i ->
                i.getLabCode().equalsIgnoreCase(labCode) &&
                i.getMachineNumber() == machineNumber &&
                i.getStatus() == NovedadEstado.ABIERTA
        );
    }

    @Override
    public int nextSequence() {
        return ++sequence;
    }
}