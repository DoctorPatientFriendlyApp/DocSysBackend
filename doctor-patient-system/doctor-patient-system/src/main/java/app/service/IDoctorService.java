package app.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import app.dto.DoctorDTO;
import app.dto.DoctorRegisterDTO;

public interface IDoctorService {

	  // ➕ Create a new doctor
    DoctorDTO createDoctor(DoctorRegisterDTO doctorDTO);

    // 🔍 Get all doctors
    List<DoctorDTO> getAllDoctors();

    // 🔍 Get all active doctors
    List<DoctorDTO> getAllActiveDoctors();

    // 🔍 Get doctor by ID
    DoctorDTO getDoctorById(Long id);

    // 🔍 Get active doctor by ID
    DoctorDTO getActiveDoctorById(Long id);

    // ✏️ Update existing doctor
    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO);

    // 🗑️ Soft delete (deactivate) doctor
    void deactivateDoctor(Long id);
    
    
 // 🔹 Upload doctor certificate file
    DoctorDTO uploadDoctorCertificate(Long doctorId, MultipartFile file) throws IOException;

   // verify doctor 
	public DoctorDTO verifyDoctor(Long id);
    
}
