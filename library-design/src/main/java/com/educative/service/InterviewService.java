package com.educative.service;

public class InterviewService {

    public int addPositiveNumbers(int num1, int num2) throws Exception {
        if(num1 <0 || num2 <0)
            throw new Exception("Only positive numbers allowed" + (num1 < 0 ? num1 : num2));
        return num1 + num2;
    }
}
