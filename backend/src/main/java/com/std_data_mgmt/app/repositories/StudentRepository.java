package com.std_data_mgmt.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.std_data_mgmt.app.entities.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("select s from #{#entityName} s join fetch s.transcript where s.departmentId = ?1")
    List<Student> findWithTranscript(Optional<String> departmentId);
}
