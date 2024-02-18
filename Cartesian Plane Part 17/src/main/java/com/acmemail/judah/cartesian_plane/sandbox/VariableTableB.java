package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.acmemail.judah.cartesian_plane.components.NameEditor;

public class VariableTableB
{
    private static final String     lineSep = System.lineSeparator();
    
    private final Object[]          headers = { "Name", "Value" };
    private final Vector<Object>    vHeader = new Vector<>();
    private final Object[][]        data    = { { "", 0. } };
    
    private final LocalTableModel   model   = 
        new LocalTableModel( data, headers );
    private final JTable            table   = new JTable( model );
    
    private int     dPrecision  = 4;
    private String  format      = "%." + dPrecision + "f";
    
    public VariableTableB()
    {
        this( null );
    }
    
    public VariableTableB( String inFile )
    {
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column0     = colModel.getColumn( 0 );
        TableColumn         column1     = colModel.getColumn( 1 );
        column0.setCellEditor( new NameEditor() );
        column1.setCellRenderer( new ValueRenderer() );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoCreateRowSorter( true );
        Collections.addAll( vHeader, headers );

        if ( inFile != null )
            load( new File( inFile ) );
        model.addTableModelListener( this::tableChanged );
    }
    
    public JPanel getPanel()
    {
        JPanel      panel       = new JPanel( new BorderLayout() );
        JScrollPane scrollPane  = new JScrollPane( table );
        
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
        JButton minus   = new JButton( "\u2796" );
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
    
    @SuppressWarnings("rawtypes")
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
        if ( row == 0 && col == 0 && !strVal.isEmpty() )
        {
            Vector<Vector>      data    = model.getDataVector();
            Object[]            row0    = { "", 0d };
            Vector<Object>      v0      = new Vector<>();
            Collections.addAll( v0, row0 );
            data.add( 0, v0 );
            model.setDataVector( data, vHeader );
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
