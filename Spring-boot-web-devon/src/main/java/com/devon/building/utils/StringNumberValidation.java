package com.devon.building.utils;

public class StringNumberValidation {
    private StringNumberValidation() {
        /* This utility class should not be instantiated */
    }


    public static boolean isNullOrEmpty(String str){
        return str == null || str.isBlank();
    }
    public static boolean isNull(Long num){
        return num == null;
    }
}
