package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorFeedback;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class ProfileEditorFeedbackTestGUI
{
    private static ProfileEditorFeedbackTestGUI testGUI     = null;
    
    private final JFrame      feedbackFrame;
    private final ProfileEditorFeedback feedbackPanel;
    private BufferedImage   image;
    
    private Integer adHocInt;
    
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

    private ProfileEditorFeedbackTestGUI( Profile profile )
    {
        feedbackPanel = new ProfileEditorFeedback( profile );
        feedbackFrame = new JFrame( "ProfileEditor Test GUI" );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( feedbackPanel, BorderLayout.CENTER );
        
        feedbackFrame.setContentPane( contentPane );
        feedbackFrame.pack();
        feedbackFrame.setVisible( true );
    }
    
    public void repaint()
    {
        GUIUtils.schedEDTAndWait( () -> feedbackPanel.repaint() );
    }
    
    public BufferedImage getImage()
    {
        GUIUtils.schedEDTAndWait( () -> {
            int     width       = feedbackPanel.getWidth();
            int     height      = feedbackPanel.getHeight();
            int     type        = BufferedImage.TYPE_INT_RGB;
            image = new BufferedImage( width, height, type );
            
            Graphics    graphics    = image.getGraphics();
            feedbackPanel.paintComponent( graphics );
        });
        return image;
    }
    
    public int getWidth()
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocInt = feedbackPanel.getWidth()
        );
        return adHocInt;
    }
    
    public int getHeight()
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocInt = feedbackPanel.getHeight()
        );
        return adHocInt;
    }
}
