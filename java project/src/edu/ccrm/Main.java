package edu.ccrm;

import edu.ccrm.cli.Menu;
import edu.ccrm.service.impl.InMemoryCourseService;
import edu.ccrm.service.impl.InMemoryEnrollmentService;
import edu.ccrm.service.impl.InMemoryStudentService;
import edu.ccrm.service.impl.SimpleTranscriptService;

public class Main {
    public static void main(String[] args) {
        InMemoryStudentService studentService = new InMemoryStudentService();
        InMemoryCourseService courseService = new InMemoryCourseService();
        InMemoryEnrollmentService enrollmentService = new InMemoryEnrollmentService();
        SimpleTranscriptService transcriptService = new SimpleTranscriptService(studentService);

        new Menu(studentService, courseService, enrollmentService, transcriptService).start();
    }
}


