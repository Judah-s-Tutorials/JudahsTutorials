package com.acmemail.judah.sandbox;

import java.util.Arrays;

/**
 * An instance of the class encapsulates a matrix.
 * 
 * @author Jack Straub
 *
 */
public class Matrix
{
    private double  data[][];
    
    /**
     * Application entry point.
     * 
     * @param args  command-line ar
     */
    public static void main( String[] args )
    {
        double[][]  dataArg =
        {
            { 101, 102, 103, 104, 105 },
            { 201, 202, 203, 204, 205 },
            { 301, 302, 303, 304, 305 },
        };
        
        Matrix  matrix  = new Matrix( dataArg );
        double[]    row = matrix.getRow( 2 );
        System.out.println( Arrays.toString( row ) );
    }
    
    /**
     * Constructs a new matrix from the given data.
     * The given data is copied into a new array;
     * changes to the given array will not affect
     * the encapsulated matrix.
     * 
     * @param data  the given data
     */
    public Matrix( double data[][] )
    {
        int rowNum  = 0;
        this.data = new double[data.length][];
        for ( double[] row : data )
        {
            this.data[rowNum] = Arrays.copyOf( row, row.length );
            rowNum++;
        }
        for ( int row = 0 ; row < data.length ; ++row )
        {
            for ( int col = 0 ; col < data[row].length ; ++col )
            {
                System.out.print( this.data[row][col] + " " );
            }
            System.out.println();
        }
    }
    
    /**
     * Returns a copy the given row of the matrix.
     * Changes to the returned array
     * will not affect the encapsulated matrix.
     * If the given row number is out of range
     * an ArrayIndexOutOfBoundsException is thrown.
     * 
     * @param rowNum    the number of the row to return
     * 
     * @return the given row of the matrix
     * 
     * @throws  ArrayIndexOutOfBoundsException
     *          if the given row number is out of range
     */
    public double[] getRow( int rowNum )
    {
        double[]    row = Arrays.copyOf( data[rowNum], data[rowNum].length );
        return row;
    }
}
