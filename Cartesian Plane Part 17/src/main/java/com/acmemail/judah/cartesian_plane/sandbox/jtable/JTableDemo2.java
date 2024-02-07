package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class JTableDemo2
{
    private static String[]     headers     =
    { "State", "Abbreviation", "Population" };
    private static Object[][]   data        =
        State.getDataSet( "state", "abbreviation", "population" );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        JTableDemo2 demo    = new JTableDemo2();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    private void buildGUI()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        TableModel  model       = new LocalTableModel( data, headers );
        JTable      table       = new JTable( model );
        JScrollPane scrollPane  = new JScrollPane( table );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getButtonPanel()
    {
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( exit );
        return buttonPanel;
    }
    
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        public LocalTableModel( Object[][] data, Object[] headers)
        {
            super( data, headers );
        }
        
        @Override
        public Class<?> getColumnClass( int col )
        {
            Class<?>    clazz   = col == 2 ?
                Integer.class : super.getColumnClass( col );
            return clazz;
        }
    }
//    
//    @SuppressWarnings("serial")
//    private class LocalCellRenderer extends DefaultTableCellRenderer
//    {
//        @Override
//        public Component getTableCellRendererComponent(
//            JTable table, 
//            Object value, 
//            boolean isSelected, 
//            boolean hasFocus, 
//            int row, 
//            int col
//        )
//        {
//            Component   comp    = value instanceof Component ?
//                (Component)value :
//                super.getTableCellRendererComponent(
//                    table, 
//                    value, 
//                    isSelected, 
//                    hasFocus, 
//                    row, 
//                    col
//                );
//            return comp;       
//        }
//    }
}
