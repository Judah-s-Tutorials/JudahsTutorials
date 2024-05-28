package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Graphics;

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
public abstract class Feedback extends JComponent
{
    /** The default background color for this component. */
    private static final Color  defBackground   = Color.WHITE;
    /** The default foreground color for this component. */
    private static final Color  defForeground   = Color.BLACK;
    /** The default weight for this component. */
    private static final float  defWeight       = 3;
    
    /** 
     * The weight of lines drawn in this component.
     * Not used by all subclasses.
     */
    private float   weight  = defWeight;
    
    /**
     * Constructor.
     * Determines the source
     * for the length 
     * of the horizontal feedback line.
     */
    public Feedback()
    {
        setBackground( defBackground );
        setForeground( defForeground );
        setWeight( defWeight );
        setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
    }

    /**
     * Sets the weight
     * of the lines drawn by this component
     * to the given value.
     * Automatically calls
     * this component's repaint method.
     * 
     * @param weight    the given value
     */
    public void setWeight( float weight )
    {
        this.weight = weight;
    }
    
    /**
     * Gets the weight
     * of the lines
     * drawn by this component.
     * 
     * @return  the weight feedback lines
     */
    public float getWeight()
    {
        return weight;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
    }
    
    @Override
    public boolean isOpaque()
    {
        return true;
    }
}
