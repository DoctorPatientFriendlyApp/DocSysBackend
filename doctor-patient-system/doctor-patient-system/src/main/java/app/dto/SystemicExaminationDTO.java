package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemicExaminationDTO {

	 // 🔹 Respiratory System
    private String respiratoryInspection;
    private String respiratoryPalpation;
    private String respiratoryPercussion;
    private String respiratoryAuscultation;

    // 🔹 Per Abdomen
    private String abdomenInspection;
    private String abdomenPalpation;
    private String abdomenPercussion;
    private String abdomenAuscultation;

    // 🔹 CVS
    private String cvsInspection;
    private String cvsPalpation;
    private String cvsPercussion;
    private String cvsAuscultation;

    // 🔹 CNS
    private String cranialReflexes;
    private String motorReflexes;
    private String peripheralReflexes;
}
