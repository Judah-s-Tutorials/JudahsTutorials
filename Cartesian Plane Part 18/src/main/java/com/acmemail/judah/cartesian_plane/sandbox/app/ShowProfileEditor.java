package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.profile.Profile;
import com.acmemail.judah.cartesian_plane.sandbox.profile.ProfileEditor;

/**
 * Displays the panel containing the profile editor.
 * This application does not display the feedback component.
 * To view the editor in conjunction with its feedback component,
 * run {@linkplain ShowProfileEditorDialog};
 * 
 * @author Jack Straub
 */
public class ShowProfileEditor
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ShowProfileEditor   editor  = new ShowProfileEditor();
        SwingUtilities.invokeLater( () -> editor.buildGUI() );
    }
    
    /**
     * Build and display an application window
     * containing the ProfileEditor component.
     */
    private void buildGUI()
    {
        JFrame  frame   = new JFrame( "Show Profile Editor" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        JPanel          pane    = new JPanel( new BorderLayout() );
        ProfileEditor   editor  = new ProfileEditor( new Profile() );
        pane.add( editor, BorderLayout.WEST );
        frame.setContentPane( pane );
        frame.pack();
        GUIUtils.center( frame );
        frame.setVisible( true );
    }
}
