package com.jaysyko.wrestlechat.utils;

/**
 * Created by jarushaan on 2016-02-08.
 */
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
