package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.TranscriptService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class Menu {
    private final ConsoleIO io = new ConsoleIO();
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final TranscriptService transcriptService;
    private final ImportExportService importExportService = new ImportExportService();
    private final BackupService backupService = new BackupService();

    public Menu(StudentService ss, CourseService cs, EnrollmentService es, TranscriptService ts) {
        this.studentService = ss;
        this.courseService = cs;
        this.enrollmentService = es;
        this.transcriptService = ts;
    }

    public void start() {
        outer: while (true) {
            System.out.println("=== Campus Course & Records Manager ===");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Operations");
            System.out.println("4. Grade Management");
            System.out.println("5. Import/Export Data");
            System.out.println("6. Backup & Utilities");
            System.out.println("7. Reports & Analytics");
            System.out.println("8. Exit");

            int choice = io.readInt("Choose option (1-8): ", 1, 8);
            switch (choice) {
                case 1: studentsMenu(); break;
                case 2: coursesMenu(); break;
                case 3: enrollmentMenu(); break;
                case 4: gradesMenu(); break;
                case 5: importExportMenu(); break;
                case 6: backupMenu(); break;
                case 7: reportsMenu(); break;
                case 8: break outer;
                default: System.out.println("Invalid choice");
            }
            System.out.println();
        }
    }

    private void studentsMenu() {
        boolean loop = true;
        do {
            System.out.println("-- Student Management --");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back");
            int c = io.readInt("Choose (1-5): ", 1, 5);
            switch (c) {
                case 1: {
                    String name = io.readNonEmpty("Full name: ");
                    String email = io.readEmail("Email: ");
                    String regNo = io.readNonEmpty("Reg No: ");
                    LocalDate regDate = io.readDate("Registration date (yyyy-MM-dd): ", "yyyy-MM-dd");
                    Student s = new Student(0, name, email, regDate, regNo, Student.EnrollmentStatus.ACTIVE);
                    studentService.create(s);
                    System.out.println("Added student with ID: " + s.getId());
                    break;
                }
                case 2: studentService.getAll().forEach(System.out::println); break;
                case 3: {
                    long id = io.readInt("ID to update: ", 1, Integer.MAX_VALUE);
                    java.util.Optional<Student> opt = studentService.getById(id);
                    if (opt.isPresent()) {
                        Student s = opt.get();
                        s.setName(io.readNonEmpty("New name: "));
                        s.setEmail(io.readEmail("New email: "));
                        s.setRegNo(io.readNonEmpty("New reg no: "));
                        studentService.update(s);
                        System.out.println("Updated.");
                    } else {
                        System.out.println("Not found.");
                    }
                    break;
                }
                case 4: {
                    long id = io.readInt("ID to delete: ", 1, Integer.MAX_VALUE);
                    String conf = io.readNonEmpty("Type DELETE to confirm: ");
                    if ("DELETE".equalsIgnoreCase(conf)) {
                        boolean ok = studentService.delete(id);
                        System.out.println(ok ? "Deleted." : "Not found.");
                    } else System.out.println("Cancelled.");
                    break;
                }
                case 5: loop = false; break;
            }
        } while (loop);
    }

    private void coursesMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("-- Course Management --");
            System.out.println("1. Add Course");
            System.out.println("2. List Courses");
            System.out.println("3. Back");
            int c = io.readInt("Choose (1-3): ", 1, 3);
            switch (c) {
                case 1: {
                    String dep = io.readNonEmpty("Department (e.g. CS): ");
                    String num = io.readNonEmpty("Number (e.g. 101): ");
                    String sec = io.readNonEmpty("Section (e.g. A): ");
                    String title = io.readNonEmpty("Title: ");
                    int credits = io.readInt("Credits (1-6): ", 1, 6);
                    String semester = io.readNonEmpty("Semester (SPRING/SUMMER/FALL): ");
                    Course course = new Course.Builder()
                            .code(new CourseCode(dep, num, sec))
                            .title(title)
                            .credits(credits)
                            .semester(Semester.valueOf(semester.toUpperCase()))
                            .department(dep)
                            .build();
                    courseService.create(course);
                    System.out.println("Added course: " + course);
                    break;
                }
                case 2: courseService.getAll().forEach(System.out::println); break;
                case 3: loop = false; break;
            }
        }
    }

    private void enrollmentMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("-- Enrollment Operations --");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. List Student Enrollments");
            System.out.println("4. Back");
            int c = io.readInt("Choose (1-4): ", 1, 4);
            switch (c) {
                case 1: {
                    long id = io.readInt("Student ID: ", 1, Integer.MAX_VALUE);
                    String code = io.readNonEmpty("Course code (e.g. CS101-A): ");
                    java.util.Optional<Student> studentOpt = studentService.getById(id);
                    java.util.Optional<Course> courseOpt = courseService.getByCode(code);
                    if (!studentOpt.isPresent()) { System.out.println("Student not found."); break; }
                    if (!courseOpt.isPresent()) { System.out.println("Course not found."); break; }
                    try {
                        enrollmentService.enroll(studentOpt.get(), courseOpt.get());
                        System.out.println("Enrolled successfully.");
                    } catch (edu.ccrm.util.Exceptions.DuplicateEnrollmentException e) {
                        System.out.println("Already enrolled.");
                    } catch (edu.ccrm.util.Exceptions.MaxCreditLimitExceededException e) {
                        System.out.println("Cannot enroll: max credit limit exceeded.");
                    } catch (Exception e) {
                        System.out.println("Enrollment failed: " + e.getMessage());
                    }
                    break;
                }
                case 2: {
                    long id = io.readInt("Student ID: ", 1, Integer.MAX_VALUE);
                    String code = io.readNonEmpty("Course code: ");
                    java.util.Optional<Student> studentOpt = studentService.getById(id);
                    java.util.Optional<Course> courseOpt = courseService.getByCode(code);
                    if (!studentOpt.isPresent() || !courseOpt.isPresent()) { System.out.println("Not found."); break; }
                    boolean ok = enrollmentService.unenroll(studentOpt.get(), courseOpt.get());
                    System.out.println(ok ? "Unenrolled." : "Not enrolled.");
                    break;
                }
                case 3: {
                    long id = io.readInt("Student ID: ", 1, Integer.MAX_VALUE);
                    java.util.Optional<Student> studentOpt = studentService.getById(id);
                    if (!studentOpt.isPresent()) { System.out.println("Student not found."); break; }
                    java.util.List<Enrollment> list = studentService.getEnrollments(id);
                    if (list.isEmpty()) System.out.println("No enrollments.");
                    else list.forEach(e -> System.out.println(e.getCourse() + " | Grade: " + e.getGrade()));
                    break;
                }
                case 4: loop = false; break;
            }
        }
    }

    private void gradesMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("-- Grade Management --");
            System.out.println("1. Record Grade for Student Course");
            System.out.println("2. Back");
            int c = io.readInt("Choose (1-2): ", 1, 2);
            switch (c) {
                case 1: {
                    long id = io.readInt("Student ID: ", 1, Integer.MAX_VALUE);
                    String code = io.readNonEmpty("Course code: ");
                    String gradeStr = io.readNonEmpty("Grade (S/A/B/C/D/F): ").toUpperCase();
                    double marks;
                    try { marks = Double.parseDouble(io.readNonEmpty("Marks (0-100): ")); }
                    catch (NumberFormatException ex) { System.out.println("Invalid marks."); break; }
                    java.util.Optional<Student> studentOpt = studentService.getById(id);
                    java.util.Optional<Course> courseOpt = courseService.getByCode(code);
                    if (!studentOpt.isPresent() || !courseOpt.isPresent()) { System.out.println("Not found."); break; }
                    try {
                        edu.ccrm.domain.Grade grade = edu.ccrm.domain.Grade.valueOf(gradeStr);
                        enrollmentService.recordGrade(studentOpt.get(), courseOpt.get(), grade, marks);
                        System.out.println("Grade recorded.");
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Invalid grade value.");
                    }
                    break;
                }
                case 2: loop = false; break;
            }
        }
    }

    private void importExportMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("-- Import/Export --");
            System.out.println("1. Export Students CSV");
            System.out.println("2. Import Students CSV");
            System.out.println("3. Export Courses CSV");
            System.out.println("4. Import Courses CSV");
            System.out.println("5. Back");
            int c = io.readInt("Choose (1-5): ", 1, 5);
            switch (c) {
                case 1: {
                    Path p = Paths.get("data/students.csv");
                    try { importExportService.exportStudentsCSV(p, studentService.getAll()); System.out.println("Exported to " + p); }
                    catch (Exception e) { System.out.println("Export failed: " + e.getMessage()); }
                    break;
                }
                case 2: {
                    Path p = Paths.get("data/students.csv");
                    try { List<Student> list = importExportService.importStudentsCSV(p); for (Student s : list) { studentService.create(s); } System.out.println("Imported " + list.size() + " students"); }
                    catch (Exception e) { System.out.println("Import failed: " + e.getMessage()); }
                    break;
                }
                case 3: {
                    Path p = Paths.get("data/courses.csv");
                    try { importExportService.exportCoursesCSV(p, courseService.getAll()); System.out.println("Exported to " + p); }
                    catch (Exception e) { System.out.println("Export failed: " + e.getMessage()); }
                    break;
                }
                case 4: {
                    Path p = Paths.get("data/courses.csv");
                    try { List<Course> list = importExportService.importCoursesCSV(p); for (Course c2 : list) { courseService.create(c2); } System.out.println("Imported " + list.size() + " courses"); }
                    catch (Exception e) { System.out.println("Import failed: " + e.getMessage()); }
                    break;
                }
                case 5: loop = false; break;
            }
        }
    }

    private void backupMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("-- Backup & Utilities --");
            System.out.println("1. Create Backup");
            System.out.println("2. Show Backup Size");
            System.out.println("3. List Files (depth)");
            System.out.println("4. Back");
            int c = io.readInt("Choose (1-4): ", 1, 4);
            switch (c) {
                case 1: {
                    try {
                        AppConfig cfg = AppConfig.getInstance();
                        Path backup = backupService.createTimestampedBackup(Paths.get("data"), cfg.getBackupLocation());
                        System.out.println("Backup created at: " + backup);
                    } catch (Exception e) { System.out.println("Backup failed: " + e.getMessage()); }
                    break;
                }
                case 2: {
                    try {
                        long size = backupService.directorySize(AppConfig.getInstance().getBackupLocation());
                        System.out.println("Backup dir size: " + size + " bytes");
                    } catch (Exception e) { System.out.println("Size calc failed: " + e.getMessage()); }
                    break;
                }
                case 3: {
                    int depth = io.readInt("Depth: ", 1, 20);
                    try { backupService.listFilesWithDepth(Paths.get("data"), depth); } catch (Exception e) { System.out.println("List failed: " + e.getMessage()); }
                    break;
                }
                case 4: loop = false; break;
            }
        }
    }

    private void reportsMenu() {
        System.out.println("-- Reports & Analytics --");
        System.out.println("1. Top Students by Enrolled Credits");
        System.out.println("2. GPA for Student");
        System.out.println("3. Async Demo (Runnable Lambda)");
        System.out.println("4. Back");
        int c = io.readInt("Choose (1-4): ", 1, 4);
        switch (c) {
            case 1: {
                java.util.List<Student> ranked = new java.util.ArrayList<>(studentService.getAll());
                java.util.Collections.sort(ranked, new java.util.Comparator<Student>() {
                    @Override public int compare(Student a, Student b) {
                        int ac = a.getEnrolledCourses().stream().mapToInt(e -> e.getCourse().getCredits()).sum();
                        int bc = b.getEnrolledCourses().stream().mapToInt(e -> e.getCourse().getCredits()).sum();
                        return Integer.compare(bc, ac);
                    }
                });
                if (ranked.size() > 10) ranked = ranked.subList(0, 10);
                for (Student s : ranked) {
                    int credits = s.getEnrolledCourses().stream().mapToInt(e -> e.getCourse().getCredits()).sum();
                    System.out.println(s.getName() + " - " + credits + " credits");
                }
                break;
            }
            case 2: {
                long id = io.readInt("Student ID: ", 1, Integer.MAX_VALUE);
                Transcript t = transcriptService.generateTranscript(id);
                if (t == null) System.out.println("Not found");
                else System.out.println("GPA: " + t.getCumulativeGPA());
                break;
            }
            case 3: {
                Runnable task = new Runnable() { @Override public void run() { System.out.println("Async task executed on " + Thread.currentThread().getName()); } };
                new Thread(task).start();
                break;
            }
            case 4: default: break;
        }
    }
}


