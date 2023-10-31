package com.acmemail.judah.cartesian_plane.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.function.DoubleSupplier;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * This component
 * provides visual feedback
 * about the selected length
 * of a line.
 * The feedback
 * consists of a horizontal line
 * drawn to a length
 * provided by the user.
 * When instantiating this component
 * the user provides a <em>Supplier&lt;Number&gt;</em>
 * which supplies
 * the desired length of the line.
 * The <em>Supplier</em> is consulted
 * each time this component's
 * <em>paintComponent</em> method is invoked.
 * To force the <em>paintComponent</em> method
 * to be invoked,
 * call this component's 
 * <em>repaint</em> method.
 * <p>
 * The weight 
 * of the horizontal line
 * can be changed
 * via the component's
 * <em>setWeight</em> method.
 * The foreground and background colors
 * can be set
 * via the superclass's
 * <em>setForeground</em> and <em>setBackground</em>
 * methods.
 * If necessary,
 * set the component's width and height
 * via the superclass's
 * <em>sePreferredSize</em> method.
 *</p>
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class LengthFeedback extends JComponent
{
    /** The default background color for this component. */
    private static final Color  defBackground   = Color.WHITE;
    /** The default foreground color for this component. */
    private static final Color  defForeground   = Color.BLACK;
    /** The default weight for this component. */
    private static final float  defWeight       = 3;
    
    /** Contains the coordinates of the horizontal feedback line. */
    private final Line2D            line    = new Line2D.Double();
    /** Supplies the desired length of the horizontal feedback line. */
    private final DoubleSupplier    lengthSupplier;
    /** Contains the weight of the horizontal feedback line. */
    private Stroke                  stroke;
    
    /**
     * Constructor.
     * Determines the source
     * for the length 
     * of the horizontal feedback line.
     * 
     * @param valueSource   
     *      source for the length of the horizontal feedback line
     */
    public LengthFeedback( DoubleSupplier valueSource )
    {
        lengthSupplier = valueSource;
        setBackground( defBackground );
        setForeground( defForeground );
        setWeight( defWeight );
        setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
    }

    /**
     * Sets the weight
     * of the horizontal feedback line
     * to the given value.
     * Automatically calls
     * this component's repaint method.
     * 
     * @param weight    the given value
     */
    public void setWeight( float weight )
    {
        stroke = new BasicStroke( weight );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx     = (Graphics2D)graphics.create();
        int         width   = getWidth();
        int         height  = getHeight();
        double      length  = lengthSupplier.getAsDouble();
        double      xco1    = (width - length) / 2;
        double      xco2    = xco1 + length;
        double      yco     = height / 2;
        
        gtx.setColor( getBackground() );
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setColor( getForeground() );
        gtx.setStroke( stroke );
        line.setLine( xco1, yco, xco2, yco );
        gtx.draw( line );
        gtx.dispose();
    }
    
    @Override
    public boolean isOpaque()
    {
        return true;
    }
}
