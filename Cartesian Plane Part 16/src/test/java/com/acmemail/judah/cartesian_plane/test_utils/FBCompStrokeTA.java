package com.acmemail.judah.cartesian_plane.test_utils;

import java.util.function.DoubleSupplier;

import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.Feedback;
import com.acmemail.judah.cartesian_plane.components.StrokeFeedback;

public class FBCompStrokeTA extends FBCompTA
{
    private static final String     subdir      = "Stroke";
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( FBCompStrokeTA::new );
    }
    
    public FBCompStrokeTA()
    {
        super(subdir);
    }

    @Override
    public Feedback getFeedbackInstance( DoubleSupplier supplier )
    {
        Feedback    feedback    = new StrokeFeedback( supplier );
        return feedback;
    }

}
