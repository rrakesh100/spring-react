package com.educative.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class InterviewTest {

    InterviewService interviewService = new InterviewService();

    @Test
    public void testForPositiveNumbers() {
        int result = 0;
        try {
            result = interviewService.addPositiveNumbers(12, 13);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(result, 25);
    }

    @Test(expected = Exception.class)
    public void testNegativeNumbers() throws Exception {
        int result = interviewService.addPositiveNumbers(-10, 12);
    }
}
