package com.acmemail.judah.battleship.artwork;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import javax.swing.JPanel;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship2D.Cell2D;
import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Ship2D;

public class GraphicalGrid extends JPanel implements Artwork
{
    private static final long serialVersionUID = -8860690179464933270L;
    
    private static final Font   font            = new 
        Font( Font.SANS_SERIF, Font.PLAIN, 14 );
    private static final Color  bgColor         = new Color( 0xf5f5f5 );
    private static final Color  lineColor       = Color.BLACK;
//    private static final Color  hitColor        = Color.PINK;
    private static final Color  shipColor       = 
        new Color( .75f, .75f, .75f );
    private static final Color  deadColor       = Color.RED;
    private static final Color  validColor      = 
        new Color( 0x4d, 0x4d, 0x4d, 0xbf );
//    private static final Color  inValidcolor    = 
//        new Color( 0xFF, 0x74, 0x74, 0xbf );
    private static final Stroke gridStroke      = new BasicStroke( 1 );
    private static final Stroke shipStroke      = new BasicStroke( 3 );
    
    private Ship2D      ghostShip   = null;
    
    private Grid2D      grid        = null;
    private int         cellSide    = 15;
    private int         labelWidth  = 25;
    private int         labelHeight = 25;
    private int         margin      = 5;
    private int         numRows     = Grid2D.getNumRows();
    private int         numCols     = Grid2D.getNumCols();
    
    private int         width;
    private int         height;
    private FontMetrics fontMetrics;
    private Graphics2D  gtx;
    
    public GraphicalGrid( Grid2D grid )
    {
        this.grid = grid;
        int         prefWidth   = 
            2 * margin + numCols * cellSide + labelWidth;
        int         prefHeight  = 
            2 * margin + numRows * cellSide + labelHeight;
        Dimension   prefSize    = new Dimension( prefWidth, prefHeight );
        setPreferredSize( prefSize );
        addMouseMotionListener( new MouseMonitor() );
    }
    
    @Override
    public void update( Grid2D grid )
    {
        if ( grid != null )
            this.grid = grid;
        if ( this.grid == null )
        {
            String  message = "No grid to update";
            throw new BattleshipException( message );
        }
        repaint();
    }
    
    public Ship2D getGhostShip()
    {
        return ghostShip;
    }
    
    public void setGhostShip( Ship2D ship )
    {
        ghostShip = ship;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        width = getWidth();
        height = getHeight();
        gtx = (Graphics2D)graphics;
        
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0,  width, height );
        
        gtx.setFont( font );
        fontMetrics = gtx.getFontMetrics();
        Collection<Cell2D>  cells   = grid.getCells().toList();
        
        gtx.translate( margin, margin );
        fillCells( cells );
        drawCells( cells );
        drawRowLabels();
        drawColLabels();
        fillGhostShip();
    }
    
    private void drawCells( Collection<Cell2D> cells )
    {
        Rectangle2D rect    = new Rectangle2D.Double();
        gtx.setColor( lineColor );
        for ( Cell2D cell : cells )
        {
            GridCoords  coords  = cell.getCoords();
            int         gridXco = coords.xco();
            int         gridYco = coords.yco();
            int         xco     = gridXco * cellSide + labelWidth;
            int         yco     = gridYco * cellSide + labelHeight;
            Stroke      stroke  = cell.getShip() == null ? shipStroke : gridStroke;
            
            rect.setRect( xco, yco, cellSide, cellSide );
            gtx.setStroke( stroke );
            gtx.draw( rect );
        }
    }
    
    private void fillCells( Collection<Cell2D> cells )
    {
        Rectangle2D rect    = new Rectangle2D.Double();
        for ( Cell2D cell : cells )
        {
            GridCoords  coords  = cell.getCoords();
            int         gridXco = coords.xco();
            int         gridYco = coords.yco();
            int         xco     = gridXco * cellSide + labelWidth;
            int         yco     = gridYco * cellSide + labelHeight;
            Color       color   = bgColor;
            
            if ( cell.isSplatted() )
                color = deadColor;
            else
                color = shipColor;
            rect.setRect( xco, yco, cellSide, cellSide );
            gtx.setColor( color );
            gtx.fill( rect );
        }
    }
    
    private void drawRowLabels()
    {
        for ( int row = 0 ; row < numRows ; ++row )
        {
            String  label           = Label.decimalToAlpha( row );
            int     yco             = (row + 1) * cellSide + labelHeight;
            int     width           = fontMetrics.stringWidth( label );
            int     strXco          = labelWidth - width - 3;
            int     strYco          = yco - 3;
            gtx.drawString( label, strXco, strYco );
            gtx.drawLine( 0, yco, labelWidth, yco );
        }
    }
    
    private void drawColLabels()
    {
        AffineTransform origTransform   = gtx.getTransform();
        int yco = labelHeight;
        for ( int col = 0 ; col < numCols ; ++col )
        {
            String  label           = Label.intToString( col );
            int     xco             = col * cellSide + labelWidth;
            int     width           = fontMetrics.stringWidth( label );
            int     strXco          = xco + 3;// + cellSide / 2;
            int     strYco          = yco - width - 3;
            gtx.translate( strXco, strYco );
            gtx.rotate( Math.PI / 2 );
            gtx.drawString( label, 0, 0 );
            gtx.setTransform( origTransform );
            gtx.drawLine( xco + cellSide, 0, xco + cellSide, yco );
        }
    }
    
    private void fillGhostShip()
    {
        if ( ghostShip != null )
        {
            Rectangle2D     rect    = getGhostRect();
            gtx.setColor( validColor );
            gtx.fill( rect );
        }
    }
    
    private Rectangle2D getGhostRect()
    {
        Rectangle2D ghostRect   = new Rectangle2D.Double();
        if ( ghostShip != null )
        {
            Rectangle2D cellRect    = ghostShip.getBounds();
            double      xco         = 
                cellRect.getX() * cellSide + labelWidth;
            double      yco         = 
                cellRect.getY() * cellSide + labelHeight;
            double      width       = cellRect.getWidth() * cellSide;
            double      height      = cellRect.getHeight() * cellSide;
            ghostRect.setRect( xco, yco, width, height );
        }
        return ghostRect;
    }
    
    @SuppressWarnings("unused")
    private Cell2D getCell( Point2D point )
    {
        Cell2D  cell    = null;
        double  xco     = point.getX() - margin;
        double  yco     = point.getY() - margin;
        int     col     = (int)(xco / cellSide);
        int     row     = (int)(yco / cellSide);
        if ( col >= 0 && col < numCols
            && row >= 0 && row < numRows )
        {
            GridCoords  coords  = new GridCoords( col, row );
            cell = new Cell2D( coords );
        }
        return cell;
    }
    
    private class MouseMonitor extends MouseAdapter
    {
//        private Cell selectedCell   = null;
        
        @Override
        public void mousePressed( MouseEvent evt )
        {
            
        }
        
        @Override
        public void mouseMoved( MouseEvent evt )
        {
//            if ( ghostShip != null )
//            {
//                GridCoords  curr    = getGridCoords( evt.getX(), evt.getY() );
//                if ( !curr.equals( ghostShip.getFirstSquare() ) )
//                {
//                    ShipType    type        = ghostShip.getType();
//                    Orientation orientation = ghostShip.getOrientation();
//                    Ship        ship        = 
//                        new Ship( type, curr, orientation );
//                    setGhostShip( ship );
//                    update();
//                }
//            }
        }
        
//        private GridCoords getGridCoords( int pixelXco, int pixelYco )
//        {
//            int         xco     = (pixelXco - margin - labelWidth) / cellSide;
//            int         yco     = (pixelYco - margin - labelHeight) / cellSide;
//            GridCoords  coords  = new GridCoords( xco, yco );
//            return coords;
//        }
    }
}
