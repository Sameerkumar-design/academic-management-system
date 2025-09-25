package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student create(Student student);
    Optional<Student> getById(long id);
    List<Student> getAll();
    Student update(Student student);
    boolean delete(long id);

    List<Enrollment> getEnrollments(long studentId);
}


