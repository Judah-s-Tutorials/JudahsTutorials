package com.acmemail.judah.battleship.artwork.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.model.Grid2D;

/**
 * This class formats a table using text,
 * where each cell contains 
 * one of the currently declared grids.
 * The home grid is configured
 * in the upper-left corner.
 * The output is a list of strings
 * suitable for printing to stdout.
 * <p>
 * By default,
 * the width of the target display is 132 characters,
 * and the number of columns in the table
 * is calculated from the display width,
 * the width of a grid.
 * and the width of the column separators.
 * The goal is for the table
 * to fit the display width
 * without needing a horizontal scroll bar.
 * If the width of a table
 * is greater than the width of the display,
 * the table will be configured as one column
 * which will be wider than the display.
 * <p>
 * The presumed width of the target display
 * can be set by the client
 * (see {@link #ofDisplayWidth(int)};
 * alternatively the client can specify the number columns
 * without reference to the target display
 * (see {@link #ofNumCols(int)}.
 * Row separators are drawn between rows,
 * and a table separator
 * is drawn at the end of the table.
 * 
 * @see #ofDisplayWidth(int)
 * @see #ofNumCols(int)
 * @see BasicTextGrid
 */
public class TextGridParent
{
    /** The default width of the target display. */
    private static final int    DEF_DISP_WIDTH  = 132;
    /** 
     * Character to repeat to generate a column separator
     * for the table of grids.
     */
    private static final char   COL_SEP_CHAR    = '\u2588';
    /** COL_SEP_CHAR stringified; declared here for convenience. */
    private static final String COL_SEP_STR     = String.valueOf( COL_SEP_CHAR );
    /** 
     * Character to repeat to generate a row separator
     * for the table of grids.
     */
    private static final char   ROW_SEP_CHAR    = '\u2500';
    /** ROW_SEP_CHAR stringified; declared here for convenience. */
    private static final String ROW_SEP_STR     = String.valueOf( ROW_SEP_CHAR );
    /** 
     * Character to repeat to generate a table separator
     * for the table of grids.
     */
    private static final char   TABLE_SEP_CHAR  = '\u2584';
    /** ROW_SEP_CHAR stringified; declared here for convenience. */
    private static final String TABLE_SEP_STR   = String.valueOf( TABLE_SEP_CHAR );
    
    /** The number of columns in the generated table. */
    private final int               numCols;
    /** String for formatting column separator. */
    private final String            colSep;
    /** String for formatting row separator. */
    private final String            rowSep;
    /** String for formatting table separator. */
    private final String            tableSep;
    /** Height of a grid in the table, in lines. */
    private final int               gridHeight;
    /** Width of a grid in the table, in characters. */
    private final int               gridWidth;
    /** Width of the table, in characters. */
    private final int               tableWidth;
    /** List of all grids in the table */
    private final List<Grid2D>      allGrids;
    
    /**
     * Creates a TextGridParent using the given display width.
     * 
     * @param displayWidth  the given display width
     * 
     * @return  the created TextGridParent
     */
    public static TextGridParent ofDisplayWidth( int displayWidth )
    {
        TextGridParent  parent  = new TextGridParent( displayWidth, -1 );
        return parent;
    }
        
    /**
     * Creates a TextGridParent with the given number of columns.
     * 
     * @param numCols   the given number of columns
     * 
     * @return  the created TextGridParent
     */
    public static TextGridParent ofNumCols( int numCols )
    {
        TextGridParent  parent  = new TextGridParent( DEF_DISP_WIDTH, numCols );
        return parent;
    }
    
    /**
     * Default constructor.
     * Instantiates this object assuming the default display width.
     * 
     * @see #ofDisplayWidth(int)
     * @see #ofNumCols(int)
     */
    public TextGridParent()
    {
        this( DEF_DISP_WIDTH, -1 );
    }
    
    /**
     * Constructor.
     * Sets all formatting parameters
     * based on the given display width or column count.
     * If the column count is greater than 0,
     * the display width is ignored.
     * 
     * @param dispWidth the given display width
     * @param colCount  
     *      the explicit number of columns, or -1 to 
     *      determine the number of columns from dispWidth
     */
    private TextGridParent( int dispWidth, int colCount )
    {
        allGrids = getAllGrids();
        colSep = " " + COL_SEP_STR.repeat( 1 ) + " ";
        int             colSepLen       = colSep.length();
        List<GridLines> allGridLines    = getAllGridLines( allGrids );
        gridWidth = validateGridWidth( allGridLines );
        gridHeight = validateGridHeight( allGridLines );
        
        numCols = colCount > 0 ?
            colCount :
            Math.max( 1, (dispWidth + colSepLen) / (gridWidth + colSepLen) );
        tableWidth = numCols * gridWidth + (numCols - 1) * colSepLen;
        rowSep = ROW_SEP_STR.repeat( tableWidth );
        tableSep = TABLE_SEP_STR.repeat( tableWidth );
    }
    
    /**
     * Formats the table of grids.
     * Returns a list of strings
     * describing the table,
     * one per line.
     * 
     * @return  the formatted table of grids
     */
    public List<String> exec()
    {
        int     nextGrid    = 0;
        List<GridLines> allGridLines    = getAllGridLines( allGrids );
        int     numGrids    = allGridLines.size();
        List<String>    allTables   = new ArrayList<>();
        while ( nextGrid < numGrids )
        {
            int fromGrid    = nextGrid;
            nextGrid = (int)Math.min( nextGrid + numCols, numGrids );
            List<String>    table   = 
                layout( allGridLines.subList( fromGrid, nextGrid ) );
            allTables.addAll( table );
            if ( nextGrid < numGrids )
                allTables.add( rowSep );
        }
        allTables.add( tableSep );
        return allTables;
    }
    
    /**
     * Creates one row of the table from the given list of grids.
     * 
     * @param list  the given list of grids
     * 
     * @return  the formatted row
     */
    private List<String> layout( List<GridLines> list )
    {
        Objects.requireNonNull( list, "list" );
        if ( list.isEmpty() )
            throw new BattleshipException( "cannot process empty list" );
        
        int             numCols     = list.size();
        int             lastCol     = numCols - 1;
        List<String>    table       = new ArrayList<>();
        for ( int row = 0 ; row < gridHeight ; ++row )
        {
            StringBuilder   rowBuilder  = new StringBuilder();
            for ( int col = 0 ; col < numCols ; ++col )
            {
                GridLines   grid    = list.get( col );
                String      line    = grid.getLine( row );
                int         lineLen = GridLines.visibleLineLength( line );
                int         padding = gridWidth - lineLen;
                rowBuilder.append( line );
                rowBuilder.append( " ".repeat( padding ) );
                if ( col != lastCol )
                    rowBuilder.append( colSep );
            }
            table.add( rowBuilder.toString() );
        }
        return table;
    }
    
    /**
     * Verifies that all the grids in the table are the same height.
     * Returns the verified height.
     * 
     * @param list  list of grids to verify
     * 
     * @return  the verified grid height
     * 
     * @throws BattleshipException if verification fails
     */
    private static int validateGridHeight( List<GridLines> list )
    {
        int     numLines    = list.getFirst().getNumLines();
        int     testLines   =
            list.stream()
                .mapToInt( GridLines::getNumLines )
                .filter( l -> l != numLines )
                .findFirst().orElse( numLines );
        if ( testLines != numLines )
            throw new BattleshipException( "grid length not consistent" );
        return numLines;
    }
    
    /**
     * Verifies that all the grids in the table are the same width.
     * Returns the verified width.
     * 
     * @param list  list of grids to verify
     * 
     * @return  the verified grid width
     * 
     * @throws BattleshipException if verification fails
     */
    private static int validateGridWidth( List<GridLines> list )
    {
        int     maxWidth    = list.getFirst().getMaxLineLen();
        int     testWidth   =
            list.stream()
                .mapToInt( GridLines::getMaxLineLen )
                .filter( w -> w != maxWidth )
                .findFirst().orElse( maxWidth );
        if ( testWidth != maxWidth )
            throw new BattleshipException( "grid width not consistent" );
        return maxWidth;
    }
    
    /**
     * Gets a list of all currently declared grids,
     * ensuring that the home grid is first in the list.
     * 
     * @return  a list of all currently declared grids
     */
    private static List<Grid2D> getAllGrids()
    {
        Grid2D          homeGrid    = Grid2D.getHomeGrid();
        List<Grid2D>    grids       = new ArrayList<>( Grid2D.getAllGrids() );
        grids.remove( homeGrid );
        grids.addFirst( homeGrid );
        return grids;
    }
    
    /**
     * Gets a list of all currently declared grids,
     * ensuring that the home grid is first in the list.
     * 
     * @param allGrids  a list of all declared grids, home grid first
     * 
     * @return  a list of GridLines corresponding to currently declared grids
     */
    private static List<GridLines> getAllGridLines( List<Grid2D> allGrids )
    {
        List<GridLines> gridLines   =
            allGrids.stream()
            .map( GridLines::new )
            .toList();
        return gridLines;
    }
    
    /**
     * Formats a grid as a list of strings,
     * one string per line.
     */
    private static class GridLines
    {
        /** The strings describing the grid. */
        private final List<String>  gridLines;
        
        /**
         * Constructor.
         * Creates a textual representation of a given grid.
         * 
         * @param grid  the given grid; may not be null
         */
        public GridLines( Grid2D grid )
        {
            Objects.requireNonNull( grid, "grid" );
            gridLines = new BasicTextGrid( grid ).toPrintString();
        }
        
        /**
         * Gets a line from the list of lines.
         * 
         * @param num   the index to the line to get
         * 
         * @return  the line at the given index
         */
        public String getLine( int num )
        {
            String  line   = gridLines.get( num );
            return line;
        }
        
        /**
         * Gets the number of lines constituting the grid.
         * 
         * @return  the number of lines constituting the grid
         */
        public int getNumLines()
        {
            return gridLines.size();
        }
        
        /**
         * Gets the length of the longest line in the grid.
         * 
         * @return  the length of the longest line in the grid
         */
        public int getMaxLineLen()
        {
            int maxLen  =
                gridLines.stream()
                    .mapToInt( s -> visibleLineLength( s ) )
                    .max().orElse( 0 );
            return maxLen;
        }
        
        /**
         * Get the number of characters in a given string
         * after stripping the ANSI escape sequences.
         * 
         * @param escapedStr    the given string
         * 
         * @return  the number of visible characters in escapedStr
         */
        private static int visibleLineLength( String escapedStr )
        {
            final String ansiRegex = "\\u001B\\[[;0-9]*[a-zA-Z]";
            String  visibleChars    = escapedStr.replaceAll( ansiRegex, "" );
            int     visibleCount    = visibleChars.length();
            return visibleCount;
        }
    }
}
