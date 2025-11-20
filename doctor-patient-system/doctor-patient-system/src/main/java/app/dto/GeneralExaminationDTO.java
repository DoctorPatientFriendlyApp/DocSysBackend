package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class GeneralExaminationDTO {
    private double height;
    private double weight;
    private String build;
    private String nourishment;
    private String clubbing;
    private String cyanosis;
    private String edema;
    private String gait;
    private String icterus;
}
