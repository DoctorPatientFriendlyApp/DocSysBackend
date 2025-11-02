package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	
}
