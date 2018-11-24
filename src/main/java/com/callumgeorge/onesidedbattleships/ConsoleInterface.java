package com.callumgeorge.onesidedbattleships;

import com.callumgeorge.onesidedbattleships.boards.Board;
import com.callumgeorge.onesidedbattleships.ships.ShipType;
import com.callumgeorge.onesidedbattleships.utils.BoardUtils;
import com.callumgeorge.onesidedbattleships.utils.CoordinateUtils;

import java.awt.*;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console/Terminal Interface for the one-side-battleships game.
 */
public class ConsoleInterface {

    private static final int BOARD_HEIGHT = 10;
    private static final int BOARD_WIDTH = 10;
    private Board board;

    public static void main(String[] args) {
        ConsoleInterface consoleInterface = new ConsoleInterface();
        consoleInterface.startGame();
    }

    public ConsoleInterface(){
        this.board = new Board.BoardBuilder(BOARD_WIDTH, BOARD_HEIGHT)
                .addShip(ShipType.BATTLESHIP)
                .addShip(ShipType.DESTROYER)
                .addShip(ShipType.DESTROYER)
                .build();
    }

    /**
     * Begin execution of the game. This will prompt user input.
     */
    public void startGame(){

        int turnCount = 0;

        Scanner reader = new Scanner(System.in);
        System.out.println(BoardUtils.generateAsciiBoard(board));

        while(!board.allShipsDestroyed()) {
            // Increment the turn count.
            turnCount++;

            // Request target square.
            Point targetSquare;
            try {
                targetSquare = requestTargetSquare(reader);
            }catch (NoSuchElementException e){
                // This will occur if the scanner no longer has any lines to read. Assume program has been prematurely stopped.
                return;
            }

            // Fire at the target square.
            Board.ShotResult shotResult = this.board.fireAtSquare(targetSquare);

            // Reprint the board to show the result.
            System.out.print("\n\n\n");
            System.out.println(BoardUtils.generateAsciiBoard(board));
            System.out.println();

            // Tell the user what happened.
            System.out.print("Result: ");
            switch (shotResult){
                case MISS:
                    System.out.println("MISS");
                    break;
                case HIT:
                    System.out.println("HIT");
                    break;
                case DESTROYED:
                    System.out.println("You sunk my " + board.getShip(targetSquare).getShipType().name() + "!");
            }
        }

        // Reprint the board one last time to show the result.
        System.out.print("\n\n\n");
        System.out.println(BoardUtils.generateAsciiBoard(board));
        System.out.println();

        // All ships have been destroyed so inform the player and wait for user input before ending.
        System.out.println("All ships have been sunk. Congratulations!");
        System.out.println(String.format("You completed the game in %d turns.", turnCount));
        System.out.println();

        System.out.println("Press enter to end game...");
        reader.nextLine();

        // Close the scanner.
        reader.close();
    }

    /**
     * Request the users input for the target square.
     * @param reader Scanner for the console.
     * @return target point.
     */
    private Point requestTargetSquare(Scanner reader) throws NoSuchElementException {
        // Retrieve the coordinates from the user and validate them before continuing.
        String targetCoordinates;
        boolean coordinatesValid;

        do {
            // Request user input.
            System.out.print("Enter Cell to Target: ");

            // Get the user input and validate it, if it's invalid then inform the user.
            targetCoordinates = reader.nextLine().toUpperCase().trim();
            coordinatesValid = CoordinateUtils.isValid(targetCoordinates);

            if(!coordinatesValid)
                System.out.println("\nCoordinates are not in a valid format e.g. A10. Please try again.");

        } while (!coordinatesValid);

        return CoordinateUtils.toPoint(targetCoordinates);
    }
}
