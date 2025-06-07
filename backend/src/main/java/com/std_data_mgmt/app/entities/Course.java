package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.dtos.CourseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "course")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Course {
    @Id
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "code")
    private String code;

    @Column(name = "credits")
    private Integer credits;

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
