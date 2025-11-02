package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.entity.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByMobile(String mobile);
    boolean existsByAadhaar(String aadhaar);
    boolean existsByPan(String pan);
    
    // Custom query to get only active patients
    List<Patient> findByActiveTrue();

    // Optional: To find by ID but only if active
    Optional<Patient> findByIdAndActiveTrue(Long id);
}
