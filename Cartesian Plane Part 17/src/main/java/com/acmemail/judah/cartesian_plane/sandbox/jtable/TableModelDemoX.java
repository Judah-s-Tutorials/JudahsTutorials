package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class TableModelDemoX
{
    private final String[]      headers = 
        { "State", "Population", "Latitude", "Longitude" };
    private final Object[][]    data    = 
        State.getDataSet( "state", "population", "latitude", "longitude" );
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new TableModelDemoX() );
    }
    
    public TableModelDemoX()
    {
        JFrame      frame       = new JFrame( "Tabel Model Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        LocalTableModel     model       = 
            new LocalTableModel( data, headers );
        JTable              table       = new JTable( model );
        TableColumnModel    colModel    = table.getColumnModel();
        colModel.getColumn( 1 )
            .setCellRenderer( new IntTableCellRenderer() );
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
//        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void testAction( DefaultTableModel model )
    {
        IntStream.range( 0, 5 ).forEach( i -> 
            System.out.println( model.getValueAt( i, 2) ) );
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
    
    @SuppressWarnings("serial")
    private static class IntTableCellRenderer 
        extends DefaultTableCellRenderer
    {
        @Override
        public void setValue( Object value )
        {
            Object  modValue    = value;
            if ( value != null )
            {
                String  name    = value.getClass().getSimpleName();
                System.out.println( value + ", " + name );
                modValue = String.format( "%,d", value );
            }
            super.setValue( modValue );
        }
        
        @Override
        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
        )
        {
            if ( value != null )
            {
                String  name    = value.getClass().getSimpleName();
                System.out.print( value + ", " + name + ": " );
            }
            Component   comp    = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if ( comp instanceof JLabel )
            ((JLabel)comp).setHorizontalAlignment(SwingConstants.RIGHT);
            System.out.println( comp.getClass().getSimpleName() );
            return comp;
        }
    }
}
