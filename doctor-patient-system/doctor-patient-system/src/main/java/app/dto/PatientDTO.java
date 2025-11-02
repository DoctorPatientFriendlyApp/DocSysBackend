package app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

import app.entity.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {

	//enum 
    private Gender sex;
    private SocialClass socialEconomicalStatus;
    private ZodiacSign zodiacSign;
	
    private Long id; // for response purpose we are adding id here. we are not using this id for registering patient
    private String name;
    private Integer age;
    private String address;
    private String mobile;
    private String aadhaar;
    private String pan;
    private LocalDate dob;
    private String bloodGroup;
    private String diagnosis;
    private String doctorAdvice;
    private boolean active;

    // 🧠 History (nested DTOs)
    private HistoryDTO history;

    // 👩‍⚕️ Doctor relationships (only IDs)
    private List<Long> doctorIds;

//    // 💊 Treatments
    private List<TreatmentDTO> treatments;
//
//    // 🧪 Reports
    private List<ReportDTO> reports;
}
