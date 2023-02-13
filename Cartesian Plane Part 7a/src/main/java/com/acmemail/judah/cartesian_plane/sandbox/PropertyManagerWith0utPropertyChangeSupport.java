/**
 * 
 */
package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * Manage the properties for this application.
 * <p style="margin-left: 2em; max-width: 25em;font-weight: bold;">
 * This is a singleton class
 * implemented as an enum
 * as recommended by
 * <a href="https://www.w3schools.blog/java-singleton-design-pattern">
 * W3schools
 * </a>
 * and <em>Effective Java</em> by Joshua Bloch.
 * </p>
 * The name of a property
 * and its corresponding value
 * are stored in a Properties object
 * as String pairs.
 * <p id="initialValues">
 * <b>Establishing the Initial Values of Properties</b>
 * </p>
 * Upon instantiation
 * the initial values of all properties
 * are obtained by:
 * <ol>
 * <li>
 * Checking the command line
 * for property definitions
 * using the java command line option <em>-Dname=value</em>.
 * Note that these are <em>command line options</em>
 * NOT <em>command line arguments</em>.
 * Command line options 
 * are positioned on the command line
 * <em>before</em> the name of the executable class:<br>
 * <span style="font-family: Monospace;">
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * java -cp . -DaxisWeight=3 app.Main
 * </span>
 * <br>
 * If the value of the property has not 
 * been defined on the command line:
 * </li>
 * <li>
 * The environment is searched.
 * If the property is not found
 * in the environment:
 * </li>
 * <li>
 * If a user properties file
 * has been configured
 * the user properties file is searched.
 * If the value of the property
 * is not found in 
 * the user properties file
 * (or if no such file
 * has been configured):
 * </li>
 * <li>
 * The default configuration file 
 * is searched.
 * If, after searching the default configuration file,
 * a value for the property
 * has not been established:
 * </li>
 * <li>
 * A default value will be applied.
 * </li>
 * </ol>
 * <p>
 * The user can listen for property changes
 * by registering a listener with:
 * </p>
 * <ul>
 * <li>
 * {@linkplain #addPropertyChangeListener(PropertyChangeListener)}
 * to listen for changes to all properties; and/or
 * </li>
 * <li>
 * {@linkplain #addPropertyChangeListener(String, PropertyChangeListener)}
 * to listen for changes to a specific property.
 * </li>
 * </ul> 
 * 
 * @author Jack Straub
 * 
 * @see #addPropertyChangeListener(PropertyChangeListener)
 * @see #addPropertyChangeListener(String, PropertyChangeListener)
 */
public enum PropertyManagerWith0utPropertyChangeSupport
{
    /** The single instance of this class. */
    INSTANCE;
    
    /** 
     * The name of the default properties file.
     */
    private static final String         appPropertiesName   = 
        "AppProperties.ini";
    /** 
     * Property name/value pairs from the user's properties file.
     * Used only during initialization.
     */
    private final Properties            userProperties  = new Properties();
    /** 
     * Property name/value pairs from the default properties file.
     * Used only during initialization.
     */
    private final Properties            appProperties   = new Properties();
    
    /** Map of property names to property values. */
    private final Map<String,String>    propertyMap     = new HashMap<>();
    
    /** List of property listeners. */
    private final List<PropertyChangeListener>  propertyListeners   =
        new ArrayList<>();
    /** Map of per-property listeners. */
    private final Map<String,List<PropertyChangeListener>>
        perPropertyListeners    = new HashMap<>();
    
    /**
     * Constructor.
     * Private as required by enums 
     * and the singleton programming pattern.
     * Establishes the initial values
     * of all properties.
     * 
     * @see <a href="#initialValues">
     *      Establishing the Initial Values of Properties
     *      </a>
     */
    private PropertyManagerWith0utPropertyChangeSupport()
    {
        String  val = null;
        
        getAppProperties();
        getUserProperties();
        
        val = getProperty( 
            CPConstants.TIC_MAJOR_COLOR_PN, 
            CPConstants.TIC_MAJOR_COLOR_DV
        );
        propertyMap.put( CPConstants.TIC_MAJOR_COLOR_PN, val );
        
        val = getProperty( 
            CPConstants.TIC_MAJOR_WEIGHT_PN, 
            CPConstants.TIC_MAJOR_WEIGHT_DV
        );
        propertyMap.put( CPConstants.TIC_MAJOR_WEIGHT_PN, val );
        
        val = getProperty( 
            CPConstants.TIC_MAJOR_LEN_PN, 
            CPConstants.TIC_MAJOR_LEN_DV
        );
        propertyMap.put( CPConstants.TIC_MAJOR_LEN_PN, val );
    }
    
    /**
     * Registers a property-change listener for changes
     * to all properties.
     * 
     * @param listener  listener to register
     */
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        propertyListeners.add( listener );
    }
    
    /**
     * Removes a listener from the registry
     * of property-change listeners
     * for all properties.
     * 
     * @param listener  listener to remove
     */
    public void 
    removePropertyChangeListener( PropertyChangeListener listener )
    {
        propertyListeners.remove( listener );
    }
    
    /**
     * Registers a property-change listener for changes
     * to a given property.
     * 
     * @param property  the name of the given property
     * @param listener  the listener to register
     */
    public void addPropertyChangeListener( 
        String                  property, 
        PropertyChangeListener  listener
    )
    {
        List<PropertyChangeListener>    list    = 
            perPropertyListeners.get( property );
        if ( list == null )
        {
            list = new ArrayList<PropertyChangeListener>();
            perPropertyListeners.put( property, list );
        }
        propertyListeners.add( listener );
    }
    
    /**
     * Removes a listener from the registry
     * of property-change listeners
     * for a given properties.
     * 
     * @param property  the name of the given property
     * @param listener  the listener to remove
     */
    public void removePropertyChangeListener( 
        String                  property, 
        PropertyChangeListener  listener
    )
    {
        List<PropertyChangeListener>    list    = 
            perPropertyListeners.get( property );
        if ( list != null )
            list.remove( listener );
    }
    
    /**
     * Gets the value associated with a given property name
     * and returns it as an Integer.
     * 
     * @param propName  the given property name
     * 
     * @return the converted Integer, or null if the property can't be found
     * 
     * @throws  NumberFormatException if the value associated with propName
     *          cannot be converted to an Integer
     */
    public Integer asInteger( String propName )
    {
        Integer iVal    = null;
        String  sVal    = propertyMap.get( propName );
        if ( sVal != null )
            iVal = Integer.parseInt( sVal );
        return iVal;
    }
    
    /**
     * Sets the value of a given property. 
     * The previous value of the property is returned
     * as type <em>Object</em>;
     * if the property does not have a previous value
     * null is returned.
     * 
     * @param propName  the given property name
     * @param propVal   the property value
     * 
     * @return  the previous value of the property,
     *          or null if none
     */
    public Object setProperty( String propName, int propVal )
    {
        String  sVal    = Integer.toString( propVal );
        Object  oVal    = propagatePropertyChange( propName, sVal );
        return oVal;
    }
    
    /**
     * Gets the value associated with a given property name
     * and returns it as a Float.
     * 
     * @param propName  the given property name
     * 
     * @return the converted Float, or null if the property can't be found
     * 
     * @throws  NumberFormatException if if the value associated with propName
     *          cannot be converted to a Float
     */
    public Float asFloat( String propName )
    {
        Float   fVal    = null;
        String  sVal    = propertyMap.get( propName );
        if ( sVal != null )
            fVal = Float.parseFloat( sVal );
        return fVal;
    }
    
    /**
     * Sets the value of a given property. 
     * The previous value of the property is returned
     * as type <em>Object</em>;
     * if the property does not have a previous value
     * null is returned.
     * 
     * @param propName  the given property name
     * @param propVal   the property value
     * 
     * @return  the previous value of the property,
     *          or null if none
     */
    public Object setProperty( String propName, float propVal )
    {
        String  sVal    = Float.toString( propVal );
        Object  oVal    = propagatePropertyChange( propName, sVal );
        return oVal;
    }
    
    /**
     * Gets the value associated with a given property name
     * and returns it as a Boolean.
     * 
     * @param propName  the given property name
     * 
     * @return the converted Boolean, or null if the property can't be found
     */
    public Boolean asBoolean( String propName )
    {
        Boolean bVal    = null;
        String  sVal    = propertyMap.get( propName );
        if ( sVal != null )
            bVal = Boolean.parseBoolean( sVal );
        return bVal;
    }
    
    /**
     * Sets the value of a given property. 
     * The previous value of the property is returned
     * as type <em>Object</em>;
     * if the property does not have a previous value
     * null is returned.
     * 
     * @param propName  the given property name
     * @param propVal   the property value
     * 
     * @return  the previous value of the property,
     *          or null if none
     */
    public Object setProperty( String propName, boolean propVal )
    {
        String  sVal    = Boolean.toString( propVal );
        Object  oVal    = propagatePropertyChange( propName, sVal );
        return oVal;
    }
    
    /**
     * Sets the value of a given property. 
     * The previous value of the property is returned
     * as type <em>Object</em>;
     * if the property does not have a previous value
     * null is returned.
     * 
     * @param propName  the given property name
     * @param propVal   the property value
     * 
     * @return  the previous value of the property,
     *          or null if none
     */
    public Object setProperty( String propName, String propVal )
    {
        Object  oVal    = propagatePropertyChange( propName, propVal );
        return oVal;
    }

    /**
     * Gets the value associated with a given property name
     * and returns it as a Color.
     * 
     * @param propName  the given property name
     * 
     * @return the converted Color, or null if the property can't be found
     * 
     * @throws  NumberFormatException if the value associated with propName
     *          cannot be converted to a Color
     */
    public Color asColor( String propName )
    {
        Color   cVal    = null;
        String  sVal    = propertyMap.get( propName );
        if ( sVal != null )
        {
            int     iVal    = Integer.decode( sVal );
            cVal    = new Color( iVal );
        }
        return cVal;
    }
    
    /**
     * Sets the value of a given Color property. 
     * The given value is converted to an integer
     * by calling <em>Color.getRGB()</em>
     * then the integer becomes the stored value.
     * The previous value of the property is returned
     * as type <em>Object</em>;
     * if the property does not have a previous value
     * null is returned.
     * 
     * @param propName  the given property name
     * @param propVal   the property value
     * 
     * @return  the previous value of the property,
     *          or null if none
     */
    public Object setProperty( String propName, Color propVal )
    {
        int     cVal    = propVal.getRGB();
        String  sVal    = Integer.toString( cVal );
        Object  oVal    = propagatePropertyChange( propName, sVal );
        return oVal;
    }

    /**
     * Convert the value associated with a given property name
     * to a font style return the result.
     * Integer values for font styles are defined in the Font class.
     * Input is case-insensitive; valid values are 
     * PLAIN, BOLD and ITALIC.
     * 
     * @param propName  the given property name
     * 
     * @return the converted font style, or null if the property can't be found
     * 
     * @throws  IllegalArgumentException if the property value
     *          cannot be converted to a font style.
     */
    /**
     * @param propName
     * @return
     */
    public Integer asFontStyle( String propName )
    {
        Integer iVal    = null;
        String  sVal    = propertyMap.get( propName );
        if ( sVal != null )
        {
            String  csVal   = sVal.toUpperCase();
            switch ( csVal )
            {
            case "PLAIN":
                iVal = Font.PLAIN;
                break;
            case "BOLD":
                iVal = Font.BOLD;
                break;
            case "ITALIC":
                iVal = Font.ITALIC;
                break;
            default:
                String  err = 
                    "\"" + sVal + "\"" + "is not a valid font style";
                throw new IllegalArgumentException( err );
            }
        }
        return iVal;
    }

    /**
     * Read the application properties file from the resources directory.
     * Properties are loaded into the appProperties object.
     * 
     * Precondition: appProperties object has been instantiated
     */
    private void getAppProperties()
    {
        ClassLoader loader      = PropertyManagerWith0utPropertyChangeSupport.class.getClassLoader();
        InputStream inStream    = loader.getResourceAsStream( appPropertiesName );
        if ( inStream == null )
        {
            String  msg = "System properties file \"" 
                + appPropertiesName + "\" not found";
            System.err.println( msg );
        }
        else
        {
            try
            {
                appProperties.load( inStream );
            }
            catch ( IOException exc )
            {
                String  msg = "Error reading system properties file: \""
                    + appPropertiesName + "\"";
                System.err.println( msg );
                System.err.println( exc.getMessage() );
            }
        }
        
        if ( inStream != null )
        {
            try
            {
                inStream.close();
            }
            catch ( IOException exc )
            {
                String  msg = "Error closing system properties file: \""
                    + appPropertiesName + "\"";
                System.err.println( msg );
                System.err.println( exc.getMessage() );
            }
        }
    }
    
    /**
     * Load the properties from the user's properties file
     * if it exists. 
     * The location of the file
     * is determined by the USER_PROPERTIES_PN property.
     */
    private void getUserProperties()
    {
        String      propsFile   = 
            getProperty(
                CPConstants.USER_PROPERTIES_PN,
                CPConstants.USER_PROPERTIES_DV
            );
        
        if ( propsFile != null && !propsFile.isEmpty() )
        {
            try ( FileInputStream inStream = new FileInputStream( propsFile ) )
            {
                userProperties.load( inStream );
            }
            catch ( IOException exc )
            {
                String  msg = "Error reading user properties file: \""
                    + propsFile + "\"";
                System.err.println( msg );
                System.err.println( exc.getMessage() );
            }
        }
    }

    /**
     * This method is called to get the initial value
     * of a named property.
     * The value is obtained
     * according to the algorithm
     * described in the class header.
     * 
     * @param propName  property name
     * @param defValue  default value to be applied if necessary
     * 
     * @return  the value of the given property
     * 
     * @see <a href="#initialValues">
     *      Establishing the Initial Values of Properties
     *      </a>
     */
    private String getProperty( String propName, String defValue )
    {
        String  val = null;
        if ( (val = System.getProperty( propName )) != null )
            ;
        else if ( (val = System.getenv( propName )) != null )
            ;
        else if ( (val = userProperties.getProperty( propName )) != null )
            ;
        else if ( (val = appProperties.getProperty( propName )) != null )
            ;
        else 
            val = defValue;
        
        return val;
    }
    
    private Object propagatePropertyChange( String propName, String newVal )
    {
        Object  oldVal  = propertyMap.put( propName, newVal );
        // Don't propagate a property change event unless the
        // value of the property has actually changed.
        if ( !oldVal.equals( newVal ) )
        {
            PropertyChangeEvent eventObj    = 
                new PropertyChangeEvent(
                    INSTANCE,
                    propName,
                    oldVal,
                    newVal
                );
            for ( PropertyChangeListener listener : propertyListeners )
                listener.propertyChange( eventObj );
            
            List<PropertyChangeListener>    list    = 
                perPropertyListeners.get( propName );
            if ( list != null )
            {
                for ( PropertyChangeListener listener : propertyListeners )
                    listener.propertyChange( eventObj );
            }
        }
        
        return oldVal;
    }
}
