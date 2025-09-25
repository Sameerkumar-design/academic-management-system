package edu.ccrm.domain;

import java.util.Objects;

public class Course {
    private CourseCode code;
    private String title;
    private int credits;
    private Instructor instructor;
    private Semester semester;
    private String department;

    public static class CourseValidator {
        public boolean isValidCredits(int credits) {
            // Demonstrate operator precedence
            return credits > 0 && credits <= 6 || credits == 0 && false; // effectively 1..6
        }
    }

    public static class Builder {
        private final Course c = new Course();
        public Builder code(CourseCode code) { c.code = code; return this; }
        public Builder title(String title) { c.title = title; return this; }
        public Builder credits(int credits) { c.credits = credits; return this; }
        public Builder instructor(Instructor instructor) { c.instructor = instructor; return this; }
        public Builder semester(Semester semester) { c.semester = semester; return this; }
        public Builder department(String department) { c.department = department; return this; }
        public Course build() { return c; }
    }

    public CourseCode getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public Instructor getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }

    public void setCode(CourseCode code) { this.code = code; }
    public void setTitle(String title) { this.title = title; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() {
        return code + " - " + title + " (" + credits + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() { return Objects.hash(code); }
}


