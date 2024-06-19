package com.acmemail.judah.cartesian_plane.blog_support;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This application shows how to computer
 * the axes of a grid
 * encapsulated in a bounding rectangle.
 * <img 
 *     src="doc-files/AxisPositioning.png" 
 *     alt="Axis Positioning Demo"
 *     style="float:left; width:10%; height:auto; margin-right: 1em;"
 * >
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class NonAxialPositioning extends JPanel
{
    /** The width of the application window. */
    private static final int            winWidth    = 700;
    /** The height of the application window. */
    private static final int            winHeight   = 200;
    /** The color of the application window. */
    private static final Color          winColor    = Color.WHITE;
    /** The color of the axes. */
    private static final Color          axisColor   = 
        new Color( 0xFF474C );
    /** The color of non-axial lines drawn at unit coordinates. */
    private static final Color          unitColor   = Color.BLACK;
    /** The color of non-axial lines drawn at non-unit coordinates. */
    private static final Color          lineColor   = 
        new Color( 0x00834e );
    /** The color of the text. */
    private static final Color          textColor   = Color.BLACK;
    /** The stroke used to draw axes in figure. */
    private static final Stroke         axisStroke  = 
        new BasicStroke( 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL );
    /** The stroke used to draw lines at unit coordinates. */
    private static final Stroke         unitStroke  = 
        new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL );
    /** The stroke used to draw lines at line coordinates. */
    private static final Stroke         lineStroke  = 
        new BasicStroke( 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL );
    /** Pixels per unit. */
    private static final double         gpu         = 150;
    /** Lines per unit. */
    private static final double         lpu         = 2;
    /** Spacing between lines. */
    private static final double         spacing     = gpu / lpu;
    
    /**
     * The length of the lines that delimit the dimension properties,
     * for example width: ------- 150 -------. 
     * Specified as a percentage of the dimension
     * of the bounding rectangle being tagged.
     */
    private static final double         dimLinePC   = .33;
    
    /** X-coordinate of the bounding rectangle. */
    private static final double         ulcXco      = 20;
    /** Y-coordinate of the bounding rectangle. */
    private static final double         ulcYco      = 50;
    /** Width of the bounding rectangle. */
    private static final int            rectWidth   = 650;
    /** Height of the bounding rectangle. */
    private static final int            rectHeight  = 100;
    /** Fill color of the bounding rectangle. */
    private static final Color          rectColor   = 
        new Color( 0xefefef );
    /** The bounding rectangle. */
    private static final Rectangle2D    rect        = 
        new Rectangle2D.Double( ulcXco, ulcYco, rectWidth, rectHeight );
    /** The x-coordinate of the origin. */
    private static final double         midXco      = rect.getCenterX();
    /** The y-coordinate of the origin. */
    private static final double         midYco      = rect.getCenterY();
    /** The x-coordinate of the left of the bounding rectangle. */
    private static final double         leftXco     = ulcXco;
    /** The x-coordinate of the right of the bounding rectangle. */
    private static final double         rightXco    = leftXco + rectWidth;
    /** The y-coordinate of the top of the bounding rectangle. */
    private static final double         topYco      = ulcYco;
    /** The y-coordinate of the bottom of the bounding rectangle. */
    private static final double         bottomYco   = topYco + rectHeight;
    /** Offset for non-axial lines above and below x-axis */
    private static final double         yOffset     = rectHeight / 4;
    
    /** All vertical lines. */
    private final List<Line2D>  allLines    = new LinkedList<>();
    
    /** Font for drawing coordinates on lines. */
    private final Font          lineFont;
    /** Font for drawing all other text. */
    private final Font          textFont;
    /** Copy of the graphics context. Initialized in paintComponent. */
    private Graphics2D          gtx;
    /** Context for position coordinates. */
    private FontRenderContext   frc;
    
    /**
     * Application entry point.
     * 
     * @param args  Command line arguments, not used.
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> {
            JFrame  frame   = new JFrame( "Axes Positioning Figure" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
            frame.setLocation( 100, 100 );
            frame.setContentPane( new NonAxialPositioning() );
            frame.pack();
            frame.setVisible( true );
        });
    }
    
    /**
     * Constructor.
     * Sets the size of the application window.
     */
    public NonAxialPositioning()
    {
        Dimension   dim     = new Dimension( winWidth, winHeight );
        setPreferredSize( dim );
        lineFont = getFont().deriveFont( Font.PLAIN );
        textFont = getFont().deriveFont( Font.ITALIC );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        gtx = (Graphics2D)graphics.create();
        gtx.setFont( lineFont );
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( winColor );
        gtx.fillRect( 0, 0, getWidth(), getHeight() );
        gtx.setColor( rectColor );
        gtx.fill( rect );

        gtx.setFont( lineFont );
        gtx.setColor( axisColor );
        gtx.setStroke( axisStroke );
        drawAxes();

        gtx.setColor( unitColor );
        gtx.setStroke( unitStroke );
        drawUnitLines();
        
        gtx.setColor( lineColor );
        gtx.setStroke( lineStroke );
        drawLines();
        
        gtx.setFont( textFont );
        gtx.setColor( textColor );
        drawDistances();
        drawLegend();
        
        gtx.dispose();
    }
    
    private void drawAxes()
    {
        Line2D  lineV   =
            new Line2D.Double( midXco, topYco, midXco, bottomYco );
        allLines.add( lineV );
        addLabel( lineV );
        gtx.draw( lineV );
        Line2D  lineH   =
            new Line2D.Double( leftXco, midYco, rightXco, midYco );
        gtx.draw( lineH );
    }
    
    private void drawUnitLines()
    {
        double      xco = 0;
        for ( xco = midXco + gpu ; xco < rightXco ; xco += gpu )
        {
            Line2D  line    = 
                new Line2D.Double( xco, topYco, xco, bottomYco );
            gtx.draw( line );
            addLabel( line );
        }
        for ( xco = midXco - gpu ; xco > leftXco ; xco -= gpu )
        {
            Line2D  line    = 
                new Line2D.Double( xco, topYco, xco, bottomYco );
            gtx.draw( line );
            addLabel( line );
        }
    }
    
    private void drawLines()
    {
        double      xco     = 0;
        double      yco1    = midYco - yOffset;
        double      yco2    = midYco + yOffset;
        for ( xco = midXco + spacing ; xco < rightXco ; xco += spacing )
        {
            boolean drawLabel   = ((xco - midXco) % gpu) != 0;
            Line2D  line    = 
                new Line2D.Double( xco, yco1, xco, yco2 );
            gtx.draw( line );
            allLines.add( line );
            if ( drawLabel )
                addLabel( line );
        }
        for ( xco = midXco - spacing ; xco > leftXco ; xco -= spacing )
        {
            boolean drawLabel   = ((midXco - xco) % gpu) != 0;
            Line2D  line    = 
                new Line2D.Double( xco, yco1, xco, yco2 );
            gtx.draw( line );
            allLines.add( 0, line );
            if ( drawLabel )
            {
                addLabel( line );
            }
        }
    }
    
    private void addLabel( Line2D line )
    {
        double      xco         = line.getX1();
        String      label       = String.format( "x=%.0f", xco );
        TextLayout  layout      = new TextLayout( label, lineFont, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      textXco     = xco - bounds.getWidth() / 2;
        double      textYco     = line.getY2() + bounds.getHeight();
        layout.draw( gtx, (float)textXco, (float)textYco );
    }
    
    private void drawDistances()
    {
        int     last    = allLines.size() - 1;
        for ( int inx = 0 ; inx < last ; ++inx )
        {
            double      left    = allLines.get( inx ).getX1();
            double      right   = allLines.get( inx + 1 ).getX1();
            double      mid     = left + (right - left) / 2;
            String      label       = 
                String.format( "%.0f pixels", spacing );
            TextLayout  layout      = new TextLayout( label, textFont, frc );
            Rectangle2D bounds      = layout.getBounds();
            double      textXco     = mid - bounds.getWidth() / 2;
            double      textYco     = midYco + 2 * bounds.getHeight();
            layout.draw( gtx, (float)textXco, (float)textYco );
        }
    }
    
    private void drawLegend()
    {
        String      label       = 
            String.format( "GPU: %.0f pixels", gpu );
        drawLegend( label, 2 );
        label = String.format( "LPU: %.0f", lpu );
        drawLegend( label, 1 );
        label = String.format( "Spacing: %.0f pixels", spacing );
        drawLegend( label, 0 );
    }
    
    private void drawLegend( String label, int line )
    {
        TextLayout  layout      = new TextLayout( label, textFont, frc );
        Rectangle2D bounds      = layout.getBounds();
        double      textXco     = rightXco - bounds.getWidth() - 5;
        double      textYco     = topYco - line * (bounds.getHeight() + 2);
        layout.draw( gtx, (float)textXco, (float)textYco );
    }
}
