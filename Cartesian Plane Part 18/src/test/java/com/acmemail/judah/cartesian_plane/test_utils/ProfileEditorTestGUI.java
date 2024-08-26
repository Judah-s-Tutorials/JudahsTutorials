package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.ProfileEditor;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * An instance of this class
 * is used to display and manage a ProfileEditor.
 * Interaction with the ProfileEditor
 * is conducted via operations
 * performed on the EDT.
 * 
 * @author Jack Straub
 * 
 * @see ProfileEditorTestBase
 */
public class ProfileEditorTestGUI extends ProfileEditorTestBase
{    
    /** The singleton for this GUI test object. */
    private static ProfileEditorTestGUI testGUI;
    
    /**
     * Instantiates and returns a ProfileEditorTestGUI.
     * May be invoked from within the EDT.
     * If not invoked from the EDIT,
     * instantiation is scheduled via a task on the EDT.
     * 
     * @param profile   
     *      the profile to be encapsulated in the ProfileEditor
     *      
     * @return the instantiated ProfileEditorTestGUI
     */
    public static ProfileEditorTestGUI getTestGUI( Profile profile )
    {
        if ( testGUI != null )
            ;
        else if ( SwingUtilities.isEventDispatchThread() )
            testGUI = new ProfileEditorTestGUI( profile );
        else
            GUIUtils.schedEDTAndWait( () -> 
                testGUI = new ProfileEditorTestGUI( profile )
            );
        return testGUI;
    }
    
    /**
     * Constructor.
     * Fully initializes this ProfileEditorTestGUI_old.
     * Must be invoked from the EDT.
     * 
     * @param profile   Profile to install in the ProfileEditor
     */
    private ProfileEditorTestGUI( Profile profile )
    {
        super( new ProfileEditor( profile ) );

        JFrame          editorFrame = new JFrame( "ProfileEditor Test GUI" );
        JPanel          contentPane     = new JPanel( new BorderLayout() );
        ProfileEditor   profileEditor = getProfileEditor();
        contentPane.add( profileEditor, BorderLayout.CENTER );
        
        editorFrame.setContentPane( contentPane );
        editorFrame.pack();
        editorFrame.setVisible( true );
    }
}
