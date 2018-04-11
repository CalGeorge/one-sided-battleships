package com.callumgeorge.onesidedbattleships.utils;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for converting and validating Coordinates.
 */
public class CoordinateUtils {

    /**
     * Converts the coordinates in the format AN to a Point.
     * @param coordinates Coordinates to convert into a point.
     * @return Converted point. null if in an invalid format.
     */
    public static Point toPoint(String coordinates) {
        // If the coordinates aren't valid then return null.
        if(!isValid(coordinates))
            return null;

        // Convert the letter into a number by subtracting the 'A' unicode character,
        // this will give its position relative to A. e.g. A = 0, B = 1, C = 2
        int xPosition = coordinates.charAt(0) - 'A';
        int yPosition = Integer.parseInt(coordinates.substring(1)) - 1;

        return new Point(xPosition, yPosition);
    }

    /**
     * Validates that the Coordinate is in a valid format. Min coordinates = A1. Max coordinates = J10.
     * @param coordinates Coordinate to validate.
     * @return True if coordinate is valid, else false
     */
    public static boolean isValid(String coordinates) {
        // validate the format is correct
        Pattern pattern = Pattern.compile("[A-J][0-9][0]?");
        Matcher matcher = pattern.matcher(coordinates);

        // If the pattern does not match then return false, else true.
        return matcher.matches();
    }
}
