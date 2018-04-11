package com.callumgeorge.onesidedbattleships.board;

import com.callumgeorge.onesidedbattleships.boards.Board;
import com.callumgeorge.onesidedbattleships.ships.ShipType;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class BoardTest {

    @Test
    public void fireAtSquare_ShipAtSquare_HitResultReturned(){
        // Build the board and add a battleship.
        Board board = new Board.BoardBuilder(10, 10)
                .addShip(ShipType.BATTLESHIP)
                .build();

        // Get the ship points so we can determine what to target.
        Point[] points = board.getShipPoints(0);

        Assert.assertEquals(Board.ShotResult.HIT, board.fireAtSquare(points[0]));
    }

    @Test
    public void fireAtSquare_NothingAtSquare_MissResultReturned(){
        // Build the board and add a battleship.
        Board board = new Board.BoardBuilder(10, 10)
                .addShip(ShipType.BATTLESHIP)
                .build();

        // Determine what point along the first row does not have the ship.
        Point emptyPoint = null;
        for(int x = 0; x < 10; x++){
            emptyPoint = new Point(x, 0);

            if(board.getShip(emptyPoint) == null)
                break;
        }

        Assert.assertEquals(Board.ShotResult.MISS, board.fireAtSquare(emptyPoint));
    }

    @Test
    public void fireAtSquare_ShipDestroyed_DestroyedResultReturned(){
        // Build the board and add a battleship.
        Board board = new Board.BoardBuilder(10, 10)
                .addShip(ShipType.BATTLESHIP)
                .build();

        // Get the ship points so we can determine what to target.
        Point[] points = board.getShipPoints(0);

        // Destroyed all points, the last one will be the only one to return destroyed.
        for(int i = 0; i < 4; i++)
            Assert.assertEquals(Board.ShotResult.HIT, board.fireAtSquare(points[i]));

        Assert.assertEquals(Board.ShotResult.DESTROYED, board.fireAtSquare(points[4]));
    }

    @Test
    // Ensure that a ship won't be destroyed if the same point is hit multiple times.
    public void fireAtSquare_ShipHitAtSameSquareMultipleTimes_HitResultReturned(){
        // Build the board and add a battleship.
        Board board = new Board.BoardBuilder(10, 10)
                .addShip(ShipType.BATTLESHIP)
                .build();

        // Get the ship points so we can determine what to target.
        Point[] points = board.getShipPoints(0);

        // Hit the same point 4 times to the point it would usually be destroyed.
        for(int i = 0; i < 4; i++)
            Assert.assertEquals(Board.ShotResult.HIT, board.fireAtSquare(points[0]));

        // Ensure that hitting the point a fifth time only returns a hit.
        Assert.assertEquals(Board.ShotResult.HIT, board.fireAtSquare(points[0]));
    }

    @Test
    public void allShipsDestroyed_NoShipHaveBeenDestroyed_FalseReturned(){
        // Build the board and add a battleship and two destroyers.
        Board board = new Board.BoardBuilder(10, 10)
                .addShip(ShipType.BATTLESHIP)
                .addShip(ShipType.DESTROYER)
                .addShip(ShipType.DESTROYER)
                .build();

        Assert.assertFalse(board.allShipsDestroyed());
    }

    @Test
    public void allShipsDestroyed_OneShipHasBeenDestroyed_FalseReturned(){
        // Build the board and add a battleship and two destroyers.
        Board board = new Board.BoardBuilder(10, 10)
                .addShip(ShipType.BATTLESHIP)
                .addShip(ShipType.DESTROYER)
                .addShip(ShipType.DESTROYER)
                .build();

        // Get the ship points so we can determine what to target.
        Point[] points = board.getShipPoints(0);

        // Destroy all the points on the battleship.
        for (Point point : points)
            board.fireAtSquare(point);

        // Assert that the destroyer has been destroyed.
        Assert.assertTrue(board.getShip(points[0]).isShipDestroyed());

        // Assert that not all ships have been destroyed.
        Assert.assertFalse(board.allShipsDestroyed());
    }

    @Test
    public void allShipsDestroyed_AllShipsDestroyed_ReturnTrue(){
        // Build the board and add a battleship and two destroyers.
        Board board = new Board.BoardBuilder(10, 10)
                .addShip(ShipType.BATTLESHIP)
                .addShip(ShipType.DESTROYER)
                .addShip(ShipType.DESTROYER)
                .build();

        // Destroy all the ships.
        for(int shipIndex = 0; shipIndex < 3; shipIndex++){
            Point[] points = board.getShipPoints(shipIndex);
            for (Point point : points)
                board.fireAtSquare(point);
        }

        // Assert that all ships have been destroyed.
        Assert.assertTrue(board.allShipsDestroyed());
    }

}
