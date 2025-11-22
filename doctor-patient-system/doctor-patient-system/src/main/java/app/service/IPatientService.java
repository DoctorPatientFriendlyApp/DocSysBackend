package app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import app.dto.DoctorDTO;
import app.dto.LoginDTO;
import app.dto.PatientDTO;
import app.dto.PatientRegisterDTO;
import app.entity.Doctor;
import app.entity.Patient;
import app.entity.Report;

public interface IPatientService {

    // 🔹 Register a new patient
    PatientDTO createPatient(PatientRegisterDTO dto);
    
    // Login
    PatientDTO login(LoginDTO dto);

    //---------------------------------------------------------
    // 🔹 Update existing patient details
    public PatientDTO updatePatient(Long id, PatientDTO dto, List<MultipartFile> files);

    
    //----------------------------------------------------------

    // 🔹 Upload report to Cloudinary & link to patient
    public Report uploadPatientReport(Long patientId,
            MultipartFile file,
            String reportType,
            String notes,
            String description) throws IOException;

    // 🔹 Get all patients (active + inactive)
    List<PatientDTO> getAllPatients();

    // 🔹 Get only active patients
    List<PatientDTO> getAllActivePatients();

    // 🔹 Get one active patient by ID
    PatientDTO getActivePatientById(Long id);

    // 🔹 Get patient (any status) by ID
    PatientDTO getPatientById(Long id);

    // 🔹 Soft delete (deactivate)
    void deactivatePatient(Long id);

    // 🔹 Assign doctor to patient
    PatientDTO assignDoctor(Long patientId, Long doctorId);

	List<PatientDTO> findPatientByDoctorId(Long doctorId);

	ResponseEntity<List<Patient>> getUnassignedPatients();

	List<DoctorDTO> getDoctorsByPatient(Long patientId);

	
}
