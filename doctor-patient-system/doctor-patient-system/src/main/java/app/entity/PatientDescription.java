package app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_description")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String head;
    private String hair;
    private String eyes;
    private String vision;
    private String ears;
    private String hearing;
    private String nose;
    private String smell;
    private String tongue;
    private String taste;
    private String skin;
    private String sensation;
    private String nails;
    private String breast;
}
