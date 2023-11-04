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
 * about the spacing
 * between two lines
 * of the same property,
 * for example the spacing
 * between grid lines.
 * The feedback
 * consists of a two vertical lines
 * separated by the space,
 * in pixels,
 * provided by the user.
 * When instantiating this component
 * the user provides a <em>Supplier&lt;Number&gt;</em>
 * which supplies
 * the desired spacing value.
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
public class SpacingFeedback extends JComponent
{
    /** The default background color for this component. */
    private static final Color  defBackground   = Color.WHITE;
    /** The default foreground color for this component. */
    private static final Color  defForeground   = Color.BLACK;
    /** The default weight for this component. */
    private static final float  defWeight       = 3;
    /** 
     * The proportion of the height of the component that
     * the vertical feedback lines should occupy.
     */
    private static final float  percentHeight   = .5f;
    
    /** Contains the coordinates of the left vertical feedback line. */
    private final Line2D            left    = new Line2D.Double();
    /** Contains the coordinates of the right vertical feedback line. */
    private final Line2D            right   = new Line2D.Double();
    /** Supplies the desired length of the horizontal feedback line. */
    private final DoubleSupplier    spacingSupplier;
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
    public SpacingFeedback( DoubleSupplier valueSource )
    {
        spacingSupplier = valueSource;
        setBackground( defBackground );
        setForeground( defForeground );
        setWeight( defWeight );
        setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
    }

    /**
     * Sets the weight
     * of the horizontal feedback lines
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
    
    /**
     * Gets the weight
     * of the vertical feedback lines.
     * 
     * @return  the weight of the vertical feedback lines
     */
    public float getWeight()
    {
        float   weight  = ((BasicStroke)stroke).getLineWidth();
        return weight;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx             = (Graphics2D)graphics.create();
        int         width           = getWidth();
        int         height          = getHeight();
        double      centerXco       = width / 2.;
        double      centerYco       = height / 2.;
        double      yOffset         = (height * percentHeight) / 2.;
        double      xOffset         = spacingSupplier.getAsDouble() / 2.;
        double      xcoLeft         = centerXco - xOffset;
        double      xcoRight        = centerXco + xOffset;
        double      ycoTop          = centerYco - yOffset;
        double      ycoBottom       = centerYco + yOffset;
        
        gtx.setColor( getBackground() );
        gtx.fillRect( 0, 0, width, height );
        
        gtx.setColor( getForeground() );
        gtx.setStroke( stroke );
        left.setLine( xcoLeft, ycoTop, xcoLeft, ycoBottom );
        right.setLine( xcoRight, ycoTop, xcoRight, ycoBottom );
        gtx.draw( left );
        gtx.draw( right );
        gtx.dispose();
    }
}
