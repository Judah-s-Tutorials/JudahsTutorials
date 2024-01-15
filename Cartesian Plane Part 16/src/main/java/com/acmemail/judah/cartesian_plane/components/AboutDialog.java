package com.acmemail.judah.cartesian_plane.components;

import java.awt.Window;

import javax.swing.JDialog;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Encapsulates a dialog containing the text
 * in the About.html file
 * from the menu bar resource directory.
 * The text is displayed in an HTML-aware JEditorPane,
 * contained in a dialog provided by the 
 * {@linkplain MessagePane} class.
 * 
 * @author Jack Straub
 */
public class AboutDialog
{
    /** Title for the ABOUT dialog. */
    private static final String aboutTitle  = "About This Application";
    /** Default resource directory for files. */
    private static final String defResDir   = "MenuBarDocs/";
    /** Default resource directory for files. */
    private static final String aboutFile   = "About.html";
    /** Resource file for about message. */
    private final String        aboutResource;
    /** This object's message panel. */
    private final MessagePane  msgPanel;
    /** This object's dialog. */
    private final JDialog       dialog;
    
    /**
     * Constructor.
     * Creates dialog with null parent
     * and default resource directory.
     */
    public AboutDialog()
    {
        this( null,defResDir );
    }
    
    /**
     * Constructor.
     * Creates dialog with given parent
     * and default resource directory.
     */
    public AboutDialog( Window topWindow )
    {
        this( topWindow,defResDir );
    }

    /**
     * Constructor.
     * Creates dialog using given resource directory.
     * 
     * @param resDir    given resource directory
     * 
     * @throws ComponentException if dialog cannot be created
     */
    public AboutDialog( Window parent, String resDir )
    {
        aboutResource = resDir + "/" + aboutFile;
        msgPanel = MessagePane.ofResource( aboutResource, null );
        dialog = msgPanel.getDialog( parent, aboutTitle );
    }
    
    /**
     * Changes the visibility of the encapsulated dialog
     * to the given state.
     * If the given state is true,
     * the original text from the About.html file is restored.
     * 
     * @param   show    the given state
     * 
     * @throws ComponentException 
     *      if there's a problem reading the About.html resource file
     */
    public void showDialog( boolean show )
    {
        if ( show )
            msgPanel.setTextFromResource( aboutResource );
        dialog.setVisible( show );
    }
    
    /**
     * Returns the dialog produced by
     * an instance of this class.
     * 
     * @return  the dialog produced by this object
     */
    public JDialog getDialog()
    {
        return dialog;
    }
}
