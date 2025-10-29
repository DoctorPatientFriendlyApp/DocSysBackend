package app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportType; // e.g. "CBC", "MRI"
    private String fileUrl;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
