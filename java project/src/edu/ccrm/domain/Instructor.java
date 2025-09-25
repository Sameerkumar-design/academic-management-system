package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person {
    private String department;
    private final List<Course> coursesAssigned = new ArrayList<>();

    public Instructor() {}

    public Instructor(long id, String name, String email, LocalDate registrationDate, String department) {
        super(id, name, email, registrationDate);
        this.department = department;
    }

    @Override
    public String getProfile() {
        return "Instructor{" + getId() + ", " + getName() + ", dept=" + department + "}";
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public List<Course> getCoursesAssigned() { return coursesAssigned; }
}


