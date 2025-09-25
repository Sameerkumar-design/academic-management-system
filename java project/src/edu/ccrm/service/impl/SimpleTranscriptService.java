package edu.ccrm.service.impl;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Transcript;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.TranscriptService;

import java.util.List;

public class SimpleTranscriptService implements TranscriptService {
    private final StudentService studentService;

    public SimpleTranscriptService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public double calculateGPA(List<Enrollment> enrollments) {
        int totalCredits = 0;
        double totalPoints = 0.0;
        for (Enrollment e : enrollments) {
            if (e.getGrade() == null) continue;
            int credits = e.getCourse().getCredits();
            totalCredits += credits;
            totalPoints += credits * e.getGrade().getPoints();
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    @Override
    public Transcript generateTranscript(long studentId) {
        Student s = studentService.getById(studentId).orElse(null);
        if (s == null) return null;
        List<Enrollment> enrols = studentService.getEnrollments(studentId);
        double cgpa = calculateGPA(enrols);
        return new Transcript.Builder()
                .student(s)
                .semesterGPA(cgpa)
                .cumulativeGPA(cgpa)
                .build();
    }
}


