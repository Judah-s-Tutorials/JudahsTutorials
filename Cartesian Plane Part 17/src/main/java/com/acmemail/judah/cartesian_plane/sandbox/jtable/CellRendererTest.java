package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

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

public class CellRendererTest
{
    private final Object[]      headers = { "Col 1", "Col 2" };
    private final Object[][]    data    = { 
        { "a", 1000 },{ "b", 2000 },{ "c", 3000 }
    };
    
    private final DefaultTableModel model   = 
        new LocalTableModel( data, headers );
    private final JTable            table   = new JTable( model );
    
    private int count   = 0;
    
    private Robot robot;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( CellRendererTest::new );
    }
    
    private CellRendererTest()
    {
        try
        {
            robot = new Robot();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
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
        Rectangle   rect        = table.getCellRect( 1, 1, false );
        Point       tableLoc    = table.getLocationOnScreen();
        int         xco         = tableLoc.x + rect.x + rect.width / 2;
        int         yco         = tableLoc.y + rect.y + rect.height / 2;
        robot.mouseMove( xco, yco );
        robot.mousePress( InputEvent.BUTTON1_DOWN_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
        System.out.println( rect );
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
