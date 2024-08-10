package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.ProfileEditor;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * An instance of this class
 * is used to display and manage a ParameterPanel.
 * Interaction with the ParameterPanel
 * is conducted via operations
 * performed on the EDT.
 * 
 * @author Jack Straub
 */
public class ProfileEditorTestGUI
{
    private static final String gridTitle       = "Grid";
    private static final String axesTitle       = "Axes";
    private static final String gridLinesTitle  = "Grid Lines";
    private static final String ticMajorTitle   = "Major Tics";
    private static final String ticMinorTitle   = "Major Tics";
    
    private static final String nameLabel       = "Name";
    private static final String spacingLabel    = "Lines/Unit";
    private static final String lengthLabel     = "Lines/Unit";
    private static final String weightLabel     = "Weight";
    
    /** The GUI test object. */
    private static ProfileEditorTestGUI testGUI;
    /* The Profile encapsulated by the ProfileEditor under test. */
    private final Profile               profile;
    /** The application frame that displays the ProfileEditor. */
    private final JFrame                editorFrame;
    /** The ProfileEditor under test. */
    private final ProfileEditor         profileEditor;
    
    private final Map<String,JPanel>    titlePanelMap   = new HashMap<>();
    
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
        if ( SwingUtilities.isEventDispatchThread() )
            testGUI = new ProfileEditorTestGUI( profile );
        else
            GUIUtils.schedEDTAndWait( () -> 
                testGUI = new ProfileEditorTestGUI( profile )
            );
        return testGUI;
    }
    
    /**
     * Constructor.
     * Fully initializes this ProfileEditorTestGUI.
     * 
     * @param profile   Profile to install in the ProfileEditor
     */
    private ProfileEditorTestGUI( Profile profile )
    {
        this.profile = profile;
        editorFrame = new JFrame( "ProfileEditor Test GUI" );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        profileEditor = new ProfileEditor( profile );
        contentPane.add( profileEditor, BorderLayout.CENTER );
    }
    
    private void getAllTitledPanels( Container source )
    {
        if ( source instanceof JPanel )
        {
            JPanel  panel   = (JPanel)source;
            String  title   = getBorderTitle( panel.getBorder() );
            if ( title != null )
                titlePanelMap.put( title, panel );
        }
        Arrays.stream( source.getComponents() )
            .filter( c -> c instanceof Container )
            .map( c -> (Container)c )
            .forEach( c -> getAllTitledPanels( c ) );
    }
    
    private String getBorderTitle( Border border )
    {
        String  title   = null;
        if ( border instanceof TitledBorder )
        {
            title = ((TitledBorder)border).getTitle();
        }
        else if ( border instanceof CompoundBorder )
        {
            CompoundBorder  compBorder  = (CompoundBorder)border;
            Border          inside      = compBorder.getInsideBorder();
            Border          outside     = compBorder.getOutsideBorder();
            if ( (title = getBorderTitle( inside )) == null )
                title = getBorderTitle( outside );
        }
        return title;
    }
    
    private class LinePropertyComponents
    {
//        private final JSpinner      spacingComponent;
//        private final JSpinner      weightComponent;
//        private final JSpinner      lengthComponent;
//        private final boolean       drawComponent;
//        private final ColorEditor   colorComponent;
//        
//        private JPanel getTitledPanel( String title )
//        {
//            
//        }
    }
}
