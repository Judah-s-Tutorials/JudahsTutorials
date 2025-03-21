package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.ProfileFileManager;

/**
 * This class represents a dialog
 * that the operator can use
 * to edit the properties
 * encapsulated in a {@link Profile}.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class ProfileEditorDialog extends JDialog
{
    /** The title for this dialog. */
    private static final String         dialogTitle     = "Profile Editor";
    /** The component that the sample graph is drawn on. */
    private final JComponent            canvas;
    /** The encapsulated ProfileEditor. */
    private final ProfileEditor         editor;
    /** The file manager for opening and saving Profile property files. */
    private final ProfileFileManager    fileMgr     = 
        new ProfileFileManager();
        
    /** 
     * Records the final status of the dialog (OK/Cancel).
     * Updated in the {@link #close(int)} method.
     * Returned by the {@link #showDialog} method.
     */
    private int result  = JOptionPane.CANCEL_OPTION;

    /**
     * Constructor.
     * Initializes the editor dialog.
     * 
     * @param profile   profile to edit
     */
    public ProfileEditorDialog( Window parent, Profile profile )
    {
        super( parent, dialogTitle, ModalityType.APPLICATION_MODAL );

        editor = new ProfileEditor( profile );
        canvas = editor.getFeedBack();
        
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( BorderLayout.CENTER, canvas );
        contentPane.add( BorderLayout.WEST, editor );
        contentPane.add( BorderLayout.SOUTH, getControlPanel() );
        
        // Make sure editor's profile is updated every time
        // this dialog becomes visible.
        WindowListener  listener    = new WindowAdapter() {
            @Override
            public void windowOpened( WindowEvent evt )
            {
                editor.reset();
            }
        };
        addWindowListener( listener );
        
        setContentPane( contentPane );
        pack();
        
        GUIUtils.center( this );
    }
    
    /**
     * Gets the ProfileEditor encapsulated in this dialog.
     * 
     * @return  the ProfileEditor encapsulated in this dialog
     */
    public ProfileEditor getProfileEditor()
    {
        return editor;
    }
    
    public ProfileFileManager getFileManager()
    {
        return fileMgr;
    }
    
    /**
     * Posts this dialog,
     * waits for it to complete,
     * and returns the final status
     * (JOptionPane.OK_OPTION or JOptionPane.CANCEL_OPTION);

     * @return  the status of the dialog upon closing
     */
    public int showDialog()
    {
        setVisible( true );
        return result;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.add( getOKCancelPanel() );
        panel.add( getOpenSavePanel() );
        return panel;
    }
    
    /**
     * Creates a panel with a FlowLayout
     * that contains the dialog control buttons
     * (Apply, Cancel, etc.).
     * 
     * @return  a panel containing the dialog control buttons
     */
    private JPanel getOKCancelPanel()
    {
        JPanel      panel   = new JPanel();
        JButton     okay    = new JButton( "OK" );
        JButton     apply   = new JButton( "Apply" );
        JButton     reset   = new JButton( "Reset" );
        JButton     cancel  = new JButton( "Cancel" );

        okay.addActionListener( e -> close( JOptionPane.OK_OPTION ) );
        cancel.addActionListener( e -> close( JOptionPane.CANCEL_OPTION ) );
        apply.addActionListener( e -> editor.apply() );
        reset.addActionListener( e -> resetProfile() );

        panel.add( okay );
        panel.add( apply );
        panel.add( reset );
        panel.add( cancel );

        return panel;
    }
    
    private JPanel getOpenSavePanel()
    {
        JPanel      panel   = new JPanel();
        JButton     open    = new JButton( "Open File" );
        JButton     save    = new JButton( "Save" );
        JButton     saveAs  = new JButton( "Save As" );
        JButton     close   = new JButton( "Close File" );

        open.addActionListener( e -> {
            Profile profile = editor.getProfile();
            if ( fileMgr.open( profile ) != null )
            {
                editor.refresh();
                editor.repaint();
            }
        });
        save.addActionListener( e -> 
            fileMgr.save( editor.getProfile() )
        );
        saveAs.addActionListener( e -> {
            editor.apply();
            fileMgr.saveAs( editor.getProfile() );
        });
        close.addActionListener( e -> fileMgr.close() );

        panel.add( open );
        panel.add( save );
        panel.add( saveAs );
        panel.add( close );

        return panel;
    }
    
    private void resetProfile()
    {
        Profile profile = editor.getProfile();
        File    file    = fileMgr.getCurrFile();
        if ( file != null )
            fileMgr.open( file, profile );
        else
            profile.reset();
        editor.refresh();
    }
    
    /**
     * Records the final dialog result
     * and closes the dialog.
     * If the final result is OK
     * the changes in the editor
     * are applied to the encapsulated Profile,
     * otherwise the Profile is reset.
     * 
     * @param result    the final dialog result
     */
    private void close( int result )
    {
        this.result = result;
        if ( result == JOptionPane.OK_OPTION )
            editor.apply();
        else
            editor.reset();
        setVisible( false );
    }
}
