package com.acmemail.judah.cartesian_plane;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
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
 * You can use {@linkplain #getProperties()}
 * to produce a stream of strings (Stream&lt;String&gt;)
 * representing the encapsulated properties.
 * Strings in the stream
 * consists of a tag followed by a colon
 * and a value.
 * Each string represents one of:
 * </p>
 * <ol>
 *      <li>
 *          The "class" tag
 *          followed by the name of the class of properties for
 *          the main graph window (GraphPropertySetMW)
 *          or one of the line properties classes
 *          (LinePropertySetAxes, LinePropertySetGridLines,
 *          LinePropertySetTicMajor, or LinePropertySetTicMinor).
 *          For example:<br>
 *          <span style="font-family: monospace; margin-left:2em;">
 *              Class: LinePropertySetTicMinor
 *          </span>
 *      </li>
 *      <li>
 *          The name of a property
 *          followed by a colon(:)
 *          and 0 or more spaces,
 *          and the value of the property.
 *          For example:<br>
 *          <span style="font-family: monospace; margin-left:2em;">
 *              color: 0x000000
 *          </span>
 *      </li>
 * </ol>
 * <p>
 * The first string in the stream
 * is always the name/value
 * of the grid unit.
 * Properties on a line
 * following a class name
 * apply to the properties
 * of that class.
 * Other than the grid unit property,
 * any property name/value 
 * that is not preceded by a class name
 * must be considered a severe error.
 * For the purpose of parsing the output,
 * the programmer should assume that any string
 * might containing leading or trailing spaces,
 * and the delimiter between
 * a property name and its value
 * must consist of a single colon
 * and 0 or more spaces
 * on either side of the colon.
 * </p>
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
    /** The tag preceding a class name. */
    public static final String CLASS        = "class";
    /** The tag for the grid unit property. */
    public static final String GRID_UNIT    = CPConstants.GRID_UNIT_PN;
    /** The tag for the draw property. */
    public static final String DRAW         = "draw";
    /** The tag for the width  property. */
    public static final String STROKE       = "stroke";
    /** The tag for the lines/unit property. */
    public static final String SPACING      = "spacing";
    /** The tag for the length property. */
    public static final String LENGTH       = "length";
    /** The tag for the color property (for lines). */
    public static final String COLOR        = "color";
    /** The tag for the foreground color property (for the main window). */
    public static final String FG_COLOR     = "fgColor";
    /** The tag for the background color property (for the main window). */
    public static final String BG_COLOR     = "bgColor";
    /** The tag for the font name property. */
    public static final String FONT_NAME    = "font_name";
    /** The tag for the font size property. */
    public static final String FONT_SIZE    = "font_size";
    /** The tag for the font style property. */
    public static final String FONT_STYLE   = "font_style";
    
    /** Convenient for accessing the PropertyManager singleton. */
    private static final PropertyManager        pMgr                =
        PropertyManager.INSTANCE;
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
    private Float                               gridUnit;
    
    /**
     * Constructor.
     * All properties are configured
     * from the PropertyManager.
     */
    public Profile()
    {
        initProperties();
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
    
    /**
     * Returns a stream of strings
     * representing the properties
     * encapsulated in this Profile.
     * See the class documentation
     * for description of the format
     * of the strings in the stream.
     *  
     * @return  
     *      a stream of strings representing the encapsulate properties
     *      of this Profile object
     *      
     * @see Profile
     */
    public Stream<String> getProperties()
    {
        Stream.Builder<String>  bldr    = Stream.<String>builder();
        bldr.add( fromFloat( GRID_UNIT, gridUnit ) );
        compile( mainWindow, bldr );
        linePropertySetMap.values().forEach( v -> compile( v, bldr ) );
        
        return bldr.build();
    }
    
    /**
     * Compile the strings
     * representing the properties of the given GraphPropertySet
     * and add them to the given Stream builder.
     * Each string represents a single property
     * in the format:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;name: value
     * 
     * @param set   the given GraphPropertySet
     * @param bldr  the given Stream builder
     * 
     * @see Profile
     */
    private void 
    compile( GraphPropertySet set, Stream.Builder<String> bldr )
    {
        bldr.add( fromClass( set.getClass() ) );
        bldr.add( fromColor( BG_COLOR, set.getBGColor() ) );
        bldr.add( fromColor( FG_COLOR, set.getFGColor() ) );
        bldr.add( fromBoolean( DRAW, set.isFontDraw() ) );
        bldr.add( format( FONT_NAME, set.getFontName() ) );
        bldr.add( fromFloat( FONT_SIZE, set.getFontSize() ) );
        bldr.add( format( FONT_STYLE, "" + set.getFontStyle() ) );
    }
    
    /**
     * Compile the strings
     * representing the properties of the given LinePropertySet
     * and add them to the given Stream builder.
     * Each string represents a single property
     * in the format:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;name: value
     * 
     * @param set   the given LinePropertySet
     * @param bldr  the given Stream builder
     * 
     * @see Profile
     */
    private void 
    compile( LinePropertySet set, Stream.Builder<String> bldr )
    {
        bldr.add( fromClass( set.getClass() ) );
        if ( set.hasColor() )
            bldr.add( fromColor( COLOR, set.getColor() ) );
        if ( set.hasDraw() )
            bldr.add( fromBoolean( DRAW, set.getDraw() ) );
        if ( set.hasLength() )
            bldr.add( fromFloat( LENGTH, set.getLength() ) );
        if ( set.hasSpacing() )
            bldr.add( fromFloat( SPACING, set.getSpacing() ) );
        if ( set.hasStroke() )
            bldr.add( fromFloat( STROKE, set.getStroke() ) );
    }
    
    /**
     * Format a string from a given label and value
     * as "label: value."
     * 
     * @param label the given label
     * @param value the given value
     * 
     * @return  
     *      a string containing the given label and value
     *      in the form "label: value"
     */
    private String format( String label, String value )
    {
        return label + ": " + value;
    }
    
    /**
     * Format a string from a given label and value.
     * 
     * @param label the given label
     * @param fVal the given value
     * 
     * @return  
     *      a string formatted with the given label and value
     * 
     * @see #format(String, String)
     */
    private String fromFloat( String label, float fVal )
    {
        String  sVal    = String.valueOf( fVal );
        String  result  = format( label, sVal );
        return result;
    }
    
    /**
     * Format a string from a given label and value.
     * 
     * @param label the given label
     * @param color the given value
     * 
     * @return  
     *      a string formatted with the given label and value
     * 
     * @see #format(String, String)
     */
    private String fromColor( String label, Color color )
    {
        int     iVal    = color.getRGB() & 0xFFFFFF;
        String  sVal    = String.format( "0x%06x", iVal );
        String  result  = format( label, sVal );
        return result;
    }
    
    /**
     * Format a string from a given label and value.
     * 
     * @param label the given label
     * @param bVal the given value
     * 
     * @return  
     *      a string formatted with the given label and value
     * 
     * @see #format(String, String)
     */
    private String fromBoolean( String label, boolean bVal )
    {
        String  sVal    = String.valueOf( bVal );
        String  result  = format( label, sVal );
        return result;
    }
    
    /**
     * Format a string
     * consisting of the CLASS tag
     * followed by the simple name
     * of the given class.
     * 
     * @param clazz the given class
     * 
     * @return  
     *      a string consisting of the CLASS tag followed by
     *      the simple name of the given class
     * 
     * @see #format(String, String)
     */
    private String fromClass( Class<?> clazz )
    {
        String  result  = format( CLASS, clazz.getSimpleName() );
        return result;
    }
    
    private void  initProperties()
    {
        gridUnit = pMgr.asFloat( CPConstants.GRID_UNIT_PN );
        // mainWindow is initialized in its declaration
        Stream.of(
            LinePropertySetAxes.class,
            LinePropertySetGridLines.class,
            LinePropertySetTicMajor.class,
            LinePropertySetTicMinor.class
        ).forEach( this::putClass );
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
