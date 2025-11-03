package app.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportType; // e.g. "CBC", "MRI"
    @Column(length=500)
    private String fileUrl;
    private String notes;
    private String description;
    private LocalDate reportDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference // Add this to prevent infinite recursion
    private Patient patient;
    
   

}
