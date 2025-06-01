package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.dtos.DepartmentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "department")
@Getter
@Setter
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
