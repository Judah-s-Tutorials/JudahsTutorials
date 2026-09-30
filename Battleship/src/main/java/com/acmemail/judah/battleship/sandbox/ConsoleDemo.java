package com.acmemail.judah.battleship.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;

public class ConsoleDemo
{
    public static final String  ANSI_RESET  = "\u001B[0m";
    public static final String  ANSI_RED    = "\u001B[31m";
    public static final String  ANSI_GREEN  = "\u001B[32m";
    public static final String  ANSI_YELLOW = "\u001B[33m";
    public static final char    HLINE       = '\u2500';
    public static final char    VLINE       = '\u2502';
    public static final char    TLC         = '\u250C';
    public static final char    TMC         = '\u252C';
    public static final char    TRC         = '\u2510';
    public static final char    BLC         = '\u2514';
    public static final char    BMC         = '\u2534';
    public static final char    MMC         = '\u253C';
    public static final char    BRC         = '\u2518';
    public static final char    MLC         = '\u251C';
    public static final char    MRC         = '\u2524';

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final List<String>   envVars =
        List.of( "TERM", "COLORTERM", "WT_SESSION", "TERM_PROGRAM" );
    
    private static final int    numRows     = Grid2D.getNumRows();
    private static final int    numCols     = Grid2D.getNumCols();

    public static void main(String[] args)
    {
        envVars.forEach( s -> {
            
            String  val = System.getenv( s );
            System.out.printf( "%s: %s%n", s, val );
        });
        System.out.println( ANSI_RED + "red text" + ANSI_RESET );
        Grid2D  grid    = Grid2D.getHomeGrid();
        printGrid( grid );
        
    }

    private static void printGrid( Grid2D grid )
    {
        if ( numCols > 99 )
        {
            String  msg = 
                "MAX GRID COLS 99; FOUND " + numCols;
            throw new IllegalStateException( msg );
        }
        if ( numRows > 99 )
        {
            String  msg = 
                "MAX GRID ROWS 99; FOUND " + numRows;
            throw new IllegalStateException( msg );
        }
        
        colLabels().forEach( System.out::println );
        topRow().forEach( System.out::println );
        IntStream.range( 1, numRows )
            .mapToObj( i -> midRow( i ) )
            .forEach( l -> l.forEach( System.out::println ) );
        bottomLine().forEach( System.out::println );
    }
    
    private static List<String> topRow()
    {
        List<String>    lines   = new ArrayList<>();
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "  " ).append( TLC ).append( HLINE ).append( HLINE );
        IntStream.range( 1, numCols )
            .forEach( i -> bldr.append( TMC ).append( HLINE).append( HLINE ) );
        bldr.append( TRC );
        lines.add( bldr.toString() );
        
        bldr.setLength( 0 );
        bldr.append( " A" ).append( VLINE );
        IntStream.range( 0, numCols )
            .forEach( i -> bldr.append( "  " ).append( VLINE) );
        lines.add( bldr.toString() );

        return lines;
    }
    
    private static List<String> midRow( int rowNum )
    {
        List<String>    lines       = new ArrayList<>();
        GridCoords      coords      = new GridCoords( 0, rowNum );
        Label           label       = new Label( coords );
        String          rowStr      = label.getRowStr();
        String          fmt         = "%2s" + VLINE;
        String          labelStr    = String.format( fmt, rowStr );
        
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( "  " ).append( MLC ).append( HLINE ).append( HLINE );
        IntStream.range( 1, numCols )
            .forEach( i -> bldr.append( MMC ).append( HLINE ).append( HLINE ) );
        bldr.append( MRC );
        lines.add( bldr.toString() );
        
        bldr.setLength( 0 );
        bldr.append( labelStr );
        IntStream.range( 0, numCols )
            .forEach( i -> bldr.append( "  " ).append( VLINE) );
        lines.add( bldr.toString() );

        return lines;
    }
    
    private static List<String> bottomLine()
    {
        List<String>    lines   = new ArrayList<>();
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "  " ).append( BLC ).append( HLINE ).append( HLINE );
        IntStream.range( 1, numCols )
            .forEach( i -> bldr.append( BMC ).append( HLINE ).append( HLINE ) );
        bldr.append( BRC );
        lines.add( bldr.toString() );
        return lines;
    }
    
    private static List<String> colLabels()
    {
        List<String>    labels      = new ArrayList<>();
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( "  " ).append( VLINE );
        
        bldr.setLength( 0 );
        bldr.append( "  " ).append( VLINE );
        for ( int inx = 0 ; inx < numCols ; ++inx )
        {
            GridCoords  coords      = new GridCoords( inx, 0 );
            Label       label       = new Label( coords );
            String      colStr      = label.getColStr();
            String      labelStr    = String.format( "%2s", colStr );
            bldr.append( labelStr ).append( VLINE );
        }
        labels.add( bldr.toString() );
        
        return labels;
    }
}
