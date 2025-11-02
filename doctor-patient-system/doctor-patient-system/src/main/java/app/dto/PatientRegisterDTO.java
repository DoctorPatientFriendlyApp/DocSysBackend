package app.dto;

import java.time.LocalDate;
import java.util.List;

import app.entity.Gender;
import app.entity.History;
import app.entity.SocialClass;
import app.entity.ZodiacSign;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class PatientRegisterDTO {

	 private String email;
     private String password;
	
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
//    private String diagnosis;
//    private String doctorAdvice;
    private boolean active;

    // Optional nested info (if required)
    private History history;
    private List<Long> doctorIds;  // IDs only — avoid circular refs
}
