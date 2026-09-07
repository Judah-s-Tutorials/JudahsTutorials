package com.acmemail.judah.battleship.artwork.awt.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.artwork.awt.GridWindow;
import com.acmemail.judah.battleship.model.Grid2D;

public class GridProber
{
    public static void main( String[] args )
    {
        Grid2D          grid    = new Grid2D();
        GridWindow      window  = new GridWindow( grid );
        Dimension       dim     = window.getPreferredSize();
        int             type    = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   image   = new 
            BufferedImage( dim.width, dim.height, type );
        window.paintComponent( image.getGraphics() );
        GridProperties  props   = new GridProperties( image, grid );
    }
    
    private static class GridProperties
    {
        public BufferedImage    gridImage;
        public Grid2D           logicalGrid;
        public int              numCols;
        public int              numRows;
        public int              imageWidth;
        public int              imageHeight;
        public Color            backgroundColor;
        public Color            gridlineColor;
        public int              gridlineWidth;
        public int              minCellSide;
        public int              maxCellSide;
        public int              leftMargin;
        public int              rightMargin;
        public int              topMargin;
        public int              bottomMargin;
        
        public GridProperties( BufferedImage gridImage, Grid2D logicalGrid )
        {
            this.gridImage = gridImage;
            this.logicalGrid = logicalGrid;
            imageWidth = gridImage.getWidth();
            imageHeight = gridImage.getHeight();
            numCols = logicalGrid.getNumRows();
            numRows = logicalGrid.getNumCols();
            maxCellSide = imageWidth / numCols;
            getGridColors();
        }
        
        private void getGridColors()
        {
            int     maxXco      = imageWidth - 1;
            int     minXco      = imageWidth / 2;
            int     centerYco   = imageHeight / 2;
            int     minYco      = centerYco - maxCellSide;
            int     maxYco      = centerYco + maxCellSide;
            int     numLines    = 2 * maxCellSide;
            int     currLine    = 0;
            List<Segment>[] segLists = (List<Segment>[]) new List[numLines];
            for ( int yco = minYco ; yco < maxYco ; ++yco )
            {
                segLists[currLine] = new ArrayList<>();
                for ( int xco = maxXco ; xco > minXco ;  )
                {
                    int count       = 0;
                    int saveColor   = gridImage.getRGB( xco--, yco );
                    int nextColor   = saveColor;
                    do 
                    {
                        ++count;
                        nextColor = gridImage.getRGB( xco--, yco );
                    } while ( xco > minXco && saveColor == nextColor );
                    xco++;
                    segLists[currLine].add( new Segment( saveColor, count) );
                }
                currLine++;
            }
            getBackgroundColor( segLists );
            getGridlineProps( segLists );
        }
        
        private void getBackgroundColor( List<Segment>[] segs )
        {
            Segment baseLine    = segs[0].get( 0 );
            System.out.println( baseLine );
            boolean same0       = Arrays.stream( segs )
                .map( s -> s.get( 0 ) )
                .peek( System.out::println )
                .filter( s -> !s.equals( baseLine ) )
                .findAny().isEmpty();
            if ( !same0 ) 
            {
                String  msg = "segment[0] not consistent ";
                throw new BattleshipException( msg );
            }
            backgroundColor = new Color( baseLine.color );
        }
        
        private void getGridlineProps( List<Segment>[] segs )
        {
            List<Segment>   workingList = new ArrayList<>();
                Arrays.stream( segs )
                    .map( s -> s.get( 1 ) )
                    .filter( s -> !workingList.contains( s ) )
                    .forEach( workingList::add );
            int             size        = workingList.size();
            if ( size != 2 )
            {
                String  msg =
                    "Failed segment[1] filter; expect size = 2 was "
                    + "size = " + size;
                throw new BattleshipException( msg );
            }
            int     iColor  = workingList.get( 0 ).color();
            gridlineColor = new Color( iColor );
            workingList.sort( (s1,s2) -> s1.extent() - s2.extent() );
            gridlineWidth = workingList.get( 0 ).extent();
        }
    }
    
    private record Segment( int color, int extent )
    {
        
    }
}
