package app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor 
public class DiagnosisDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 B. Differential Diagnosis
    private String differentialDiagnosis;
    
    private String finalDiagnosis;

    // 🔹 C. Provisional Diagnosis
    private String provisionalDiagnosis;

    // 🔹 D. Investigation Advice
    private String investigationAdvice;

    // 🔹 Hahnemannian Disease Classification (multi-select)
    // example: ["Acute", "Chronic", "Surgical"]
    private String hahnemannianDiseaseClassification; // CSV or JSON string

    // 🔹 G. Miasma (multi-select)
    // example: ["Psora", "Sycosis", "Tuberculin"]
    private String miasma;

    // 🔹 H. Repertory Used
    private String repertoryUsed;

    // 🔹 I. Why this medicine is chosen (2 lines)
    @Column(length = 500)
    private String reasonForChoosingMedicine;
}
