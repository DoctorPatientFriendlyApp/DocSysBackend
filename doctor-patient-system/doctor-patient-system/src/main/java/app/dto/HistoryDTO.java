package app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDTO {
	private Long id;
    private String pastHistory;
    private String surgeryHistory;
    private String treatmentHistory;
    private String currentHistory;
    private String familyHistory;
    private PersonalHistoryDTO personalHistory;
}
