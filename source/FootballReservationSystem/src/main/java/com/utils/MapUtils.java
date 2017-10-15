package com.utils;

import com.dto.CordinationPoint;

/**
 * @author MinhQuy
 */
public class MapUtils {
    public static Double calculateDistanceBetweenTwoPoint(CordinationPoint a, CordinationPoint b){
        double num1 = (a.getLongitude() - b.getLongitude())*111.32;
        double num2 = (a.getLatitude() - b.getLatitude())*110.57;
        return Math.sqrt(num1*num1 + num2*num2);
    }

}