package com.educative.dao;

public enum State {


    KA("Karnataka", 0),  TS("Telangana", 1);

    private String name;
    private int code;

    State(String name, int i) {
        this.name = name;
        this.code = code;
    }
}
