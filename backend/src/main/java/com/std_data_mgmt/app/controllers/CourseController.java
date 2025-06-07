package com.std_data_mgmt.app.controllers;

import com.std_data_mgmt.app.dtos.CourseDto;
import com.std_data_mgmt.app.entities.Course;
import com.std_data_mgmt.app.services.CourseService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public FormattedResponseEntity<CourseDto> getCourseById(@PathVariable String id) {
        Optional<Course> course = this.courseService.getCourseById(id);
        if (course.isEmpty()) {
            return new FormattedResponseEntity<>(HttpStatus.NOT_FOUND, false, "Course not found", null);
        }

        CourseDto courseDto = course.get().toDto();
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "Successfully get course by Id", courseDto);
    }

    @GetMapping()
    public FormattedResponseEntity<List<CourseDto>> getCourses(
            @RequestParam Optional<String> courseId,
            @RequestParam Optional<String> code,
            @RequestParam Optional<Integer> credits,
            @RequestParam Optional<String> departmentId
    ) {
        List<Course> courses = this.courseService.getCourses(
                courseId,
                code,
                credits,
                departmentId
        );
        List<CourseDto> courseDtos = courses.stream().map(Course::toDto).toList();
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "Successfully get courses", courseDtos);
    }
}
