package edu.ccrm.domain;

import java.util.Objects;

/**
 * Immutable value object representing a course code.
 */
public final class CourseCode {
    private final String department;
    private final String number;
    private final String section;

    public CourseCode(String department, String number, String section) {
        this.department = department == null ? "" : department;
        this.number = number == null ? "" : number;
        this.section = section == null ? "" : section;
    }

    public String getDepartment() { return department; }
    public String getNumber() { return number; }
    public String getSection() { return section; }

    public String asString() { return department + number + (section.isEmpty() ? "" : ("-" + section)); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseCode)) return false;
        CourseCode that = (CourseCode) o;
        return Objects.equals(department, that.department) && Objects.equals(number, that.number) && Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() { return Objects.hash(department, number, section); }

    @Override
    public String toString() { return asString(); }
}


