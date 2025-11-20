package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalSignsDTO {
    private double temperature;
    private int respirationRate;
    private int pulseRate;
    private String bloodPressure;
}
