package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.acmemail.judah.cartesian_plane.components.NameEditor;

public class VariableTableA
{
    private static final String     lineSep = System.lineSeparator();
    
    private final Object[]          headers = { "Name", "Value" };
    private final Object[][]        data    = { { "", 0. } };
    
    private final LocalTableModel   model   = 
        new LocalTableModel( data, headers );
    private final JTable            table   = new JTable( model );
    
    public VariableTableA()
    {
        this( null );
    }
    
    public VariableTableA( String inFile )
    {
        TableColumn column      = table.getColumnModel().getColumn( 0 );
        column.setCellEditor( new NameEditor() );
        if ( inFile != null )
            load( new File( inFile ) );
    }
    
    public JPanel getPanel()
    {
        JPanel      panel       = new JPanel( new GridLayout( 1, 1) );
        JScrollPane scrollPane  = new JScrollPane( table );
        panel.add( scrollPane );//, BorderLayout.CENTER );
        
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
    
    private void load( File inFile )
    {
        try ( 
            FileReader fReader  = new FileReader( inFile );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            bReader.lines()
                .map( NameRow::new )
                .map( r -> r.getData() )
                .forEach( o -> model.addRow( o ) );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
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
        {
            StringTokenizer tizer   = new StringTokenizer( row, ", " );
            if ( tizer.countTokens() != 2 )
                throw new IllegalArgumentException( "Invalid row" );
            name = tizer.nextToken().trim();
            String  strValue    = tizer.nextToken().trim();
            try
            {
                value = Double.parseDouble( strValue );
            }
            catch ( NumberFormatException exc )
            {
                String  msg = "\"" + strValue 
                    + "\" is not a valid decimal number";
                throw new IllegalArgumentException( msg, exc );
            }
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
        public LocalTableModel( Object[][] data, Object[] headers )
        {
            super( data, headers );
        }
        
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = 
                col == 1 ? Double.class : super.getColumnClass( col );
            return clazz;
        }
    }
}
