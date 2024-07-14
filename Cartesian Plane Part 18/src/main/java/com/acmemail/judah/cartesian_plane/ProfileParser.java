package com.acmemail.judah.cartesian_plane;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;

/**
 * This class provides a means
 * to save and restore
 * the property values
 * in a Profile.
 * An object of this class
 * is always associated with a "working profile,"
 * which may be supplied by the user.
 * If the user does not supply a profile
 * one will be instantiated.
 * <p>
 * You can use {@linkplain #getProperties()}
 * to produce a stream of strings (Stream&lt;String&gt;)
 * representing the encapsulated properties.
 * Strings in the stream
 * consists of a tag followed by a space
 * and a value.
 * Each string represents one of:
 * </p>
 * <ol>
 *      <li>
 *          The "class" tag followed by 0 or more spaces
 *          of the class of properties for
 *          the main graph window (GraphPropertySetMW)
 *          or one of the line properties classes
 *          (LinePropertySetAxes, LinePropertySetGridLines,
 *          LinePropertySetTicMajor, or LinePropertySetTicMinor).
 *          For example:<br>
 *          <span style="font-family: monospace; margin-left:2em;">
 *              class LinePropertySetTicMinor
 *          </span>
 *      </li>
 *      <li>
 *          The name of a property
 *          followed by 0 or more spaces,
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
 * Previously saved Profile objects
 * can be instantiated via the {@linkplain #Profile(Stream)} constructor
 * by providing it with the prior output
 * of the {@linkplain #getProperties()} method
 * or the equivalent.
 * </p>
 * @author Jack Straub
 */
public class ProfileParser
{
    /** The tag preceding a profile name. */
    public static final String PROFILE      = "profile";
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
    
    /** Working profile, to be parse from or into. */
    private final Profile                       workingProfile;
    /** Main window properties. */
    private final GraphPropertySet              mainWindow;
    
    /** 
     * Used for parsing input during 
     * {@link #loadProperties(Stream)} processing,
     * this is the LinePropertySet identified by a CLASS tag,
     * and into which parsed values will be stored.
     * If it's null the input is malformed.
     */
    private LinePropertySet     currLineSet     = null;
    /** 
     * Used for parsing input during 
     * {@link #loadProperties(Stream)} processing,
     * this is the GraphPropertySet from the target profile.
     */
    private GraphPropertySet    currGraphSet    = null;
    
    /**
     * Constructor.
     * All properties are configured
     * from the PropertyManager.
     */
    public ProfileParser()
    {
        this( new Profile() );
    }
    
    /**
     * Constructor.
     * All properties are configured
     * from the given Profile.
     * 
     * @param   workingProfile  the given Profile
     */
    public ProfileParser( Profile workingProfile )
    {
        this.workingProfile = workingProfile;
        this.mainWindow = workingProfile.getMainWindow();
    }
    
    /**
     * Returns a stream of strings
     * representing the properties
     * encapsulated in the working profile.
     * See the class documentation
     * for description of the format
     * of the strings in the stream.
     *  
     * @return  
     *      a stream of strings representing the encapsulate properties
     *      of this Profile object
     *      
     * @see ProfileParser
     */
    public Stream<String> getProperties()
    {
        Stream.Builder<String>  bldr    = Stream.<String>builder();
        bldr.add( fromFloat( GRID_UNIT, workingProfile.getGridUnit() ) );
        compile( mainWindow, bldr );
        Stream.of(
            LinePropertySetAxes.class,
            LinePropertySetGridLines.class,
            LinePropertySetTicMajor.class,
            LinePropertySetTicMinor.class
        )
            .map( Class::getSimpleName )
            .map( workingProfile::getLinePropertySet )
            .forEach( v -> compile( v, bldr ) );

        return bldr.build();
    }
    
    /**
     * Processes a given stream of property name/value pairs
     * representing the properties
     * to be applied to the working profile.
     * See the class documentation
     * for description of the format
     * of the strings in the stream.
     *  
     * @return  
     *      the given stream of property name/value pairs
     *      
     * @see ProfileParser
     */
    public void loadProperties( Stream<String> inStream )
    {
        inStream
            .map( String::trim )
            .filter( s -> !s.isEmpty() )
            .map( s -> s.split( "\\s" ) )
            .filter( a -> isNameValue( a ) )
            .forEach( this::parseNameValue );
    }
    
    /**
     * Returns the working profile.
     * 
     * @return  the working profile
     */
    public Profile getProfile()
    {
        return workingProfile;
    }
    
    /**
     * This method verifies that in input string
     * has been parsed into exactly tokens.
     * 
     * @param args  the tokens to validate
     * 
     * @return  true if the given array consists of exactly two tokens
     */
    private boolean isNameValue( String[] args )
    {
        boolean result  = 
            args.length == 2 && !args[1].isEmpty();
        return result;
    }
    
    /**
     * Parses a given name/value pair
     * and applies the value
     * to the property of the given name.
     * If the name token is CLASS,
     * the value is used to determine
     * which of the property sets in the working Profile
     * the value is to be applied.
     * If the name token is GRID_UNIT
     * the value is applied to the working Profile's
     * grid-unit property.
     * All other properties
     * will be applied to the most recently identified
     * property set.
     * If a property set was not previously identified
     * an error message will be posted.
     * Error messages will also be posted
     * for invalid names or values.
     * 
     * @param args  the given name/value pair
     */
    private void parseNameValue( String[] args )
    {
        switch ( args[0] )
        {
        case CLASS:
            parseClass( args[1] );
            break;
        case GRID_UNIT:
            parseFloat( workingProfile::setGridUnit, args );
            break;
        case DRAW:
            Consumer<Boolean>   consumer    = currGraphSet != null ?
                currGraphSet::setFontDraw :
                currLineSet::setDraw;
            parseBoolean( consumer, args );
            break;
        case STROKE:
            parseFloat( currLineSet::setStroke, args );
            break;
        case SPACING:
            parseFloat( currLineSet::setSpacing, args );
            break;
        case LENGTH:
            parseFloat( currLineSet::setLength, args );
            break;
        case COLOR:
            parseColor( currLineSet::setColor, args );
            break;
        case FG_COLOR:
            parseColor( currGraphSet::setFGColor, args );
            break;
        case BG_COLOR:
            parseColor( currGraphSet::setBGColor, args );
            break;
        case FONT_NAME:
            parseString( currGraphSet::setFontName, args );
            break;
        case FONT_SIZE:
            parseFloat( currGraphSet::setFontSize, args );
            break;
        case FONT_STYLE:
            parseString( currGraphSet::setFontStyle, args );
            break;
        default:
            postParseError( args[0], "is not a valid property" );
        }
    }
    
    /**
     * Parses a CLASS property.
     * The given value is used to determine
     * the property to which subsequent
     * name/value pairs are to be applied.
     * If the name begins with "Graph"
     * the property set is assumed to be
     * the main window property set
     * from the working profile.
     * Otherwise the value must be the name
     * of one of the concrete LinePropertySet classes
     * encapsulated in the working profile.
     * 
     * @param value the name of the property set
     */
    private void parseClass( String value )
    {
        if ( value.startsWith( "Graph" ) )
        {
            currGraphSet = workingProfile.getMainWindow();
            currLineSet = null;
        }
        else
        {
            currGraphSet = null;
            currLineSet = workingProfile.getLinePropertySet( value );
            if ( currLineSet == null )
                postParseError( value, "not a valid property set name" );
        }
    }
    
    /**
     * Given a name value pair
     * and a consumer,
     * the value of the pair is assumed to be a String
     * and will be applied
     * using the given consumer.
     * Technically only the value
     * of the name/value pair (args[1])
     * is needed to complete the operation,
     * but the invoker also passes the name (args[0])
     * which may be used to format an error message.
     * 
     * @param consumer  the given consumer
     * @param pair      the given name/value pair
     */
    private void parseString( Consumer<String> consumer, String[] pair )
    {
        try
        {
            String  val = pair[1];
            consumer.accept( val );
        }
        catch ( NullPointerException exc )
        {
            postError( "No current property set" );
        }
    }

    /**
     * Given a name value pair
     * and a consumer,
     * the value of the pair is assumed to be a float
     * which will be parsed and applied
     * using the given consumer.
     * Technically only the value
     * of the name/value pair (args[1])
     * is needed to complete the operation,
     * but the invoker also passes the name (args[0])
     * which may be used to format an error message.
     * 
     * @param consumer  the given consumer
     * @param pair      the given name/value pair
     */
    private void parseFloat( Consumer<Float> consumer, String[] pair )
    {
        try
        {
            float   val = Float.parseFloat( pair[1] );
            consumer.accept( val );
        }
        catch ( NumberFormatException exc )
        {
            postParseError( pair );
        }
    }
    
    /**
     * Given a name value pair
     * and a consumer,
     * the value of the pair is assumed to be
     * the integer representation of a color,
     * which will be parsed and applied
     * using the given consumer.
     * Technically only the value
     * of the name/value pair (args[1])
     * is needed to complete the operation,
     * but the invoker also passes the name (args[0])
     * which may be used to format an error message.
     * 
     * @param consumer  the given consumer
     * @param pair      the given name/value pair
     */
    private void parseColor( Consumer<Color> consumer, String[] pair )
    {
        try
        {
            int     val     = Integer.decode( pair[1] );
            Color   color   = new Color( val );
            consumer.accept( color );
        }
        catch ( NumberFormatException exc )
        {
            postParseError( pair );
        }
    }
    
    /**
     * Given a name value pair
     * and a consumer,
     * the value of the pair is assumed to be a boolean
     * which will be parsed and applied
     * using the given consumer.
     * Technically only the value
     * of the name/value pair (args[1])
     * is needed to complete the operation,
     * but the invoker also passes the name (args[0])
     * which may be used to format an error message.
     * 
     * @param consumer  the given consumer
     * @param pair      the given name/value pair
     */
    private void parseBoolean( Consumer<Boolean> consumer, String[] pair )
    {
        try
        {
            boolean val     = Boolean.parseBoolean( pair[1] );
            consumer.accept( val );
        }
        catch ( NumberFormatException exc )
        {
            postParseError( pair );
        }
    }
    
    /**
     * Given a name/value pair,
     * an error message will be formatted
     * indicating that the given value
     * is not appropriate for the property
     * for the property with the given name.
     * 
     * @param pair  the given name/value pair
     */
    private void postParseError( String[] pair )
    {
        String  fmt     = "Failed to parse \"%s\" for field \"%s";
        String  message = String.format( fmt, pair[1], pair[0] );
        postError( message );
    }
    
    /**
     * Given a value and description
     * an error message will be formatted
     * indicated that the given value is not valid
     * for the reason identified by the given description.
     * 
     * @param value     the given value
     * @param descrip   the given description
     */
    private void postParseError( String value, String descrip )
    {
        String  message = "\"" + value + "\" " + descrip;
        postError( message );
    }
    
    /**
     * Posts an error dialog
     * containing the given message.
     * 
     * @param message   the given message
     */
    private void postError( String message )
    {
        JOptionPane.showMessageDialog(
            null, 
            message, 
            "Profile Parse Error", 
            JOptionPane.ERROR_MESSAGE
        );
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
     * @see ProfileParser
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
     * @see ProfileParser
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
}
