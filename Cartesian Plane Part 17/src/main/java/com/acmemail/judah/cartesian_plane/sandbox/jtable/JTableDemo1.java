package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class JTableDemo1
{
    private final String[]      headers = 
    { "First", "Last", "ID" };
    private final Object[][]    data    =
    {
        { "Jill", "Biden", 131157 },
        { "Melania", "Trump", 171129 },
        { "Michelle", "Obama", 200231 },
        { "Laura", "Bush", 100719 },
        { "Hillary", "Clinton", 131157 },
    };
    private final JTable        table   = new JTable( data, headers );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( JTableDemo1::new );
    }
    
    private JTableDemo1()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
}
