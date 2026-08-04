package com.example.laboratorios2.domain.model;
import lombok.Getter;
import lombok.AllArgsConstructor;
@AllArgsConstructor
@Getter
public class User {
   private final int id;
    private final String name;
    private final String role; // "estudiante" o "admin"
    private final String idCard;
    private final String areaOrProgram;

    public boolean isStudent() {
        return "estudiante".equalsIgnoreCase(role);
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
} 

