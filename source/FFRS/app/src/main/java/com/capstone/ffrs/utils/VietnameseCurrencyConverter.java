/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.ffrs.utils;

/**
 * The Vietnamese number to words converter which provide function for
 * converting
 *
 * @author HuanPMSE61860
 */
public class VietnameseCurrencyConverter {

    // Array of Vietnamese words from 0 to 9
    static String[] units = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};

    /**
     * The main function for converting numbers to Vietnamese words.
     * <p>
     * Algorithm: divide number into groups of 3 digits then convert each group
     * into words and combine them and some additional words such as "tỉ",
     * "triệu", "nghìn", etc.
     * <p>
     * Note: the number spelling may be different for some people, so the
     * program is for the most common speaking in Vietnam
     *
     * @param strNumber : the string containing number
     * @return String type or null if strNumber is not number
     */
    public static String convertNumber(String strNumber) {
        // Initialize result string
        String result = "";
        // Check if strNumber is a number (is not null and only have digits)
        if (strNumber != null && strNumber.matches("\\d+")) {
            // Check if strNumber has only 0s, return string "không"
            if (strNumber.matches("0+")) {
                return "không";
            }
            // Calculate the number's length
            int size = strNumber.length();
            // Add 0s for spliting numbers into group of 3-digit number
            switch (size % 3) {
                // Case the number has 1 spare digits. Ex: 1 001
                case 1:
                    // Add 2 digits 0 into the left of strNumber
                    strNumber = "00" + strNumber;
                    break;
                // Case the number has 2 spare digits. Ex: 42 113
                case 2:
                    // Add 1 digits 0 into the left of strNumber
                    strNumber = "0" + strNumber;
                    break;
            }
            // Recalculate size after adding spare 0 digits
            size = strNumber.length();
            // Calculate the number of groups
            int numberOfGroups = size / 3;
            // Initialize the array of groups with the calculated size
            String[] strGroups = new String[numberOfGroups];
            /*
            * Access to each group of 3-digits and convert it into words based
            * on digits and it's position
             */
            for (int i = 0; i < numberOfGroups; i++) {
                // Get 3 continuous digits into a group
                strGroups[i] = strNumber.substring(3 * i, 3 * i + 3);
                int position = strGroups.length - i - 1;
                // Check if the result have no words yet
                if (result.equals("")) {
                    // Convert first group of digits which is not "000"
                    result += convertFirstGroup(strGroups[i], position);
                } else {
                    result += convertGroup(strGroups[i], position);
                }
            }
        } else {
            // Return null if the string is not a number or words otherwise
            return null;
        }

        return result.trim() + " đồng";
    }

    /**
     * The function for converting 1st group of 3-digit which is not "000"
     *
     * @param strGroup : the group of 3-digit number
     * @param position : the position of group in number from left to right,
     *                 start from 0
     * @return String type
     */
    private static String convertFirstGroup(String strGroup, int position) {
        String result = "";
        // Check if group is not valid (more than or less than 3 digits)
        // or position is not valid
        if (strGroup == null || !strGroup.matches("\\d{3}") || position < 0) {
            result = null;
        } else // Check if group is "000", end the method by returning ""
            if (strGroup.equals("000")) {
                result = "";
            } else {
                // Convert the first digit in group
                if (strGroup.charAt(0) != '0') {
                    result += units[strGroup.charAt(0) - '0'] + " trăm";
                }
                // Convert the second digit in group
                if (strGroup.charAt(1) != '0') {
                    if (strGroup.charAt(0) != '0') {
                        result += " ";
                    }
                    if (strGroup.charAt(1) != '1') {
                        result += units[strGroup.charAt(1) - '0'] + " mươi";
                    } else {
                        result += "mười";
                    }
                }
                // Convert the last digit in group
                if (strGroup.charAt(2) != '0') {
                    if (strGroup.charAt(1) == '0' && strGroup.charAt(0) != '0') {
                        result += " lẻ";
                    }
                    if (strGroup.charAt(0) != '0' || strGroup.charAt(1) != '0') {
                        result += " ";
                    }
                    if (strGroup.charAt(1) != '0') {
                        if (strGroup.charAt(1) != '0') {
                            switch (strGroup.charAt(2)) {
                                case '1':
                                    if (strGroup.charAt(1) != '1') {
                                        result += "mốt";
                                    } else {
                                        result += "một";
                                    }
                                    break;
                                case '5':
                                    result += "lăm";
                                    break;
                                default:
                                    result += units[strGroup.charAt(2) - '0'];
                                    break;
                            }
                        }
                    } else {
                        result += units[strGroup.charAt(2) - '0'];
                    }
                }
                // Add positioning words. Ex: "tỉ", "triệu", "nghìn"
                switch (position % 3) {
                    case 0:
                        if (position / 3 > 0) {
                            result += " tỉ";
                        }
                        break;
                    case 1:
                        result += " nghìn";
                        break;
                    case 2:
                        result += " triệu";
                        break;
                }
            }
        return result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();
    }

    /**
     * The function for converting the group of 3-digit after first group is
     * converted
     *
     * @param strGroup : the group of 3-digit number
     * @param position : the position of group in number from left to right,
     *                 start from 0
     * @return String type
     */
    private static String convertGroup(String strGroup, int position) {
        String result = "";
        // Check if group is not valid (more than or less than 3 digits) 
        // or position is not valid
        if (strGroup == null || !strGroup.matches("\\d{3}") || position < 0) {
            result = null;
        } else // Check if group is "000"
        {
            if (strGroup.equals("000")) {
                // Check if group is in "tỉ" (billion) position
                if (position >= 3 && position % 3 == 0) {
                    result = " tỉ";
                } else {
                    result = "";
                }
            } else {
                // Convert the first digit in group
                result += " " + units[strGroup.charAt(0) - '0'] + " trăm";
                // Convert the second digit in group
                if (strGroup.charAt(1) != '0') {
                    if (strGroup.charAt(1) != '1') {
                        result += " " + units[strGroup.charAt(1) - '0'] + " mươi";
                    } else {
                        result += " mười";
                    }
                }
                // Convert the last digit in group
                if (strGroup.charAt(2) != '0') {
                    if (strGroup.charAt(1) == '0') {
                        result += " lẻ";
                    }
                    if (strGroup.charAt(1) != '0') {
                        switch (strGroup.charAt(2)) {
                            case '1':
                                if (strGroup.charAt(1) != '1') {
                                    result += " mốt";
                                } else {
                                    result += " một";
                                }
                                break;
                            case '5':
                                result += " lăm";
                                break;
                            default:
                                result += " " + units[strGroup.charAt(2) - '0'];
                                break;
                        }
                    } else {
                        result += " " + units[strGroup.charAt(2) - '0'];
                    }
                }
                // Add positioning words. Ex: "tỉ", "triệu", "nghìn"
                switch (position % 3) {
                    case 0:
                        if (position / 3 > 0) {
                            result += " tỉ";
                        }
                        break;
                    case 1:
                        result += " nghìn";
                        break;
                    case 2:
                        result += " triệu";
                        break;
                }
            }
        }
        return result;
    }
}
