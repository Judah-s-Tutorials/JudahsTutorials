package com.acmemail.judah.battleship.artwork.text;

import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.BG_CYAN;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.BG_DEFAULT;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.BLC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.BMC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.BRC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.CLEAR_SCR;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.FG_CYAN;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.FG_DEFAULT;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.FG_RED;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.FG_YELLOW;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.HLINE;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.MLC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.MMC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.MRC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.SOLID_BLOCK;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.TLC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.TMC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.TRC;
import static com.acmemail.judah.battleship.artwork.text.TextGridConstants.VLINE;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.StatusMessages;
import com.acmemail.judah.battleship.model.Cell2DView;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;

/**
 * Using text,
 * formulates a visual approximation
 * of a given Grid.
 * The first line of the visualization
 * is the name of the grid.
 * Following lines depict the rows and columns of the grid,
 * using colors to indicate cells that are known
 * to be occupied by ships and/or attacked.
 * Column headings (1, 2, 3, ...) 
 * and row labels (A, B, C)
 * are provided.
 * The maximum number of rows and columns
 * is limited to 99 each.
 */
public class BasicTextGrid
{
    /** Maximum number of rows supported. */
    public static final int MAX_ROWS    = 99;
    /** Maximum number of columns supported. */
    public static final int MAX_COLS    = 99;
    
    /** Foreground color of a cell occupied by a ship. */
    private static final String     fgShipColor     = FG_CYAN;
    /** Background color of a cell occupied by a ship. */
    private static final String     bgShipColor     = BG_CYAN;
    /** Color of the splat on a cell occupied by a ship. */
    private static final String     splatColorShip  = FG_RED;
    /** Color of the splat on an unoccupied cell. */
    private static final String     splatColorEmpty = FG_YELLOW;
    /** The Unicode character for a splat on a cell occupied by a ship. */
    private static final char       splatCharShip   = SOLID_BLOCK;
    /** The Unicode character for a splat on an unoccupied cell. */
    private static final char       splatCharEmpty  = SOLID_BLOCK;
    
    /** The encapsulated logical grid. */
    private final Grid2D    grid2D;
    /** The number of rows in the encapsulated logical grid. */
    private final int       numRows;
    /** The number of columns in the encapsulated logical grid. */
    private final int       numCols;
    /** 
     * The width of a cell (in characters) in the visualization
     * of the encapsulated grid 
     */
    private final int       cellWidth;
    
    /**
     * Constructor.
     * Determines all the parameters needed
     * to visualize a give grid.
     * 
     * @param grid2D    the given grid; may not be null
     * 
     * @throws NullPointerException if grid2D is null
     * @throws IllegalStateException 
     *      if the dimensions of the given grid
     *      exceed the maximum limits
     */
    public BasicTextGrid( Grid2D grid2D )
    {
        Objects.requireNonNull( grid2D, "grid2D" );
        this.grid2D = grid2D;
        numRows = Grid2D.getNumRows();
        if ( numRows > MAX_ROWS )
            throw new IllegalStateException( StatusMessages.MAX_ROWS_EXCEEDED );
        numCols = Grid2D.getNumCols();
        if ( numCols > MAX_COLS )
            throw new IllegalStateException( StatusMessages.MAX_COLS_EXCEEDED );
        cellWidth = 2;
    }
    
    /**
     * Returns a string containing the ANSI escape sequence
     * to clear a console's display.
     * 
     * @return
     *      string containing the ANSI escape sequence
     *      to clear a console's display
     */
    public String getClearScreen()
    {
        return CLEAR_SCR;
    }
    
    /**
     * Formats a list of strings
     * representing the lines in the encapsulated grid.
     * Each element of the list
     * corresponds to one line of text.
     * 
     * @see #printGrid(PrintStream)
     * 
     * @return  a list of strings representing the lines in the grid
     */
    public List<String> toPrintString()
    {
        List<String>    lines   = new ArrayList<>();
        lines.addAll( header() );
        lines.addAll( colLabels() );
        lines.addAll( topRow() );
          IntStream.range( 1, numRows )
              .mapToObj( i -> midRow( i ) )
              .forEach( lines::addAll );
        lines.addAll( bottomLines() );
        return lines;
    }
    
    /**
     * Prints the encapsulated grid
     * to a given stream.
     * 
     * @param stream    the given stream
     */
    public void printGrid( PrintStream stream )
    {
        System.out.print( CLEAR_SCR );
        toPrintString().forEach( stream::println );
    }
    
    /**
     * Returns a list of lines
     * representing the header that precedes the grid visualization.
     * 
     * @return  a list of lines constituting the grid header
     */
    private List<String> header()
    {
        List<String>    header  = List.of( grid2D.getName() );
        return header;
    }
    
    /**
     * Returns a list of lines constituting
     * the grid's column labels.
     * 
     * @return  a list of lines constituting the grid column labels
     */
    private List<String> colLabels()
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

    /**
     * Returns the grid's first row,
     * including the top of the grid's boundary.
     * 
     * @return  the grid's first row
     */
    private List<String> topRow()
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
            .mapToObj( i -> new GridCoords( i, 0 ) )
            .map( this::getCell )
            .forEach( s -> bldr.append( s ).append( VLINE) );
        lines.add( bldr.toString() );

        return lines;
    }
    
    /**
     * Formats and returns the lines 
     * constituting a row in the grid
     * after the first,
     * and before the last.
     * 
     * @param rowNum    the y-coordinate of the row to format
     * 
     * @return  the formatted row
     */
    private List<String> midRow( int rowNum )
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
            .mapToObj( i -> new GridCoords( i, rowNum ) )
            .map( this::getCell )
            .forEach( s -> bldr.append( s ).append( VLINE) );
        lines.add( bldr.toString() );

        return lines;
    }
    
    /**
     * Returns the the lines constituting the bottom of the grid,
     * including the bottom of it boundary.
     * 
     * @return  the grid's bottom lines
     */
    private List<String> bottomLines()
    {
        List<String>    lines   = new ArrayList<>();
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "  " ).append( BLC ).append( HLINE ).append( HLINE );
        IntStream.range( 1, numCols )
            .forEach( i -> bldr.append( BMC ).append( HLINE ).append( HLINE ) );
        bldr.append( BRC ).append( "" );
        lines.add( bldr.toString() );
        return lines;
    }
    
    /**
     * Returns a string representing the content of a given cell,
     * including the ANSI escape sequences
     * needed to format the cell color.
     * The formatting of the string
     * accounts for whether or not 
     * the cell is known to be occupied,
     * and whether or not it has been attacked.
     * 
     * @param coords    the coordinates of the given cell
     * 
     * @return  a string representing the content of a given cell
     */
    private String getCell( GridCoords coords )
    {
        Cell2DView      gridCell    = grid2D.getCellView( coords );
        StringBuilder   cellStr     = new StringBuilder();
        StringBuilder   suffix      = new StringBuilder();
        char[]          cellChar  = { ' ' };
        if ( gridCell.getShip() != null )
        {
            cellStr.append( bgShipColor );
            if ( gridCell.isSplatted() )
            {
                cellStr.append( splatColorShip );
                cellChar[0] = splatCharShip;
            }
            else
                cellStr.append( fgShipColor );
            suffix.append( FG_DEFAULT ).append( BG_DEFAULT );
        }
        else if ( gridCell.isSplatted() )
        {
            if ( gridCell.isOpponent() )
            {
                cellStr.append( splatColorShip );
                cellChar[0] = splatCharShip;
            }
            else
            {
                cellStr.append( splatColorEmpty );
                cellChar[0] = splatCharEmpty;
            }
            suffix.append( FG_DEFAULT );
        }
        
        IntStream.range( 0, cellWidth )
            .forEach( i-> cellStr.append( cellChar[0] ));
        cellStr.append( suffix );
        return cellStr.toString();
    }
}
