package com.callumgeorge.onesidedbattleships.utils;

import com.callumgeorge.onesidedbattleships.boards.Board;
import com.callumgeorge.onesidedbattleships.boards.BoardSquare;

/**
 * Utilities to provide addition functionality to the board.
 */
public class BoardUtils {

    /**
     * Generate an Ascii board to represent the Board Game. Will only show the state of squares that have been fired at.
     * @param board Board to generate an Ascii board from.
     * @return Generated board.
     */
    public static String generateAsciiBoard(Board board){
        StringBuilder textBoard = new StringBuilder();

        int boardWidth = board.getWidth();

        // Add the first horizontal boundary.
        textBoard.append(getAsciiHorizontalBoundary(boardWidth));
        textBoard.append("\n");

        // Add the header.
        textBoard.append(getAsciiHeader(boardWidth));
        textBoard.append("\n");

        // Add a horizontal boundary under the header.
        textBoard.append(getAsciiHorizontalBoundary(boardWidth));
        textBoard.append("\n");

        // Add each board row.
        for(int i = 0; i < board.getHeight(); i++){
            BoardSquare[] row = board.getRow(i);
            textBoard.append(getAsciiRow( i, row));
            textBoard.append("\n");

            // Add  horizontal boundary below the row.
            textBoard.append(getAsciiHorizontalBoundary(boardWidth));
            textBoard.append("\n");
        }

        return textBoard.toString();
    }

    /**
     * Generate a horizontal boundary that can be added between the rows.
     * @param width Width of the board.
     * @return Generated horizontal boundary.
     */
    private static String getAsciiHorizontalBoundary(int width){
        String cellSpacing = getCellPadding(width, " ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i <= width; i++) {
            builder.append("+");
            builder.append(getCellPadding(width, "-"));
            builder.append("-");
            builder.append(getCellPadding(width, "-"));
        }

        builder.append("+");
        return builder.toString();
    }

    /**
     * Get the Header for the table e.g. 1, 2, 3, 4, 5
     * @param width Width of the board.
     * @return Header for the table.
     */
    private static String getAsciiHeader(int width){
        String cellSpacing = getCellPadding(width, " ");
        StringBuilder headerBuilder = new StringBuilder();

        // Add the first cell.
        headerBuilder.append(getHeaderCell(" "));

        // Add all the letters.
        for(int i = 0; i < width; i++){
            // 65 is the Ascii position for "A".
            // Max supported length is currently 25, after that we'll run out of alpha numeric values.
            // When Characters getting into double digits and beyond we need to remove one space.
            headerBuilder.append(cellSpacing);
            headerBuilder.append((char)(65 + i));
            headerBuilder.append(cellSpacing);
            headerBuilder.append("|");
        }

        return headerBuilder.toString();
    }

    /**
     * Get the row in an ascii format.
     * @param rowIndex Index of the row on the grid.
     * @param boardRow All the boardSquares on that row.
     * @return The row in an ascii format.
     */
    private static String getAsciiRow(int rowIndex, BoardSquare[] boardRow){
        int rowWidth = boardRow.length;
        String cellSpacing = getCellPadding(rowWidth, " ");

        StringBuilder rowBuilder = new StringBuilder();

        //Add the header cell.
        rowBuilder.append(getHeaderCell(Integer.toString(rowIndex + 1)));

        // Add each of the records.
        for(BoardSquare boardSquare : boardRow){
            rowBuilder.append(cellSpacing);
            switch (boardSquare.getState()) {
                case NOTHING:
                    rowBuilder.append(" ");
                    break;
                case HIT:
                    rowBuilder.append("X");
                    break;
                case MISS:
                    rowBuilder.append("~");
                    break;
            }

            rowBuilder.append(cellSpacing);
            rowBuilder.append("|");
        }

        return rowBuilder.toString();
    }

    /**
     * Get the Header cell for the row.
     * @param cellValue The value to put in the cell.
     * @return The Header Cell Ascii formatted.
     */
    public static String getHeaderCell(String cellValue){
        StringBuilder headerCellBuilder = new StringBuilder();

        headerCellBuilder.append("|  ");
        headerCellBuilder.append(cellValue);

        // Add the last spacing, adjust this if the cell value length is greater than 1,
        // else it will push all subsequent cells across.
        if(cellValue.length() > 1)
            headerCellBuilder.append(" ");
        else headerCellBuilder.append("  ");

        headerCellBuilder.append("|");

        return headerCellBuilder.toString();
    }

    /**
     * Gets the padding to add in the cells.
     * This is to ensure that the cell width is in line with the width of the header cells.
     * @param width Width of the board.
     * @return Cell padding.
     */
    private static String getCellPadding(int width, String spacingCharacters){
        String cellSpacing = "";
        int repeatAmount = String.valueOf(width).length();

        for(int i = 0; i < repeatAmount; i++){
            cellSpacing = cellSpacing + spacingCharacters;
        }

        return cellSpacing;
    }
}
