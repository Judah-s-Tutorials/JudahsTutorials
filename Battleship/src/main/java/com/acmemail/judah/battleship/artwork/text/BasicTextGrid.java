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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.StatusMessages;
import com.acmemail.judah.battleship.model.Cell2DView;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;

public class BasicTextGrid
{
    public static final int MAX_ROWS    = 99;
    public static final int MAX_COLS    = 99;
    
    private static final String     fgShipColor     = FG_CYAN;
    private static final String     bgShipColor     = BG_CYAN;
    private static final String     splatColorShip  = FG_RED;
    private static final String     splatColorEmpty = FG_YELLOW;
    private static final char       splatCharShip   = SOLID_BLOCK;
    private static final char       splatCharEmpty  = SOLID_BLOCK;
    
    private final Grid2D    grid2D;
    private final int       numRows;
    private final int       numCols;
    private final int       cellWidth;
    
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
    
    public void printGrid()
    {
        System.out.print( CLEAR_SCR );
        toPrintString().forEach( System.out::println );
    }
    
    private List<String> header()
    {
        List<String>    header  = List.of( grid2D.getName() );
        return header;
    }
    
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
