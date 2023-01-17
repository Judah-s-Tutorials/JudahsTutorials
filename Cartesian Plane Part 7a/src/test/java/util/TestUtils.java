package util;

import javax.swing.JFrame;

/**
 * This class contains utilities 
 * that are helpful in testing
 * classes in the CartesianPlane project.
 * 
 * @author Jack Straub
 *
 */
public class TestUtils
{
    /**
     * Get the JFrame that is contained in the Root class
     * of this application.
     * 
     * @return  the JFrame that is contained in the Root class
     *          of this application
     */
    public static JFrame getRootFrame()
    {
        // implementation to be provided at a later time
        return null;
    }
    
    /**
     * Put the current thread to sleep
     * for a given number of milliseconds.
     * An InterruptedException may occur;
     * if it does it will be ignored.
     * 
     * @param millis    the given number of milliseconds
     */
    public static void pause( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( InterruptedException exc )
        {
            // ignore exception
        }
    }
}
