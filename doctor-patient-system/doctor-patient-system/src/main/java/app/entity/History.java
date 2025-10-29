package app.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
//@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String pastHistory;

    @Column(length = 1000)
    private String surgeryHistory;

    @Column(length = 1000)
    private String treatmentHistory;

    @Column(length = 1000)
    private String currentHistory;

    @Column(length = 1000)
    private String familyHistory;

    @Embedded
    private PersonalHistory personalHistory;

}
