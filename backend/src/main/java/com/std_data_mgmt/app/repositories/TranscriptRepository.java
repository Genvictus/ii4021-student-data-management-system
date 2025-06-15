package com.std_data_mgmt.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.std_data_mgmt.app.entities.Transcript;

public interface TranscriptRepository extends JpaRepository<Transcript, String> {
    @Query("select t from #{#entityName} t join fetch t.student s where t.studentId = ?1")
    Optional<Transcript> findByStudentId(String studentId);

    List<Transcript> findByStudentIdIn(List<String> studentIds);

    @Query("select t from #{#entityName} t join fetch t.student s where t.hodId = ?1")
    List<Transcript> findByHodId(String hodId);

    @Query("select t from #{#entityName} t join fetch t.student s where t.transcriptId = ?1")
    Optional<Transcript> findWithStudent(String transcriptId);

    @Query("select t from #{#entityName} t join fetch t.student s where s.supervisorId = ?1")
    List<Transcript> findBySupervisorId(String supervisorId);
}
