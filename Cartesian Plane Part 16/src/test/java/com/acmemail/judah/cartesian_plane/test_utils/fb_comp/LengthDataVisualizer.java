package com.acmemail.judah.cartesian_plane.test_utils.fb_comp;

import java.util.function.DoubleSupplier;

import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.Feedback;
import com.acmemail.judah.cartesian_plane.components.LengthFeedback;

public class LengthDataVisualizer extends FBCompTAVisualizer
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( LengthDataVisualizer::new );
    }
    
    
    public LengthDataVisualizer()
    {
        super( FBCompLengthTA.SUBDIR, s -> getComponent( s ) );
    }
    
    private static Feedback getComponent( DoubleSupplier supplier )
    {
        Feedback    feedback    = new LengthFeedback( supplier );
        return feedback;
    }
}
