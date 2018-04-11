package com.callumgeorge.onesidedbattleships.ships;

import org.junit.Assert;
import org.junit.Test;

public class ShipTest {

    @Test
    public void Ship_Battleship_LengthIs5(){
        Ship ship = new Ship(ShipType.BATTLESHIP);
        Assert.assertEquals(5, ship.getLength());
    }

    @Test
    public void Ship_Destroyer_LengthIs4(){
        Ship ship = new Ship(ShipType.DESTROYER);
        Assert.assertEquals(4, ship.getLength());
    }

}
