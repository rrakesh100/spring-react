package com.educative.dao;

public enum State {


    KARNATAKA("KARNATAKA", 0),  TELANGANA("TELANGANA", 1);

    private String name;
    private int code;

    State(String name, int i) {
        this.name = name;
        this.code = i;
    }
}
