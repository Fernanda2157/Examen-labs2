package com.example.laboratorios2.domain.valueobject;
import lombok.Getter;
@Getter
public enum TipoNovedad {
    HARDWARE("hardware"),
    SOFTWARE("software"),
    RED("red"),
    PERIFERICOS("perifericos"),
    OTRO("otro");

    private final String code;

    TipoNovedad(String code) {
        this.code = code;
    }

    public Prioridad calculatePriority() {
        if (this == RED || this == HARDWARE) {
            return Prioridad.ALTA;
        }
        if (this == SOFTWARE) {
            return Prioridad.MEDIA;
        }
        return Prioridad.BAJA;
    }

    public String getTargetArea() {
        if (this == HARDWARE || this == PERIFERICOS) {
            return "hardware";
        }
        if (this == RED) {
            return "redes";
        }
        return "software";
    }
}

