package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.util.function.Predicate;

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
    
    public static void disableFrames()
    {
        for ( Frame frame : Frame.getFrames() )
            frame.setEnabled( false );
    }
    
    public static Component
    getComponent( Predicate<Component> pred )
    {
        Frame[]     frames  = Frame.getFrames();
        Component   result  = null;
        for ( int inx = 0 ; inx < frames.length && result == null ; ++inx )
        {
            Frame   frame   = frames[inx];
            if  ( frame.isEnabled() )
            {
                if ( pred.test( frame ) )
                    result = frame;
                else if ( frame instanceof JFrame )
                    result = getComponent( ((JFrame)frame).getContentPane(), pred );
                else
                    ;
            }
        }
        return result;
    }
    
    public static Component 
    getComponent( Container container, Predicate<Component> pred )
    {
        Component   result      = pred.test( container ) ? container : null;
        Component[] children    = container.getComponents();
        for ( int inx = 0 ; inx < children.length && result == null ; ++inx )
        {
            Component   child   = children[inx];
            if ( pred.test( child ) )
                result = child;
            else if ( child instanceof Container )
                result = getComponent( (Container)child, pred );
            else
                ;
        }
        return result;
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
