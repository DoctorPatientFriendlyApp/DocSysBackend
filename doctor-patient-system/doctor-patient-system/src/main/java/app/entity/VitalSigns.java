package app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vital_signs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VitalSigns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double temperature;       // °C
    private int respirationRate;      // breaths/min
    private int pulseRate;            // bpm
    private String bloodPressure;     // e.g. 120/80
}
