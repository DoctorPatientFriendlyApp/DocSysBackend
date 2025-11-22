package app.dto;

import lombok.*;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id; // for updating reports 
    private String reportType;
    private String description;
    private String fileUrl;
    private LocalDate reportDate;
    private String notes;

    @JsonIgnore     // ✔️ prevents JSON parsing error
    private MultipartFile file;
}
