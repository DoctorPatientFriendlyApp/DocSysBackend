package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentDTO {
    private Long id;             // For updates
    private String medicineName;
    private Double amount;
    private Integer days;
    private String beforeCondition;
    private String afterCondition;
}
	