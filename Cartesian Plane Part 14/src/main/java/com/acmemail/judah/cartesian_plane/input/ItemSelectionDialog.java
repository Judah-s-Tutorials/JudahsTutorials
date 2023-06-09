package com.acmemail.judah.cartesian_plane.input;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.function.Predicate;

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

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;

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

    /** Final status of this selection process; OK_STATUS or CANCEL_STATUS. */
    private int closeStatus     = -1;
    
    /**
     * Constructor.
     * Configures this dialog.
     * Sets the owner of this dialog
     * to null.
     * 
     * @param title dialog title
     * @param items items available for selection in the dialog
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
     * @param items items available for selection in the dialog
     */
    public ItemSelectionDialog( Window owner, String title, Object[] items )
    {
        jList = new JList<>( items );
        dialog  = new JDialog( owner, title );
        dialog.setModal( true );
        dialog.setContentPane( getContentPane() );
        dialog.pack();
        
        if ( items.length > 0 )
            jList.setSelectedIndex( 0 );
        else
            enableOKButton( false );
        
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
        @SuppressWarnings("serial")
        Action      action      = new AbstractAction() {
            public void actionPerformed( ActionEvent evt ) {
                setAndClose( CANCEL_STATUS );
            }
        };
        
        char        esc         = '\u001b';
        KeyStroke   keyStroke   = KeyStroke.getKeyStroke( esc );
        String      key         = "com.acmemail.judah.CancelOnEscape";
        InputMap    inMap       = jList.getInputMap();
        ActionMap   actMap      = jList.getActionMap();
        
        // At runtime, Swing detects that the escape key has been pressed...
        // 'esc' is used as a key to the input map to lookup an object...
        // ... which is used as key to the action map to lookup an Action...
        // the actionPerformed method of the Action is executed
        actMap.put( key, action );
        inMap.put( keyStroke, key );

        return pane;
    }
    
    private void enableOKButton( boolean status )
    {
        Predicate<JComponent>   pred    = c ->
            c instanceof JButton &&
            ((JButton)c).getText().equals( "OK" );
        JComponent              comp    =
            ComponentFinder.find( dialog, pred );
        comp.setEnabled( status );
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
