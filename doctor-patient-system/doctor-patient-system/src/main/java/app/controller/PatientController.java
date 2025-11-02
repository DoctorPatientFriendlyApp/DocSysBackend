package app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.dto.PatientDTO;
import app.dto.PatientRegisterDTO;
import app.entity.Gender;
import app.entity.SocialClass;
import app.entity.ZodiacSign;
import app.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    // ✅ Register new patient
    @PostMapping              
    @Operation(description = "Create Patient ")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientRegisterDTO dto) {
        return ResponseEntity.ok(patientService.createPatient(dto));
    }

    // ✅ Update diagnosis or advice
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
}
