package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDTO {

    private String pastHistory;
    private String surgeryHistory;
    private String treatmentHistory;
    private String chiefComplaint;
    private String familyHistory;
    private PersonalHistoryDTO personalHistory;
}
