package app.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id;           // for updates
    private String reportType;
    private String description;
    private String fileUrl;
    private LocalDate reportDate;
    private String notes;
}
