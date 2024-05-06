package com.acmemail.judah.cartesian_plane.sandbox.experimental.line_editor_rev2;

import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

public class ShowGraphManagerDialog
{
    private final GraphManagerDialog    dialog;
    private final ActivityLog           log;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(ShowGraphManagerDialog::new  );
    }
    
    public ShowGraphManagerDialog()
    {
        JFrame      frame   = new JFrame( "Show Font Editor" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        dialog  = new GraphManagerDialog( frame );
        log = new ActivityLog( "Activity Log" );

        Border      border  = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        JButton     edit    = new JButton( "Edit" );
        edit.addActionListener( this::showDialog );
        
        JPanel      panel   = new JPanel();
        panel.setBorder( border );
        panel.add( exit );
        panel.add( edit );
        
        frame.setContentPane( panel );
        frame.setLocation( 200, 200 );
        log.setLocation( 400, 200 );
        frame.pack();
        frame.setVisible( true );
        log.setVisible( true );
    }
    
    private void showDialog( ActionEvent evt )
    {
        int     iResult = dialog.showDialog();
        String  sResult = null;
        if ( iResult == JOptionPane.OK_OPTION )
            sResult = "OK";
        else if ( iResult == JOptionPane.CANCEL_OPTION )
            sResult = "Cancel";
        else
            sResult = "???????";
        
        log.append( "result: " + sResult );
        log.append( "*****************************" );
    }
}
