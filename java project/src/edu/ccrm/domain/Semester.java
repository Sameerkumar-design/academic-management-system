package edu.ccrm.domain;


/**
 * Semester enum with associated calendar year.
 */
public enum Semester {
    SPRING, SUMMER, FALL;

    public String labelWithYear(int year) {
        return name() + "_" + year;
    }

    public static Semester fromString(String value) {
        return Semester.valueOf(value.trim().toUpperCase());
    }
}


