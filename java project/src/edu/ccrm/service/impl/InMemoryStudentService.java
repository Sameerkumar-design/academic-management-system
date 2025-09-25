package edu.ccrm.service.impl;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import edu.ccrm.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryStudentService implements StudentService {
    private final List<Student> students = new ArrayList<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Student create(Student student) {
        student.setId(seq.getAndIncrement());
        students.add(student);
        return student;
    }

    @Override
    public Optional<Student> getById(long id) {
        return students.stream().filter(s -> s.getId() == id).findFirst();
    }

    @Override
    public List<Student> getAll() {
        return new ArrayList<>(students);
    }

    @Override
    public Student update(Student student) {
        getById(student.getId()).ifPresent(existing -> {
            existing.setName(student.getName());
            existing.setEmail(student.getEmail());
            existing.setRegistrationDate(student.getRegistrationDate());
            existing.setRegNo(student.getRegNo());
            existing.setStatus(student.getStatus());
        });
        return student;
    }

    @Override
    public boolean delete(long id) {
        return students.removeIf(s -> s.getId() == id);
    }

    @Override
    public List<Enrollment> getEnrollments(long studentId) {
        return getById(studentId)
                .map(Student::getEnrolledCourses)
                .map(list -> list.stream().collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }
}


