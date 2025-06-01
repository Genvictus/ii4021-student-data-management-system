package com.std_data_mgmt.app.services;

import com.std_data_mgmt.app.entities.Course;
import com.std_data_mgmt.app.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;


    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Optional<Course> getCourseById(String courseId) {
        return this.courseRepository.findById(courseId);
    }

    public List<Course> getCourses() {
        return this.courseRepository.findAll();
    }
}
