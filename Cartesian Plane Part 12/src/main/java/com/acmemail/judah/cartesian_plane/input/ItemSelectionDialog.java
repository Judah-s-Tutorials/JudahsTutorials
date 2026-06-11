package com.acmemail.judah.cartesian_plane.input;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 * Displays a list of items
 * from which the operator
 * may make a single selection.
 * 
 * @author Jack Straub
 */
public class ItemSelectionDialog
{    
    /** Indicates the operator made a selection. */
    private static final int OK_STATUS      = 0;
    /** Indicates the operator cancelled the operation. */
    private static final int CANCEL_STATUS  = 1;
    
    /** The frame that encapsulates the dialog. */
    private final JDialog           dialog;
    /** 
     * The component that encapsulates the list of items 
     * available for selection.
     */
    private final JList<Object>     jList;

    /**
     * Final status of this selection process;
     * OK_STATUS or CANCEL_STATUS.
     * Initialized to -1; has no meaningful value
     * until the dialog is first shown.
     */
    private int closeStatus     = -1;
    
    /**
     * Constructor.
     * Configures this dialog.
     * Sets the owner of this dialog
     * to null.
     * 
     * @param title dialog title
     * @param items items available for selection in the dialog;
     *              must not be null
     *
     * @throws NullPointerException if items is null
     */
    public ItemSelectionDialog( String title, Object[] items )
    {
        this( null, title, items );
    }

    /**
     * Constructor.
     * Configures this dialog.
     * 
     * @param owner the owner of this dialog
     * @param title dialog title
     * @param items items available for selection in the dialog;
     *              must not be null
     *
     * @throws NullPointerException if items is null
     */
    public ItemSelectionDialog( Window owner, String title, Object[] items )
    {
        Objects.requireNonNull( items, "items" );
        jList = new JList<>( items );
        if ( items.length > 0 )
            jList.setSelectedIndex( 0 );
        
        dialog  = new JDialog( owner, title );
        dialog.setModal( true );
        dialog.setContentPane( getContentPane() );
        dialog.pack();
    }
    
    /**
     * Displays this dialog, 
     * and returns the final status 
     * of the operation
     * when the dialog is dismissed.
     * If the operation ended in a selection
     * the index to the selected item is returned.
     * If no selection is made
     * -1 is returned.
     * 
     * @return  the index to the selected item, or -1 if no selection is made
     */
    public int show()
    {
        closeStatus = CANCEL_STATUS;
        dialog.setVisible( true );
        
        int rval    = 
            closeStatus == OK_STATUS ? jList.getSelectedIndex() : -1;
        return rval;
    }
    
    /**
     * Returns a fully configured component
     * for use as a content pane.
     * 
     * @return  a fully configured component for use as a content pane
     */
    private JPanel getContentPane()
    {
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( getScrolledList(), BorderLayout.CENTER );
        pane.add( getButtonPanel(), BorderLayout.SOUTH );
        return pane;
    }
    
    /**
     * Returns a panel containing
     * fully configured OK and Cancel buttons.
     * 
     * @return  a panel containing fully configured OK and Cancel buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel  panel           = new JPanel();
        JButton okButton        = new JButton( "OK" );
        JButton cancelButton    = new JButton( "Cancel" );
        panel.add( okButton );
        panel.add( cancelButton );
        
        okButton.addActionListener( e -> setAndClose( OK_STATUS ) );
        cancelButton
            .addActionListener( e -> setAndClose( CANCEL_STATUS ) );
        
        JRootPane   rootPane    = dialog.getRootPane();
        rootPane.setDefaultButton( okButton );
        
        return panel;
    }
    
    /**
     * Returns a scroll pane
     * encapsulating the list of items
     * available for selection.
     * 
     * Precondition:
     *     the list of items is fully configured
     * 
     * @return  
     *      a scroll pane encapsulating the list of items
     *      available for selection
     */
    private JScrollPane getScrolledList()
    {
        JScrollPane pane    = new JScrollPane();
        pane.setViewportView( jList );

        // Define action
        Action      action      = new AbstractAction() {
            @Override
            public void actionPerformed( ActionEvent evt ) {
                setAndClose( CANCEL_STATUS );
            }
        };
        
        KeyStroke   keyStroke   =
            KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );
        String      key         = "com.acmemail.judah.CancelOnEscape";
        // Register on the root pane with WHEN_IN_FOCUSED_WINDOW
        // so that escape cancels the dialog no matter which
        // component has focus.
        JRootPane   rootPane    = dialog.getRootPane();
        InputMap    inMap       =
            rootPane.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap   actMap      = rootPane.getActionMap();
        
        // At runtime, Swing detects that the escape key has been pressed...
        // the keystroke is used as a key to the input map to lookup an object...
        // ... which is used as key to the action map to lookup an Action...
        // the actionPerformed method of the Action is executed
        actMap.put( key, action );
        inMap.put( keyStroke, key );

        return pane;
    }
    
    /**
     * Sets the final status
     * of the dialog
     * to the given value.
     * 
     * @param closeStatus   the given value
     */
    private void setAndClose( int closeStatus )
    {
        this.closeStatus = closeStatus;
        dialog.setVisible( false );
    }
}
