package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Transcript;

import java.util.List;

public interface TranscriptService {
    double calculateGPA(List<Enrollment> enrollments);
    Transcript generateTranscript(long studentId);
}


