package com.example.laboratorios2.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class Laboratorio {
    private final String code;
    private final String name;
    private final String building;
    private final int floor;
    private final List<Integer> machines;
    private final boolean active;

    public boolean hasMachine(int number) {
        return machines != null && machines.contains(number);
    }
}
