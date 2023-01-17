/**
 * 
 */
package com.acmemail.judah.cartesian_plane;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Jack Straub
 *
 */
public enum PropertyManager
{
    INSTANCE;
    
    private static final String         appPropertiesName   = 
        "appProperties.ini";
    
    private final Map<String,String>    propertyMap     = new HashMap<>();
    private final Properties            userProperties  = new Properties();
    private final Properties            appProperties   = new Properties();
    
    private PropertyManager()
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
    
    private void getUserProperties()
    {
        String      propsFile   = 
            getProperty(
                CPConstants.USER_PROPERTIES_PN,
                CPConstants.USER_PROPERTIES_DV
            );
        
        if ( propsFile != null )
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
     * The value is obtained by:
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
     * The given default (parameter <em>defValue</em>)
     * will be applied. 
     * </li>
     * </ol>
     * @param propName  property name
     * @param defValue  default value to be applied if necessary
     * 
     * @return  the value of the given property
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
}
