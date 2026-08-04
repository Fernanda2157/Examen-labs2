package com.example.laboratorios2.domain.model;

import java.time.LocalDateTime;

import com.example.laboratorios2.domain.valueobject.NovedadEstado;
import com.example.laboratorios2.domain.valueobject.Prioridad;
import com.example.laboratorios2.domain.valueobject.Ticket;
import com.example.laboratorios2.domain.valueobject.TipoNovedad;

import lombok.Getter;

@Getter
public class Novedad {
    private final int id;
    private final Ticket ticket;
    private final int studentId;
    private final String studentName;
    private final String labCode;
    private final int machineNumber;
    private final TipoNovedad type;
    private final String description;
    private final Prioridad priority;
    private final int adminId;
    private final String adminName;
    private NovedadEstado status;
    private String solution;
    private LocalDateTime closedAt;


    public Novedad(int id, Ticket ticket, int studentId, String studentName, String labCode,
                   int machineNumber, TipoNovedad type, String description, int adminId, String adminName) {
        this.id = id;
        this.ticket = ticket;
        this.studentId = studentId;
        this.studentName = studentName;
        this.labCode = labCode;
        this.machineNumber = machineNumber;
        this.type = type;
        this.description = description;
        this.priority = type.calculatePriority();
        this.adminId = adminId;
        this.adminName = adminName;
        this.status = NovedadEstado.ABIERTA;
        this.solution = null;
        this.closedAt = null;
    }

    public void close(int adminId, String solutionText) {
        if (this.status == NovedadEstado.CERRADA) {
            throw new IllegalArgumentException("ERROR: la novedad ya estaba cerrada");
        }
        if (this.adminId != adminId) {
            throw new IllegalArgumentException("ERROR: solo el administrador asignado puede cerrar esta novedad");
        }
        if (solutionText == null || solutionText.trim().length() < 5) {
            throw new IllegalArgumentException("ERROR: debe escribir la solucion aplicada");
        }
        this.status = NovedadEstado.CERRADA;
        this.solution = solutionText.trim();
        this.closedAt = LocalDateTime.now();
    }
}