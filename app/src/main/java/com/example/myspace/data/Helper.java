package com.example.myspace.data;

public class Helper {

    public static String getStringValue(int value) {
        if(value <10) return "0" + value;
        return String.valueOf(value);
    }



}
