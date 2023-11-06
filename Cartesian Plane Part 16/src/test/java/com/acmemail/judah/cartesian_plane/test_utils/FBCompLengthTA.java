package com.acmemail.judah.cartesian_plane.test_utils;

import java.util.function.DoubleSupplier;

import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.Feedback;
import com.acmemail.judah.cartesian_plane.components.LengthFeedback;

public class FBCompLengthTA extends FBCompTA
{
    private static final String     subdir      = "Length";
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( FBCompLengthTA::new );
    }
    
    public FBCompLengthTA()
    {
        super(subdir);
    }

    @Override
    public Feedback getFeedbackInstance( DoubleSupplier supplier )
    {
        Feedback    feedback    = new LengthFeedback( supplier );
        return feedback;
    }

}
