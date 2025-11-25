package app.dto;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.List;

import app.entity.Gender;
import app.entity.Patient;
import app.entity.Role;
import app.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    
	  @Schema(description = "Database ID (used only for responses)")
	    private Long id;
	  @Schema(description = "Email ")
	    private String email;
	  @Schema(description = "Name ")
	    private String name;
	    private Integer age;
	    private Gender sex;
	    private String address;
	    private String mobile;
	    private String aadhaar;
	    private String pan;
	    private String specialization;
	    private String certificateUrl; // uploaded file
	    private boolean verified = false; 
	    private boolean active = true;
	    private Role role=Role.DOCTOR;

	 // Relations (IDs only)
	    @Schema(description = "List of Patients IDs")
	    // private List<Long> patientIds;   // references to patients (no entity here)
	    private List<Long> patientIds = new ArrayList<>();

}
