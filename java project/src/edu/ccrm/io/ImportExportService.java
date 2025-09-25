package edu.ccrm.io;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.CourseCode;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Import/Export operations using NIO.2 and stream-based processing.
 */
public class ImportExportService {

    // -------- CSV: Students --------
    public void exportStudentsCSV(Path targetFile, List<Student> students) throws IOException {
        ensureParent(targetFile);
        try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
            writer.write("id,regNo,fullName,email,status,registrationDate");
            writer.newLine();
            for (Student s : students) {
                StringJoiner j = new StringJoiner(",");
                j.add(String.valueOf(s.getId()))
                        .add(escapeCsv(s.getRegNo()))
                        .add(escapeCsv(s.getName()))
                        .add(escapeCsv(s.getEmail()))
                        .add(escapeCsv(s.getStatus()))
                        .add(String.valueOf(s.getRegistrationDate()));
                writer.write(j.toString());
                writer.newLine();
            }
        }
    }

    public List<Student> importStudentsCSV(Path sourceFile) throws IOException {
        List<Student> list = new ArrayList<>();
        if (!Files.exists(sourceFile)) return list;
        try (BufferedReader reader = Files.newBufferedReader(sourceFile, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsv(line);
                if (parts.length < 6) continue;
                Student s = new Student();
                s.setId(parseLongSafe(parts[0]));
                s.setRegNo(unescapeCsv(parts[1]));
                s.setName(unescapeCsv(parts[2]));
                s.setEmail(unescapeCsv(parts[3]));
                s.setStatus(unescapeCsv(parts[4]));
                s.setRegistrationDate(LocalDate.parse(parts[5]));
                list.add(s);
            }
        }
        return list;
    }

    // -------- CSV: Courses --------
    public void exportCoursesCSV(Path targetFile, List<Course> courses) throws IOException {
        ensureParent(targetFile);
        try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
            writer.write("code,title,credits,instructor,department,semester");
            writer.newLine();
            for (Course c : courses) {
                StringJoiner j = new StringJoiner(",");
                j.add(escapeCsv(c.getCode() == null ? "" : c.getCode().asString()))
                        .add(escapeCsv(c.getTitle()))
                        .add(String.valueOf(c.getCredits()))
                        .add(escapeCsv(c.getInstructor() == null ? "" : c.getInstructor().getName()))
                        .add(escapeCsv(c.getDepartment()))
                        .add(c.getSemester() == null ? "" : c.getSemester().name());
                writer.write(j.toString());
                writer.newLine();
            }
        }
    }

    public List<Course> importCoursesCSV(Path sourceFile) throws IOException {
        List<Course> list = new ArrayList<>();
        if (!Files.exists(sourceFile)) return list;
        try (BufferedReader reader = Files.newBufferedReader(sourceFile, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsv(line);
                if (parts.length < 6) continue;
                Course course = new Course.Builder()
                        .code(parseCourseCode(parts[0]))
                        .title(unescapeCsv(parts[1]))
                        .credits(parseIntSafe(parts[2]))
                        .instructor(new Instructor())
                        .department(unescapeCsv(parts[4]))
                        .semester(parseSemester(parts[5]))
                        .build();
                course.getInstructor().setName(unescapeCsv(parts[3]));
                list.add(course);
            }
        }
        return list;
    }

    // -------- USON (simple custom format) --------
    public void exportStudentsUSON(Path targetFile, List<Student> students) throws IOException {
        ensureParent(targetFile);
        try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
            for (Student s : students) {
                writer.write("{ id:" + s.getId()
                        + ", regNo:\"" + escapeUson(s.getRegNo()) + "\""
                        + ", name:\"" + escapeUson(s.getName()) + "\""
                        + ", email:\"" + escapeUson(s.getEmail()) + "\""
                        + ", status:\"" + escapeUson(s.getStatus()) + "\""
                        + ", registrationDate:\"" + s.getRegistrationDate() + "\" }");
                writer.newLine();
            }
        }
    }

    public List<Student> importStudentsUSON(Path sourceFile) throws IOException {
        List<Student> list = new ArrayList<>();
        if (!Files.exists(sourceFile)) return list;
        try (BufferedReader reader = Files.newBufferedReader(sourceFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String sLine = line.trim();
                if (!sLine.startsWith("{") || !sLine.endsWith("}")) continue;
                // naive parse
                sLine = sLine.substring(1, sLine.length() - 1);
                String[] fields = sLine.split(",");
                Student s = new Student();
                for (String f : fields) {
                    String[] kv = f.split(":", 2);
                    if (kv.length < 2) continue;
                    String key = kv[0].trim();
                    String val = kv[1].trim();
                    if (key.equals("id")) s.setId(parseLongSafe(val));
                    else if (key.equals("regNo")) s.setRegNo(stripQuotes(val));
                    else if (key.equals("name")) s.setName(stripQuotes(val));
                    else if (key.equals("email")) s.setEmail(stripQuotes(val));
                    else if (key.equals("status")) s.setStatus(stripQuotes(val));
                    else if (key.equals("registrationDate")) s.setRegistrationDate(LocalDate.parse(stripQuotes(val)));
                }
                list.add(s);
            }
        }
        return list;
    }

    // -------- Helpers --------
    private static void ensureParent(Path file) throws IOException {
        Path parent = file.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    private static String escapeCsv(String v) {
        if (v == null) return "";
        boolean needs = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        String e = v.replace("\"", "\"\"");
        return needs ? "\"" + e + "\"" : e;
    }

    private static String unescapeCsv(String value) {
        if (value == null) return "";
        String trimmed = value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            String inner = trimmed.substring(1, trimmed.length() - 1);
            return inner.replace("\"\"", "\"");
        }
        return trimmed;
    }

    private static String[] splitCsv(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') { current.append('"'); i++; }
                else inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString()); current.setLength(0);
            } else { current.append(c); }
        }
        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }

    private static int parseIntSafe(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }
    private static long parseLongSafe(String s) { try { return Long.parseLong(s.replaceAll("[^0-9]","")); } catch (Exception e) { return 0L; } }

    private static Semester parseSemester(String s) {
        if (s == null) return null;
        String v = s.trim();
        int idx = v.indexOf('_');
        if (idx > 0) v = v.substring(0, idx);
        try { return Semester.valueOf(v.toUpperCase()); } catch (Exception e) { return null; }
    }

    private static CourseCode parseCourseCode(String s) {
        String raw = unescapeCsv(s);
        if (raw == null) return null;
        String dep = ""; String num = raw; String sec = "";
        int dash = raw.indexOf('-');
        if (dash >= 0) { sec = raw.substring(dash + 1); num = raw.substring(0, dash); }
        for (int i = 0; i < num.length(); i++) {
            if (Character.isDigit(num.charAt(i))) { dep = num.substring(0, i); num = num.substring(i); break; }
        }
        return new CourseCode(dep, num, sec);
    }

    private static String escapeUson(String s) { return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\""); }
    private static String stripQuotes(String s) {
        String t = s.trim();
        if (t.startsWith("\"") && t.endsWith("\"")) return t.substring(1, t.length()-1);
        return t;
    }
}


