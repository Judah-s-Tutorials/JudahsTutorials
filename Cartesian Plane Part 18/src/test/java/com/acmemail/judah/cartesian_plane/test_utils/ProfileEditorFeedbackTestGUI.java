package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorFeedback;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorFeedbackTest;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * This class is for support of the 
 * {@linkplain ProfileEditorFeedbackTest} JUnit test.
 * It's main job is to make sure that,
 * when required,
 * AWT operations are conducted
 * in the context of the EDT.
 * It's also capable of posting
 * a modal dialog displaying a BufferedImage,
 * which may be useful for debugging purposes.
 * 
 * @author Jack Straub
 */
public class ProfileEditorFeedbackTestGUI
{
    /** The sole instance of this class. */
    private static ProfileEditorFeedbackTestGUI testGUI     = null;
    
    /** 
     * Main application frame for displaying the
     * feedback panel under test.
     */
    private final JFrame                feedbackFrame;
    /** The feedback panel under test. */
    private final ProfileEditorFeedback feedbackPanel;
    /** 
     * Dialog for displaying an image
     * at the client's request.
     */
    private final JDialog               adHocDialog;
    /** 
     * JPanel for displaying an image
     * at the client's request.
     */
    private final AdHocPanel            adHocPanel;
    
    /** Most recent image generated from feedback panel. */
    private BufferedImage   image;
    
    /** For short term use, particularly in lambdas. */
    private Integer         adHocInt;
    
    /**
     * Obtain a singleton of this class.
     * May be invoked from inside or outside
     * the EDT.
     * 
     * @param profile   
     *      Profile to be encapsulated in the feedback panel under test
     * @return
     */
    public static 
    ProfileEditorFeedbackTestGUI getTestGUI( Profile profile )
    {
        if ( testGUI != null )
            ;
        else if ( SwingUtilities.isEventDispatchThread() )
            testGUI = new ProfileEditorFeedbackTestGUI( profile );
        else
            GUIUtils.schedEDTAndWait( () -> 
                testGUI = new ProfileEditorFeedbackTestGUI( profile )
            );
        return testGUI;
    }

    /**
     * Constructor.
     * Fully initializes an object of this type.
     * Must be invoked from the EDT.
     * 
     * @param profile   
     *      Profile used to instantiate the feedback panel under test
     */
    private ProfileEditorFeedbackTestGUI( Profile profile )
    {
        feedbackPanel = new ProfileEditorFeedback( profile );
        adHocPanel = new AdHocPanel( feedbackPanel );
        adHocDialog = makeAdHocDialog( adHocPanel );
        feedbackFrame = new JFrame( "ProfileEditor Test GUI" );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( feedbackPanel, BorderLayout.CENTER );
        
        feedbackFrame.setContentPane( contentPane );
        feedbackFrame.pack();
        feedbackFrame.setVisible( true );
    }
    
    /**
     * Refreshes and returns an image
     * of the feedback panel under test.
     * 
     * @return  the refreshed image
     */
    public BufferedImage getImage()
    {
        GUIUtils.schedEDTAndWait( () -> {
            feedbackPanel.repaint();
            
            int     width       = feedbackPanel.getWidth();
            int     height      = feedbackPanel.getHeight();
            int     type        = BufferedImage.TYPE_INT_RGB;
            image = new BufferedImage( width, height, type );
            
            Graphics    graphics    = image.getGraphics();
            feedbackPanel.paintComponent( graphics );
        });
        return image;
    }
    
    /**
     * Gets the current width of the feedback panel under test.
     * 
     * @return  the current width of the feedback panel under test
     */
    public int getWidth()
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocInt = feedbackPanel.getWidth()
        );
        return adHocInt;
    }
    
    /**
     * Gets the current height of the feedback panel under test.
     * 
     * @return  the current height of the feedback panel under test
     */
    public int getHeight()
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocInt = feedbackPanel.getHeight()
        );
        return adHocInt;
    }
    
    /**
     * Displays the given image in a modal dialog.
     * Returns after the dialog is dismissed.
     * 
     * @param image the given image
     */
    public void showAdHocDialog( BufferedImage image )
    {
        adHocPanel.setImage( image );
        adHocDialog.setVisible( true );
    }
    
    /**
     * Instantiates the dialog used for
     * ad hoc display of images.
     * 
     * @param adHocPanel    the panel to embed in the dialog
     * 
     * @return  the instantiated dialog
     * 
     * @see #showAdHocDialog(BufferedImage)
     * @see AdHocPanel
     */
    private JDialog makeAdHocDialog( JComponent adHocPanel )
    {
        JDialog     dialog      = new JDialog();
        dialog.setTitle( "Ad Hoc Dialog" );
        dialog.setModal( true );
        dialog.setContentPane( adHocPanel );
        dialog.pack();
        return dialog;
    }
    
    /**
     * Panel to display an image of the client's choice.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class AdHocPanel extends JPanel
    {
        /** The image to display. */
        private BufferedImage   image   = null;
        
        /**
         * Constructor.
         * Sets the preferred size of this panel
         * to the preferred size of the given component.
         * 
         * @param comp  the given component
         */
        public AdHocPanel( JComponent comp )
        {
            Dimension   prefSize    = comp.getPreferredSize();
            setPreferredSize( prefSize );
        }
        
        /**
         * Sets the image to display in this panel
         * to the given image.
         * 
         * @param image the given image
         */
        public void setImage( BufferedImage image )
        {
            this.image = image;
        }
        
        @Override
        public void paintComponent( Graphics gtx )
        {
            super.paintComponent( gtx );
            if ( image != null )
                gtx.drawImage( image, 0, 0, this );
        }
    }
}
