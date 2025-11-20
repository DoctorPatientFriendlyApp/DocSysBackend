package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDTO {

    private String remedyWithPotency;
    private String repetition;
    private String followUp;
    private String advice;
}
