package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_END;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_START;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Interrogate a sequence of lines to discover a textual grid.
 * <p>
 * If a line in the sequence contains CONFIG_GRID_START
 * the following line is determined to the first line of the grid,
 * otherwise 0 is treated as the first line.
 * <p>
 * If a line in the sequence contains CONFIG_GRID_END
 * the previous line is determined to the last line of the grid,
 * otherwise the last line in the sequence 
 * is treated as the last line.
 * <p>
 * If grid-end is less than or equal to grid start,
 * the grid is presumed to be empty.
 * <p>
 * The grid name is assumed to be on the first line of the grid;
 * if the grid is empty,
 * the name is set to the empty string.
 * <p>
 * The first line of the column header 
 * is the first line in the grid 
 * to contain at least one digit,
 * and the last is the last line in the grid
 * to contain at least one digit.
 * If no lines contain digits,
 * the list of column headers will be empty.
 * <p>
 * The first line of the first row
 * is assumed to be the line after
 * the last line of the column header.
 * <p>
 * A labeled-row is a line of text that begins with
 * 0 or more spaces followed by a character
 * in the range A-Z.
 * Note that a row can contain more than one line;
 * lines that don't match the above criteria
 * are ignored.
 * The search for labeled rows
 * begins with the first line
 * following the column header;
 * if no column header is found
 * the search begins at line 0.
 */
public class TextGridParser
{
    private final List<String>  input;
    private final List<String>  grid;
    private final String        name;
    private final List<String>  columnHeader;
    private final List<String>  labeledRows;
    
    public TextGridParser( List<String> lines )
    {
        Predicate<String>   isColumnHeader  = s -> s.matches( ".*\\d+.*" );
        Predicate<String>   isLabeledRow    = 
            s -> s.matches( "^\\s*[A-Z]+.*" );
        
        input = lines;
        int     inputSize       = input.size();
        
        int     beginGrid   =
            IntStream.range( 0, inputSize )
                .filter( i -> input.get( i ).contains( CONFIG_GRID_START ) )
                .findFirst().orElse( -1 ) + 1;
        int     endGrid     =
            IntStream.range( 0, inputSize )
                .filter( i -> input.get( i ).contains( CONFIG_GRID_END ) )
                .findFirst().orElse( inputSize );
        
        int     beginColHeader  =
            IntStream.range( 0, inputSize )
            .filter( i -> isColumnHeader.test( input.get( i ) ) )
            .findFirst().orElse( 0 );
        int     endColHeader    =
            IntStream.range( beginColHeader, inputSize )
            .filter( i -> !isColumnHeader.test( input.get( i ) ) )
            .findFirst().orElse( 0 );
        
        columnHeader = endColHeader > beginColHeader ?
            input.subList( beginColHeader, endColHeader ) : 
            Collections.emptyList();
        
        grid = endColHeader > 0 ?
            input.subList( beginGrid, endGrid ) : 
            Collections.emptyList();
        
        labeledRows = endGrid > endColHeader ?
            IntStream.range( endColHeader, endGrid )
                .mapToObj( input::get )
                .filter( isLabeledRow::test )
                .toList()
            : Collections.emptyList();

        name = beginColHeader > 0 ? input.get( beginColHeader - 1 ) : "";
    }

    /**
     * @return the input
     */
    public List<String> getInput()
    {
        return input;
    }

    /**
     * @return the grid
     */
    public List<String> getGrid()
    {
        return grid;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the columnHeader
     */
    public List<String> getColumnHeader()
    {
        return columnHeader;
    }

    /**
     * @return the labeledRows
     */
    public List<String> getLabeledRows()
    {
        return labeledRows;
    }
    
    
}