package com.callumgeorge.onesidedbattleships.boards;

import com.callumgeorge.onesidedbattleships.ships.Ship;

/**
 * Represents a single square/cell on the Board.
 */
public class BoardSquare {

    /**
     * State of the square.
     */
    public enum BoardSquareState { NOTHING, HIT, MISS }

    private Ship.ShipNode shipNode;
    private BoardSquareState state;

    public BoardSquare(){
        this.state = BoardSquareState.NOTHING;
    }

    /**
     * Get the current state of the square.
     * @return current state of the square.
     */
    public BoardSquareState getState(){
        return this.state;
    }

    /**
     * Check if the ship currently has a node.
     * @return True if the ship has a node, else false.
     */
    public boolean hasShipNode() {
        return this.shipNode != null;
    }

    /**
     * Gets the ship node for the square.
     * @return ship node for square. Null if none is present.
     */
    public Ship.ShipNode getShipNode(){
        return this.shipNode;
    }

    /**
     * Set the ship node for the square.
     * @param shipNode ShipNode to set.
     */
    protected void setShipNode(Ship.ShipNode shipNode){
        this.shipNode = shipNode;
    }

    /**
     * Set the state of the square.
     * @param state State to set the square to.
     */
    protected void setState(BoardSquareState state){
        this.state = state;
    }

}
