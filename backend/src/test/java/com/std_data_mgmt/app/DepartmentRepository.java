package com.std_data_mgmt.app;

import com.std_data_mgmt.app.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {
}


