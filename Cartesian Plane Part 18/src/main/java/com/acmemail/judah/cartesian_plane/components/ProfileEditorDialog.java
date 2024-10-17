package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

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
    private static final String dialogTitle     = "Profile Editor";
    /** The component that the sample graph is drawn on. */
    private final JComponent        canvas;
    /** The encapsulated ProfileEditor. */
    private final ProfileEditor     editor;
    
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
        contentPane.add( BorderLayout.SOUTH, getButtonPanel() );
        
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
    
    /**
     * Creates a panel with a FlowLayout
     * that contains the dialog control buttons
     * (Apply, Cancel, etc.).
     * 
     * @return  a panel containing the dialog control buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel      panel   = new JPanel();
        JButton     okay    = new JButton( "OK" );
        JButton     apply   = new JButton( "Apply" );
        JButton     reset   = new JButton( "Reset" );
        JButton     cancel  = new JButton( "Cancel" );

        okay.addActionListener( e -> close( JOptionPane.OK_OPTION ) );
        cancel.addActionListener( e -> close( JOptionPane.CANCEL_OPTION ) );
        apply.addActionListener( e -> editor.apply() );
        reset.addActionListener( e -> editor.reset() );
        reset.addActionListener( e -> canvas.repaint() );

        panel.add( okay );
        panel.add( apply );
        panel.add( reset );
        panel.add( cancel );

        return panel;
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
