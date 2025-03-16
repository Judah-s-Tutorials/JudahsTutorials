package com.acmemail.judah.cartesian_plane;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

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
public enum PropertyManager
{
    /** The single instance of this class. */
    INSTANCE;

    /** 
     * The name of the default properties file.
     */
    private static final String         appPropertiesName   = 
        CPConstants.APP_PROPERTIES_NAME;
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
    
    /** 
     * List of property listeners; this object must be instantiated
     * in a constructor. 
     */
    private final PropertyChangeSupport changeSupport;
    
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
    private PropertyManager()
    {
        changeSupport = new PropertyChangeSupport( this );

        // Compile the properties stored in the application ini file.
        getAppProperties();
        // Merge the properties from the user's ini file, if any;
        // note that anything in the user's ini file will overwrite
        // with a matching property name.
        getUserProperties();
        
        // Get all property names and their default values.
        for ( Field pnField : CPConstants.class.getFields() )
        {
            String  fieldName   = pnField.getName();
            if ( fieldName.endsWith( "_PN" ) )
            {
                // derive the name of the associated DV field...
                // ... get the name of the PN field
                int     pNameLen    = fieldName.length();
                // ... get the start of the PN field name (without the _PN)
                String  pNamePrefix = fieldName.substring( 0, pNameLen - 3 );
                // ... construct the name of the DV field
                String  dvName      = pNamePrefix + "_DV";
                
                String  propName    = "";
                String  propDefault = "";
                try
                {
                    // Get the field that describes the default value
                    // must be inside a try block because getField
                    // throws a checked exception.
                    Field   dvField = CPConstants.class.getField( dvName );
                    
                    // Because we are interrogating a class field
                    // (declared static) we don't need to pass an
                    // object to the Field.get method.
                    // Must be inside a try block because get
                    // throws a checked exception.
                    propName = (String)pnField.get( null );
                    propDefault = (String)dvField.get( null );
                }
                catch ( NoSuchFieldException exc )
                {
                    // This exception indicates a programming error.
                    String  msg = dvName + ": field not found";
                    System.err.println( msg );
                    exc.printStackTrace();
                    System.exit( 1 );
                }
                catch ( IllegalAccessException exc )
                {
                    // this is a wholly unexpected error;
                    // print an error and exit.
                    exc.printStackTrace();
                    System.exit( 1 );
                }
                
                // Interrogate command line, environment, and
                // ini files for a value overriding the default.
                String  finalVal    = getProperty( propName, propDefault );
                propertyMap.put( propName, finalVal );
            }
        }
    }
    
    /**
     * Registers a property-change listener for changes
     * to all properties.
     * 
     * @param listener  listener to register
     */
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        changeSupport.addPropertyChangeListener( listener );
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
        changeSupport.removePropertyChangeListener( listener );
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
        changeSupport.addPropertyChangeListener( property, listener );
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
        changeSupport.removePropertyChangeListener( property, listener );
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
    public Integer asInt( String propName )
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
     * and returns it as a String.
     * 
     * @param propName  the given property name
     * 
     * @return the property value, or null if the property can't be found
     */
    public String asString( String propName )
    {
        String  sVal    = propertyMap.get( propName );
        return sVal;
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
        ClassLoader loader      = PropertyManager.class.getClassLoader();
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
        System.err.println( propsFile );
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
    
    /**
     * Finalize property change logic and fire the property change listeners.
     * The prior value of the property, if any, is returned;
     * if the property has no prior value null is returned.
     * 
     * @param propName  name of the property to be changed
     * @param newVal    the new value of the property to be changed
     * 
     * @return  the old value of the property that was changed
     */
    private Object propagatePropertyChange( String propName, String newVal )
    {
        Object  oldVal  = propertyMap.put( propName, newVal );
        changeSupport.firePropertyChange( propName, oldVal, newVal );
        
        return oldVal;
    }
}
