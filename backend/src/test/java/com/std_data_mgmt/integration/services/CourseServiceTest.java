package com.std_data_mgmt.integration.services;

import com.std_data_mgmt.app.entities.Course;
import com.std_data_mgmt.app.repositories.CourseRepository;
import com.std_data_mgmt.app.services.CourseService;
import com.std_data_mgmt.integration.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CourseService Integration Tests")
public class CourseServiceTest extends BaseIntegrationTest {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    private Course course1;
    private Course course2;
    private Course course3;

    private String course1Id;
    private String course2Id;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();

        course1 = new Course();
        course1Id = UUID.randomUUID().toString();
        course1.setCourseId(course1Id);
        course1.setCode("IF101");
        course1.setCredits(3);
        course1.setDepartmentId("135");

        course2 = new Course();
        course2Id = UUID.randomUUID().toString();
        course2.setCourseId(course2Id);
        course2.setCode("II201");
        course2.setCredits(4);
        course2.setDepartmentId("182");

        course3 = new Course();
        course3.setCourseId(UUID.randomUUID().toString());
        course3.setCode("IF202");
        course3.setCredits(3);
        course3.setDepartmentId("135");

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
    }

    @Test
    @DisplayName("Should retrieve a course by its ID")
    void getCourseById_ExistingId_ReturnsCourse() {
        Optional<Course> foundCourse = courseService.getCourseById(course1Id);

        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getCourseId()).isEqualTo(course1Id);
        assertThat(foundCourse.get().getCode()).isEqualTo("IF101");
        assertThat(foundCourse.get().getCredits()).isEqualTo(3);
        assertThat(foundCourse.get().getDepartmentId()).isEqualTo("135");
    }

    @Test
    @DisplayName("Should return empty optional for a non-existing course ID")
    void getCourseById_NonExistingId_ReturnsEmptyOptional() {
        Optional<Course> foundCourse = courseService.getCourseById("NONEXISTENT");

        assertThat(foundCourse).isNotPresent();
    }

    @Test
    @DisplayName("Should retrieve all courses when no parameters are provided")
    void getCourses_NoParams_ReturnsAllCourses() {
        List<Course> courses = courseService.getCourses(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertThat(courses).hasSize(3);
        assertThat(courses).containsExactlyInAnyOrder(course1, course2, course3);
    }

    @Test
    @DisplayName("Should retrieve courses filtered by course ID")
    void getCourses_ByCourseId_ReturnsMatchingCourse() {
        List<Course> courses = courseService.getCourses(
                Optional.of(course2Id),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertThat(courses).hasSize(1);
        assertThat(courses.getFirst()).isEqualTo(course2);
    }

    @Test
    @DisplayName("Should retrieve courses filtered by code")
    void getCourses_ByCode_ReturnsMatchingCourse() {
        List<Course> courses = courseService.getCourses(
                Optional.empty(),
                Optional.of("IF202"),
                Optional.empty(),
                Optional.empty()
        );

        assertThat(courses).hasSize(1);
        assertThat(courses.getFirst()).isEqualTo(course3);
    }

    @Test
    @DisplayName("Should retrieve courses filtered by credits")
    void getCourses_ByCredits_ReturnsMatchingCourses() {
        List<Course> courses = courseService.getCourses(
                Optional.empty(),
                Optional.empty(),
                Optional.of(3),
                Optional.empty()
        );

        assertThat(courses).hasSize(2);
        assertThat(courses).containsExactlyInAnyOrder(course1, course3);
    }

    @Test
    @DisplayName("Should retrieve courses filtered by department ID")
    void getCourses_ByDepartmentId_ReturnsMatchingCourses() {
        List<Course> courses = courseService.getCourses(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of("135")
        );

        assertThat(courses).hasSize(2);
        assertThat(courses).containsExactlyInAnyOrder(course1, course3);
    }

    @Test
    @DisplayName("Should retrieve courses filtered by multiple parameters")
    void getCourses_ByMultipleParams_ReturnsMatchingCourse() {
        List<Course> courses = courseService.getCourses(
                Optional.empty(),
                Optional.of("IF101"),
                Optional.of(3),
                Optional.of("135")
        );

        assertThat(courses).hasSize(1);
        assertThat(courses.getFirst()).isEqualTo(course1);
    }

    @Test
    @DisplayName("Should return empty list if no course matches the criteria")
    void getCourses_NoMatchingCriteria_ReturnsEmptyList() {
        List<Course> courses = courseService.getCourses(
                Optional.empty(),
                Optional.of("NONEXISTENT"),
                Optional.empty(),
                Optional.empty()
        );

        assertThat(courses).isEmpty();
    }
}
