package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class TableModelDemo2C
{
    private final String[]      headers = 
        { "State", "Capital", "Population" };
    private final Object[][]    data    = 
        State.getDataSet( "state", "capital", "population" );
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new TableModelDemo2C() );
    }
    
    public TableModelDemo2C()
    {
        JFrame      frame       = new JFrame( "Tabel Model Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        @SuppressWarnings("serial")
        DefaultTableModel   model       =   
            new DefaultTableModel( data, headers ) {
                public Class<?> getColumnClass( int col ) 
                {
                    Class<?>    clazz   = col == 2 ? 
                        Integer.class : super.getColumnClass( col );
                    return clazz;
                }
        };
        
        JTable              table       = new JTable( model );
        JPanel              contentPane = new JPanel( new BorderLayout() );
        JScrollPane         scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        JButton test            = new JButton( "Test" );
        test.addActionListener( e -> testAction( model) );
        buttonPanel.add( test );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
                
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void testAction( DefaultTableModel model )
    {
        IntStream.range( 0, 5 ).forEach( i -> 
            System.out.println( model.getValueAt( i, 2) ) );
    }
}
