package edu.ccrm.util;

public class Exceptions {
    public static class DuplicateEnrollmentException extends RuntimeException {
        public DuplicateEnrollmentException(String msg) { super(msg); }
    }
    public static class MaxCreditLimitExceededException extends RuntimeException {
        public MaxCreditLimitExceededException(String msg) { super(msg); }
    }
}


