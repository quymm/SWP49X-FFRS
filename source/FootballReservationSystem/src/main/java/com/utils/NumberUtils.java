package com.utils;

/**
 * @author MinhQuy
 */
public class NumberUtils {
    public static Double parseFromStringToDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
