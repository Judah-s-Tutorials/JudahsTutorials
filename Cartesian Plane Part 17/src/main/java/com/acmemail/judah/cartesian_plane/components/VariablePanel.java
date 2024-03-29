package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
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
 * The name cannot be edited,
 * however variable name/value pairs
 * can be added and deleted.
 * 
 * @author Jack Straub
 * 
 * @see #getPanel()
 */
@SuppressWarnings("serial")
public class VariablePanel extends JPanel
{
    /** Platform-specific line separator. */
    private static final String             lineSep = 
        System.lineSeparator();
    /** Prompt the operator for a new name and optional value. */
    private static final String             prompt  =
        "Enter [name] or [name,value]";
    /** Reduces typing when accessing PropertyManager singleton. */
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
    private final LocalTableModel   model   = new LocalTableModel();
    /** Encapsulated JTable. */
    private final JTable    table   = new JTable( model );
    
    /** Number of decimal points for value display. */
    private int         dPrecision  = 4;
    
    /** The currently loaded equation. */
    private Equation    equation;
    
    /**
     * Constructor.
     * Creates and initializes an empty table.
     */
    public VariablePanel()
    {
        super( new BorderLayout() );        
        configureTableModel();

        Border      border      =
            BorderFactory.createEmptyBorder( 3, 3, 0, 3 );
        setBorder( border );
        
        JScrollPane scrollPane  = new JScrollPane( table );
        JLabel      temp1       = new JLabel( headers[0].toString() );
        JLabel      temp2       = new JLabel( headers[1].toString() );
        int         prefWidth   =
            temp1.getPreferredSize().width + 
            temp2.getPreferredSize().width;
        int         prefHeight  = 10 * temp1.getPreferredSize().height;
        Dimension   spSize      = new Dimension( prefWidth, prefHeight );
        scrollPane.setPreferredSize( spSize );
        
        add( scrollPane, BorderLayout.CENTER );
        add( getButtonPanel(), BorderLayout.SOUTH );
        load( equation );
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
        if ( equation != null )
        {
            equation.getVars().entrySet().stream()
                .map( e -> new Object[] { e.getKey(), e.getValue() } )
                .forEach( o -> model.addRow( o ) );
        }
    }
    
    /**
     * Sets the number of decimal point
     * used to display a floating point number.
     * 
     * @param prec  the number of decimal point
     */
    public void setDPrecision( int prec )
    {
        dPrecision = prec;
    }
    
    /**
     * Gets the number of decimal point
     * used to display a floating point number.
     * @return
     *      the number of decimal point
     *      used to display a floating point number.
     */
    public int getPrecision()
    {
        return dPrecision;
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
     * Creates the JPanel containing the add (+) and delete (-) buttons.
     * 
     * @return  the JPanel containing the add and delete buttons
     */
    private JPanel getButtonPanel()
    {
        JButton plus    = new JButton( "\u2795" );
        plus.addActionListener( this::addAction );
        plus.setEnabled( false );
        pMgr.addPropertyChangeListener(
            CPConstants.DM_OPEN_EQUATION_PN, e -> openEqChange( plus )
        );
        
        JButton minus   = new JButton( "\u2796" );
        minus.addActionListener( this::deleteAction );
        minus.setEnabled( false );        
        pMgr.addPropertyChangeListener(
            CPConstants.DM_OPEN_EQUATION_PN, e -> minus.setEnabled( false )
        );

        ListSelectionModel  selModel  = table.getSelectionModel();
        selModel.addListSelectionListener( e -> 
            minus.setEnabled( table.getSelectedRowCount() != 0 )
        );

        JPanel  buttonPanel = new JPanel();
        buttonPanel.add( plus );
        buttonPanel.add( minus );
        return buttonPanel;
    }
    
    /**
     * Configures JTable as required
     * to support editing of variable name/value pairs.
     */
    private void configureTableModel()
    {
        model.setColumnIdentifiers( headers );
        TableColumnModel    colModel    = table.getColumnModel();
        TableColumn         column1     = colModel.getColumn( 1 );
        column1.setCellRenderer( new ValueRenderer() );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        table.getTableHeader().setReorderingAllowed( false );
        model.addTableModelListener( e -> 
            pMgr.setProperty( CPConstants.DM_MODIFIED_PN, true )
        );
     
        pMgr.addPropertyChangeListener(
            CPConstants.DM_OPEN_EQUATION_PN, e -> openEqChange( table ) );
    }
    
    /**
     * Enable or disable the given component
     * whenever the DM_OPEN_EQUATION_PN
     * value changes to true (enable) or false (disable).
     * 
     * @param evt   event object that describes the property change
     * @param comp  the given component
     */
    private void openEqChange( JComponent comp )
    {
        boolean hasEquation = 
            pMgr.asBoolean( CPConstants.DM_OPEN_EQUATION_PN );
        comp.setEnabled( hasEquation );
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
     *      a row suitable to be added to the GUI's JTable
     * @throws IllegalArgumentException
     */
    private Object[] parseInput( String input )
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
        
        /**
         * Returns true if the given column is 0,
         * else returns false;
         * 
         * @param   row row of cell being considered; not used
         * @param   col column of cell being considered
         */
        @Override
        public boolean isCellEditable(int row, int col )
        {
            boolean editable    = col != 0;
            return editable;
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
                String  format   = "%." + dPrecision + "f";
                String  fmtValue = String.format( format, value );
                setText( fmtValue );
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            else
                setText( "" );
        }
    }
}
