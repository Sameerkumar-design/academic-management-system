package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Transcript {
    private Student student;
    private double semesterGPA;
    private double cumulativeGPA;
    private final List<Enrollment> enrolledCourses = new ArrayList<>();

    public static class Builder {
        private final Transcript t = new Transcript();
        public Builder student(Student s) { t.student = s; return this; }
        public Builder semesterGPA(double g) { t.semesterGPA = g; return this; }
        public Builder cumulativeGPA(double g) { t.cumulativeGPA = g; return this; }
        public Builder addEnrollment(Enrollment e) { t.enrolledCourses.add(e); return this; }
        public Transcript build() { return t; }
    }

    public Student getStudent() { return student; }
    public double getSemesterGPA() { return semesterGPA; }
    public double getCumulativeGPA() { return cumulativeGPA; }
    public List<Enrollment> getEnrolledCourses() { return enrolledCourses; }
}


