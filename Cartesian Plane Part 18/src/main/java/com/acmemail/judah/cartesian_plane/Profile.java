package com.acmemail.judah.cartesian_plane;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * The class provides a means
 * to collect a set of display properties
 * that can be applied to
 * a graph configuration.
 * The properties include:
 * <ol>
 *      <li>
 *          Grid unit (GRID_UNIT):
 *          the width, in pixels,
 *          of a single unit in a graph display.
 *          For example, 
 *          The distance between 0 and 1 on a graph.
 *      </li>
 *      </li>
 *          Main window properties: 
 *          these properties
 *          constitute a subset
 *          of the properties
 *          defined by {@linkplain GraphPropertySet}.
 *          They are:
 *          <ul>
 *              <li>
 *                  Background color (BG_COLOR):
 *                  the color of the surface of the graph.
 *              </li>
 *              <li>
 *                  Foreground color (FG_COLOR):
 *                  the color of the text
 *                  displayed on the graph.                  
 *              </li>
 *              <li>
 *                  Font name (FONT_NAME):
 *                  the name of the font
 *                  used to display text on the graph.
 *              </li>
 *              <li>
 *                  Font size (FONT_SIZE):
 *                  the size of the font
 *                  used to display text on the graph.
 *              </li>
 *              <li>
 *                  Font style (FONT_STYLE):
 *                  the style of the font
 *                  used to display text on the graph;
 *                  PLAIN or a combination of 
 *                  BOLD and ITALIC.
 *              </li>
 *          </ul>
 *      </li>
 *      <li>
 *          Line properties:
 *          these are the same properties
 *          collected in {@linkplain LinePropertySet},
 *          and are applied to 
 *          axes, grid lines, major tic marks, and minor tic marks.
 *          The are:
 *          <ul>
 *              <li>
 *                  Color (COLOR):
 *                  the color of a line.
 *              </li>
 *              <li>
 *                  Width (STROKE):
 *                  the width of a line.
 *              </li>
 *              <li>
 *                  Length (LENGTH):
 *                  the length of a line,
 *                  may be infinite.
 *              </li>
 *              <li>
 *                  Lines per unit (SPACING):
 *                  for one of the line categories
 *                  <em>grid lines, major tics,</em>
 *                  or <em>minor tics,</em>
 *                  the number of lines
 *                  to draw evenly spaced 
 *                  within a unit.
 *              </li>
 *              <li>
 *                  Draw (DRAW):
 *                  A Boolean value
 *                  that indicates whether or not
 *                  a category of line
 *                  is to be rendered.
 *              </li>
 *          </ul>
 *      </li>
 * </ol>
 * <p>
 * Property values can be saved from
 * or applied to
 * a profile via
 * {@link ProfileParser#getProperties()} and
 * {@link ProfileParser#loadProperties(Stream)}.
 * <p>
 * To interrogate or edit values
 * the programmer can obtain
 * the GraphPropertySet for the main window 
 * {@linkplain #getMainWindow()}
 * or the LinePropertySet for any category of line 
 * {@linkplain #getLinePropertySet(String)}.
 * Modifications made by a method
 * to a set of properties persist
 * after the method returns.
 * See also {@linkplain #getGridUnit()}
 * and {@linkplain #setGridUnit(float)}.
 * Upon instantiation by the default constructor,
 * all properties are initialized
 * from the PropertyManager.
 * The user can write changes
 * to the PropertyManager
 * by calling the {@linkplain #apply()} method,
 * or reset property values
 * from the PropertyManager via the {@linkplain #reset()} method.
 * Previously saved Profile objects
 * can be instantiated via the {@linkplain #Profile(Stream)} constructor
 * by providing it with the prior output
 * of the {@linkplain #getProperties()} method
 * or the equivalent.
 * </p>
 * @author Jack Straub
 */
public class Profile
{
    /** Convenient for accessing the PropertyManager singleton. */
    private static final PropertyManager        pMgr                =
        PropertyManager.INSTANCE;
    
    /** List of all concrete subclasses of LinePropertySet. */
    private static final 
    List<Class<? extends LinePropertySet>>      linePropertyClasses =
        List.of( 
            LinePropertySetAxes.class,
            LinePropertySetGridLines.class,
            LinePropertySetTicMajor.class,
            LinePropertySetTicMinor.class
        );
    
    /** Main window properties. */
    private final GraphPropertySet              mainWindow          =
        new GraphPropertySetMW();
    /** 
     * Map of simple class name (Class.getSimpleName() to Class class
     * for LinePropertySet subclasses.
     */
    private final Map<String,LinePropertySet>   linePropertySetMap  =
        new HashMap<>();
    /** Grid unit property. */
    private float                               gridUnit;
    
    /**
     * Constructor.
     * All properties are configured
     * from the PropertyManager.
     */
    public Profile()
    {
        gridUnit = pMgr.asFloat( CPConstants.GRID_UNIT_PN );
        // mainWindow is initialized in its declaration
        linePropertyClasses.stream()
            .forEach( this::putClass );
    }
    
    /**
     * Reset the values of all properties
     * the the values
     * currently stored by the PropertyManager.
     */
    public void reset()
    {
        gridUnit = pMgr.asFloat( CPConstants.GRID_UNIT_PN );
        mainWindow.reset();
        linePropertySetMap.values().forEach( s -> s.reset() );
    }
    
    /**
     * Update the property manager with
     * the current values of all properties.
     */
    public void apply()
    {
        pMgr.setProperty( CPConstants.GRID_UNIT_PN, gridUnit );
        mainWindow.apply();
        linePropertySetMap.values().forEach( s -> s.apply() );
    }
    
    /**
     * Returns the value of the grid unit property.
     * 
     * @return  the value of the grid unit property
     */
    public Float getGridUnit()
    {
        return gridUnit;
    }
    
    /**
     * Sets to grid unit property
     * to the given value.
     * 
     * @param gridUnit  the given value
     */
    public void setGridUnit( float gridUnit )
    {
        this.gridUnit = gridUnit;
    }
    
    /**
     * Gets the GraphPropertySet for the main window.
     * 
     * @return  the GraphPropertySet for the main window
     */
    public GraphPropertySet getMainWindow()
    {
        return mainWindow;
    }
    
    /**
     * Gets the LinePropertySet 
     * for the category of lines
     * represented by the given class name. Example:
     * <p style="margin-left: 2em; font-family:monospace;">
     *      String         name        = <br>
     *      &nbsp;&nbsp;&nbsp;&nbsp;
     *          LinePropertySetTicMajor.class.getSimpleName();<br>
     *      LinePropertySet majorTicSet = <br>
     *      &nbsp;&nbsp;&nbsp;&nbsp;
     *          profile.getLinePropertySet( name );
     * </p>
     * <p>
     * The LinePropertySet object returned
     * is modifiable,
     * so changes made by the calling method
     * will persist of the method returns.
     * </p>
     * 
     * @param simpleName    
     *      the simple name of the target subclass of LinePropertySet.
     * 
     * @return
     *      the LinePropertySet for the lines
     *      represented by the given class name
     */
    public LinePropertySet getLinePropertySet( String simpleName )
    {
        LinePropertySet set = linePropertySetMap.get( simpleName );
        return set;
    }

    @Override
    public int hashCode()
    {
        int hashCode    =
            Objects.hash( gridUnit, mainWindow, linePropertySetMap );
        return hashCode;
    }

    @Override
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( this == other )
            result = true;
        else if ( other == null )
            result = false;
        else if ( getClass() != other.getClass() )
            result = false;
        else
        {
            Profile that    = (Profile)other;
            result          =
                this.gridUnit == that.gridUnit
                && this.mainWindow.equals( that.mainWindow )
                && this.linePropertySetMap.equals( that.linePropertySetMap );
        }
        return result;
    }

    /**
     * Instantiates an object of the given class
     * and adds it to the LinePropertySet map.
     * <p>
     * Precondition:
     * The given class must be a subclass
     * of LinePropertySet.
     * 
     * @param clazz the given class
     */
    private void putClass( Class<? extends LinePropertySet> clazz )
    {
        try
        {
            String          name    = clazz.getSimpleName();
            LinePropertySet set     = 
                clazz.getDeclaredConstructor().newInstance();
            linePropertySetMap.put( name, set );
        }
        catch ( 
            NoSuchMethodException | 
            SecurityException |
            InvocationTargetException |
            IllegalAccessException |
            InstantiationException exc
        )
        {
            String  msg =
                "Could not instantiate " + clazz.getSimpleName();
            throw new ComponentException( msg );
        }
    }
}
