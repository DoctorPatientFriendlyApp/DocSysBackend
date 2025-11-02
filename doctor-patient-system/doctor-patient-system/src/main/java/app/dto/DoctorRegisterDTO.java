package app.dto;

import java.util.List;

import app.entity.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegisterDTO {
	
    private String email;
    private String password;
    private String name;
    private Integer age;
    private Gender sex;
    private String address;
    private String mobile;
    private String aadhaar;
    private String pan;
    private String specialization;
    private String certificateUrl;
    // 
    private List<Long> patientIds;
}
