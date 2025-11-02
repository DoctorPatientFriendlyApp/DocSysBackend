package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.entity.Treatment;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {}
