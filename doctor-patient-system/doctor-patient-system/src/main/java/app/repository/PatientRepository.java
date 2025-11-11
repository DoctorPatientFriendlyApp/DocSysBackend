package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import app.dto.PatientDTO;
import app.entity.Doctor;
import app.entity.Patient;
import app.entity.Report;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	
	Optional<Patient> findByUserEmailAndUserPassword(String email, String password);
	
    Optional<Patient> findByMobile(String mobile);
    boolean existsByAadhaar(String aadhaar);
    boolean existsByPan(String pan);
    
    // Custom query to get only active patients
    List<Patient> findByActiveTrue();

    // Optional: To find by ID but only if active
    Optional<Patient> findByIdAndActiveTrue(Long id);
    
    @Query("SELECT p FROM Patient p JOIN p.doctors d WHERE d.id = :doctorId")
    List<Patient> findByDoctorId(@Param("doctorId") Long doctorId);
    
    @Query("SELECT p FROM Patient p WHERE p.doctors IS EMPTY")
    List<Patient> findPatientsWithoutDoctor();

}
