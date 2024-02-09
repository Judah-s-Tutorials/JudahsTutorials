package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class RenderingDemo1B
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
        SwingUtilities.invokeLater( () -> new RenderingDemo1B() );
    }
    
    /**
     * Constructor.
     * Configures and displays application frame.
     * Must be invoked on the EDT.
     */
    public RenderingDemo1B()
    {
        JFrame      frame       = new JFrame( "Rendering Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        LocalTableModel model           = 
            new LocalTableModel( data, headers );
        JTable          table           = new JTable( model );
        CoordRenderer   coordRenderer   = new CoordRenderer();
        IntRenderer     intRenderer     = new IntRenderer();
        setCellRenderer( table, 1, intRenderer );
        setCellRenderer( table, 2, coordRenderer );
        setCellRenderer( table, 3, coordRenderer );

        JPanel          contentPane     = new JPanel( new BorderLayout() );
        JScrollPane     scrollPane      = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        JPanel          buttonPanel     = new JPanel();
        JButton         exit            = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
                
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void 
    setCellRenderer( JTable table, int col, TableCellRenderer renderer )
    {
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column      = colModel.getColumn( col );
        column.setCellRenderer( renderer );
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
    private static class IntRenderer extends DefaultTableCellRenderer
    {
        @Override
        public void setValue( Object value )
        {
            if ( value != null )
            {
                String fmtValue = String.format( "%,d", value );
                setText( fmtValue );
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            else
                setText( "" );
        }
    }
    
    @SuppressWarnings("serial")
    private static class CoordRenderer extends DefaultTableCellRenderer
    {
        @Override
        public void setValue( Object value )
        {
            if ( value != null )
            {
                String  fmtValue = String.format( "%.3f", value );
                setText( fmtValue );
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            else
                setText( "" );
        }
    }
}
