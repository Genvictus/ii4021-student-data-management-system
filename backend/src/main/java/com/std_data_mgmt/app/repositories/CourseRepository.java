package com.std_data_mgmt.app.repositories;

import com.std_data_mgmt.app.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}