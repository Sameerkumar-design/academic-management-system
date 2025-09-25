package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;

import java.util.List;

public interface EnrollmentService {
    Enrollment enroll(Student student, Course course);
    boolean unenroll(Student student, Course course);
    void recordGrade(Student student, Course course, Grade grade, double marks);
    List<Enrollment> getEnrollmentsForStudent(Student student);
}


