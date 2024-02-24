package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * Encapsulation of defined variable names
 * and associated values.
 * Maintained in a JTable with two columns:
 * name, which must be a valid identifier,
 * and value, which must be a valid floating point number. 
 * 
 * @author Jack Straub
 * 
 * @see #getPanel()
 */
public class VariableTable
{
    /** Platform-specific line separator. */
    private static final String     lineSep = System.lineSeparator();
    /** Prompt the operator for a new name and optional value. */
    private static final String     prompt  =
        "Enter [name] or [name,value]";
    /** Convenient representation of the PropertyManager singleton. */
    private static final PropertyManager    pMgr    =
        PropertyManager.INSTANCE;
    
    /** Header row for JTable. */
    private final Object[]          headers = { "Name", "Value" };
    /** 
     * Header row expressed as a vector, for the convenience
     * of updating the JTable.
     * @see #deleteAction(ActionEvent)
     */
    private final Vector<Object>    vHeader = 
        new Vector<>( Arrays.asList( headers ) );
    
    /** Underlying data model for JTable. */
    private final LocalTableModel   model   = 
        new LocalTableModel( headers );
    /** Encapsulated JTable. */
    private final JTable    table   = new JTable( model );
    
    /** Number of decimal points for value display. */
    private int         dPrecision  = 4;
    /** Formatter for value display. */
    private String      format      = "%." + dPrecision + "f";
    
    /** The currently loaded equation. */
    private Equation    equation    = null;
    /** The text field containing the name of the equation. */
    JTextField          nameField   = new JTextField();
    
    /**
     * Constructor.
     * Creates and initializes an empty table.
     */
    public VariableTable()
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
    
    /**
     * Loads the variable table from a text file
     * of comma separated values.
     * 
     * @param inFile    input file path
     * 
     * @throws IOException  if an input error occurs
     */
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
        
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
    }
    
    /**
     * Loads the variable table from text stream
     * of comma separated values.
     * 
     * @param inStream      input stream
     * 
     * @throws IOException  if an input error occurs
     */
    public void load( InputStream inStream )
        throws IOException
    {
        try ( Reader reader = new InputStreamReader( inStream  ) )
        {
            load( reader );
        }
        
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
    }
    
    /**
     * Loads the variable table from a reader
     * whose lines consist of comma separated values.
     * 
     * @param inStream      input stream
     * 
     * @throws IOException  if an input error occurs
     */
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
            
            pMgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
        }
    }
    
    /**
     * Loads the variable table from a reader
     * whose lines consist of comma separated values.
     * 
     * @param inStream      input stream
     * 
     * @throws IOException  if an input error occurs
     */
    public void load( Equation equation )
    {
        this.equation = equation;
        Object[][]  vars    = NameRow.getDataArray( equation );
        model.setDataVector( vars, headers );
        nameField.setText( equation.getName() );
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, false );
    }
    
    /**
     * Returns a JPanel with a BorderLayout.
     * The center region consists of a JScrollPane
     * whose viewport viewer is the encapsulated JTable.
     * 
     * @return
     */
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
        int         prefHeight  = 10 * temp1.getPreferredSize().height;
        spSize.width = prefWidth;
        spSize.height = prefHeight;
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
        panel.add( getNamePanel(), BorderLayout.NORTH );
        
        return panel;
    }
    
    /**
     * Returns a string describing all name/value pairs
     * on separate lines.
     */
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
     * Gets the panel containing 
     * the JTextField for the equation name
     * preceded by a descriptive label.
     * 
     * @return  
     *      panel containing a labeled text field 
     *      for designating an equation name
     */
    private JPanel getNamePanel()
    {
        JPanel  panel   = new JPanel( new GridLayout( 2, 1 ) );
        panel.add( new JLabel( "Eq. Name" ) );
        panel.add( nameField );
        return panel;
    }

    /**
     * Adds a new row 
     * after the first selected row
     * of the GUI's JTable.
     * If no row is selected,
     * the new row is added
     * to the end of the table.
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
    
    /**
     * Deletes all selected rows in the GUI's JTable.
     * 
     * @param evt   object that accompanies an action event; not used
     */
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
     * Asks the operator to enter a name
     * and optional value
     * for a new variable.
     * If unspecified, the value will default to 0.
     * The name must be a unique, valid identifier.
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
                if ( !isUniqueName( nameRow.name ) )
                {
                    String message = 
                        "\"" + nameRow.name + "\" is not unique.";
                    throw new IllegalArgumentException( message );
                }
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
        if ( row >= 0 && col >= 0 )
        {
            Object  val = model.getValueAt( row, col );
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( row ).append( "," )
                .append( col ).append( "," )
                .append( val );
            System.out.println( bldr );
        }
        
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, true );
    }
    
    /**
     * Tests a given name for uniqueness compared 
     * to all other names in the GUI's JTable.
     * Returns true if the name is unique.
     * 
     * @param name  the given name
     * 
     * @return  true if the name is unique
     */
    private boolean isUniqueName( String name )
    {
        boolean isUnique   =
            IntStream.range( 0, table.getRowCount() )
                .mapToObj( r -> model.getValueAt( r, 0 ) )
                .map( name::equals )
                .findFirst().orElse( true );
        return isUnique;
    }
    
    /**
     * Encapsulates a name/value pair.
     * The name is assumed to be a valid identifer,
     * and the value a valid double.
     * 
     * @author Jack Straub
     */
    public static class NameRow
    {
        public final String     name;
        public final double     value;
        
        public NameRow( String name, double value )
        {
            this.name = name;
            this.value = value;
        }
        
        /**
         * Constructor.
         * Converts a given string to a name value pair.
         * The value is optional,
         * if not present it will default to 0.
         * If the value is present
         * it must be separated from the name
         * by at least one command and/or space.
         * 
         * @param row   the given string
         * 
         * @throws IllegalArgumentException
         *      if the given string is not a valid name/value pair
         */
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
        
        /**
         * Returns a two-element Object array
         * containing the name and value
         * from the encapsulated pair.
         *
         * @return  
         *      a two-element Object array containing the name and value
         *      of this pair
         */
        public Object[] getData()
        {
            Object[]    row = { name, value };
            return row;
        }
        
        /**
         * Returns a string representation 
         * of this name/value pair.
         * 
         * @return string representation of this name/value pair
         */
        @Override
        public String toString()
        {
            return name + "," + value;
        }
        
        /**
         * Converts a given collection of NameRow values
         * to a 2-dimensional object array.
         * Each row in the array consists of two elements,
         * with the name of the corresponding NameRow value
         * on the first element,
         * and the value in the second.
         * This is convenient for producing
         * an Object[][] array suitable for initializing 
         * a JTable's data model.
         * 
         * @param coll  the given collection
         * 
         * @return  
         *      an Object[][] array containing all values
         *      in the given collection
         */
        public static Object[][] getDataArray( Collection<NameRow> coll )
        {
            int     len     = coll.size();
            Object[][] data =
                coll.stream()
                    .map( r -> new Object[] { r.name, r.value } )
                    .toArray( a -> new Object[len][] );
            return data;
        }
        
        /**
         * Converts a given collection of NameRow values
         * to a 2-dimensional object array.
         * Each row in the array consists of two elements,
         * with the name of the corresponding NameRow value
         * on the first element,
         * and the value in the second.
         * This is convenient for producing
         * an Object[][] array suitable for initializing 
         * a JTable's data model.
         * 
         * @param coll  the given collection
         * 
         * @return  
         *      an Object[][] array containing all values
         *      in the given collection
         */
        public static Object[][] getDataArray( Equation equation )
        {
            Map<String,Double>  vars    = equation.getVars();
            Set<String>         keys    = vars.keySet();
            List<String>        keyList = 
                keys.stream().collect( Collectors.toList() );
            keyList.sort( (i1,i2) -> i1.compareTo( i2 ) );
            Object[][] data =
                keyList.stream()
                    .map( n -> new Object[] { n, vars.get( n ) } )
                    .toArray( a -> new Object[keys.size()][] );
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
        /**
         * Constructor.
         * Initializes the DefaultTableModel superclass
         * to the given header row, 
         * and 0 rows of data.
         * 
         * @param headers   the given header row
         */
        public LocalTableModel( Object[] headers )
        {
            super( headers, 0 );
        }
        
        /**
         * Returns Double.class for the value column of the table,
         * otherwise defaults to the value
         * returned by the superclass.
         * 
         * @param   col the index of the column to process
         * 
         * @return  the appropriate class for the given table column
         */
        @Override
        public Class<?> getColumnClass( int col ) 
        {
            Class<?>    clazz   = 
                col == 1 ? Double.class : super.getColumnClass( col );
            return clazz;
        }
        
//        @Override
//        public boolean isCellEditable( int row, int col )
//        {
//            boolean editable    = col == 0 ? false : true;
//            return editable;
//        }
    }
    
    /**
     * Table cell renderer for floating point values.
     * Formats values with a given number of decimals,
     * and comma separators.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private class ValueRenderer extends DefaultTableCellRenderer
    {
        /**
         * Converts an Object to a String
         * for display in a JTable cell.
         * The object is assumed to be type double.
         * It is formatted with a given number of decimals,
         * and comma separators.
         */
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
