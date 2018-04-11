package com.callumgeorge.onesidedbattleships.ships;

/**
 * Represents the type of the ship.
 */
public enum ShipType {
    BATTLESHIP (5),
    DESTROYER  (4);

    private final int length;

    ShipType(int length){
        this.length = length;
    }

    /**
     * Get the length of this ship type.
     * @return Length of the ship type.
     */
    public int getLength(){
        return this.length;
    }
}
