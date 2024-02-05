package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.acmemail.judah.cartesian_plane.sandbox.jtable.panels.State;

public class JTableDemo3
{
    private static final String     prompt          =
        "Enter the name of a state and its population (in millions)";
    private static final String     stateHeader     =
        "<html><br>State</html>";
    private static final String     popHeader       =
        "<html><center>Population<br>(Millions)</center></html>";
    private static final String     actionHeader    =
        "<html><br>Action</html>";
    private static final String[]   headers         = 
    { stateHeader, popHeader, actionHeader };
    private static final Vector     headerVec       = 
        new Vector( Arrays.asList( headers ) );
    private static final Object[][] data            =
        State.getDataSet( stateHeader, popHeader );
//    {
//        { "Alabama", 5.024, false },
//        { "Alaska", .733, false },
//        { "Arizona", 7.152, false },
//        { "Arkansas", 3.1, false },
//        { "California", 39.538, false },
//        { "Colorado", 5.773, false },
//        { "Connecticut", 3.606, false },
//        { "Delaware", .990, false },
//        { "Florida", 21.538, false },
//        { "Georgia", 10.712, false },
//        { "Hawaii", 1.455, false },
//        { "Idaho", 1.839, false },
//        { "Illinois", 12.813, false },
//    };
    private static final Vector<Vector>  dataVec     = 
        new Vector( Arrays.asList( data ) );
    
    private JTable              table;
    private LocalTableModel     model;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        JTableDemo3 demo    = new JTableDemo3();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    private void buildGUI()
    {
        JFrame      frame       = new JFrame( "JTable Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        model = new LocalTableModel();
        table = new JTable( model );
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
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
        JButton     apply       = new JButton( "Apply" );
        JButton     exit        = new JButton( "Exit" );
        JButton     delete      = new JButton( "Delete Selected" );
        JButton     add         = new JButton( "New" );
        apply.addActionListener( this::applyAction );
        delete.addActionListener( this::deleteAction );
        add.addActionListener( this::newAction );
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( apply );
        buttonPanel.add( add );
        buttonPanel.add( delete );
        buttonPanel.add( exit );
        return buttonPanel;
    }
    
    private void newAction( ActionEvent evt )
    {
        String  input   = JOptionPane.showInputDialog( prompt );
        if ( input != null )
        {
            try
            {
                StringTokenizer tizer   = 
                    new StringTokenizer( input, ", " );
                int             count   = tizer.countTokens();
                if ( count < 1 )
                    throw new NumberFormatException( "Invalid entry" );
                if ( count > 2)
                    throw new NumberFormatException( "Invalid entry" );
                double      value   = 0;
                String      name    = tizer.nextToken().strip();
                if ( highlightState( name ) >= 0 )
                    throw new NumberFormatException( "Duplicate name" );
                if ( tizer.hasMoreTokens() )
                    value = Double.parseDouble( tizer.nextToken() );
                Object[]    newRow  = { name, value, false };
                ((LocalTableModel)table.getModel()).addRow( newRow );
            }
            catch ( NumberFormatException exc )
            {
                String  err = "Parse error: " + exc.getMessage();
                JOptionPane.showMessageDialog( null, err );
            }
        }
    }
    
    private int highlightState( String name )
    {
        int         rowCount    = table.getRowCount();
        int         inx         =
            IntStream.range( 0, rowCount )
                .filter( i -> name.equalsIgnoreCase( getName( i ) ) )
                .findFirst()
                .orElse( -1 );
        if ( inx >= 0 )
            table.setRowSelectionInterval( inx, inx );
        return inx;
    }
    
    private String getName( int inx )
    {
        if ( inx >= table.getRowCount() )
            throw new IndexOutOfBoundsException( "invalid row index" );
        Object  obj     = table.getValueAt( inx, 0 );
        if ( !(obj instanceof String) )
            throw new Error( "column not String" );
        String  name    = (String)obj;
        return name;
    }
    
    @SuppressWarnings("rawtypes")
    private void deleteAction( ActionEvent evt )
    {
        Vector<Vector>      dataVec     = model.getDataVector();
        Iterator<Vector>    iter        = dataVec.iterator();
        while ( iter.hasNext() )
        {
            Vector<?>vec = iter.next();
            Object  obj = vec.get( 2 );
            if ( obj instanceof Boolean )
            {
                if ( (Boolean)obj )
                    iter.remove();
            }
        }
        Vector vHeaders = new Vector( Arrays.asList( headers ) );
        model.setDataVector(dataVec, vHeaders);
    }
    
    private void applyAction( ActionEvent evt )
    {
        int rows    = model.getRowCount();
        int cols    = model.getColumnCount();
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++col )
            {
                System.out.print( model.getValueAt( row, col ) );
                if ( col < (cols - 1 ) )
                    System.out.print( ", " );
                else
                    System.out.println();
            }
    }
    
    private class LocalTableModel extends DefaultTableModel
    {
        public LocalTableModel()
        {
            super( data, headers );
//            super( dataVec, headerVec );
        }
        
        public LocalTableModel( Object[] headings )
        {
            super( null, headings );
        }
        
        @Override
        public Class<?> getColumnClass( int col )
        {
//            Class<?>    clazz   = col == 2 ?
//                Boolean.class : super.getColumnClass( col );
            Class<?>    clazz   = null;
            if ( col == 1 )
                clazz = Double.class;
            else if ( col == 2 )
                clazz = Boolean.class;
            else
                clazz = super.getColumnClass( col );
            return clazz;
        }
    }
    
    @SuppressWarnings("serial")
    private class LocalCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(
            JTable table, 
            Object value, 
            boolean isSelected, 
            boolean hasFocus, 
            int row, 
            int col
        )
        {
            Component   comp    = value instanceof Component ?
                (Component)value :
                super.getTableCellRendererComponent(
                    table, 
                    value, 
                    isSelected, 
                    hasFocus, 
                    row, 
                    col
                );
            return comp;       
        }
    }
}
