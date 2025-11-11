package app.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import app.dto.LoginDTO;
import app.dto.PatientDTO;
import app.dto.PatientRegisterDTO;
import app.entity.Gender;
import app.entity.Patient;
import app.entity.Report;
import app.entity.SocialClass;
import app.entity.ZodiacSign;
import app.repository.PatientRepository;
import app.repository.ReportRepository;
import app.service.CloudinaryService;
import app.service.IPatientService;
import app.service.PatientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

    private final IPatientService patientService;//✅ inject interface


    // ✅ Register new patient
    @PostMapping               
    @Operation(description = "Create Patient ")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientRegisterDTO dto) {
        return ResponseEntity.ok(patientService.createPatient(dto));
    }
    
    @PostMapping("/login")
    public ResponseEntity<PatientDTO> login(@RequestBody LoginDTO dto) {
        PatientDTO patient = patientService.login(dto);
        return ResponseEntity.ok(patient);
    }

    // ✅ Update Patient : diagnosis or advice
    @PutMapping("/{id}")
    @Operation(description = " Update Patient ")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO dto) {
        return ResponseEntity.ok(patientService.updatePatient(id, dto));
    }

    // ✅ Get all
    @GetMapping
    @Operation(description = "Get all Patients ")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    
    // ✅ Get all Active Patients
    @GetMapping("/activepatients")
    @Operation(description = "Get all Active Patients ")
    public ResponseEntity<List<PatientDTO>> getAllActivePatients() {
        return ResponseEntity.ok(patientService.getAllActivePatients());
    }
    
    
   
    // ✅ Get one by ID
    @GetMapping("/{id}")
    @Operation(description = " Get Patient By Id")
    public ResponseEntity<PatientDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
    
    // ✅ Get Active Patient by ID
    @GetMapping("/activepatient/{id}")
    @Operation(description = " Get Active Patient by ID")
    public ResponseEntity<PatientDTO> getActivePatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getActivePatientById(id));
    }

    // ✅ Soft delete (mark inactive)
    @DeleteMapping("/{id}")
    @Operation(description = " Delete Patient ")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
    	patientService.deactivatePatient(id);
        return ResponseEntity.ok("Patient marked inactive");
    }
    
    
    //-------------------------------------------------------------------------------
    // ✅ Upload multiple reports for a patient
    @PostMapping(value = "/{id}/reports", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadReport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file, // single file now
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String description) {

        try {
            Report report = patientService.uploadPatientReport(id, file, reportType, notes, description);
            return ResponseEntity.ok(report);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error uploading report: " + e.getMessage());
        }
    }


    //---------------------------------------------------------------------------------
    
    @GetMapping("/doctor/{doctorId}")
    public List<Patient> getPatientsByDoctor(@PathVariable Long doctorId) {
        return patientService.findPatientByDoctorId(doctorId);
    }

  
    @GetMapping("/unassigned")
    public ResponseEntity<List<Patient>> getUnassignedPatients() {
       return patientService.getUnassignedPatients();
    }

}
