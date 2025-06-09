package com.std_data_mgmt.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.std_data_mgmt.app.entities.Transcript;

public interface TranscriptRepository extends JpaRepository<Transcript, String> {
    Optional<Transcript> findByStudentId(String studentId);

    @Query("select t from #{#entityName} t join fetch t.student where t.transcriptId = ?1")
    Optional<Transcript> findWithStudent(String transcriptId);
}
