package com.callumgeorge.onesidedbattleships.boards;

import com.callumgeorge.onesidedbattleships.ships.Ship;
import com.callumgeorge.onesidedbattleships.ships.ShipType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Board that will hold the references to the ships.
 */
public class Board {

    public enum ShotResult {HIT, MISS, DESTROYED}

    private int width;
    private int height;

    private int shipsAlive;

    private List<Point[]> shipPoints;
    private BoardSquare[][] boardSquareGrid;

    private Board(BoardBuilder builder){
        this.width = builder.width;
        this.height = builder.height;
        this.shipsAlive = builder.shipsAlive;
        this.shipPoints = builder.shipPoints;
        this.boardSquareGrid = builder.boardSquareGrid;
    }

    /**
     * Get the width of the board.
     * @return Width of the board.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the board.
     * @return Height of the board.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Fire at the square on the board.
     * @param point Point to fire at.
     * @return Result of the shot.
     */
    public ShotResult fireAtSquare(Point point){
        BoardSquare boardSquare = boardSquareGrid[point.x][point.y];

        // Default the result to a miss.
        ShotResult shotResult = ShotResult.MISS;

        // Check if this square has already been fired at. If it has then return the previous result.
        BoardSquare.BoardSquareState currentState = boardSquare.getState();
        if (currentState == BoardSquare.BoardSquareState.HIT){
            return ShotResult.HIT;
        } else if(currentState == BoardSquare.BoardSquareState.MISS){
            return ShotResult.MISS;
        }

        // If the square has a ship node, then destroy it.
        if(boardSquare.hasShipNode()){
            boolean isShipDestroyed = boardSquare.getShipNode().destroyNode();

            if(isShipDestroyed){
                shotResult = ShotResult.DESTROYED;
                this.shipsAlive--;
            } else
                shotResult = ShotResult.HIT;
        }

        // Record the result in the square.
        if (shotResult == ShotResult.MISS)
            boardSquare.setState(BoardSquare.BoardSquareState.MISS);
        else boardSquare.setState(BoardSquare.BoardSquareState.HIT);

        return shotResult;
    }

    /**
     * Get the ship at the provided position.
     * @param point Point to retrieve ship.
     * @return Returns the ship found at the point. If no ship is found then null is returned.
     */
    public Ship getShip(Point point){
        BoardSquare boardSquare = boardSquareGrid[point.x][point.y];

        if(!boardSquare.hasShipNode())
            return null;

        return boardSquare.getShipNode().getShip();
    }

    /**
     * Get the points for the ship at the specified index.
     * @param index Index for the ship to retrieves. Relates to the position the ship was added at i.e First Ship added = 0.
     * @return Points for the specified ship.
     */
    public Point[] getShipPoints(int index){
        return this.shipPoints.get(index);
    }

    /**
     * Determine if the all ships on the board have been destroyed.
     * @return True if all ships have been destroyed, else false.
     */
    public boolean allShipsDestroyed(){
        return this.shipsAlive == 0;
    }

    /**
     * Get the Record at the specific index.
     * @param index Index for the Row.
     * @return Array of BoardSquares for that row.
     */
    public BoardSquare[] getRow(int index){
        BoardSquare[] row = new BoardSquare[this.width];

        for(int i = 0; i < this.width; i++){
            row[i] = boardSquareGrid[i][index];
        }

        return row;
    }


    /**
     * Directions that a ship can point on the board.
     */
    private enum Direction {NORTH, EAST, SOUTH, WEST}

    /**
     * Constructs the Board object.
     */
    public static class BoardBuilder{
        private int width;
        private int height;
        private int shipsAlive;

        private List<Point[]> shipPoints;
        private BoardSquare[][] boardSquareGrid;

        Random randomX = new Random();
        Random randomY = new Random();
        Random randomDirection = new Random();

        /**
         * @param width How long the Board should be on the X axis.
         * @param height How long the Board should be on the Y axis.
         */
        public BoardBuilder(int width, int height){
            this.width = width;
            this.height = height;
            this.shipsAlive = 0;
            this.shipPoints = new ArrayList<Point[]>();

            // Setup the board square boardSquareGrid and populated each position.
            this.boardSquareGrid = new BoardSquare[width][height];

            for (int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    this.boardSquareGrid[x][y] = new BoardSquare();
                }
            }
        }

        /**
         * Build and return the Board.
         * @return Constructed Board.
         */
        public Board build(){
            return new Board(this);
        }

        /**
         * Add a ship to the boards. The ship will be placed in a random position.
         * @param shipType Ship to add to the boards.
         */
        public BoardBuilder addShip(ShipType shipType){
            // Create the ship based off the ship type.
            Ship ship = new Ship(shipType);

            // Get the root position for the ship.
            Point rootPosition = getRootPosition(ship);

            // Get the direction the ship is to face.
            Direction direction = getShipDirection(rootPosition, ship);

            // Place the ship on the board.
            Point[] points = getShipPoints(rootPosition, ship, direction);

            for(int i = 0; i < points.length; i++) {
                Point shipPoint = points[i];
                BoardSquare boardSquare = this.boardSquareGrid[shipPoint.x][shipPoint.y];
                boardSquare.setShipNode(ship.getShipNode(i));
            }

            // Add the ships to the ship points so we can tracck the position of the ships.
            shipPoints.add(points);

            // Increment the ships count.
            this.shipsAlive++;

            return this;
        }

        /**
         * Returns a valid root position that's not already populated.
         * @return Position of the root node.
         */
        private Point getRootPosition(Ship ship){
            // Determine the root node.
            Point rootPoint = new Point(randomX.nextInt(width), randomY.nextInt(height));

            // Determine if the node is already populated, and that at least one direction is free.
            // If the node isn't valid make a recursive call to generate new root node.
            // The recursion will stop once a valid node has been found.
            if(isSquarePopulated(rootPoint)
                || (isDirectionObstructed(rootPoint, ship, Direction.NORTH)
                    && isDirectionObstructed(rootPoint, ship, Direction.EAST)
                    && isDirectionObstructed(rootPoint, ship, Direction.SOUTH)
                    && isDirectionObstructed(rootPoint, ship, Direction.WEST)))
                return getRootPosition(ship);
            else return rootPoint;
        }

        /**
         * Generates as random direction for the ship
         * @param rootPoint Starting Point for the Ship
         * @param ship The ship intended to be placed
         * @return Random direction to place ship.
         */
        private Direction getShipDirection(Point rootPoint, Ship ship){
            Direction direction = Direction.values()[randomDirection.nextInt(4)];

            if(isDirectionObstructed(rootPoint, ship, direction))
                return getShipDirection(rootPoint, ship);
            else return direction;
        }

        /**
         * Check if the direction the ship is to be placed is obstructed.
         * i.e. Off the boards, or if another ship is in place.
         * @param rootPoint
         * @param ship
         * @param direction
         * @return
         */
        private boolean isDirectionObstructed(Point rootPoint, Ship ship, Board.Direction direction){
            // Check if this direction will put us off the boards.
            if(isDirectionOffTheBoard(rootPoint, ship, direction)) return true;

            // Check if all the cells in that specific direction are free.
            Point[] points = getShipPoints(rootPoint, ship, direction);
            for(Point point : points)
                if(isSquarePopulated(point)) return true;

            // Validation of the direction has passed so return false.
            return false;
        }

        /**
         * Check if the direction the ship is to be placed puts it off the boards.
         * @param rootPoint Start point for the ship.
         * @param ship The ship intended to be placed.
         * @param direction The direction the ship it to point.
         * @return True is the direction would put the ship off the boards, else false.
         */
        private boolean isDirectionOffTheBoard(Point rootPoint, Ship ship, Board.Direction direction){
            // Calculate if the rootPoint +/- the ship length puts the ship of the boards.
            boolean isDirectionOffTheBoard = false;

            switch (direction){
                case NORTH:
                    isDirectionOffTheBoard = rootPoint.y + (ship.getLength() - 1) > height - 1;
                    break;
                case EAST:
                    isDirectionOffTheBoard = rootPoint.x + (ship.getLength() - 1) > width - 1;
                    break;
                case SOUTH:
                    isDirectionOffTheBoard = rootPoint.y - (ship.getLength() - 1) < 0;
                    break;
                case WEST:
                    isDirectionOffTheBoard = rootPoint.x - (ship.getLength() - 1) < 0;
                    break;
            }

            // Direction isn't off the boards, so return false.
            return isDirectionOffTheBoard;
        }

        /**
         * Get all the points on the boards that the ship will be placed on.
         * @param rootPoint Start point for the ship.
         * @param ship The ship intended to be placed.
         * @param direction The direction the ship it to point.
         * @return All the points the ship is intended to be placed at.
         */
        private Point[] getShipPoints(Point rootPoint, Ship ship, Direction direction){
            Point[] points = new Point[ship.getLength()];
            int pointCount = 0;

            switch (direction){
                case NORTH:
                    for(int i = 0, y = rootPoint.y; i < ship.getLength(); i++, y++)
                        points[i] = new Point(rootPoint.x, y);
                    break;
                case EAST:
                    for(int i = 0, x = rootPoint.x; i < ship.getLength(); i++, x++)
                        points[i] = new Point(x, rootPoint.y);
                    break;
                case SOUTH:
                    for(int i = 0, y = rootPoint.y; i < ship.getLength(); i++, y--)
                        points[i] = new Point(rootPoint.x, y);
                    break;
                case WEST:
                    for(int i = 0, x = rootPoint.x; i < ship.getLength(); i++, x--)
                        points[i] = new Point(x, rootPoint.y);
                    break;
            }

            return points;
        }

        /**
         * Determine if a point on the board has a ship.
         * @param point X and Y position.
         * @return True if populated, else false.
         */
        private boolean isSquarePopulated(Point point){
            return this.boardSquareGrid[point.x][point.y].hasShipNode();
        }
    }
}
