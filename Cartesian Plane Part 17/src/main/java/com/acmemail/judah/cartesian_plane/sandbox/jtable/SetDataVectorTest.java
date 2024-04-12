package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class SetDataVectorTest
{
    private final Object[]      headers = { "Col 1", "Col 2" };
    private final Object[][]    data    = { 
        { "a", 1000 },{ "b", 2000 },{ "c", 3000 }
    };
    
    private final DefaultTableModel model   = 
        new LocalTableModel( data, headers );
    private final JTable            table   = new JTable( model );
    
    private int count   = 0;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( SetDataVectorTest::new );
    }
    
    private SetDataVectorTest()
    {
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column1     = colModel.getColumn( 1 );
        column1.setCellRenderer( new ValueRenderer() );

        JFrame      frame       = new JFrame( "SetDataVectorTest" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( table, BorderLayout.CENTER );
        
        JPanel      buttonPanel = new JPanel();
        JButton     delete      = new JButton( "Delete" );
        JButton     exit        = new JButton( "Exit" );
        delete.addActionListener( this::deleteAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( delete );
        buttonPanel.add( exit );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    @SuppressWarnings("rawtypes")
    private void deleteAction( ActionEvent evt )
    {
        Vector<Vector>  data        = model.getDataVector();
        List<Object>    cHeaders    = Arrays.asList( headers );
        Vector<Object>  vHeaders    = new Vector<>( cHeaders );
        model.setDataVector( data, vHeaders );
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column1     = colModel.getColumn( 1 );
        column1.setCellRenderer( new ValueRenderer() );
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
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
    
    @SuppressWarnings("serial")
    private class ValueRenderer extends DefaultTableCellRenderer
    {
        @Override
        public void setValue( Object value )
        {
            System.out.println( "setValue: " + count++ );
            if ( value != null )
            {
                String  format   = "%,d";
                String  fmtValue = String.format( format, value );
                setText( fmtValue );
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            else
                setText( "" );
        }
    }
}
