package com.example.laboratorios2.presentation;

import org.springframework.boot.CommandLineRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.laboratorios2.application.NovedadService;
@Component
@RequiredArgsConstructor

public class SimuladorNovedades   implements CommandLineRunner{
   
    private final NovedadService app;

    @Override
    public void run(String... args) {
        System.out.println("### DEMO SISTEMA DE NOVEDADES - UNIVERSIDAD CENTRAL (Arquitectura por Capas) ###\n");

        app.registerIncident(1, "LAB-FING-01", 5, "hardware", "El computador no enciende, no da video");
        System.out.println();

        app.registerIncident(2, "LAB-FING-02", 12, "software", "No abre el Visual Studio Code, marca error");
        System.out.println();

        app.registerIncident(2, "LAB-FING-02", 30, "red", "No hay internet en esta fila de maquinas");
        System.out.println();

        app.registerIncident(1, "LAB-FING-01", 99, "hardware", "Pantalla azul constante");
        System.out.println();

        app.registerIncident(1, "LAB-ADMIN-01", 3, "software", "El office no tiene licencia activa");
        System.out.println();

        app.registerIncident(2, "LAB-FING-01", 5, "hardware", "Sigue sin encender el equipo numero cinco");
        System.out.println();

        app.closeIncident(1, 3, "Se cambio la fuente de poder del equipo");
        System.out.println();

        app.closeIncident(2, 3, "intento indebido");
        System.out.println();

        app.listAdminIncidents(3);
        app.listAdminIncidents(4);
        System.out.println();

        app.generateReport();
        System.out.println();

        app.printEmails();
    }
}

   

