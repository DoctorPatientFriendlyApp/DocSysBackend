	package app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.List;

import app.entity.Gender;
import app.entity.Role;
import app.entity.SocialClass;
import app.entity.User;
import app.entity.ZodiacSign;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {

    @Schema(description = "Database ID (used only for responses)")
    private Long id;

    @Schema(description = "Full name of the patient")
    private String name;
    
    @Schema(description = "Email")
    private String email;
    
    @Schema(description = "Age of the patient")
    private Integer age;

    @Schema(description = "Patient's residential address")
    private String address;

    @Schema(description = "Patient mobile number")
    private String mobile;

    @Schema(description = "Aadhaar number of the patient")
    private String aadhaar;

    @Schema(description = "PAN number of the patient")
    private String pan;

    @Schema(description = "Date of Birth")
    private LocalDate dob;

    @Schema(description = "Blood Group (A+, A−, B+, O−, etc)")
    private String bloodGroup;
    
    @Schema(description = "Doctor's advice or recommendation")
    private String doctorAdvice;

    @Schema(description = "Whether patient is active or not")
    private boolean active;

    @Schema(description = " Role ")
    private Role role=Role.PATIENT;

    // ---------------------------
    // ENUMS
    // ---------------------------

    @Schema(description = "Gender of patient")
    private Gender sex;

    @Schema(description = "Social economical class")
    private SocialClass socialEconomicalStatus;

    @Schema(description = "Zodiac sign of the patient")
    private ZodiacSign zodiacSign;


    // ---------------------------
    // RELATIONSHIP IDs
    // ---------------------------

    @Schema(description = "List of Doctor IDs associated with patient")
    private List<Long> doctorIds;

    @Schema(description = "User ID for login mapping (optional)")
    private Long userId;


    // ---------------------------
    // NESTED DTOs
    // ---------------------------
    private GeneralExaminationDTO generalExamination;
    private PatientDescriptionDTO patientDescription;
    private VitalSignsDTO vitalSigns;
    private HistoryDTO history;
    private SystemicExaminationDTO systemicExamination;
    private DiagnosisDetailsDTO diagnosisDetails;
    private PrescriptionDTO prescription;


    // ---------------------------
    // REPORTS AND TREATMENTS
    // ---------------------------

    @Schema(description = "List of lab reports for the patient")
    private List<ReportDTO> reports;

    @Schema(description = "List of treatments taken by the patient")
    private List<TreatmentDTO> treatments;

}
