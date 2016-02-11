package com.jaysyko.wrestlechat.utils;

public class FormValidation {

    public static boolean formIsClean(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
