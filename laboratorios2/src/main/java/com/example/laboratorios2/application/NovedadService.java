package com.example.laboratorios2.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.example.laboratorios2.domain.model.Laboratorio;
import com.example.laboratorios2.domain.model.Novedad;
import com.example.laboratorios2.domain.model.User;


import com.example.laboratorios2.domain.repository.LaboratorioRepository;
import com.example.laboratorios2.domain.repository.NovedadRepository;
import com.example.laboratorios2.domain.repository.UserRepository;


import com.example.laboratorios2.domain.valueobject.NovedadEstado;
import com.example.laboratorios2.domain.valueobject.Ticket;
import com.example.laboratorios2.domain.valueobject.TipoNovedad;

@Service
@RequiredArgsConstructor
public class NovedadService {

    private final NovedadRepository incidentRepo;
    private final UserRepository userRepo;
    private final LaboratorioRepository labRepo;
    private final List<String> emailOutbox = new ArrayList<>();

    public Novedad registerIncident(int userId, String labCode, int machineNumber, String rawType, String description) {
        User student = userRepo.findById(userId).orElse(null);
        if (student == null) {
            System.out.println("ERROR: usuario no existe");
            return null;
        }
        if (!student.isStudent()) {
            System.out.println("ERROR: solo un estudiante puede registrar una novedad");
            return null;
        }

        Laboratorio lab = labRepo.findByCode(labCode).orElse(null);
        if (lab == null) {
            System.out.println("ERROR: laboratorio no existe");
            return null;
        }
        if (!lab.isActive()) {
            System.out.println("ERROR: el laboratorio " + labCode + " esta inactivo, no se aceptan novedades");
            return null;
        }
        if (!lab.hasMachine(machineNumber)) {
            System.out.println("ERROR: la maquina " + machineNumber + " no pertenece al lab " + labCode);
            return null;
        }

        TipoNovedad type;
        try {
            type = TipoNovedad.valueOf(rawType.toUpperCase());
        } catch (Exception e) {
            System.out.println("ERROR: tipo de incidente invalido. Use: [hardware, software, red, perifericos, otro]");
            return null;
        }

        if (description == null || description.trim().length() < 10) {
            System.out.println("ERROR: la descripcion debe tener al menos 10 caracteres");
            return null;
        }

        if (incidentRepo.hasOpenIncident(labCode, machineNumber)) {
            System.out.println("AVISO: ya existe una novedad abierta para esa maquina");
            return null;
        }

        User admin = userRepo.findAdminByArea(type.getTargetArea()).orElse(null);
        if (admin == null) {
            System.out.println("ERROR: no hay administrador para el area " + type.getTargetArea());
            return null;
        }

        int seq = incidentRepo.nextSequence();
        Ticket ticket = Ticket.generate(seq);

        Novedad incident = new Novedad(seq, ticket, student.getId(), student.getName(), labCode, machineNumber, type, description, admin.getId(), admin.getName());
        incidentRepo.save(incident);

        String mail = "Para: " + admin.getName() + " | Asunto: [" + incident.getPriority() + "] Nueva novedad " + ticket.getValue() + " en " + labCode;
        emailOutbox.add(mail);

        System.out.println(">> Novedad registrada: " + ticket.getValue());
        System.out.println("   Lab: " + labCode + " | Maquina: " + machineNumber + " | Tipo: " + type.getCode());
        System.out.println("   Prioridad: " + incident.getPriority() + " | Asignado a: " + admin.getName() + " (" + type.getTargetArea() + ")");

        return incident;
    }

    public void closeIncident(int incidentId, int adminId, String solution) {
        Novedad incident = incidentRepo.findById(incidentId).orElse(null);
        if (incident == null) {
            System.out.println("ERROR: novedad no existe");
            return;
        }
        try {
            incident.close(adminId, solution);
            System.out.println(">> Novedad " + incident.getTicket().getValue() + " cerrada por " + incident.getAdminName());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listAdminIncidents(int adminId) {
        System.out.println("--- Bandeja del administrador " + adminId + " ---");
        boolean hay = false;
        for (Novedad n : incidentRepo.findAll()) {
            if (n.getAdminId() == adminId) {
                hay = true;
                System.out.println("  " + n.getTicket().getValue() + " | " + n.getStatus() + " | " + n.getPriority() + " | " + n.getLabCode() + " maq " + n.getMachineNumber() + " | " + n.getType().getCode());
            }
        }
        if (!hay) {
            System.out.println("  (sin novedades)");
        }
    }

    public void generateReport() {
        System.out.println("========== REPORTE GENERAL ==========");
        List<Novedad> list = incidentRepo.findAll();
        long abiertas = list.stream().filter(i -> i.getStatus() == NovedadEstado.ABIERTA).count();
        System.out.println("Total: " + list.size() + " | Abiertas: " + abiertas + " | Cerradas: " + (list.size() - abiertas));
        System.out.println("=====================================");
    }

    public void printEmails() {
        System.out.println("########## CORREOS ENVIADOS ##########");
        for (String m : emailOutbox) {
            System.out.println(m);
            System.out.println("----------------------------------------");
        }
    }
}