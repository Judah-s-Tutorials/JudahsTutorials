package com.acmemail.judah.battleship.sandbox;

import java.util.Arrays;
import java.util.stream.Stream;

import com.acmemail.judah.battleship.Cell;

public class ArrayStream2D
{
    private static final int    rows    = 10;
    private static final int    cols    = 15;
    private final Cell[][]      cells   = new Cell[rows][cols];
    
    public static void main(String[] args)
    {
        ArrayStream2D   streamer    = new ArrayStream2D();
        streamer.getCells().forEach( System.out::println );
//        for ( int yco = 0 ; yco < rows ; ++yco )
//            for ( int xco = 0 ; xco < cols ; ++xco )
//                System.out.println( streamer.cells[yco][xco] );
    }

    public ArrayStream2D()
    {
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++col )
                cells[row][col] = new Cell( row, col );
    }

    public Stream<Cell> getCells()
    {
        return Arrays.stream( cells ).flatMap( t -> Arrays.stream( t ) );
    }
}
