package app.controller;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.dto.DoctorDTO;
import app.dto.DoctorRegisterDTO;
import app.dto.LoginDTO;
import app.service.CloudinaryService;
import app.service.DoctorServiceImpl;
import app.service.IDoctorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/doctors")
//@CrossOrigin(origins = "http://localhost:5173") // specific frontend
@AllArgsConstructor
public class DoctorController {

	private final IDoctorService doctorService;
	private final CloudinaryService cloudinaryService;

  // Register Doctor 
//  @PostMapping
//  @Operation(description = " Create Doctor ")
//  public ResponseEntity<DoctorDTO> createdoctor(@RequestBody DoctorRegisterDTO doctorDTO){
//	  return ResponseEntity.ok(doctorService.createDoctor(doctorDTO));
//  }
	
	// ✅ Create Doctor (with certificate file upload)
	  @PostMapping(consumes = "multipart/form-data")
	  @Operation(description = "Create a new Doctor with optional certificate file upload")
	    public ResponseEntity<DoctorDTO> createDoctor(
	            @ModelAttribute DoctorRegisterDTO doctorDTO,
	            @RequestPart(value = "certificate", required = false) MultipartFile certificate) throws IOException {

	        // ✅ Upload certificate to Cloudinary if provided
	        if (certificate != null && !certificate.isEmpty()) {
	            String uploadedUrl = cloudinaryService.uploadFile(certificate, "doctor_certificates");
	            doctorDTO.setCertificateUrl(uploadedUrl);
	        }

	        DoctorDTO createdDoctor = doctorService.createDoctor(doctorDTO);
	        return ResponseEntity.ok(createdDoctor);
	    }

	  
	  @PostMapping("/login")
	  public ResponseEntity<DoctorDTO> login(@RequestBody LoginDTO dto){
		  return ResponseEntity.ok(doctorService.login(dto));
	  }
	  
  
//  // Get all doctors 
  @GetMapping
  @Operation(description = " Get All Doctors ")
  public ResponseEntity<List<DoctorDTO>> getalldoctors(){
	  return ResponseEntity.ok(doctorService.getAllDoctors());
  }
  
  // Get only active doctors
  @GetMapping("/activedoctors")
  @Operation(description = " Get All Active Doctors ")
  public ResponseEntity<List<DoctorDTO>> getallactivedoctors(){
	  
	  return ResponseEntity.ok(doctorService.getAllActiveDoctors());
  }
  
  // Get doctor by id
  @GetMapping("/{id}")
  @Operation(description = "Get Doctor By Id")
  public ResponseEntity<DoctorDTO> getdoctorById(@PathVariable Long id){
	
	  return ResponseEntity.ok(doctorService.getDoctorById(id));
  }
  
  // Get doctor by id
  @GetMapping("/active/{id}")
  @Operation(description = " Get Doctor By Id who is Active ")
  public ResponseEntity<DoctorDTO> getactivedoctorById(@PathVariable Long id){
	
	  return ResponseEntity.ok(doctorService.getActiveDoctorById(id));
  }
  
  
  // ✅ Update doctor
  // @PutMapping("/{id}/edit")
  // @Operation(description = " Update Doctor ")
  // public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
  //     return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
  // }
	// Update also handles file uplode
	// -------------------------------------------------------------------------
    // UPDATE DOCTOR (WITH OPTIONAL CERTIFICATE UPLOAD)
    // -------------------------------------------------------------------------
@PutMapping(value = "/{id}/edit", consumes = "multipart/form-data")
public ResponseEntity<DoctorDTO> updateDoctor(
        @PathVariable Long id,
        @ModelAttribute DoctorDTO doctorDTO,
        @RequestPart(value = "certificate", required = false) MultipartFile certificate) throws IOException {

    if (certificate != null && !certificate.isEmpty()) {
        String url = cloudinaryService.uploadFile(certificate, "doctor_certificates");
        doctorDTO.setCertificateUrl(url);
    }

    return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
}



	
  // ✅ Soft delete (deactivate doctor)
  @DeleteMapping("/{id}")
  @Operation(description = " Deactivate Doctor")
  public ResponseEntity<Void> deactivateDoctor(@PathVariable Long id) {
      doctorService.deactivateDoctor(id);
      return ResponseEntity.noContent().build();
  }
  
  
  // ✅ Upload certificate
  @PostMapping(value = "/{id}/certificate", consumes = "multipart/form-data")
  @Operation(description = "Upload doctor certificate (PDF/Image)")
  public ResponseEntity<?> uploadCertificate(
          @PathVariable Long id,
          @RequestPart("file") MultipartFile file) {

      try {
          DoctorDTO updatedDoctor = doctorService.uploadDoctorCertificate(id, file);
          return ResponseEntity.ok(updatedDoctor);
      } catch (IOException e) {
          return ResponseEntity.internalServerError()
                  .body("Error uploading certificate: " + e.getMessage());
      }
  }
  
  @PutMapping("/{id}/verify")
  public ResponseEntity<DoctorDTO> verifyDoctor(@PathVariable Long id) {
      return ResponseEntity.ok(doctorService.verifyDoctor(id));
  }

  
  // ✅ Assign existing patient to this doctor
  @PostMapping("/{doctorId}/assignpatienttodoctor/{patientId}")
  public ResponseEntity<?> assignPatientToDoctor(@PathVariable Long doctorId, @PathVariable Long patientId) {
      boolean success = doctorService.assignPatientToDoctor(doctorId, patientId);
      if (success) {
          return ResponseEntity.ok(Map.of("message", "Patient assigned successfully!"));
      } else {
          return ResponseEntity.badRequest().body(Map.of("message", "Failed to assign patient."));
      }
  }
  
  
}
