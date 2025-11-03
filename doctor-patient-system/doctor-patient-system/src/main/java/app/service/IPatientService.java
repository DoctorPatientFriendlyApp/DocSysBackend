package app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import app.dto.PatientDTO;
import app.dto.PatientRegisterDTO;
import app.entity.Report;

public interface IPatientService {

    // 🔹 Register a new patient
    PatientDTO createPatient(PatientRegisterDTO dto);

    // 🔹 Update existing patient details
    PatientDTO updatePatient(Long id, PatientDTO patientDTO);

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
}
