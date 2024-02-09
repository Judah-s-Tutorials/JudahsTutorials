package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class RenderingDemo1A
{
    /** Header array for configuring GUI's JTable. */
    private final String[]      headers = 
        { "State", "Population", "Latitude", "Longitude" };
    /** Data array for configuring GUI's JTable. */
    private final Object[][]    data    = 
        State.getDataSet( "state", "population", "latitude", "longitude" );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new RenderingDemo1A() );
    }
    
    /**
     * Constructor.
     * Configures and displays application frame.
     * Must be invoked on the EDT.
     */
    public RenderingDemo1A()
    {
        JFrame      frame       = new JFrame( "Tabel Model Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        LocalTableModel     model       = 
            new LocalTableModel( data, headers );
        JTable              table       = new JTable( model );
        JPanel              contentPane = new JPanel( new BorderLayout() );
        JScrollPane         scrollPane  = new JScrollPane( table );
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
    
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        public LocalTableModel( Object[][] data, Object[] headers )
        {
            super( data, headers );
        }
        
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = null;
            if ( col == 1 )
                clazz = Integer.class;
            else if ( col == 2 || col == 3)
                clazz = Double.class;
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
}
