package app.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
//@Builder
public class PersonalHistory {
    private String diet;
    private String appetite;
    private String desiredFood;
    private String aversion;
    private String thirst;
    private String bowel;
    private Integer micturitionPerDay;
    private Integer micturitionPerNight;
    private String sleep;
    private String dreams;
    private String habits;
    private String perspiration;
    private Integer masturbationPerWeek;
    private Integer masturbationPerMonth;
    private String personalSexualHistory;
    private String extraMaritalHistory;
    private String menstrualHistory;
    private String obstetricHistory;
    private String gynecologicalHistory;
    private String milestoneHistory;
    private String socalHistory;
    private String immunizationHistory;
    private String criminalHistory;
    private String psychologicalTraumatizedHistory;
    private String accidentalHistory;
//    mind : current psy state with date
} 
