package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.entity.History;

public interface HistoryRepository extends JpaRepository<History, Long> {}
