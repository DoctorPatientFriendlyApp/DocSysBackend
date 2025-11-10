package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.dto.DoctorDTO;
import app.entity.Doctor;

import java.util.Optional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // ✅ Query by user.email using nested property path
    Optional<Doctor> findByUserEmail(String email);
    
    Optional<Doctor> findByUserEmailAndUserPassword(String email, String password);

    Optional<Doctor> findByMobile(String mobile);

    List<Doctor> findBySpecialization(String specialization);

    boolean existsByUserEmail(String email);
    boolean existsByMobile(String mobile);
    
    // find doctor who is active
    // ✅ Fetch only active doctors
    List<Doctor> findByActiveTrue();

	
}
