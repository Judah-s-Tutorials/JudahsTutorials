package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
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
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;

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
@SuppressWarnings("serial")
public class VariablePanel extends JPanel
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
    private Equation    equation    = new Exp4jEquation();
    /** The text field containing the name of the equation. */
    JTextField          nameField   = new JTextField();
    
    /**
     * Constructor.
     * Creates and initializes an empty table.
     */
    public VariablePanel()
    {
        super( new BorderLayout() );
        
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column0     = colModel.getColumn( 0 );
        TableColumn         column1     = colModel.getColumn( 1 );
        NameEditor          nameEditor  = new NameEditor();
        column0.setCellEditor( nameEditor );
        column1.setCellRenderer( new ValueRenderer() );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoCreateRowSorter( true );
        model.addTableModelListener( this::tableChanged );
        
        JFormattedTextField fmt = nameEditor.getComponent();
        fmt.addPropertyChangeListener( "value", this::nameChanged );
        
        load( equation );
        Border      border      =
            BorderFactory.createEmptyBorder( 3, 3, 0, 3 );
        JScrollPane scrollPane  = new JScrollPane( table );
        setBorder( border );
        
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
        add( scrollPane, BorderLayout.CENTER );
        
        JPanel  buttons = new JPanel();
        JButton add     = new JButton( "\u2795" );
        add.addActionListener( this::addAction );
        JButton minus   = new JButton( "\u2796" );
        minus.addActionListener( this::deleteAction );
        buttons.add( add );
        buttons.add( minus );
        add( buttons, BorderLayout.SOUTH );
        add( getNamePanel(), BorderLayout.NORTH );
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
        model.setRowCount( 0 );
        equation.getVars().entrySet().stream()
            .map( e -> new Object[] { e.getKey(), e.getValue() } )
            .forEach( o -> model.addRow( o ) );
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
        {
            model.insertRow( position, row );
            equation.setVar( (String)row[0], (Double)row[1] );
        }
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
            Vector<?>  next    = iter.next();
            if ( Arrays.binarySearch( selected, currInx++ ) >= 0 )
            {
                String  name    = (String)next.get( 0 );
                equation.removeVar( name );
                iter.remove();
            }
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
                row = parseInput( input );
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
    
    /**
     * Parse operator input into a name/value pair.
     * If unspecified, the value will default to 0.
     * If specified, the value must follow the name,
     * and be separated from the name
     * by at least one space and/or comma.
     * The name must be a unique, valid identifier.
     * 
     * @param input the input to parse
     * 
     * @return  
     *      a row suitable to be added to the GUI's JTable,
     *      or null if the operation is aborted
     * @throws IllegalArgumentException
     */
    public Object[] parseInput( String input )
        throws IllegalArgumentException
    {
        StringTokenizer tizer       = new StringTokenizer( input, ", " );
        int             tokenCount  = tizer.countTokens();
        if ( tokenCount < 1 || tokenCount > 2 )
            throw new IllegalArgumentException( "Invalid row" );
        String  name = tizer.nextToken().trim();
        if ( !NameValidator.isIdentifier( name ) )
            throw new IllegalArgumentException( "Invalid name" );
        Optional<?> opt = equation.getVar( name );
        if ( opt.isPresent() )
            throw new IllegalArgumentException( "Name not unique" );
        double          value       = 0;
        if ( tokenCount == 2 )
        {
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
        
        Object[]    row     = new Object[]{ name, value };
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
    
    private void nameChanged( PropertyChangeEvent evt )
    {
        Object  temp    = evt.getOldValue();
        if ( temp == null )
            return;

        Object  source  = evt.getSource();
        if ( !(source instanceof JFormattedTextField) )
            throw new ComponentException( "Spurious event" );
        JFormattedTextField field   = (JFormattedTextField)source;
        if ( !evt.getPropertyName().equals( "value" ) )
            throw new ComponentException( "Spurious event" );
        
        String  oldName = evt.getOldValue().toString();
        String  newName = evt.getNewValue().toString();
        System.out.println( "old=" + oldName );
        System.out.println( "new=" + newName );
        // If name was "changed" to itself, ignore
        if ( oldName.equals( newName ) )
            ;
        // Remove the original name, then check for dupes
        else
        {
            Optional<Double>    oldVal  = equation.getVar( oldName );
            equation.removeVar( oldName );
            // if dupe display error, then put old name back
            Optional<?> test    = equation.getVar( newName );
            if ( test.isPresent() )
            {
                String  msg =
                    "\"" + newName + "\" is a duplicate name";
                JOptionPane.showMessageDialog(
                    null,
                    msg,
                    "Duplicate Name Error",
                    JOptionPane.ERROR_MESSAGE
                );
                equation.setVar( oldName, oldVal.get() );
                field.setValue( oldName );
            }
            // otherwise add the new name to the equation
            else
                equation.setVar( newName, oldVal.get() );
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
    }
    
    /**
     * Table cell renderer for floating point values.
     * Formats values with a given number of decimals,
     * and comma separators.
     * 
     * @author Jack Straub
     */
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
