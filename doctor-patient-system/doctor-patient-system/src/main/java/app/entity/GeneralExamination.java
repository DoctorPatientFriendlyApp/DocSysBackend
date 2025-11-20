package app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general_examination")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double height;        // cm
    private double weight;        // kg
    private String build;         // average / obese / thin
    private String nourishment;
    private String clubbing;
    private String cyanosis;
    private String edema;
    private String gait;
    private String icterus;
}
