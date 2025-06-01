package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.dtos.CourseDto;
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
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Course {
    @Id
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "code")
    private String code;

    @Column(name = "credits")
    private int credits;

    @Column(name = "department_id")
    private String departmentId;

    public CourseDto toDto() {
        return new CourseDto(
                this.courseId,
                this.code,
                this.credits,
                this.departmentId
        );
    }
}
