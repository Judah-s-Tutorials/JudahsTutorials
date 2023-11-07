package com.acmemail.judah.cartesian_plane.test_utils.fb_comp;

import java.util.function.DoubleSupplier;

import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.Feedback;
import com.acmemail.judah.cartesian_plane.components.SpacingFeedback;

/**
 * Application to generate test data
 * for the LengthFeedback component class.
 * All the work is performed by 
 * the superclass, {@link FBCompTA}.
 * 
 * @author Jack Straub
 * 
 * @see FBCompTA
 */
public class FBCompSpacingTA extends FBCompTA
{
    /** 
     * The name of the subdirectory in which test data
     * for the encapsulated component
     * is to be stored.
     */
    public static final String  SUBDIR      = "Spacing";
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( FBCompSpacingTA::new );
    }
    
    /**
     * Constructor.
     * Establishes the subdirectory
     * in which test data files are stored.
     */
    public FBCompSpacingTA()
    {
        super(SUBDIR);
    }

    /**
     * Obtains an instance
     * of the class under test.
     * 
     * @param   supplier    supplier with which to construct instance
     */
    @Override
    public Feedback getFeedbackInstance( DoubleSupplier supplier )
    {
        Feedback    feedback    = new SpacingFeedback( supplier );
        return feedback;
    }

}
