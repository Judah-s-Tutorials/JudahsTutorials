package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ItemSelectionDialog
{    
    public static final int OK_STATUS       = 0;
    public static final int CANCEL_STATUS   = 1;
    
    private final JDialog           dialog;
    private final JList<Object>     jList;

    private int closeStatus     = -1;
    
    public ItemSelectionDialog( String title, Object[] items )
    {
        this( null, title, items );
    }

    public ItemSelectionDialog( Window owner, String title, Object[] items )
    {
        jList = new JList<>( items );
        if ( items.length > 0 )
            jList.setSelectedIndex( 0 );
        
        dialog  = new JDialog( owner, title );
        dialog.setModal( true );
        dialog.setContentPane( getContentPane() );
        dialog.pack();
    }
    
    public int show()
    {
        closeStatus = CANCEL_STATUS;
        dialog.setVisible( true );
        
        int rval    = 
            closeStatus == OK_STATUS ? jList.getSelectedIndex() : -1;
        return rval;
    }
    
    private JPanel getContentPane()
    {
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( getScrolledList(), BorderLayout.CENTER );
        pane.add( getButtonPanel(), BorderLayout.SOUTH );
        return pane;
    }
    
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
        
        return panel;
    }
    
    private JScrollPane getScrolledList()
    {
        JScrollPane pane    = new JScrollPane();
        pane.setViewportView( jList );
        return pane;
    }
    
    private void setAndClose( int closeStatus )
    {
        this.closeStatus = closeStatus;
        dialog.setVisible( false );
    }
}
