package app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;
    private Double amount;
    private Integer days;
    private String beforeCondition;
    private String afterCondition;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
