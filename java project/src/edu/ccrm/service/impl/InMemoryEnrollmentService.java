package edu.ccrm.service.impl;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.service.EnrollmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryEnrollmentService implements EnrollmentService {
    private final List<Enrollment> enrollments = new ArrayList<>();

    @Override
    public Enrollment enroll(Student student, Course course) {
        Objects.requireNonNull(student);
        Objects.requireNonNull(course);

        // Duplicate enrollment prevention
        boolean exists = enrollments.stream().anyMatch(e -> e.getStudent().equals(student) && e.getCourse().equals(course));
        if (exists) {
            throw new edu.ccrm.util.Exceptions.DuplicateEnrollmentException("Duplicate enrollment");
        }

        int maxCredits = AppConfig.getInstance().getMaxCreditsPerStudent();
        int currentCredits = enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
        int proposed = currentCredits + course.getCredits();
        assert proposed >= 0 && proposed <= 1000 : "credits calculation overflow";
        if (proposed > maxCredits) {
            throw new edu.ccrm.util.Exceptions.MaxCreditLimitExceededException("Max credit limit exceeded");
        }

        Enrollment e = new Enrollment(student, course, java.time.LocalDate.now());
        enrollments.add(e);
        student.getEnrolledCourses().add(e);
        return e;
    }

    @Override
    public boolean unenroll(Student student, Course course) {
        return enrollments.removeIf(en -> en.getStudent().equals(student) && en.getCourse().equals(course));
    }

    @Override
    public void recordGrade(Student student, Course course, Grade grade, double marks) {
        enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                .findFirst()
                .ifPresent(e -> { e.setGrade(grade); e.setMarks(marks); });
    }

    @Override
    public List<Enrollment> getEnrollmentsForStudent(Student student) {
        List<Enrollment> list = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(student)) list.add(e);
        }
        return list;
    }
}


