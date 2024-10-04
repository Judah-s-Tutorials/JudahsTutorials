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
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class ProfileEditorFeedbackTestGUI
{
    private static ProfileEditorFeedbackTestGUI testGUI     = null;
    
    private final JFrame                feedbackFrame;
    private final ProfileEditorFeedback feedbackPanel;
    private final JDialog               adHocDialog;
    private final AdHocPanel            adHocPanel;
    
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
        adHocPanel = new AdHocPanel( feedbackPanel );
        adHocDialog = makeAdHocDialog( adHocPanel );
        feedbackFrame = new JFrame( "ProfileEditor Test GUI" );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( feedbackPanel, BorderLayout.CENTER );
        
        feedbackFrame.setContentPane( contentPane );
        feedbackFrame.pack();
        feedbackFrame.setVisible( true );
    }
    
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
    
    public void showAdHocDialog( BufferedImage image )
    {
        adHocPanel.setImage( image );
        adHocDialog.setVisible( true );
    }
    
    private JDialog makeAdHocDialog( JComponent adHocPanel )
    {
        JDialog     dialog      = new JDialog();
        dialog.setTitle( "Ad Hoc Dialog" );
        dialog.setModal( true );
        dialog.setContentPane( adHocPanel );
        dialog.pack();
        return dialog;
    }
    
    @SuppressWarnings("serial")
    private static class AdHocPanel extends JPanel
    {
        private BufferedImage   image   = null;
        
        public AdHocPanel( JComponent feedbackPanel )
        {
            Dimension   prefSize    = feedbackPanel.getPreferredSize();
            setPreferredSize( prefSize );
        }
        
        public void setImage( BufferedImage image )
        {
            this.image = image;
        }
        
        @Override
        public void paintComponent( Graphics gtx )
        {
            if ( image != null )
                gtx.drawImage( image, 0, 0, this );
        }
    }
}
