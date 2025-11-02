package app.dto;

import java.util.ArrayList;
import java.util.List;

import app.entity.Gender;
import app.entity.Patient;
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
    
	    private Long id;
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
	   

	 // Relations (IDs only)
	    private List<Long> patientIds;   // references to patients (no entity here)
}
