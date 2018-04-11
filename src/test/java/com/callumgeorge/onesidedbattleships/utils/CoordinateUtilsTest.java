package com.callumgeorge.onesidedbattleships.utils;


import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class CoordinateUtilsTest {

    @Test
    public void isValid_XAxisGreaterThanMaxAlphaCharacter_ReturnFalse(){
        boolean isValid = CoordinateUtils.isValid("K10");
        Assert.assertFalse(isValid);
    }

    @Test
    public void isValid_XAxisNonAlphaCharacter_ReturnFalse(){
        boolean isValid = CoordinateUtils.isValid("010");
        Assert.assertFalse(isValid);
    }

    @Test
    public void isValid_YAxisNonGreaterThanMaxNumricCharacter_ReturnFalse(){
        boolean isValid = CoordinateUtils.isValid("J11");
        Assert.assertFalse(isValid);
    }

    @Test
    public void isValid_YAxisNonNumericCharacters_ReturnFalse(){
        boolean isValid = CoordinateUtils.isValid("JAA");
        Assert.assertFalse(isValid);
    }

    @Test
    public void isValid_MinXandYCoordinates_ReturnTrue(){
        boolean isValid = CoordinateUtils.isValid("A1");
        Assert.assertTrue(isValid);
    }

    @Test
    public void isValid_MaxXandYCoordinates_ReturnTrue(){
        boolean isValid = CoordinateUtils.isValid("J10");
        Assert.assertTrue(isValid);
    }

    @Test
    public void toPoint_InvalidCoordinate_ReturnNull(){
        Point point = CoordinateUtils.toPoint("JAA");
        Assert.assertNull(point);
    }

    @Test
    public void toPoint_ValidMinXMaxYCoordinate_ReturnPoint(){
        Point point = CoordinateUtils.toPoint("A10");
        Assert.assertEquals(0, point.x);
        Assert.assertEquals(9, point.y);
    }

    @Test
    public void toPoint_ValidMaxXMinYCoordinate_ReturnPoint(){
        Point point = CoordinateUtils.toPoint("J1");
        Assert.assertEquals(9, point.x);
        Assert.assertEquals(0, point.y);
    }

}
