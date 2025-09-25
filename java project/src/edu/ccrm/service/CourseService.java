package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course create(Course course);
    Optional<Course> getByCode(String code);
    List<Course> getAll();
    Course update(Course course);
    boolean delete(String code);

    List<Course> filterByInstructor(Instructor instructor);
    List<Course> filterByDepartment(String department);
}


