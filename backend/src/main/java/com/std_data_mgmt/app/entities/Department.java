package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.dtos.DepartmentDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "department")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department {
    @Id
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    public DepartmentDto toDto() {
        return new DepartmentDto(departmentId, name, code);
    }
}
