package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    public static class EnrollmentStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String SUSPENDED = "SUSPENDED";
    }

    private String regNo;
    private String status = EnrollmentStatus.ACTIVE;
    private final List<Enrollment> enrolledCourses = new ArrayList<>();

    public Student() {}

    public Student(long id, String name, String email, LocalDate registrationDate, String regNo, String status) {
        super(id, name, email, registrationDate);
        this.regNo = regNo;
        this.status = status == null ? EnrollmentStatus.ACTIVE : status;
    }

    @Override
    public String getProfile() {
        return "Student{" + getId() + ", " + getName() + ", regNo=" + regNo + "}";
    }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<Enrollment> getEnrolledCourses() { return enrolledCourses; }
}


