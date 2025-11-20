package app.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisDetailsDTO {

    private String differentialDiagnosis;
    private String provisionalDiagnosis;
    private String investigationAdvice;
    private String finalDiagnosis;

    // MULTI SELECT
    private String hahnemannianDiseaseClassification;
    private String miasma;

    private String repertoryUsed;
    private String reasonForChoosingMedicine; // 2 lines text
}
