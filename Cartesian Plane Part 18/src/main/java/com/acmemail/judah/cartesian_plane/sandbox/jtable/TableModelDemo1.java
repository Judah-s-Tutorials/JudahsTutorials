package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * Base class for showing how to 
 * configure a JTable's headers and data
 * using a TableModel.
 * This class does not have a main method.
 * The main method is supplied by
 * a client class that
 * invokes this class's constructor
 * with a TableModel.
 * This class then instantiates
 * and displays a JTable.
 * All the interesting code
 * is in the client classes.
 * 
 * @author Jack Straub
 * 
 * @see TableModelDemo1A
 * @see TableModelDemo1B
 * @see TableModelDemo1C
 */
public class TableModelDemo1
{
    public TableModelDemo1( TableModel model )
    {
        JFrame      frame       = new JFrame( "Tabel Model Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JTable      table       = new JTable( model );
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
