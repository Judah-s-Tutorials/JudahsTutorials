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
 * about the weight (stroke)
 * of a line.
 * The feedback
 * consists of a horizontal line
 * drawn with a weight
 * provided by the user.
 * When instantiating this component
 * the user provides a <em>Supplier&lt;Number&gt;</em>
 * which supplies
 * the desired weight of the line.
 * The <em>Supplier</em> is consulted
 * each time this component's
 * <em>paintComponent</em> method is invoked.
 * To force the <em>paintComponent</em> method
 * to be invoked,
 * call this component's 
 * <em>repaint</em> method.
 * <p>
 * The length  
 * of the horizontal line
 * is calculated
 * as a percentage
 * of the component's width.
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
public class StrokeFeedback extends Feedback
{
    /** 
     * The percentage of the width of the component
     * occupied by the horizontal feedback line.
     */
    public static final float   percentWidth    = .75f;
    
    /** Contains the coordinates of the horizontal feedback line. */
    private final Line2D            line    = new Line2D.Double();
    /** Supplies the desired weight of the horizontal feedback line. */
    private final DoubleSupplier    weightSupplier;
    
    /**
     * Constructor.
     * Determines the source
     * for the weight 
     * of the horizontal feedback line.
     * 
     * @param valueSource   
     *      source for the weight of the horizontal feedback line
     */
    public StrokeFeedback( DoubleSupplier valueSource )
    {
        super();
        weightSupplier = valueSource;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx         = (Graphics2D)graphics.create();
        int         width       = getWidth();
        int         height      = getHeight();
        double      weight      = weightSupplier.getAsDouble();
        double      centerXco   = width / 2d;
        double      yco         = height / 2;
        double      xcoOffset   = (width * percentWidth) / 2;
        double      xco1        = centerXco - xcoOffset;
        double      xco2        = centerXco + xcoOffset;
        
        gtx.setColor( getBackground() );
        gtx.fillRect( 0, 0, width, height );
        
        Stroke      stroke      = new BasicStroke( (float)weight );
        gtx.setColor( getForeground() );
        gtx.setStroke( stroke );
        line.setLine( xco1, yco, xco2, yco );
        gtx.draw( line );
        gtx.dispose();
    }
}
