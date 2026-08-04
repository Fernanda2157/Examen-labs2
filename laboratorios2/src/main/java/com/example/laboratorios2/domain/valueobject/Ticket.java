package com.example.laboratorios2.domain.valueobject;
import lombok.Value;
import java.time.Year;
@Value
public class Ticket {
    String value;

    public static Ticket generate(int sequence) {
        return new Ticket(String.format("NOV-%d-%04d", Year.now().getValue(), sequence));
    }
}
