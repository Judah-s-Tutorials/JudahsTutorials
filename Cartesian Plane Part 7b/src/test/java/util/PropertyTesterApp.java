package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.acmemail.judah.cartesian_plane.PropertyManager;

/**
 * This is an application 
 * to support unit testing
 * of the PropertyManager class.
 * The intent is for a client
 * to send the name of a property
 * via <em>stdin</em>
 * and expect the value of the property
 * to be returned via <em>stdout;</em>
 * presumably the client
 * will then validate the result.
 * The client can terminate the application
 * by sending {@linkplain #EXIT_COMMAND} to <em>stdin</em>.
 * 
 * @author Jack Straub
 *
 */
public class PropertyTesterApp
{
    /** This is the response if a property name can't be found. */
    public static final String  NOT_FOUND       = "++NULL";
    /** Receiving this command will cause this process to exit. */
    public static final String  EXIT_COMMAND    = "++exit";
    /** Status code for normal exit. */
    public static final int     EXIT_SUCCESS    = 0;
    /** Status code for abnormal exit. */
    public static final int     EXIT_FAILURE    = 1;
    
    /** PropertyManager singleton; declared here for convenience. */
    private static PropertyManager  pMgr    = PropertyManager.INSTANCE;
    
    /**
     * Application entry point.
     * Executes a loop in which a string is read from <em>stdin</em>.
     * If the input string is equal to EXIT_COMMAND
     * the application exits normally
     * with a status of EXiT_SUCCESS
     * and <em>with no response sent to stdout.</em>
     * Otherwise the input is assumed
     * to be the name of a Cartesian plane project property.
     * The value of the property is read
     * from the PropertyManager and 
     * printed to <em>stdout.</em>
     * All property values
     * are treated as strings.
     * If the property can't  be found
     * {PropertyManager.asString(String) return null)
     * NOT_FOUND is printed.
     * <p>
     * If an error occurs
     * the application exits with a status of
     * EXIT_FAILURE.
     * </p>
     * 
     * @param args  command line arguments; not used
     * 
     * @see #EXIT_COMMAND
     * @see #EXIT_SUCCESS
     * @see #EXIT_FAILURE
     * @see #NOT_FOUND
     */
    public static void main(String[] args)
    {
        System.err.println( "child: main" );
        try ( Reader reader  = new InputStreamReader( System.in ); )
        {
            BufferedReader  bufReader   = new BufferedReader( reader );
            String          propName    = "";
            while ( !(propName = bufReader.readLine()).equals( EXIT_COMMAND ) )
            {
                System.err.println( "child: " + propName );
                String  propValue   = pMgr.asString( propName );
                System.err.println( "child: " + propName + "->" + propValue );
                System.out.println( propValue );
            }
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( EXIT_FAILURE );
        }
        System.exit( EXIT_SUCCESS );
    }
    
    
}
