package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.acmemail.judah.cartesian_plane.components.NameEditor;
import com.acmemail.judah.cartesian_plane.components.NameValidator;

public class VariableTableB
{
    private static final String     prompt  =
        "Enter [name] or [name,value]";
    private static final String     lineSep = System.lineSeparator();
    
    private final Object[]          headers = { "Name", "Value" };
    private final Vector<Object>    vHeader = 
        new Vector<>( Arrays.asList( headers ) );
    
    private final LocalTableModel   model   = 
        new LocalTableModel( headers );
    private final JTable            table   = new JTable( model );
    
    private int     dPrecision  = 4;
    private String  format      = "%." + dPrecision + "f";
    
    public VariableTableB()
    {
        ctorInit();
    }
    
    public VariableTableB( String inFile )
        throws IOException
    {
        ctorInit();
        if ( inFile != null )
        {
            try ( 
                FileInputStream inStream = new FileInputStream( inFile );
                Reader reader = new InputStreamReader( inStream );
            )
            {
                load( reader );
            }
        }
    }
    
    public VariableTableB( InputStream inStream )
        throws IOException
    {
        ctorInit();
        if ( inStream != null )
        {
            try ( Reader reader = new InputStreamReader( inStream  ) )
            {
                load( reader );
            }
        }
    }
    
    public VariableTableB( Reader reader )
        throws IOException
    {
        ctorInit();
        if ( reader != null )
        {
            load( reader );
        }
    }
    
    private void ctorInit()
    {
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column0     = colModel.getColumn( 0 );
        TableColumn         column1     = colModel.getColumn( 1 );
        column0.setCellEditor( new NameEditor() );
        column1.setCellRenderer( new ValueRenderer() );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoCreateRowSorter( true );
        model.addTableModelListener( this::tableChanged );
    }
    
    public void load( String inFile )
        throws IOException
    {
        try ( 
            FileInputStream inStream = new FileInputStream( inFile );
            Reader reader = new InputStreamReader( inStream );
        )
        {
            load( reader );
        }
    }
    
    public void load( InputStream inStream )
        throws IOException
    {
        try ( Reader reader = new InputStreamReader( inStream  ) )
        {
            load( reader );
        }
    }
    
    public void load( Reader reader )
        throws IOException
    {
        try ( 
            BufferedReader bReader = new BufferedReader( reader );
        )
        {
            bReader.lines()
                .map( NameRow::new )
                .map( r -> r.getData() )
                .forEach( o -> model.addRow( o ) );
        }
    }
    
    public JPanel getPanel()
    {
        Border      border      =
            BorderFactory.createEmptyBorder( 3, 3, 0, 3 );
        JPanel      panel       = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        panel.setBorder( border );
        
        Dimension   spSize      = scrollPane.getPreferredSize();
        JLabel      temp1       = new JLabel( headers[0].toString() );
        JLabel      temp2       = new JLabel( headers[1].toString() );
        int         prefWidth   =
            temp1.getPreferredSize().width + 
            temp2.getPreferredSize().width;
        spSize.width = prefWidth;
        scrollPane.setPreferredSize( spSize );
        panel.add( scrollPane, BorderLayout.CENTER );
        
        JPanel  buttons = new JPanel();
        JButton add     = new JButton( "\u2795" );
        add.addActionListener( this::addAction );
        JButton minus   = new JButton( "\u2796" );
        minus.addActionListener( this::deleteAction );
        buttons.add( add );
        buttons.add( minus );
        panel.add( buttons, BorderLayout.SOUTH );
        
        return panel;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        IntStream.range( 1, model.getRowCount() )
            .peek( i -> bldr.append( model.getValueAt( i, 0 ) ) )
            .peek( i -> bldr.append( ", " ) )
            .peek( i -> bldr.append( model.getValueAt( i, 1 ) ) )
            .forEach( i -> bldr.append( lineSep ) );
        return bldr.toString();
    }

    /**
     * Begins the "add" process.
     * A new row will be added to the bottom
     * of the GUI's JTable.
     * 
     * @param evt   object that accompanies an action event; not used
     */
    private void addAction( ActionEvent evt )
    {
        int     position    = table.getSelectedRow();
        if ( position < 0 )
            position = table.getRowCount();
        Object[]    row = getNewRow();
        if ( row != null )
            model.insertRow( position, row );
    }
    
    @SuppressWarnings("rawtypes")
    private void deleteAction( ActionEvent evt )
    {
        int[]               selected    = table.getSelectedRows();
        int                 currInx     = 0;
        Vector<Vector>      data        = model.getDataVector();
        Iterator<Vector>    iter        = data.iterator();
        while ( iter.hasNext() )
        {
            iter.next();
            if ( Arrays.binarySearch( selected, currInx++ ) >= 0 )
                iter.remove();
        }
        model.setDataVector( data, vHeader );
    }
    
    /**
     * Asks the operator to enter three values
     * for the columns displayed in the GUI's JTable:
     * <pre>    name, abbreviation, population</pre>
     * The three fields must be separated by commas.
     * The three fields are parsed
     * and collected into an object array,
     * and the array is returned.
     * If the operator cancels the operation
     * null is returned.
     * If a data entry error is detected
     * null is returned.

     * @return  
     *      a row suitable to be added to the GUI's JTable,
     *      or null if the operation is aborted
     *      
     * @see #parseInput(String)
     */
    private Object[] getNewRow()
    {
        String      input   = JOptionPane.showInputDialog( prompt );
        Object[]    row     = null;
        if ( input != null )
        {
            try
            {
                NameRow nameRow = new NameRow( input );
                row = nameRow.getData();
            }
            catch ( IllegalArgumentException exc )
            {
                JOptionPane.showMessageDialog(
                    null, 
                    exc.getMessage(), 
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return row;
    }

    private void tableChanged( TableModelEvent evt )
    {
        int     row     = evt.getFirstRow();
        int     col     = evt.getColumn();
        String  strVal  = "";
        if ( row >= 0 && col >= 0 )
        {
            Object  val = model.getValueAt( row, col );
            strVal = val.toString();
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( row ).append( "," )
                .append( col ).append( "," )
                .append( val );
            System.out.println( bldr );
        }
    }
    
    public static class NameRow
    {
        public final String     name;
        public final double     value;
        
        public NameRow( String name, double value )
        {
            this.name = name;
            this.value = value;
        }
        
        public NameRow( String row )
            throws IllegalArgumentException
        {
            StringTokenizer tizer       = new StringTokenizer( row, ", " );
            int             tokenCount  = tizer.countTokens();
            if ( tokenCount < 1 || tokenCount > 2 )
                throw new IllegalArgumentException( "Invalid row" );
            name = tizer.nextToken().trim();
            if ( !NameValidator.isIdentifier( name ) )
                throw new IllegalArgumentException( "Invalid name" );
            double          tempValue   = 0;
            if ( tokenCount == 2 )
            {
                String  strValue    = tizer.nextToken().trim();
                try
                {
                    tempValue = Double.parseDouble( strValue );
                }
                catch ( NumberFormatException exc )
                {
                    String  msg = "\"" + strValue 
                        + "\" is not a valid decimal number";
                    throw new IllegalArgumentException( msg, exc );
                }
            }
            value = tempValue;
        }
        
        public Object[] getData()
        {
            Object[]    row = { name, value };
            return row;
        }
        
        @Override
        public String toString()
        {
            return name + "," + value;
        }
        
        public static Object[][] getDataArray( Collection<NameRow> coll )
        {
            int     len     = coll.size();
            Object[][] data =
                coll.stream()
                    .map( r -> new Object[] { r.name, r.value } )
                    .toArray( a -> new Object[len][] );
            return data;
        }
    }
    
    /**
     * Subclass of DefaultTableModel
     * that is used to establish the types
     * of the columns in the GUI's JTable.
     * 
     * @author Jack Straub
     * 
     * @see #getColumnClass(int)
     */
    @SuppressWarnings("serial")
    private static class LocalTableModel extends DefaultTableModel
    {
        public LocalTableModel( Object[] headers )
        {
            super( headers, 0 );
        }
        
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = 
                col == 1 ? Double.class : super.getColumnClass( col );
            return clazz;
        }
    }
    
    @SuppressWarnings("serial")
    private class ValueRenderer extends DefaultTableCellRenderer
    {
        @Override
        public void setValue( Object value )
        {
            if ( value != null )
            {
                String  fmtValue = String.format( format, value );
                setText( fmtValue );
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            else
                setText( "" );
        }
    }
}
