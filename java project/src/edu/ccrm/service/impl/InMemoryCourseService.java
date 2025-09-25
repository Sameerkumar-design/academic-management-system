package edu.ccrm.service.impl;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryCourseService implements CourseService {
    private final List<Course> courses = new ArrayList<>();

    @Override
    public Course create(Course course) {
        courses.add(course);
        return course;
    }

    @Override
    public Optional<Course> getByCode(String code) {
        return courses.stream().filter(c -> c.getCode() != null && c.getCode().asString().equals(code)).findFirst();
    }

    @Override
    public List<Course> getAll() { return new ArrayList<>(courses); }

    @Override
    public Course update(Course course) {
        getByCode(course.getCode().asString()).ifPresent(existing -> {
            existing.setTitle(course.getTitle());
            existing.setCredits(course.getCredits());
            existing.setInstructor(course.getInstructor());
            existing.setSemester(course.getSemester());
            existing.setDepartment(course.getDepartment());
        });
        return course;
    }

    @Override
    public boolean delete(String code) {
        return courses.removeIf(c -> c.getCode() != null && c.getCode().asString().equals(code));
    }

    @Override
    public List<Course> filterByInstructor(Instructor instructor) {
        return courses.stream()
                .filter(c -> c.getInstructor() != null && c.getInstructor().equals(instructor))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterByDepartment(String department) {
        String d = department == null ? "" : department.trim().toLowerCase();
        return courses.stream()
                .filter(c -> c.getDepartment() != null && c.getDepartment().toLowerCase().contains(d))
                .collect(Collectors.toList());
    }
}


