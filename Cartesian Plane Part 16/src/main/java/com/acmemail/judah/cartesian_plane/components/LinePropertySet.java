package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.util.Optional;

import javax.swing.JComponent;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * An object of this class
 * manages properties of a line,
 * such as length, width and color.
 * Each property 
 * is directly associated
 * with a property
 * of the CartesianPlane
 * which is managed by
 * the {@linkplain PropertyManager} class,
 * and named in the 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants} class.
 * For example,
 * the <em>Color</em> property
 * may correspond to the major tic color 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#TIC_MAJOR_LEN_PN},
 * the minor tic color 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#TIC_MINOR_LEN_PN}
 * or the grid line color
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#GRID_LINE_COLOR_PN}.
 * If a category of line
 * doesn't have a named property;
 * grid lines, for example,
 * don't have a length;
 * the provided property name
 * should be the empty String.
 * The encapsulated properties are:
 * <ul>
 * <li>
 * Draw: a Boolean property
 * that determines
 * whether an entire class of lines
 * should be drawn at all,
 * for example 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#GRID_LINE_DRAW_PN }.
 * </li>
 * <li>
 * Stroke: a float property
 * that determines
 * the width of a line,
 * for example 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#GRID_LINE_WEIGHT_PN }.
 * </li>
 * <li>
 * Length: a float property
 * that determines
 * the length of a line,
 * for example 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#TIC_MINOR_LEN_PN }.
 * </li>
 * <li>
 * Spacing: a float property
 * that determines
 * the spacing between lines,
 * for example 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#TIC_MAJOR_MPU_PN }.
 * </li>
 * <li>
 * Color: a property
 * that determines
 * the color of a line,
 * for example 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants#AXIS_COLOR_PN }.
 * </li>
 * </ul>
 * <p>
 * The correspondence between encapsulated properties
 * (draw, stroke, length, etc.)
 * and PropertyManager properties
 * (e.g. CPConstants.AXIS_COLOR_PN)
 * is established by subclasses
 * via the class constructor.
 * See, for example, {@linkplain LinePropertySetTicMajor}.
 * </p>
 * @author Jack Straub
 * 
 * @see com.acmemail.judah.cartesian_plane.PropertyManager
 * @see com.acmemail.judah.cartesian_plane.CPConstants
 */
public abstract class LinePropertySet
{
    public static final  String TYPE_KEY    = "judah.property_type";
    
    private final String    drawProperty;
    private final String    strokeProperty;
    private final String    lengthProperty;
    private final String    spacingProperty;
    private final String    colorProperty;

	private final PropertyManager	pMgr	= PropertyManager.INSTANCE;
	private Optional<Boolean>  draw;
    private Optional<Float>    stroke;
    private Optional<Float>    length;
    private Optional<Float>    spacing;
    private Optional<Color>    color;
    
    /**
     * Constructor.
     * Establishes correspondence
     * between encapsulated properties
     * and properties managed by 
     * {@linkplain com.acmemail.judah.cartesian_plane.PropertyManager}.
     * 
     * @param drawProperty      
     *      the PropertyManager name
     *      associated with the encapsulated <em>draw</em> property
     * @param strokeProperty    
     *      the PropertyManager name
     *      associated with the encapsulated <em>stroke</em> property
     * @param lengthProperty
     *      the PropertyManager name
     *      associated with the encapsulated <em>length</em> property
     * @param spacingProperty
     *      the PropertyManager name
     *      associated with the encapsulated <em>spacing</em> property
     * @param colorProperty
     *      the PropertyManager name
     *      associated with the encapsulated <em>color</em> property
     */
    public LinePropertySet(
        String drawProperty,
        String strokeProperty, 
        String lengthProperty, 
        String spacingProperty, 
        String colorProperty
    )
    {
        super();
        this.drawProperty = drawProperty;
        this.strokeProperty = strokeProperty;
        this.lengthProperty = lengthProperty;
        this.spacingProperty = spacingProperty;
        this.colorProperty = colorProperty;
        
        reset();
    }
    
    /**
     * Sets the encapsulated properties
     * via the PropertyManager.
     * 
     * @see #reset()
     */
    public void apply()
    {
        if ( hasDraw() )
            pMgr.setProperty( drawProperty, draw.get() );
        if ( hasStroke() )
            pMgr.setProperty( strokeProperty, stroke.get() );
        if ( hasLength() )
            pMgr.setProperty( lengthProperty, length.get() );
        if ( hasSpacing() )
            pMgr.setProperty( spacingProperty, spacing.get() );
        if ( hasColor() )
            pMgr.setProperty( colorProperty, color.get() );
    }
    
    /**
     * Sets all encapsulate properties
     * from values provided by
     * the PropertyManager.
     * Changes that have been made
     * but not applied,
     * are discarded.
     * 
     * @see #apply()
     */
    public void reset()
    {
        draw = asBoolean( drawProperty );
        stroke = asFloat( strokeProperty );
        length = asFloat( lengthProperty );
        spacing = asFloat( spacingProperty );
        color = asColor( colorProperty );
    }
    
    /**
     * Returns a Boolean value
     * indicating whether 
     * the draw property has been set.
     * 
     * @return  
     *      a Boolean value indicating whether 
     *      the draw property has been set
     */
    public boolean hasDraw()
    {
        return draw.isPresent();
    }
    
    /**
     * Returns a Boolean value
     * indicating whether 
     * the stroke property has been set.
     * 
     * @return  
     *      a Boolean value indicating whether 
     *      the stroke property has been set
     */
    public boolean hasStroke()
    {
        return stroke.isPresent();
    }
    
    /**
     * Returns a Boolean value
     * indicating whether 
     * the length property has been set.
     * 
     * @return  
     *      a Boolean value indicating whether 
     *      the length property has been set
     */
    public boolean hasLength()
    {
        return length.isPresent();
    }
    
    /**
     * Returns a Boolean value
     * indicating whether 
     * the spacing property has been set.
     * 
     * @return  
     *      a Boolean value indicating whether 
     *      the spacing property has been set
     */
    public boolean hasSpacing()
    {
        return spacing.isPresent();
    }
    
    /**
     * Returns a Boolean value
     * indicating whether 
     * the color property has been set.
     * 
     * @return  
     *      a Boolean value indicating whether 
     *      the color property has been set
     */
    public boolean hasColor()
    {
        return color.isPresent();
    }
    
    /**
     * Returns the draw property value,
     * or false if not present.
     * 
     * @return  
     *      the draw property value,
     *      or false if not present
     */
    public boolean getDraw()
    {
        return draw.orElse( false );
    }
    
    /**
     * Returns the stroke property value,
     * or -1 if not present.
     * 
     * @return  
     *      the stroke property value,
     *      or -1 if not present
     */
    public double getStroke()
    {
        return stroke.orElse( -1f );
    }
    
    /**
     * Returns the length property value,
     * or -1 if not present.
     * 
     * @return  
     *      the length property value,
     *      or -1 if not present
     */
    public double getLength()
    {
        return length.orElse( -1f );
    }
    
    /**
     * Returns the spacing property value,
     * or -1 if not present.
     * 
     * @return  
     *      the spacing property value,
     *      or -1 if not present
     */
    public double getSpacing()
    {
        return spacing.orElse( -1f );
    }
    
    /**
     * Returns the color property value,
     * or null if not present.
     * 
     * @return  
     *      the color property value,
     *      or null if not present
     */
    public Color getColor()
    {
        return color.orElse( null );
    }
    
    /**
     * Sets the draw property
     * to the given value
     * 
     * @param draw the given value
     */
    public void setDraw( boolean draw )
    {
        if ( hasDraw() )
            this.draw = Optional.of( draw );
    }
    
    /**
     * Sets the stroke property
     * to the given value
     * 
     * @param stroke the given value
     */
    public void setStroke( float stroke )
    {
        if ( hasStroke() )
            this.stroke = Optional.of( stroke );
    }
    
    /**
     * Sets the length property
     * to the given value
     * 
     * @param length the given value
     */
    public void setLength( float length )
    {
        if ( hasLength() )
            this.length = Optional.of( length );
    }
    
    /**
     * Sets the spacing property
     * to the given value
     * 
     * @param spacing the given value
     */
    public void setSpacing( float spacing )
    {
        if ( hasSpacing() )
            this.spacing = Optional.of( spacing );
    }
    
    /**
     * Sets the color property
     * to the given value
     * 
     * @param color the given value
     */
    public void setColor( Color color )
    {
        if ( hasColor() )
            this.color = Optional.of( color );
    }
    
    /**
     * Obtains and returns
     * the LinePropertySet instance
     * stored as a client property
     * in a given JComponent.
     * 
     * @param comp  the given JComponent
     * 
     * @return  
     *      the LinePropertySet instance
     *      stored in the given JComponent
     *      
     * @throws 
     *      ComponentException 
     *      if a LinePropertySet instance isn't found
     */
    public static LinePropertySet getPropertySet( JComponent comp )
    {
        Object  value   = comp.getClientProperty( TYPE_KEY );
        if ( value == null )
        {
            String  message = "Expected LinePropertySet, was null";
            throw new ComponentException( message );
        }
        if ( !(value instanceof LinePropertySet) )
        {
            String  message =
                "Expected LinePropertySet, was "
                + value.getClass().getName();
            throw new ComponentException( message );
        }
        return (LinePropertySet)value;
    }
    
    /**
     * Stores the given LinePropertySet instance
     * as a client property
     * in the given JComponent.
     * 
     * @param comp  the given JComponent
     * @param set   the given LinePropertySet
     */
    public static void putPropertySet( JComponent comp, LinePropertySet set )
    {
        comp.putClientProperty( TYPE_KEY, set );
    }
    
    /**
     * Convenience routine to get
     * a float property value
     * from the property manager.
     * If the property manager
     * successfully finds a value
     * for the property
     * it is wrapped in an Optional
     * and returned.
     * If the value is not found
     * an empty Optional
     * is returned.
     * 
     * @param propertyName  the name of the property to retrieve
     * 
     * @return
     *     if found, the value of the given property
     *     wrapped in an Optional;
     *     otherwise an empty Optional.
     */
    private Optional<Float> asFloat( String propertyName )
    {
        Float           val         = pMgr.asFloat( propertyName );
        Optional<Float> optional    = 
            val != null ? Optional.of( val ) : Optional.empty();
        return optional;
    }
    
    /**
     * Convenience routine to get
     * a Color property value
     * from the property manager.
     * If the property manager
     * successfully finds a value
     * for the property
     * it is wrapped in an Optional
     * and returned.
     * If the value is not found
     * an empty Optional
     * is returned.
     * 
     * @param propertyName  the name of the property to retrieve
     * 
     * @return
     *     if found, the value of the given property
     *     wrapped in an Optional;
     *     otherwise an empty Optional.
     */
    private Optional<Color> asColor( String propertyName )
    {
        Color           val         = pMgr.asColor( propertyName );
        Optional<Color> optional    = 
            val != null ? Optional.of( val ) : Optional.empty();
        return optional;
    }
    
    /**
     * Convenience routine to get
     * a Boolean property value
     * from the property manager.
     * If the property manager
     * successfully finds a value
     * for the property
     * it is wrapped in an Optional
     * and returned.
     * If the value is not found
     * an empty Optional
     * is returned.
     * 
     * @param propertyName  the name of the property to retrieve
     * 
     * @return
     *     if found, the value of the given property
     *     wrapped in an Optional;
     *     otherwise an empty Optional.
     */
    private Optional<Boolean> asBoolean( String propertyName )
    {
        Boolean             val         = pMgr.asBoolean( propertyName );
        Optional<Boolean>   optional    = 
            val != null ? Optional.of( val ) : Optional.empty();
        return optional;
    }
}
