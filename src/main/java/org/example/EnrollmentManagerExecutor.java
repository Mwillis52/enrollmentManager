package org.example;


public class EnrollmentManagerExecutor {

    public Integer executeEnrollmentManager() {
        return start();
    }

    private Integer start() {
        EnrollmentManagerObjectHandler handler = new EnrollmentManagerObjectHandler();
        handler.enrollmentManagerObjectHandler();
        return 0;
    }
}
