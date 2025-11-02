package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalHistoryDTO {
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
}

