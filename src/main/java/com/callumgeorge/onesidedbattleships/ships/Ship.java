package com.callumgeorge.onesidedbattleships.ships;

/**
 * Ship that can be added to a board.
 */
public class Ship {

    private ShipType shipType;
    private int shipHealth;

    private ShipNode[] shipNodes;

    public Ship(ShipType shipType){
        this.shipType = shipType;
        this.shipHealth = shipType.getLength();
        this.shipNodes = generateShipNodes(shipType.getLength());
    }

    /**
     * Generate ship nodes.
     * @param length Amount of ship nodes to generate.
     * @return Generated ship nodes.
     */
     private ShipNode[] generateShipNodes(int length){
        ShipNode[] shipNodes = new ShipNode[length];
        for(int i = 0; i < shipType.getLength(); i++){
            shipNodes[i] = new ShipNode();
        }

        return shipNodes;
     }

    /**
     * Remove 1 health from the ship.
     * @return Indicates whether the ship has been destroyed by this hit.
     */
    private boolean removeHealth(){
        if(this.shipHealth != 0){
            this.shipHealth--;
        }

        return isShipDestroyed();
    }

    /**
     * Get the length of the ship.
     * @return Length of the ship.
     */
    public int getLength(){
        return shipType.getLength();
    }

    /**
     * Get the type of the ship.
     * @return Type of the ship.
     */
    public ShipType getShipType(){
        return this.shipType;
    }


    /**
     * Determine if the ship has been destroyed.
     * @return is the ship destroyed.
     */
    public boolean isShipDestroyed(){
        return this.shipHealth == 0;
    }

    /**
     * Get a specific position of the ship.
     * @param position position of the ship (zero based)
     * @return Node at the position.
     */
    public ShipNode getShipNode(int position){
        return shipNodes[position];
    }

    /**
     * A node is point of a ship, for example a ship of length = 5 will have 5 ship nodes.
     */
    public class ShipNode {

        boolean isDestroyed;

        /**
         * Destroy this node.
         * @return True if the parent ship has been destroyed.
         */
        public boolean destroyNode(){
            if (!isDestroyed)
                return Ship.this.removeHealth();
            else return isShipDestroyed();
        }

        /**
         * Get the parent Ship.
         * @return Parent Ship.
         */
        public Ship getShip(){
            return Ship.this;
        }
    }
}
