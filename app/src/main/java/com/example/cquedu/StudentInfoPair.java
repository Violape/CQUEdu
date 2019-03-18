package com.example.cquedu;

class StudentInfoPair {
    private String field;
    private String value;
    public StudentInfoPair(String s1, String s2){
        field = s1;
        value = s2;
    }
    public String getField(){
        return field;
    }
    public String getValue(){
        return value;
    }
}
