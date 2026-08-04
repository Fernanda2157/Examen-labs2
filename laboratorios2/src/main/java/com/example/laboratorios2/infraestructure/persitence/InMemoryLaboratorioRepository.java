package com.example.laboratorios2.infraestructure.persitence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.example.laboratorios2.domain.model.Laboratorio;
import com.example.laboratorios2.domain.repository.LaboratorioRepository;

@Repository
public class InMemoryLaboratorioRepository implements LaboratorioRepository {

    private final List<Laboratorio> labs = new ArrayList<>();

    public InMemoryLaboratorioRepository() {
        List<Integer> m1 = IntStream.rangeClosed(1, 20).boxed().collect(Collectors.toList());
        List<Integer> m2 = IntStream.rangeClosed(1, 30).boxed().collect(Collectors.toList());
        List<Integer> m3 = IntStream.rangeClosed(1, 15).boxed().collect(Collectors.toList());

        labs.add(new Laboratorio("LAB-FING-01", "Lab Ingenieria 1", "FING", 2, m1, true));
        labs.add(new Laboratorio("LAB-FING-02", "Lab Ingenieria 2", "FING", 3, m2, true));
        labs.add(new Laboratorio("LAB-ADMIN-01", "Lab Administracion", "ADM", 1, m3, false));
    }

    @Override
    public Optional<Laboratorio> findByCode(String code) {
        return labs.stream()
                   .filter(l -> l.getCode().equalsIgnoreCase(code))
                   .findFirst();
    }
}